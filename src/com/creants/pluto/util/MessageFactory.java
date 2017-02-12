package com.creants.pluto.util;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.avengers.netty.core.event.SystemNetworkConstant;
import com.avengers.netty.core.util.CoreTracer;
import com.avengers.netty.gamelib.key.NetworkConstant;
import com.avengers.netty.socket.gate.wood.Message;
import com.avengers.netty.socket.gate.wood.User;
import com.creants.pluto.om.MauBinhType;
import com.creants.pluto.om.Player;
import com.creants.pluto.om.Result;
import com.creants.pluto.om.card.Card;
import com.creants.pluto.om.card.Cards;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * @author LamHa
 *
 */
public class MessageFactory {
	private static final byte PROTOCOL_VERSION = 1;

	public static Message createMessage(short commandId) {
		Message message = new Message();
		message.setCommandId(commandId);
		message.setProtocolVersion(PROTOCOL_VERSION);
		return message;
	}

	public static Message makeStartMessage(int roomId, int limitTime, List<Card> cards) {
		Message message = createMauBinhMessage(GameCommand.ACTION_START_GAME);
		try {
			message.putInt(SystemNetworkConstant.KEYI_ROOM_ID, roomId);
			message.putInt(NetworkConstant.KEYI_TIMEOUT, limitTime);
			message.putLong(GameCommand.KEYL_UTC_TIME, DateTime.now().toDateTime(DateTimeZone.UTC).getMillis());

			byte[] cardIds = new byte[cards.size()];
			for (int i = 0; i < cards.size(); i++) {
				cardIds[i] = cards.get(i).getId();
			}

			message.putBytes(NetworkConstant.KEYBLOB_CARD_LIST, cardIds);
		} catch (Exception e) {
			CoreTracer.error(MessageFactory.class, "[ERROR] makeStartMessage fail!", e);
		}

		return message;
	}

	public static Message makeFinishMessage(int userID) {
		Message message = createMauBinhMessage(GameCommand.ACTION_FINISH);
		try {
			message.putInt(SystemNetworkConstant.KEYI_USER_ID, userID);
		} catch (Exception e) {
			CoreTracer.error(MessageFactory.class, "[ERROR] makeFinishMessage fail!", e);
		}

		return message;
	}

	public static Message makeInterfaceErrorMessage(byte command, short errorCode, String errorMessage) {
		Message message = createMauBinhMessage(GameCommand.INTERFACE_ERROR);
		message.putShort(SystemNetworkConstant.KEYR_ACTION_IN_GAME, command);
		message.putShort(SystemNetworkConstant.KEYR_ERROR, errorCode);
		message.putString(SystemNetworkConstant.KEYS_MESSAGE, errorMessage);
		return message;
	}

