package com.creants.pluto.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.avengers.netty.core.util.CoreTracer;
import com.avengers.netty.gamelib.key.NetworkConstant;
import com.avengers.netty.socket.gate.wood.Message;
import com.avengers.netty.socket.gate.wood.User;
import com.creants.pluto.om.card.Card;
import com.creants.pluto.om.card.CardSet;

/**
 * @author LamHa
 *
 */
public class FinishRequestHandler extends AbstractRequestHandler {

	@Override
	public void handleRequest(User user, Message message) {
		byte[] blob = message.getBlob(NetworkConstant.KEYBLOB_CARD_LIST);
		CoreTracer.debug(this.getClass(), String.format("[DEBUG] Arrange Finished [username:%s] [cards: %s]",
				user.getUserName(), Arrays.toString(blob)));

		List<Card> listCards = new ArrayList<Card>(13);
		for (int i = 0; i < 13; i++) {
			listCards.add(CardSet.getCard(blob[i]));
		}
		gameLogic.processBinhFinish(user, listCards);
	}

}
