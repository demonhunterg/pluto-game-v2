package com.creants.pluto.logic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.avengers.netty.core.event.SystemNetworkConstant;
import com.avengers.netty.socket.gate.IMessage;
import com.avengers.netty.socket.gate.wood.Message;
import com.avengers.netty.socket.gate.wood.User;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.creants.pluto.om.Player;
import com.creants.pluto.om.Result;
import com.creants.pluto.om.card.Card;
import com.creants.pluto.om.card.CardSet;
import com.creants.pluto.om.card.Cards;
import com.creants.pluto.util.MessageFactory;

/**
 * @author LamHa
 *
 */
public class TestUnit {
	private static TestUnit instance;

	public static TestUnit getInstanse() {
		if (instance == null) {
			instance = new TestUnit();
		}
		return instance;
	}

	private TestUnit() {
	}

	public Message comparePlayersTest(IMessage data) {
		try {
			Player[] players = new Player[4];
			for (int i = 0; i < players.length; i++) {
				players[i] = new Player();
			}

			String jsonData = data.getString(SystemNetworkConstant.KEYS_JSON_DATA);
			JsonObject jo = JsonObject.fromJson(jsonData);
			JsonArray hands = jo.getArray("data");
			for (int i = 0; i < hands.size(); i++) {
				JsonObject hand = hands.getObject(i);
				User user = new User();
				user.setUserId(i);
				user.setUserName(hand.getString("name"));
				players[i].setUser(user);

				String cardsString = hand.getString("cards");
				String[] items = StringUtils.split(cardsString, ",");
				int[] cards = new int[items.length];
				for (int j = 0; j < items.length; j++) {
					cards[j] = Integer.parseInt(items[j]);
				}

				List<Card> deliveryCard = deliveryCard(cards);
				logCard(deliveryCard);
				players[i].getCards().setCards(deliveryCard);
				setFinish(players[i], deliveryCard);
			}

			MoneyManager moneyManager = new MoneyManager(1000);
			Result[][] result = GameChecker.comparePlayers(players);
			int[] winChi = GameChecker.getWinChi(players, result);
			long[] winMoney = moneyManager.calculateMoney(winChi);
			winMoney = moneyManager.addBonusChi(players, winMoney, winChi);

			for (int i = 0; i < players.length; i++) {
				if (players[i].getUser() == null)
					continue;

				Message message = MessageFactory.makeTestResultMessage(i, players, winMoney, winChi, result);
				if (message != null) {
					System.out.println("[ERROR] GAME RESULT: " + message.toString());
				}

				return message;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static List<Card> deliveryCard(int[] cards) {
		List<Card> result = new ArrayList<Card>(cards.length);
		for (int i = 0; i < cards.length; i++) {
			result.add(CardSet.getCard((byte) cards[i]));
		}
		return result;
	}

	private static void logCard(List<Card> cards) {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		String chi1 = "";
		String chi2 = "";
		String chi3 = "";
		for (Card card : cards) {
			sb.append(card.getId() + ",");
			if (count < 3) {
				chi1 += card.getName() + "   ";
			} else if (count < 8) {
				chi2 += card.getName() + "   ";
			} else {
				chi3 += card.getName() + "   ";
			}
			count++;
		}

		System.out.println(chi1 + "\n" + chi2 + "\n" + chi3);
		System.out.println("*********************************");
	}

	private static void setFinish(Player player, List<Card> listCards) {
		Cards cards = player.getCards();
		cards.clearArrangement();

		for (int i = 0; i < 3; i++) {
			cards.receivedCardTo1stSet(listCards.get(i));
		}

		int beginset2 = 3;
		for (int i = beginset2; i < 5 + beginset2; i++) {
			cards.receivedCardTo2ndSet(listCards.get(i));
		}

		int beginset3 = 8;
		for (int i = beginset3; i < 5 + beginset3; i++) {
			cards.receivedCardTo3rdSet(listCards.get(i));
		}
	}
}