	public static Message makeResultMessage(int playerIndex, Player[] players, long[] winMoney, int[] winChi,
			Result[][] result) {
		Player player = players[playerIndex];
		if (players == null || winChi == null || result == null || playerIndex < 0 || playerIndex >= players.length
				|| player.getUser() == null) {
			return null;
		}

		Message message = createMauBinhMessage(GameCommand.ACTION_END_GAME);
		JsonObject gameResult = new JsonObject();
		gameResult.addProperty("player_id", player.getUser().getPlayerId());
		gameResult.addProperty("username", player.getUser().getUserName());
		gameResult.addProperty("money", winMoney[playerIndex]);
		gameResult.addProperty("winchi_no", winChi[playerIndex]);
		Cards cards = players[playerIndex].getCards();
		byte type = cards.isFailedArrangement() ? MauBinhType.BINH_LUNG : cards.getMauBinhType();
		gameResult.addProperty("mau_binh_type", type);
		int winChiMauBinh = 0, winChi1 = 0, winChi2 = 0, winChi3 = 0, winchiAce = 0;

		JsonArray ja = new JsonArray();
		try {
			User user = null;
			JsonObject jo = null;
			for (int i = 0; i < players.length; i++) {
				user = players[i].getUser();
				if (i != playerIndex && user != null) {
					jo = new JsonObject();
					jo.addProperty("player_id", user.getPlayerId());
					jo.addProperty("username", user.getUserName());
					jo.addProperty("money", winMoney[i]);
					jo.addProperty("winchi_no", winChi[i]);
					cards = players[i].getCards();
					type = cards.isFailedArrangement() ? MauBinhType.BINH_LUNG : cards.getMauBinhType();
					jo.addProperty("mau_binh_type", type);

					List<Card> cardList = cards.getArrangeCards();
					if (cardList == null) {
						cardList = cards.list();
					}

					byte[] cardIds = new byte[cardList.size()];
					for (int j = 0; j < cardList.size(); j++) {
						cardIds[j] = cardList.get(j).getId();
					}

					int winChiMauBinh2 = result[playerIndex][i].getWinChiMauBinh();
					winChiMauBinh += winChiMauBinh2;
					int winChi01 = result[playerIndex][i].getWinChi01();
					winChi1 += winChi01;
					int winChi02 = result[playerIndex][i].getWinChi02();
					winChi2 += winChi02;
					int winChi03 = result[playerIndex][i].getWinChi03();
					winChi3 += winChi03;
					// int winChiAce2 = result[playerIndex][i].getWinChiAce();
					// winchiAce += winChiAce2;

					jo.addProperty("card_list", GsonUtils.toGsonString(cardIds));
					jo.addProperty("winchi_maubinh", -winChiMauBinh2);
					jo.addProperty("winchi_1", -winChi01);
					jo.addProperty("winchi_2", -winChi02);
					jo.addProperty("winchi_3", -winChi03);
					// jo.addProperty("winchi_ace", -winChiAce2);
					ja.add(jo);
				}
			}
		} catch (Exception e) {
			CoreTracer.error(MessageFactory.class, "[ERROR] makeResultMessage fail!", e);
		}

		gameResult.addProperty("winchi_maubinh", winChiMauBinh);
		gameResult.addProperty("winchi_1", winChi1);
		gameResult.addProperty("winchi_2", winChi2);
		gameResult.addProperty("winchi_3", winChi3);
		gameResult.addProperty("winchi_ace", winchiAce);
		gameResult.addProperty("start_after", 15);

		gameResult.add("player_list", ja);
		message.putString(SystemNetworkConstant.KEYS_JSON_DATA, gameResult.toString());

		return message;
	}

