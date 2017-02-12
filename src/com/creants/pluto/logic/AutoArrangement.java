package com.creants.pluto.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.creants.pluto.om.card.Card;

/**
 * Tự động xếp bài
 * 
 * @author LamHa
 *
 */
public class AutoArrangement {
	/**
	 * Sắp xếp bài tăng dần
	 * 
	 * @param cards
	 * @return
	 */
	public static List<Card> sortCardByOrder(List<Card> cards) {
		if (cards == null || cards.isEmpty()) {
			return null;
		}

		Collections.sort(cards);
		return cards;
	}

	/**
	 * Sắp xếp bài theo kiểu bài
	 * 
	 * @param cards
	 * @return
	 */
	public static List<Card> sortCardByType(List<Card> cards) {
		if (cards == null || cards.isEmpty()) {
			return null;
		}

		List<Card> heartList = new ArrayList<Card>();
		List<Card> diamondList = new ArrayList<Card>();
		List<Card> clubList = new ArrayList<Card>();
		List<Card> spadeList = new ArrayList<Card>();
		for (int i = 0; i < cards.size(); i++) {
			switch (cards.get(i).getCardType()) {
			case 3:
				heartList.add(cards.get(i));
				break;
			case 2:
				diamondList.add(cards.get(i));
				break;
			case 1:
				clubList.add(cards.get(i));
				break;
			case 0:
				spadeList.add(cards.get(i));
			}

		}

		List<Integer> typeNumberList = new ArrayList<Integer>();
		typeNumberList.add(Integer.valueOf(heartList.size()));
		typeNumberList.add(Integer.valueOf(diamondList.size()));
		typeNumberList.add(Integer.valueOf(clubList.size()));
		typeNumberList.add(Integer.valueOf(spadeList.size()));
		List<Integer> sortedList = new ArrayList<Integer>();
		sortedList.addAll(typeNumberList);
		Collections.sort(sortedList);

		List<Card> ret = new ArrayList<Card>();
		for (int i = 0; i < sortedList.size(); i++) {
			int index = typeNumberList.indexOf(sortedList.get(i));
			typeNumberList.set(index, Integer.valueOf(-1));
			switch (index) {
			case 0:
				ret.addAll(heartList);
				break;
			case 1:
				ret.addAll(diamondList);
				break;
			case 2:
				ret.addAll(clubList);
				break;
			case 3:
				ret.addAll(spadeList);
			}

		}

		return ret;
	}

	/**
	 * Lấy danh sách bài sau khi đã optimal
	 * 
	 * @param cards
	 * @return
	 */
	public static List<Card> getSolution(List<Card> cards) {
		if (cards == null || cards.size() != 13) {
			return null;
		}

		// optimal chi cuối
		List<Card> lastSet = getOptimalBigSet(cards);
		List<Card> remaining = new ArrayList<Card>();
		for (int i = 0; i < cards.size(); i++) {
			if (!lastSet.contains(cards.get(i))) {
				remaining.add(cards.get(i));
			}
		}

		// optimal chi giữa
		List<Card> middleSet = getOptimalBigSet(remaining);
		List<Card> result = new ArrayList<Card>();
		for (int i = 0; i < remaining.size(); i++) {
			if (!middleSet.contains(remaining.get(i))) {
				// 3 lá bài còn lại chi đầu
				result.add(remaining.get(i));
			}
		}

		result.addAll(middleSet);
		result.addAll(lastSet);

		return result;
	}

