package com.creants.pluto.om.card;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author LamHa
 *
 */
public class CardsTest {

	@BeforeClass
	public static void init() {

	}

	@Test
	public void testCard() {
		List<Card> deliveryCard = deliveryCard(new int[] { 36, 43, 46, 23, 24, 25, 28, 51, 3, 4, 9, 15, 48 });
		Cards cards = new Cards();
		cards.setCards(deliveryCard);
		logCard(deliveryCard);

		for (int i = 0; i < 3; i++) {
			cards.receivedCardTo1stSet(deliveryCard.get(i));
		}

		int beginset2 = 3;
		for (int i = beginset2; i < 5 + beginset2; i++) {
			cards.receivedCardTo2ndSet(deliveryCard.get(i));
		}

		int beginset3 = 8;
		for (int i = beginset3; i < 5 + beginset3; i++) {
			cards.receivedCardTo3rdSet(deliveryCard.get(i));
		}

		boolean check = cards.isFailedArrangement();
		Assert.assertFalse(check);
	}

	private static List<Card> deliveryCard(int[] cards) {
		List<Card> result = new ArrayList<Card>(cards.length);
		for (int i = 0; i < cards.length; i++) {
			result.add(CardSet.getCard((byte) cards[i]));
		}
		return result;
	}

	private void logCard(List<Card> cards) {
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
	
}
