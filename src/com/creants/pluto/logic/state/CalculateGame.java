package com.creants.pluto.logic.state;

import java.util.ArrayList;
import java.util.List;

import com.avengers.netty.socket.gate.wood.Message;
import com.avengers.netty.socket.gate.wood.User;
import com.creants.pluto.logic.GameChecker;
import com.creants.pluto.logic.MauBinhGame;
import com.creants.pluto.om.MauBinhType;
import com.creants.pluto.om.Player;
import com.creants.pluto.om.Result;
import com.creants.pluto.om.card.Cards;
import com.creants.pluto.util.MauBinhConfig;
import com.creants.pluto.util.MessageFactory;

/**
 * @author LamHa
 *
 */
public class CalculateGame implements GameState {
	private final int id = 3;

	@Override
	public void doAction(MauBinhGame context) {
		context.stopCountDown();
		Player[] players = context.getPlayers();
		Result[][] result = GameChecker.comparePlayers(players);
		int[] winChi = GameChecker.getWinChi(players, result);
		long[] winMoney = context.moneyManager.calculateMoney(winChi);
		winMoney = context.moneyManager.addBonusChi(players, winMoney, winChi);

		List<User> userInGame = new ArrayList<User>();
		for (int i = 0; i < players.length; i++) {
			User user = players[i].getUser();
			if (user == null)
				continue;
			userInGame.add(user);
		}

		List<User> playersList = context.room.getPlayersList();
		Message message = null;
		for (int i = 0; i < players.length; i++) {
			User user = players[i].getUser();
			if (user == null)
				continue;

			if (winMoney[i] != 0) {
				context.gameApi.updateUserMoney(user, winMoney[i], 1, "Cập nhật tiền kết thúc game");
			}

			// gửi thông tin kết quả cho player
			message = MessageFactory.makeTestResultMessage(i, players, winMoney, winChi, result);
			if (context.calculateElo(winChi[i]) > 0) {

			}

			if (message != null) {
				context.gameApi.sendToUser(message, user);
			}
		}

		if (message != null) {
			for (User user : playersList) {
				if (userInGame.contains(user))
					continue;
				context.gameApi.sendToUser(message, user);
			}
		}

		// TODO tuy vao ket qua la gi ma thoi luong show bai tuong ung
		// show bài trong 10s
		context.startCountDown(calculateShowCardTime(players));
	}

	public int calculateShowCardTime(Player[] players) {
		User user = null;
		Cards cards = null;
		int lungNo = 0;
		int notMauBinhNo = 0;
		for (int i = 0; i < players.length; i++) {
			user = players[i].getUser();
			if (user == null)
				continue;

			cards = players[i].getCards();
			byte type = cards.isFailedArrangement() ? MauBinhType.BINH_LUNG : cards.getMauBinhType();
			if (type >= 0)
				return MauBinhConfig.showCardSeconds / 2;

			if (type == MauBinhType.BINH_LUNG)
				lungNo++;

			if (type == MauBinhType.NOT_MAU_BINH)
				notMauBinhNo++;
		}

		// neu co bai lung va cac player con lai khong so chi
		if (lungNo > 0 && notMauBinhNo < 2)
			return MauBinhConfig.showCardSeconds / 2;

		return MauBinhConfig.showCardSeconds;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Calculate State";
	}

}
