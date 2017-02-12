package com.creants.pluto.logic.state;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.avengers.netty.gamelib.key.NetworkConstant;
import com.avengers.netty.socket.gate.wood.Message;
import com.creants.pluto.logic.MauBinhGame;
import com.creants.pluto.util.GameCommand;
import com.creants.pluto.util.MauBinhConfig;
import com.creants.pluto.util.MessageFactory;

/**
 * @author LamHa
 *
 */
public class WaitingGame implements GameState {
	private final int id = 0;

	@Override
	public void doAction(MauBinhGame context) {
		// đếm cho ván tiếp theo
		context.startCountDown(context.isTournament ? 3 : MauBinhConfig.startAfterSeconds);
		Message message = MessageFactory.createMauBinhMessage(GameCommand.ACTION_START_AFTER_COUNTDOWN);
		message.putInt(NetworkConstant.KEYI_TIMEOUT, MauBinhConfig.startAfterSeconds);
		message.putLong(GameCommand.KEYL_UTC_TIME, DateTime.now().toDateTime(DateTimeZone.UTC).getMillis());
		context.gameApi.sendAllInRoom(message);
	}

	@Override
	public String toString() {
		return "Waiting State";
	}

	public int getId() {
		return id;
	}

}