	public static Message makeTestResultMessage(int playerIndex, Player[] players, long[] winMoney, int[] winChi,
			Result[][] result) {
		Player player = players[playerIndex];
		if (players == null || winChi == null || result == null || playerIndex < 0 || playerIndex >= players.length
				|| player.getUser() == null) {
			return null;
		}

		Message message = createMauBinhMessage(GameCommand.ACTION_END_GAME);
		JsonObject gameResult = new JsonObject();
		JsonObject playerResult = new JsonObject();
		playerResult.addProperty("user_id", player.getUser().getCreantUserId());
		playerResult.addProperty("position", playerIndex);
		playerResult.addProperty("full_name", player.getUser().getUserName());
		playerResult.addProperty("money", winMoney[playerIndex]);
		playerResult.addProperty("winchi_no", winChi[playerIndex]);

		Cards cards = players[playerIndex].getCards();
		List<Card> cardList = cards.getArrangeCards();
		if (cardList == null) {
			cardList = cards.list();
		}

		byte[] cardIds = new byte[cardList.size()];
		for (int j = 0; j < cardList.size(); j++) {
			cardIds[j] = cardList.get(j).getId();
		}

		byte type = cards.isFailedArrangement() ? MauBinhType.BINH_LUNG : cards.getMauBinhType();
		playerResult.addProperty("mau_binh_type", type);
		playerResult.addProperty("card_list", GsonUtils.toGsonString(cardIds));

		int winChiMauBinh = 0, winChi1 = 0, winChi2 = 0, winChi3 = 0;
		JsonArray ja = new JsonArray();
		try {
			User user = null;
			JsonObject jo = null;
			for (int i = 0; i < players.length; i++) {
				user = players[i].getUser();
				if (i != playerIndex && user != null) {
					jo = new JsonObject();
					jo.addProperty("user_id", user.getCreantUserId());
					jo.addProperty("position", i);
					jo.addProperty("full_name", user.getUserName());
					jo.addProperty("money", winMoney[i]);
					jo.addProperty("winchi_no", winChi[i]);
					cards = players[i].getCards();
					type = cards.isFailedArrangement() ? MauBinhType.BINH_LUNG : cards.getMauBinhType();
					jo.addProperty("mau_binh_type", type);

					cardList = cards.getArrangeCards();
					if (cardList == null) {
						cardList = cards.list();
					}

					cardIds = new byte[cardList.size()];
					for (int j = 0; j < cardList.size(); j++) {
						cardIds[j] = cardList.get(j).getId();
					}

					int winChiMauBinh2 = result[playerIndex][i].getWinChiMauBinh();
					winChiMauBinh += winChiMauBinh2;
					int winChi01 = result[playerIndex][i].getWinChi01();
					winChi1 += winChi01;
					int winChi02 = result[playerIndex][i].getWinChi02();
					winChi2 += winChi02;
					int winChi03 = result[playerIndex][i].getWinChi03();
					winChi3 += winChi03;

					jo.addProperty("card_list", GsonUtils.toGsonString(cardIds));
					jo.addProperty("winchi_maubinh", -winChiMauBinh2);
					jo.addProperty("winchi_1", -winChi01);
					jo.addProperty("winchi_2", -winChi02);
					jo.addProperty("winchi_3", -winChi03);
					ja.add(jo);
				}
			}
		} catch (Exception e) {
			CoreTracer.error(MessageFactory.class, "[ERROR] makeResultMessage fail!", e);
		}

		playerResult.addProperty("winchi_maubinh", winChiMauBinh);
		playerResult.addProperty("winchi_1", winChi1);
		playerResult.addProperty("winchi_2", winChi2);
		playerResult.addProperty("winchi_3", winChi3);
		ja.add(playerResult);
		gameResult.addProperty("start_after", MauBinhConfig.showCardSeconds);

		gameResult.add("player_list", ja);
		message.putString(SystemNetworkConstant.KEYS_JSON_DATA, gameResult.toString());

		return message;
	}

	public static Message makeAutoArrangeResultMessage(List<Card> cards) {
		Message message = createMauBinhMessage(GameCommand.ACTION_AUTO_ARRANGE);
		try {
			byte[] cardIds = new byte[cards.size()];
			for (int i = 0; i < cards.size(); i++) {
				cardIds[i] = cards.get(i).getId();
			}

			message.putBytes(NetworkConstant.KEYBLOB_CARD_LIST, cardIds);
			message.putLong(GameCommand.KEYL_UTC_TIME, DateTime.now().toDateTime(DateTimeZone.UTC).getMillis());
		} catch (Exception e) {
			CoreTracer.error(MessageFactory.class, "[ERROR] makeAutoArrangeResultMessage fail!", e);
		}

		return message;
	}

	/**
	 * Tạo message lỗi.<br>
	 * Khi nào sử dụng createErrorMessage?<br>
	 * Khi client cần bắt những lỗi chung chung để hiện dialog.<br>
	 * Đối với các lỗi logic trong game thì nên trả về mã code lỗi theo command
	 * mà client request để client xử lý theo logic.
	 * 
	 * @param code
	 *            mã code lỗi
	 * @param errorMessage
	 *            thông tin lỗi
	 */
	public static Message createErrorMessage(short serviceId, short code, String errorMessage) {
		Message message = new Message();
		message.setCommandId(SystemNetworkConstant.COMMAND_ERROR);
		message.setProtocolVersion(PROTOCOL_VERSION);

		message.putShort(SystemNetworkConstant.KEYR_COMMAND_ID, serviceId);
		message.putShort(SystemNetworkConstant.KEYR_ERROR, code);
		message.putString(SystemNetworkConstant.KEYS_MESSAGE, errorMessage);
		return message;
	}

	public static Message createMauBinhMessage(short inGamecommandId) {
		Message message = new Message();
		message.setCommandId(NetworkConstant.COMMAND_REQUEST_IN_GAME);
		message.setProtocolVersion(PROTOCOL_VERSION);
		message.putShort(SystemNetworkConstant.KEYR_ACTION_IN_GAME, inGamecommandId);
		return message;
	}
	
}
