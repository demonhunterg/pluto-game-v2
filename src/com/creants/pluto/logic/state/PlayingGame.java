package com.creants.pluto.logic.state;

import com.avengers.netty.core.util.CoreTracer;
import com.avengers.netty.socket.gate.wood.User;
import com.creants.pluto.logic.GameChecker;
import com.creants.pluto.logic.MauBinhCardSet;
import com.creants.pluto.logic.MauBinhGame;
import com.creants.pluto.om.Player;
import com.creants.pluto.util.MauBinhConfig;
import com.creants.pluto.util.MessageFactory;

/**
 * @author LamHa
 *
 */
public class PlayingGame implements GameState {
	private final int id = 1;

	@Override
	public void doAction(MauBinhGame context) {
		try {
			context.startGameTime = System.currentTimeMillis();

			// xóc bài
			context.cardSet.xaoBai();

			// chia bài
			deliveryCard(context, context.getPlayers(), context.cardSet);

			CoreTracer.debug(PlayingGame.class,
					String.format(
							"[DEBUG] Delivery card finish [roomId: %d, roomName: %s]. Start timeout in %d seconds",
							context.room.getId(), context.room.getName(), 91));

			context.startCountDown(MauBinhConfig.limitTime);
		} catch (Exception e) {
			CoreTracer.error(MauBinhGame.class, "[ERROR] startGame fail!", e);
		}
	}

	private void deliveryCard(MauBinhGame context, Player[] players, MauBinhCardSet cardSet) {
		for (int i = 0; i < players.length; i++) {
			players[i].reset();
		}

		for (int i = 0; i < cardSet.length(); i++) {
			players[(i % 4)].getCards().receivedCard(cardSet.dealCard());
		}

		for (int i = 0; i < context.playerSize(); i++) {
			User receiver = context.getUser(i);
			if (receiver == null)
				continue;

			Player player = players[i];
			player.setUser(receiver);
			if (context.isOwner(receiver)) {
				player.setOwner(true);
			}

			context.gameApi.sendToUser(MessageFactory.makeStartMessage(context.room.getId(), MauBinhConfig.limitTime,
					player.getCardList()), receiver);
		}

		// trường hợp đang chia bài mà người chơi bấm tự động bin hết
		if (GameChecker.isFinishAll(players)) {
			context.processGameFinish();
		}
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Playing State";
	}

}
