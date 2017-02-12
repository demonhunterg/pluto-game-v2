package com.creants.pluto.om;

import java.util.List;

import com.avengers.netty.socket.gate.wood.User;
import com.creants.pluto.om.card.Card;
import com.creants.pluto.om.card.Cards;

/**
 * @author LamHa
 *
 */
public class Player {
	private User user;
	private Cards cards;
	private int bonusMoney;
	private int bonusChi;
	private boolean isFinish;
	private boolean isUsedAutoArrangement;
	private boolean timeOut;
	private boolean isReady;
	private boolean isOwner;
	private int position = -1;

	public Player() {
		reset();
	}

	public void reset() {
		user = null;
		cards = new Cards();
		bonusMoney = 0;
		bonusChi = 0;
		isFinish = false;
		isUsedAutoArrangement = false;
		timeOut = false;
		isReady = false;
		isOwner = false;
	}

	public int getCreantUserId() {
		return user.getCreantUserId();
	}

	public boolean isOwner() {
		return isOwner;
	}

	public void setOwner(boolean isOwner) {
		this.isOwner = isOwner;
	}

	public Cards getCards() {
		return cards;
	}

	public List<Card> getCardList() {
		return cards.list();
	}

	public User getUser() {
		return user;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isTimeOut() {
		return timeOut;
	}

	public void setIsTimeOut(boolean timeOut) {
		this.timeOut = timeOut;
	}

	public int getBonusMoney() {
		return bonusMoney;
	}

	public void addBonusMoney(int value) {
		bonusMoney += value;
	}

	public int getBonusChi() {
		return bonusChi;
	}

	public void addBonusChi(int chi) {
		bonusChi += chi;
	}

	public boolean isFinish() {
		return isFinish;
	}

	public void setFinishFlag(boolean value) {
		isFinish = value;
	}

	public boolean isUsedAutoArrangement() {
		return isUsedAutoArrangement;
	}

	public void setAutoArrangementFlag(boolean value) {
		isUsedAutoArrangement = value;
	}

	public boolean isReady() {
		return isReady;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}

}
