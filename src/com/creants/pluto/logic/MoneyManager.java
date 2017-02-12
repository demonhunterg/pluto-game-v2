package com.creants.pluto.logic;

import com.avengers.netty.core.util.CoreTracer;
import com.avengers.netty.socket.gate.wood.User;
import com.creants.pluto.om.Player;
import com.creants.pluto.util.MauBinhConfig;

/**
 * @author LamHa
 *
 */
public class MoneyManager {
	private long moneyBet;

	public MoneyManager(long moneyBet) {
		this.moneyBet = moneyBet;
	}

	public long getGameMoney() {
		return moneyBet;
	}

	/**
	 * Kiểm tra user có đủ tiền không
	 * 
	 * @param user
	 * @return
	 */
	public boolean checkEnoughMoney(User user) {
		if (user == null) {
			return false;
		}

		return user.getMoney() >= moneyBet * MauBinhConfig.getInstance().getStartMoneyRate();
	}

	public long[] calculateMoney(int[] winChi) {
		return new long[] { winChi[0] * moneyBet, winChi[1] * moneyBet, winChi[2] * moneyBet, winChi[3] * moneyBet };
	}

	public long[] addBonusChi(Player[] players, long[] winMoney, int[] winChi) {
		if (players == null || winMoney == null || winChi == null) {
			return null;
		}

		for (int i = 0; i < players.length; i++) {
			if (players[i].getUser() != null) {
				winChi[i] += players[i].getBonusChi();
				winMoney[i] += players[i].getBonusMoney();
			}
		}
		return winMoney;
	}

	public long updateMoneyForLeave(User leaver, int playerNo, Player[] players) {
		int value = 0;
		try {
			value = (int) Math.min(leaver.getMoney(), MauBinhConfig.getInstance().getChiLeaveBonus() * moneyBet);
			for (int i = 0; i < players.length; i++) {
				User user = players[i].getUser();
				if (user != null) {
					// chia tiền
					players[i].addBonusMoney(value);
					// mỗi thằng ăn được 6 chi
					players[i].addBonusChi(MauBinhConfig.getInstance().getChiLeaveBonus());
				}
			}

		} catch (Exception e) {
			CoreTracer.error(MoneyManager.class, "[ERROR] [IN_GAME]updateMoneyForLeave fail! ", e);
		}

		return value;
	}

}