	/**
	 * Optimal chi 5 cây
	 * 
	 * @param cards
	 * @return
	 */
	private static List<Card> getOptimalBigSet(List<Card> cards) {
		if (cards == null || cards.size() < 5) {
			return null;
		}

		List<Card> heartList = new ArrayList<Card>();
		List<Card> diamondList = new ArrayList<Card>();
		List<Card> clubList = new ArrayList<Card>();
		List<Card> spadeList = new ArrayList<Card>();
		switch (cards.get(0).getCardType()) {
		case 3:
			heartList.add(cards.get(0));
			break;
		case 2:
			diamondList.add(cards.get(0));
			break;
		case 1:
			clubList.add(cards.get(0));
			break;
		case 0:
			spadeList.add(cards.get(0));
			break;
		}

		List<Integer> singleIndex = new ArrayList<Integer>();
		List<Integer> lastPairIndex = new ArrayList<Integer>();
		List<Integer> lastThreeIndex = new ArrayList<Integer>();
		int lastFourIndex = -1;

		int sameCardNo = 0;
		for (int i = 1; i < cards.size(); i++) {
			switch (cards.get(i).getCardType()) {
			case 3:
				heartList.add(cards.get(i));
				break;
			case 2:
				diamondList.add(cards.get(i));
				break;
			case 1:
				clubList.add(cards.get(i));
				break;
			case 0:
				spadeList.add(cards.get(i));
				break;
			}

			if (cards.get(i).getCardNumber() == cards.get(i - 1).getCardNumber()) {
				sameCardNo++;
			} else {
				switch (sameCardNo) {
				case 0:
					singleIndex.add(i - 1);
					break;
				case 1:
					lastPairIndex.add(i - 1);
					break;
				case 2:
					lastThreeIndex.add(i - 1);
					break;
				case 3:
					lastFourIndex = i - 1;
					break;
				}

				sameCardNo = 0;
			}
		}

		switch (sameCardNo) {
		case 0:
			singleIndex.add(cards.size() - 1);
			break;
		case 1:
			lastPairIndex.add(cards.size() - 1);
			break;
		case 2:
			lastThreeIndex.add(cards.size() - 1);
			break;
		case 3:
			lastFourIndex = cards.size() - 1;
			break;
		}

		List<Card> ret = getOptimalStraightFlush(heartList, diamondList, clubList, spadeList);

		if (ret != null) {
			return ret;
		}

		if (lastFourIndex == 3) {
			ret = new ArrayList<Card>();
			ret.add(cards.get(0));
			ret.add(cards.get(1));
			ret.add(cards.get(2));
			ret.add(cards.get(3));
			ret.add(cards.get(4));
			return ret;
		}

		if (lastFourIndex > 3) {
			ret = new ArrayList<Card>();
			ret.add(cards.get(0));
			ret.add(cards.get(lastFourIndex - 3));
			ret.add(cards.get(lastFourIndex - 2));
			ret.add(cards.get(lastFourIndex - 1));
			ret.add(cards.get(lastFourIndex));
			return ret;
		}

		if (!lastThreeIndex.isEmpty()) {
			if (!lastPairIndex.isEmpty()) {
				ret = new ArrayList<Card>();
				ret.add(cards.get(lastPairIndex.get(0) - 1));
				ret.add(cards.get(lastPairIndex.get(0)));

				int tempIndex = lastThreeIndex.get(lastThreeIndex.size() - 1);
				ret.add(cards.get(tempIndex - 2));
				ret.add(cards.get(tempIndex - 1));
				ret.add(cards.get(tempIndex));
				Collections.sort(ret);
				return ret;
			}

			if (lastThreeIndex.size() > 1) {
				ret = new ArrayList<Card>();
				ret.add(cards.get(lastThreeIndex.get(0) - 1));
				ret.add(cards.get(lastThreeIndex.get(0)));

				int tempIndex = lastThreeIndex.get(lastThreeIndex.size() - 1);
				ret.add(cards.get(tempIndex - 2));
				ret.add(cards.get(tempIndex - 1));
				ret.add(cards.get(tempIndex));
				Collections.sort(ret);
				return ret;
			}
		}

		ret = getOptimalFlush(heartList, diamondList, clubList, spadeList);
		if (ret != null) {
			return ret;
		}

		ret = getOptimalStraight(cards);
		if (ret != null) {
			return ret;
		}

		if (!lastThreeIndex.isEmpty() && singleIndex.size() >= 2) {
			ret = new ArrayList<Card>();

			ret.add(cards.get(singleIndex.get(0)));
			ret.add(cards.get(singleIndex.get(1)));

			int tempIndex = lastThreeIndex.get(lastThreeIndex.size() - 1);
			ret.add(cards.get(tempIndex - 2));
			ret.add(cards.get(tempIndex - 1));
			ret.add(cards.get(tempIndex));
			Collections.sort(ret);
			return ret;
		}

		if (lastPairIndex.size() > 1 && !singleIndex.isEmpty()) {
			ret = new ArrayList<Card>();
			ret.add(cards.get(singleIndex.get(0)));
			ret.add(cards.get(lastPairIndex.get(lastPairIndex.size() - 1) - 1));
			ret.add(cards.get(lastPairIndex.get(lastPairIndex.size() - 1)));
			ret.add(cards.get(lastPairIndex.get(lastPairIndex.size() - 2) - 1));
			ret.add(cards.get(lastPairIndex.get(lastPairIndex.size() - 2)));
			Collections.sort(ret);
			return ret;
		}

		if (!lastPairIndex.isEmpty() && singleIndex.size() >= 3) {
			ret = new ArrayList<Card>();
			ret.add(cards.get(singleIndex.get(0)));
			ret.add(cards.get(singleIndex.get(1)));
			ret.add(cards.get(singleIndex.get(2)));
			ret.add(cards.get(lastPairIndex.get(lastPairIndex.size() - 1) - 1));
			ret.add(cards.get(lastPairIndex.get(lastPairIndex.size() - 1)));
			Collections.sort(ret);
			return ret;
		}

		ret = new ArrayList<Card>();
		ret.add(cards.get(cards.size() - 5));
		ret.add(cards.get(cards.size() - 4));
		ret.add(cards.get(cards.size() - 3));
		ret.add(cards.get(cards.size() - 2));
		ret.add(cards.get(cards.size() - 1));

		return ret;
	}

