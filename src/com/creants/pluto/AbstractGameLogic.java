package com.creants.pluto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.avengers.netty.core.om.IRoom;
import com.avengers.netty.gamelib.GameAPI;
import com.avengers.netty.gamelib.result.IPlayMoveResult;
import com.avengers.netty.socket.gate.wood.Message;
import com.avengers.netty.socket.gate.wood.User;
import com.creants.pluto.handler.AbstractRequestHandler;
import com.creants.pluto.logic.MauBinhGame;

/**
 * @author LamHa
 *
 */
public abstract class AbstractGameLogic {
	private final Map<Short, AbstractRequestHandler> handlers;

	public AbstractGameLogic(IRoom room) {
		handlers = new ConcurrentHashMap<Short, AbstractRequestHandler>();
		createGameLogic(room);
		initRequestHandler();
	}

	/**
	 * Tạo game logic để xử lý các logic của game
	 * 
	 * @return
	 */
	public abstract MauBinhGame createGameLogic(IRoom room);

	public abstract MauBinhGame getGameLogic();

	public abstract void initRequestHandler();

	public void addRequestHandler(short requestId, AbstractRequestHandler requestHandler) {
		requestHandler.setGameLogic(getGameLogic());
		handlers.put(requestId, requestHandler);
	}

	public AbstractRequestHandler getRequestHandler(short requestId) {
		return handlers.get(requestId);
	}

	/**
	 * Xử lý action của người chơi
	 * 
	 * @param requestId
	 * @param user
	 * @param message
	 * @return
	 */
	public IPlayMoveResult processRequest(short requestId, User user, Message message) {
		AbstractRequestHandler requestHandler = handlers.get(requestId);
		if (requestHandler == null) {
			return null;
		}

		requestHandler.handleRequest(user, message);
		return null;
	}

	protected void initGameApi(GameAPI gameApi) {
		for (AbstractRequestHandler handler : handlers.values()) {
			handler.initGameApi(gameApi);
		}
	}

}
