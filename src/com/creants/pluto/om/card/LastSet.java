package com.creants.pluto.om.card;

/**
 * Chi cuối
 * 
 * @author LamHa
 *
 */
public class LastSet extends BigSet {
	public int getWinChi() {
		switch (getType()) {
		case SetType.NOT_ENOUGH_CARD:
			return Integer.MIN_VALUE;
		}

		return 1;
	}
}
