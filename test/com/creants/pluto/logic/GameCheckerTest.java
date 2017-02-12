package com.creants.pluto.logic;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.avengers.netty.core.event.SystemNetworkConstant;
import com.avengers.netty.gamelib.key.NetworkConstant;
import com.avengers.netty.socket.gate.wood.ConstantMessageContentInterpreter;
import com.avengers.netty.socket.gate.wood.Message;
import com.avengers.netty.socket.gate.wood.User;
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
public class GameCheckerTest {
	public static Player[] players;
	public static MoneyManager moneyManager;

	@BeforeClass
	public static void init() {
		Message.setIntepreter(
				new ConstantMessageContentInterpreter(SystemNetworkConstant.class, NetworkConstant.class));
		moneyManager = new MoneyManager(1000);
		players = new Player[4];
		for (int i = 0; i < players.length; i++) {
			players[i] = new Player();
		}

		User user1 = new User();
		user1.setMoney(100000);
		user1.setUserId(1);
		user1.setUserName("usertest1");
		players[0].setUser(user1);
		List<Card> deliveryCard1 = createCardList(new int[] {35,41,46,11,23,25,48,49,4,12,13,14,15});
		deliveryCardForPlayer(players[0], deliveryCard1);
		players[0].getCards().setCards(deliveryCard1);
		setFinish(players[0], deliveryCard1);
		
		logCard(players[0].getCards().list());

		User user2 = new User();
		user2.setMoney(100000);
		user2.setUserId(2);
		user2.setUserName("bot1");
		players[1].setUser(user2);
		List<Card> deliveryCard2 = createCardList(new int[] { 26,34,47,1,5,20,37,38,16,17,19,28,29});
		deliveryCardForPlayer(players[1], deliveryCard2);
		players[1].getCards().setCards(deliveryCard2);
		setFinish(players[1], deliveryCard2);
		logCard(players[1].getCards().list());
	}

	@Test
	public void comparePlayersTest() {
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
		}
	}
	
	public static void deliveryCardForPlayer(Player player, List<Card> cards){
		Cards cardObj = player.getCards();
		for (Card card : cards) {
			cardObj.receivedCard(card);
		}
	}

	private static List<Card> createCardList(int[] cards) {
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