	/**
	 * Optimal sảnh
	 * 
	 * @param cards
	 * @return
	 */
	private static List<Card> getOptimalStraight(List<Card> cards) {
		if (cards == null || cards.isEmpty() || cards.size() < 5) {
			return null;
		}

		List<Card> ret = getOptimalNormalStraight(cards);

		if (ret != null && !ret.isEmpty() && MauBinhCardSet.isAce(ret.get(ret.size() - 1))) {
			return ret;
		}

		List<Card> temp = get2ndStraight(cards);
		if (temp != null && !temp.isEmpty()) {
			return temp;
		}

		return ret;
	}

	/**
	 * Lấy sảnh chi 2
	 * 
	 * @param cards
	 * @return
	 */
	private static List<Card> get2ndStraight(List<Card> cards) {
		if (cards == null || cards.isEmpty() || cards.size() < 5) {
			return null;
		}

		List<Card> ret = new ArrayList<Card>();

		if (!MauBinhCardSet.isAce(cards.get(cards.size() - 1)) || !MauBinhCardSet.is2(cards.get(0))) {
			return null;
		}

		ret.add(cards.get(cards.size() - 1));
		ret.add(cards.get(0));
		for (int i = 1; i < cards.size() - 1; i++) {
			int temp = cards.get(i).getCardNumber() - cards.get(i - 1).getCardNumber();
			switch (temp) {
			case 0:
				break;
			case 1:
				ret.add(cards.get(i));
				break;
			default:
				return null;
			}

			if (ret.size() == 5) {
				Collections.sort(ret);
				return ret;
			}
		}

		return null;
	}

