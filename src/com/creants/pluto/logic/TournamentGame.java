package com.creants.pluto.logic;

import java.util.HashSet;
import java.util.Set;

import com.avengers.netty.core.event.SystemNetworkConstant;
import com.avengers.netty.core.om.IRoom;
import com.avengers.netty.socket.gate.wood.Message;
import com.avengers.netty.socket.gate.wood.User;
import com.creants.pluto.util.GameCommand;
import com.creants.pluto.util.MessageFactory;

/**
 * @author LamHa
 *
 */
public class TournamentGame extends MauBinhGame {
	private Set<User> readyList;

	public TournamentGame(IRoom room, int moneyBet) {
		super(room, moneyBet);
		readyList = new HashSet<>(room.getMaxUsers());

		// TODO set WaitingGame countdown là 3s
	}

	@Override
	public boolean isStartCountdown() {
		return playerSize() == room.getMaxUsers();
	}

	@Override
	public void doFinish() {
		// TODO remove room cho player trở lại tournament, cho các player leave
		// room

	}

	@Override
	public int calculateElo(int winChiNo) {
		return winChiNo * 1000;
	}

	@Override
	public void ready(User user) {
		Message response = MessageFactory.createMauBinhMessage(GameCommand.ACTION_READY);
		response.putInt(SystemNetworkConstant.KEYI_USER_ID, user.getCreantUserId());
		gameApi.sendAllInRoomExceptUser(response, user);
		readyList.add(user);
		// TODO sau bao nhiêu s mà ko đủ ready thì remove room và đá các user
		// ra, các player ko ready sẽ bị abadon

		if (isStartCountdown()) {
			for (User player : readyList) {
				join(player, room);
			}
		}
	}

	@Override
	public boolean isEnoughMoney(User user) {
		// đã check trước khi vào
		return true;
	}

}
