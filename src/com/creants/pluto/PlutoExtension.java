package com.creants.pluto;

import com.avengers.netty.core.util.CoreTracer;
import com.avengers.netty.gamelib.GameExtension;
import com.avengers.netty.gamelib.GameInterface;

/**
 * @author LamHa
 *
 */
public class PlutoExtension extends GameExtension {

	public PlutoExtension() {
		CoreTracer.info(PlutoExtension.class, "=================== LOADING PLUTO EXTENSION ===================");
	}

	@Override
	public GameInterface initInterface() {
		// Chìa interface để core gọi vào
		return new GameInterfaceImpl(getCurrentRoom());
	}

}
