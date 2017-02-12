package com.creants.pluto.logic;

import com.creants.pluto.om.card.Card;
import com.creants.pluto.om.card.CardSet;

/**
 * @author LamHa
 *
 */
public class MauBinhCardSet extends CardSet {
	public static final int NUMBER_ACE = 13;
	public static final int NUMBER_FIVE = 4;
	public static final int NUMBER_THREE = 2;
	public static final int NUMBER_TWO = 1;
	public static final int TYPE_HEART = 3;
	public static final int TYPE_DIAMOND = 2;
	public static final int TYPE_CLUB = 1;
	public static final int TYPE_SPADE = 0;

	/**
	 * Con xì
	 */
	public static boolean isAce(Card card) {
		return card.getCardNumber() == NUMBER_ACE;
	}

	/**
	 * Con 3
	 */
	public static boolean is3(Card card) {
		return card.getCardNumber() == 1;
	}

	/**
	 * Con heo
	 */
	public static boolean is2(Card card) {
		return card.getCardNumber() == 0;
	}

	/**
	 * Con 5
	 */
	public static boolean is5(Card card) {
		return card.getCardNumber() == NUMBER_FIVE;
	}

	/**
	 * Quân bài đỏ
	 * 
	 * @param card
	 * @return
	 */
	public static boolean isRed(Card card) {
		return card.getCardType() == TYPE_HEART || card.getCardType() == TYPE_DIAMOND;
	}

}
