package com.creants.pluto.logic.state;

import com.avengers.netty.core.util.CoreTracer;
import com.avengers.netty.socket.gate.wood.User;
import com.creants.pluto.logic.MauBinhGame;
import com.creants.pluto.om.Player;

/**
 * @author LamHa
 *
 */
public class FinishGame implements GameState {
	private final int id = 2;

	@Override
	public void doAction(MauBinhGame context) {
		context.stopCountDown();

		// start new match
		checkNewMatch(context);
		stopGame(context.getPlayers());
		context.state = null;
		if (context.playerSize() < 2)
			return;

		context.setState(new WaitingGame());
	}

	public void checkNewMatch(MauBinhGame context) {
		context.processUserDisconnect();

		Player[] players = context.getPlayers();
		// đá player chưa sẵn sàn cho ván kế
		for (int i = 0; i < players.length; i++) {
			User user = players[i].getUser();
			if (user != null && !players[i].isReady()) {
				context.gameApi.leaveRoom(user.getUserId());
			}
		}
	}

	private void stopGame(Player[] players) {
		try {
			for (int i = 0; i < players.length; i++) {
				players[i].reset();
			}

			// gameApi.sendAllInRoom(MessageFactory.makeStopMessage());
		} catch (Exception e) {
			CoreTracer.error(MauBinhGame.class, "[ERROR] stopGame fail!", e);
		}
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Finish State";
	}

}
