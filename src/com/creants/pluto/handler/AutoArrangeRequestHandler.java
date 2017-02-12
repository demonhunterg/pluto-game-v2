package com.creants.pluto.handler;

import com.avengers.netty.socket.gate.wood.Message;
import com.avengers.netty.socket.gate.wood.User;

/**
 * @author LamHa
 *
 */
public class AutoArrangeRequestHandler extends AbstractRequestHandler {

	@Override
	public void handleRequest(User user, Message message) {
		gameLogic.processAutoArrangeCommand(user);
	}

}
