package com.creants.pluto.logic;

import java.util.List;

import com.creants.pluto.om.Player;
import com.creants.pluto.om.Result;
import com.creants.pluto.om.card.Card;
import com.creants.pluto.util.MauBinhConfig;

/**
 * @author LamHa
 *
 */
public class GameChecker {
	private static final MauBinhConfig configs = MauBinhConfig.getInstance();

	/**
	 * Số chi thắng sắp xếp theo index của người chơi
	 * 
	 * @param players
	 * @param results
	 * @return
	 */
	public static int[] getWinChi(Player[] players, Result[][] results) {
		int[] winChiArr = new int[4];
		if (results == null || players == null || players.length < 2) {
			return winChiArr;
		}

		int winchiNo = 0;
		for (int i = 0; i < 4; i++) {
			winchiNo = 0;
			for (int j = 0; j < 4; j++) {
				Result result = results[i][j];
				if (result != null) {
					winchiNo += results[i][j].getWinChi();
				}
			}

			winChiArr[i] = winchiNo;
		}

		return winChiArr;
	}

	public static Result[][] comparePlayers(Player[] players) {
		if (players == null || players.length < 2) {
			return null;
		}

		int playerNo = players.length;
		int[] winThreeSetNo = new int[playerNo];
		for (int i = 0; i < playerNo; i++) {
			winThreeSetNo[i] = 0;
		}

		Result[][] result = new Result[playerNo][playerNo];
		for (int i = 0; i < playerNo; i++) {
			if (players[i].getUser() == null)
				continue;

			result[i][i] = new Result();
			for (int j = i + 1; j < playerNo; j++) {
				if (players[j].getUser() == null)
					continue;

				result[i][j] = comparePlayers(players[i], players[j]);
				result[j][i] = result[i][j].getNegative();

				if (result[i][j].isWinThreeSet()) {
					winThreeSetNo[i] += 1;
				}

				if (result[j][i].isWinThreeSet()) {
					winThreeSetNo[j] += 1;
				}
			}
		}

		for (int i = 0; i < playerNo; i++) {
			if (winThreeSetNo[i] < 3)
				continue;

			// sụp làng, sụp hầm
			for (int j = 0; j < playerNo; j++)
				if (result[i][j] != null && result[j][i] != null) {
					result[i][j].setMultiK(configs.getChiWinAllByThreeSetRate());
					result[j][i].setMultiK(configs.getChiWinAllByThreeSetRate());
				}
		}

		return result;
	}

	/**
	 * So sánh bài
	 * 
	 * @param cardList01
	 *            danh sách bài 1
	 * @param cardList02
	 *            danh sách bài 2
	 * @return <code>0</code> (1 == 2), <code>1</code> (1 > 2), <code>-1</code>
	 *         (1 < 2)
	 */
	public static int compareCardByCard(List<Card> cardList01, List<Card> cardList02) {
		if (cardList01 == null || cardList02 == null) {
			return Integer.MIN_VALUE;
		}

		int length01 = cardList01.size();
		int length02 = cardList02.size();
		int cardNo = Math.min(length01, length02);

		for (int i = 0; i < cardNo; i++) {
			int number01 = cardList01.get(length01 - i - 1).getCardNumber();
			int number02 = cardList02.get(length02 - i - 1).getCardNumber();

			if (number01 > number02)
				return 1;
			if (number01 < number02) {
				return -1;
			}
		}

		return 0;
	}

	/**
	 * So sánh lá bài lớn nhất dùng cho mậu thầu (không có liên kết các lá bài)
	 * 
	 * @param cardList01
	 * @param cardList02
	 * @return
	 */
	public static int compareHighestCard(List<Card> cardList01, List<Card> cardList02) {
		if (cardList01 == null || cardList02 == null) {
			return Integer.MIN_VALUE;
		}

		if (cardList01.isEmpty() || cardList02.isEmpty()) {
			return 0;
		}

		int number01 = cardList01.get(cardList01.size() - 1).getCardNumber();
		int number02 = cardList02.get(cardList02.size() - 1).getCardNumber();

		if (number01 > number02)
			return 1;

		if (number01 < number02) {
			return -1;
		}

		return 0;
	}

	public static int compare2HighestCards(List<Card> cardList01, List<Card> cardList02) {
		if (cardList01 == null || cardList02 == null) {
			return Integer.MIN_VALUE;
		}

		if (cardList01.size() < 2 || cardList02.size() < 2) {
			return compareHighestCard(cardList01, cardList02);
		}

		int number01 = cardList01.get(cardList01.size() - 1).getCardNumber();
		int number02 = cardList02.get(cardList02.size() - 1).getCardNumber();

		if (number01 > number02)
			return 1;
		if (number01 < number02) {
			return -1;
		}

		number01 = cardList01.get(cardList01.size() - 2).getCardNumber();
		number02 = cardList02.get(cardList02.size() - 2).getCardNumber();

		if (number01 > number02)
			return 1;
		if (number01 < number02) {
			return -1;
		}

		return 0;
	}

	/**
	 * So sánh bài trên tay của 2 player
	 * 
	 * @param player01
	 * @param player02
	 * @return
	 */
	private static Result comparePlayers(Player player01, Player player02) {
		if (player01 == null || player02 == null || player01.getCards() == null || player02.getCards() == null
				|| !player01.getCards().isEnoughCard() || !player02.getCards().isEnoughCard()) {
			return null;
		}

		Result result = player01.getCards().compareWith(player02.getCards());
		// nếu hòa thì cho chủ bàn thắng
		if (result.haveDickens()) {
			if (player01.isOwner() || player02.isOwner()) {
				result.incrementIfOwnerDickens(player01.isOwner() ? 1 : -1);
			}
		}

		return result;
	}

	/**
	 * Tất cả player đã xếp bài xong chưa
	 * 
	 * @param players
	 * @return <code>TRUE</code> đã xếp xong
	 */
	public static boolean isFinishAll(Player[] players) {
		if (players == null) {
			return true;
		}

		for (int i = 0; i < players.length; i++) {
			Player player = players[i];
			if (player.getUser() != null) {
				if (!player.isFinish()) {
					return false;
				}
			}
		}

		return true;
	}

}
