package com.creants.pluto.om.card;

import com.creants.pluto.logic.MauBinhCardSet;
import com.creants.pluto.util.MauBinhConfig;

/**
 * Chi có 3 lá
 * 
 * @author LamHa
 *
 */
public class SmallSet extends Set {
	private boolean isStraight;
	private boolean isFlush;

	public SmallSet() {
		super(3);
		isFlush = false;
		isStraight = false;
	}

	public boolean isFlush() {
		return isFlush;
	}

	public boolean isStraight() {
		return isStraight;
	}

	protected void setType() {
		super.setType();

		switch (getType()) {
		case -1:
		case 0:
		case 1:
		case 3:
			break;
		case 4:
			isStraight = true;
			setType(SetType.HIGH_CARD);
			break;

		case 5:
			isFlush = true;
			setType(SetType.HIGH_CARD);
			break;

		case 8:
			isStraight = true;
			isFlush = true;
			setType(SetType.HIGH_CARD);
			break;
		case 2:
		case 6:
		case 7:
		default:
			setType(SetType.HIGH_CARD);
		}

	}

	public int getWinChi() {
		switch (getType()) {
		case SetType.NOT_ENOUGH_CARD:
			return Integer.MIN_VALUE;
		case SetType.THREE_OF_KIND:
			return getWinChiThreeOfKind();
		}
		return 1;
	}

	/**
	 * Xám chi: 3 lá bài đồng số
	 * 
	 * @return
	 */
	private int getWinChiThreeOfKind() {
		if (MauBinhCardSet.isAce(getCards().get(0))) {
			return MauBinhConfig.getInstance().getChiFirstThreeOfKindAce();
		}
		return MauBinhConfig.getInstance().getChiFirstThreeOfKind();
	}
}
