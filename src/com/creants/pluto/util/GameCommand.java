package com.creants.pluto.util;

/**
 * @author LamHa
 *
 */
public class GameCommand {
	public static final byte ACTION_START_AFTER_COUNTDOWN = 55;
	public static final byte ACTION_START_GAME = 56;
	public static final byte ACTION_AUTO_ARRANGE = 57;
	public static final byte ACTION_FINISH = 58;
	public static final byte ACTION_END_GAME = 59;
	public static final byte ACTION_QUIT_GAME = 60;
	public static final byte ACTION_READY = 61;
	public static final byte ACTION_RECONNECT= 62;
	public static final byte ACTION_DISCONNECT= 63;

	public static final short INTERFACE_ERROR = 1;
	public static final short SET_LIMIT_TIME = 10;
	public static final short TABLE_INFO = 15;
	public static final short SORT_BY_ORDER = 23;
	public static final short SORT_BY_TYPE = 24;
	public static final short RESULT = 40;
	public static final short STOP = 50;

	public static final short ERROR_IN_GAME_BIN_THUNG = 1;
	public static final short ERROR_IN_GAME_MISS_CARD = 2;

	public static final short KEYB_MAUBINH_TYPE = 0x30;
	public static final short KEYI_WINCHI = 0x31;
	public static final short KEYI_WINCHI_MAUBINH = 0x32;
	public static final short KEYI_WINCHI_1 = 0x33;
	public static final short KEYI_WINCHI_2 = 0x34;
	public static final short KEYI_WINCHI_3 = 0x35;
	public static final short KEYI_WINCHI_ACE = 0x36;

	
	public static final short KEYL_UTC_TIME = 0x40;
}