	/**
	 * Optimal sảnh thường
	 * 
	 * @param cards
	 * @return
	 */
	private static List<Card> getOptimalNormalStraight(List<Card> cards) {
		if (cards == null || cards.isEmpty() || cards.size() < 5) {
			return null;
		}

		List<Card> ret = new ArrayList<Card>();

		ret.add(cards.get(cards.size() - 1));
		for (int i = cards.size() - 2; i >= 0; i--) {
			int temp = cards.get(i + 1).getCardNumber() - cards.get(i).getCardNumber();
			switch (temp) {
			case 0:
				break;
			case 1:
				ret.add(cards.get(i));
				break;
			default:
				ret.clear();
				ret.add(cards.get(i));
			}

			if (ret.size() == 5) {
				Collections.sort(ret);
				return ret;
			}
		}

		return null;
	}

	/**
	 * Optimal thùng phá sảnh
	 * 
	 * @param heartList
	 * @param diamondList
	 * @param clubList
	 * @param spadeList
	 * @return
	 */
	private static List<Card> getOptimalStraightFlush(List<Card> heartList, List<Card> diamondList, List<Card> clubList,
			List<Card> spadeList) {
		List<Card> ret = null;
		List<Card> temp = null;

		if (heartList != null && heartList.size() >= 5) {
			ret = getOptimalStraight(heartList);
		}

		if (diamondList != null && diamondList.size() >= 5) {
			if (ret == null) {
				ret = getOptimalStraight(diamondList);
			} else {
				temp = getOptimalStraight(diamondList);
				if (temp != null
						&& temp.get(temp.size() - 1).getCardNumber() > ret.get(ret.size() - 1).getCardNumber()) {
					ret = temp;
				}
			}
		}

		if (clubList != null && clubList.size() >= 5) {
			if (ret == null) {
				ret = getOptimalStraight(clubList);
			} else {
				temp = getOptimalStraight(clubList);
				if ((temp != null)
						&& temp.get(temp.size() - 1).getCardNumber() > ret.get(ret.size() - 1).getCardNumber()) {
					ret = temp;
				}
			}
		}

		if (spadeList != null && spadeList.size() >= 5) {
			if (ret == null) {
				ret = getOptimalStraight(spadeList);
			} else {
				temp = getOptimalStraight(spadeList);
				if ((temp != null)
						&& temp.get(temp.size() - 1).getCardNumber() > ret.get(ret.size() - 1).getCardNumber()) {
					ret = temp;
				}
			}
		}

		return ret;
	}

	/**
	 * Optimal thùng
	 * 
	 * @param heartList
	 * @param diamondList
	 * @param clubList
	 * @param spadeList
	 * @return
	 */
	private static List<Card> getOptimalFlush(List<Card> heartList, List<Card> diamondList, List<Card> clubList,
			List<Card> spadeList) {

		List<Card> temp = null;
		if (heartList != null && heartList.size() >= 5) {
			temp = heartList;
		}

		if (diamondList != null && diamondList.size() >= 5) {
			if (temp == null) {
				temp = diamondList;
			} else if (GameChecker.compareCardByCard(diamondList, temp) == 1) {
				temp = diamondList;
			}
		}

		if (clubList != null && clubList.size() >= 5) {
			if (temp == null) {
				temp = clubList;
			} else if (GameChecker.compareCardByCard(clubList, temp) == 1) {
				temp = clubList;
			}
		}

		if (spadeList != null && spadeList.size() >= 5) {
			if (temp == null) {
				temp = spadeList;
			} else if (GameChecker.compareCardByCard(spadeList, temp) == 1) {
				temp = spadeList;
			}
		}

		if (temp == null) {
			return null;
		}

		List<Card> ret = new ArrayList<Card>();
		ret.add(temp.get(temp.size() - 5));
		ret.add(temp.get(temp.size() - 4));
		ret.add(temp.get(temp.size() - 3));
		ret.add(temp.get(temp.size() - 2));
		ret.add(temp.get(temp.size() - 1));

		return ret;
	}
}
