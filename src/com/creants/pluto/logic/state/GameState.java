package com.creants.pluto.logic.state;

import com.creants.pluto.logic.MauBinhGame;

/**
 * @author LamHa
 *
 */
public interface GameState {
	void doAction(MauBinhGame context);

	int getId();
}
