package com.creants.pluto;

import com.avengers.netty.core.event.SystemNetworkConstant;
import com.avengers.netty.core.om.IRoom;
import com.avengers.netty.core.util.CoreTracer;
import com.avengers.netty.gamelib.GameAPI;
import com.avengers.netty.gamelib.GameInterface;
import com.avengers.netty.gamelib.key.NetworkConstant;
import com.avengers.netty.gamelib.om.RoomInfo;
import com.avengers.netty.gamelib.result.IPlayMoveResult;
import com.avengers.netty.socket.gate.IMessage;
import com.avengers.netty.socket.gate.wood.Message;
import com.avengers.netty.socket.gate.wood.User;
import com.creants.pluto.handler.AutoArrangeRequestHandler;
import com.creants.pluto.handler.FinishRequestHandler;
import com.creants.pluto.handler.ReadyRequestHandler;
import com.creants.pluto.logic.MauBinhGame;
import com.creants.pluto.logic.TestUnit;
import com.creants.pluto.logic.TournamentGame;
import com.creants.pluto.util.GameCommand;
import com.creants.pluto.util.MessageFactory;
import com.google.gson.JsonObject;

/**
 * @author LamHa
 *
 */
public class GameInterfaceImpl extends AbstractGameLogic implements GameInterface {
	private MauBinhGame gameLogic;
	private GameAPI gameAPI;

	public GameInterfaceImpl(IRoom room) {
		super(room);
		CoreTracer.info(GameInterfaceImpl.class, "- Innit PLUTO");
	}

	@Override
	public MauBinhGame createGameLogic(IRoom room) {
		Object roomInfoObj = room.getProperty(NetworkConstant.ROOM_INFO);
		int moneyBet = 0;
		boolean isTournament = false;
		if (roomInfoObj != null) {
			RoomInfo roomInfo = (RoomInfo) roomInfoObj;
			moneyBet = roomInfo.getBetCoin();
			isTournament = roomInfo.isTournamentGame();
		}

		if (isTournament)
			gameLogic = new TournamentGame(room, moneyBet);
		else
			gameLogic = new MauBinhGame(room, moneyBet);

		gameLogic.isTournament = isTournament;
		return gameLogic;
	}

	@Override
	public MauBinhGame getGameLogic() {
		return gameLogic;
	}

	@Override
	public void initRequestHandler() {
		addRequestHandler(GameCommand.ACTION_AUTO_ARRANGE, new AutoArrangeRequestHandler());
		addRequestHandler(GameCommand.ACTION_FINISH, new FinishRequestHandler());
		addRequestHandler(GameCommand.ACTION_READY, new ReadyRequestHandler());
	}

	@Override
	public void dispatchEvent(short commandId, User user, Message message) {
		processRequest(commandId, user, message);
	}

	@Override
	public JsonObject getGameData() {
		return gameLogic.getGameData();
	}

	@Override
	public Object getGameDataForViewer() {
		return gameLogic.getGameData();
	}

	@Override
	public boolean isPlaying() {
		return gameLogic.isPlaying();
	}

	@Override
	public void leaveRoom(User user, IRoom room) {
		int totalUsers = room.countPlayer();
		CoreTracer.debug(this.getClass(),
				String.format("[DEBUG] [IN_GAME] [user:%s] leave room [%s], [countPlayer: %d] ", user.getUserName(),
						room.getName(), totalUsers));

		// trường hợp thoát ra còn một người chơi duy nhất thì không đếm
		if (totalUsers < 2 && gameLogic.isWaitingPlayer()) {
			gameLogic.stopCountDown();
			CoreTracer.debug(GameInterfaceImpl.class,
					String.format("[DEBUG] [IN_GAME] room [%s] stop countdown", room.getName()));
		}

		// báo người chơi còn lại user đó leave
		Message message = MessageFactory.createMauBinhMessage(GameCommand.ACTION_QUIT_GAME);
		message.putInt(SystemNetworkConstant.KEYI_USER_ID, user.getCreantUserId());
		gameAPI.sendAllInRoomExceptUser(message, user);

		gameLogic.leave(user);
	}

	@Override
	public boolean joinRoom(User user, IRoom room) {
		return gameLogic.join(user, room);
	}

	@Override
	public boolean reconnect(User user) {
		return gameLogic.reconnect(user);
	}

	@Override
	public void disconnect(User user) {
		gameLogic.disconnect(user);
	}

	@Override
	public IPlayMoveResult onPlayMoveHandle(User sender, Message message) {
		return processRequest(message.getShort(SystemNetworkConstant.KEYR_ACTION_IN_GAME), sender, message);
	}

	@Override
	public void setApi(GameAPI gameApi) {
		this.gameAPI = gameApi;
		// TODO refactor
		initGameApi(gameApi);
		gameLogic.setGameApi(gameApi);

	}

	public GameAPI getGameAPI() {
		return gameAPI;
	}

	@Override
	public void test(User user, IMessage message) {
		gameAPI.sendToUser(TestUnit.getInstanse().comparePlayersTest(message), user);
	}

}
