package com.creants.pluto.om;

import com.creants.pluto.util.MauBinhConfig;

/**
 * @author LamHa
 *
 */
public class Result {
	int winChiMauBinh;
	int winChi01;
	int winChi02;
	int winChi03;
	int winChiAce;
	int multiK;

	public Result() {
		winChiMauBinh = 0;
		winChi01 = 0;
		winChi02 = 0;
		winChi03 = 0;
		winChiAce = 0;
		multiK = 1;
	}

	public int getWinChiMauBinh() {
		return winChiMauBinh;
	}

	public int getWinChi01() {
		return winChi01;
	}

	public int getWinChi02() {
		return winChi02;
	}

	public int getWinChi03() {
		return winChi03;
	}

	public int getWinChiAce() {
		return winChiAce;
	}

	public int getWinChi() {
		if (getWinChiMauBinh() != 0) {
			return getWinChiMauBinh();
		}

		// return (getWinChi01() + getWinChi02() + getWinChi03()) * getMultiK()
		// + getWinChiAce();
		return getWinChi01() + getWinChi02() + getWinChi03();
	}

	public int getMultiK() {
		return multiK;
	}

	public void setWinChiMauBinh(int value) {
		winChiMauBinh = value;
	}

	public void setWinChi01(int value) {
		winChi01 = value;
	}

	public void setWinChi02(int value) {
		winChi02 = value;
	}

	public void setWinChi03(int value) {
		winChi03 = value;
	}

	public void setWinChiAce(int value) {
		winChiAce = value;
	}

	/**
	 * Set tỉ lệ giá trị thắng nếu win cả 3 chi
	 * 
	 * @param value
	 */
	public void setMultiK(int value) {
		multiK = value;
	}

	public Result getNegative() {
		Result ret = new Result();

		ret.setWinChiMauBinh(-getWinChiMauBinh());
		ret.setWinChi01(-getWinChi01());
		ret.setWinChi02(-getWinChi02());
		ret.setWinChi03(-getWinChi03());
		// ret.setWinChiAce(-getWinChiAce());
		ret.setMultiK(getMultiK());

		return ret;
	}

	/**
	 * Có chi nào hòa hay không
	 * 
	 * @return
	 */
	public boolean haveDickens() {
		return winChi01 == 0 || winChi02 == 0 || winChi03 == 0;
	}

	/**
	 * Đối với chủ bàn khi hòa thì xử thắng
	 * 
	 * @param value
	 */
	public void incrementIfOwnerDickens(int value) {
		if (winChi01 == 0)
			winChi01 += value;

		if (winChi02 == 0)
			winChi02 += value;

		if (winChi03 == 0)
			winChi03 += value;
	}

	/**
	 * Thắng 3 chi
	 * 
	 * @return <code>TRUE</code> thắng cả 3 chi
	 */
	public boolean isWinThreeSet() {
		return (getMultiK() == MauBinhConfig.getInstance().getChiWinThreeSetRate()
				|| getMultiK() == MauBinhConfig.getInstance().getChiWinAllByThreeSetRate()) && getWinChi01() > 0;
	}
}
