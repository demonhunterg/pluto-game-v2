/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.creants.pluto.om.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class chứa thông tin của bộ bài
 * 
 * @author LamHa
 *
 */
public class CardSet {

	/**
	 * Default 1 bộ bài có 52 lá
	 */
	public static final int DEFAULT_CARD_NUMBER = 52;
	public static final Map<Byte, Card> CARD_MAP;

	static {
		CARD_MAP = new HashMap<>();
		for (byte i = 0; i < DEFAULT_CARD_NUMBER; i++) {
			CARD_MAP.put(i, new Card(i));
		}
	}

	/**
	 * List các con bài trong bộ bài.
	 */
	private final transient ArrayList<Card> cards = new ArrayList<Card>();
	/**
	 * vị trí con bài đang sử dụng trong bộ bài.
	 */
	private transient int cardUseIndex;

	/**
	 * tạo bộ bài mới, luôn luôn gồm 52 là từ 0 đến 52, qui định 0 là con bài
	 * nhỏ nhất, 51 là con bài to nhất.
	 *
	 * @param numberCard
	 *            so luong con bai trong bo bai.
	 */
	public CardSet() {
		for (Card card : CARD_MAP.values()) {
			cards.add(card);
		}
	}

	/**
	 * Xào bài.
	 */
	public final void xaoBai() {
		Collections.shuffle(cards);
		cardUseIndex = 0;
	}

	/**
	 * Chia 1 lá bài.
	 *
	 * @return con bài hiện tại của bộ bài<br>
	 *         Return null nếu hết bài
	 */
	public final Card dealCard() {
		if (cardUseIndex < DEFAULT_CARD_NUMBER) {
			return cards.get(cardUseIndex++);
		}

		return null;
	}

	/**
	 * lấy thông tin số con bài của bộ bài.
	 *
	 * @return số lượng con bài trong bộ bài
	 */
	public final int length() {
		return cards.size();
	}

	public int getCardUseIndex() {
		return cardUseIndex;
	}

	public static final Card getCard(byte id) {
		return CARD_MAP.get(id);
	}
}
