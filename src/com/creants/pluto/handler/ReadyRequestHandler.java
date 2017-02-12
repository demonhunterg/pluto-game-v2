package com.creants.pluto.handler;

import com.avengers.netty.socket.gate.wood.Message;
import com.avengers.netty.socket.gate.wood.User;

/**
 * @author LamHa
 *
 */
public class ReadyRequestHandler extends AbstractRequestHandler {

	@Override
	public void handleRequest(User user, Message message) {
		gameLogic.ready(user);
	}

}
