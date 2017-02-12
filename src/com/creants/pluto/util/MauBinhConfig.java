package com.creants.pluto.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

/**
 * @author LamHa
 *
 */
public class MauBinhConfig extends PropertyConfigurator {
	public static final String CONFIG_FILE_NAME = "configs/maubinh_vn.properties";
	public static final int NUMBER_CARD_IN_SET = 52;
	public static final int DEFAULT_NUMBER_CARD = 13;
	public static final int DEFAULT_NUMBER_PLAYER = 4;
	public static final int MIN_NUMBER_PLAYER = 2;
	public static final int DEFAULT_NUMBER_TYPE = 4;
	public static final int NUMBER_CARD_SMALL_SET = 3;
	public static final int NUMBER_CARD_BIG_SET = 5;
	public static final int ONE_SECOND = 1000;
	public static final int RESULT_WIN = 1;
	public static final int RESULT_DRAW = 0;
	public static final int RESULT_LOSE = -1;
	public static final int RESULT_ERROR = Integer.MIN_VALUE;
	public static final int STATUS_WAIT = -1;
	public static final int STATUS_NOT_FINISH = 0;
	public static final int STATUS_FINISH = 1;
	public static final int FAILED_ARRANGEMENT = -2;
	public static final int CHI_DEFAULT = 1;
	public static final int TIME_LIMIT_TYPE_DEFAULT = 1;
	public static final int TIME_LIMIT_TYPE_SLOW = 0;
	public static final int TIME_LIMIT_TYPE_FAST = 2;
	public static final int XP_WIN = 3;
	public static final int XP_DRAW = 2;
	public static final int XP_LOSE = 1;
	public static final int XP_LEAVE = 0;
	
	public static final int limitTime = 90;
	public static final int startAfterSeconds = 10;
	public static final int showCardSeconds = 15;
	
	
	
	private static volatile MauBinhConfig instance;
	private Properties prop = null;

	private MauBinhConfig() {
		try (InputStream input = new FileInputStream(new File(CONFIG_FILE_NAME));) {
			prop = new Properties();
			prop.load(input);
		} catch (Exception e) {
			return;
		}

	}

	public static MauBinhConfig getInstance() {
		if (instance == null) {
			synchronized (MauBinhConfig.class) {
				if (instance == null) {
					instance = new MauBinhConfig();
				}
			}
		}

		return instance;
	}

	private int getIntAttribute(String attribute) {
		try {
			return Integer.parseInt(prop.getProperty(attribute));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public int getLimitTimeDefault() {
		return getIntAttribute("maubinh.LimitTimeDefault");
	}

	public int getLimitTimeSlow() {
		return getIntAttribute("maubinh.LimitTimeSlow");
	}

	public int getLimitTimeFast() {
		return getIntAttribute("maubinh.LimitTimeFast");
	}

	public int getStartMoneyRate() {
		return getIntAttribute("maubinh.StartMoneyRate");
	}

	public int getMaxLoseChi() {
		return getIntAttribute("maubinh.MaxLoseChi");
	}

	public int getChiLeaveBonus() {
		return getIntAttribute("maubinh.ChiLeaveBonus");
	}

	public int getChiWinThreeSetRate() {
		return getIntAttribute("maubinh.ChiWinThreeSetRate");
	}

	public int getChiWinAllByThreeSetRate() {
		return getIntAttribute("maubinh.ChiWinAllByThreeSetRate");
	}

	public int getChiLastFourOfKind() {
		return getIntAttribute("maubinh.ChiLastFourOfKind");
	}

	public int getChiLastFourOfKindAce() {
		return getIntAttribute("maubinh.ChiLastFourOfKindAce");
	}

	public int getChiLastStraightFlush() {
		return getIntAttribute("maubinh.ChiLastStraightFlush");
	}

	public int getChiLastStraightFlushA2345() {
		return getIntAttribute("maubinh.ChiLastStraightFlushA2345");
	}

	public int getChiLastStraightFlush10JQKA() {
		return getIntAttribute("maubinh.ChiLastStraightFlush10JQKA");
	}

	public int getChiMiddleFourOfKind() {
		return getIntAttribute("maubinh.ChiMiddleFourOfKind");
	}

	public int getChiMiddleFourOfKindAce() {
		return getIntAttribute("maubinh.ChiMiddleFourOfKindAce");
	}

	public int getChiMiddleStraightFlush() {
		return getIntAttribute("maubinh.ChiMiddleStraightFlush");
	}

	public int getChiMiddleStraightFlushA2345() {
		return getIntAttribute("maubinh.ChiMiddleStraightFlushA2345");
	}

	public int getChiMiddleStraightFlush10JQKA() {
		return getIntAttribute("maubinh.ChiMiddleStraightFlush10JQKA");
	}

	public int getChiMiddleFullHouse() {
		return getIntAttribute("maubinh.ChiMiddleFullHouse");
	}

	public int getChiFirstThreeOfKind() {
		return getIntAttribute("maubinh.ChiFirstThreeOfKind");
	}

	public int getChiFirstThreeOfKindAce() {
		return getIntAttribute("maubinh.ChiFirstThreeOfKindAce");
	}

	public int getChiMauBinhSixPair() {
		return getIntAttribute("maubinh.ChiMauBinhSixPair");
	}

	public int getChiMauBinhThreeStraight() {
		return getIntAttribute("maubinh.ChiMauBinhThreeStraight");
	}

	public int getChiMauBinhThreeFlush() {
		return getIntAttribute("maubinh.ChiMauBinhThreeFlush");
	}

	public int getChiMauBinhSameColor12() {
		return getIntAttribute("maubinh.ChiMauBinhSameColor12");
	}

	public int getChiMauBinhSixPairWithThree() {
		return getIntAttribute("maubinh.ChiMauBinhSixPairWithThree");
	}

	public int getChiMauBinhStraight13() {
		return getIntAttribute("maubinh.ChiMauBinhStraight13");
	}

	public int getChiMauBinhFourOfThree() {
		return getIntAttribute("maubinh.ChiMauBinhFourOfThree");
	}

	public int getChiMauBinhSameColor13() {
		return getIntAttribute("maubinh.ChiMauBinhSameColor13");
	}

	private String getStringAttribute(String attribute) {
		return prop.getProperty(attribute);
	}

	public String getNameFailedArrangement() {
		return getStringAttribute("maubinh.NameFailedArrangement");
	}

	public String getNameNotMauBinh() {
		return getStringAttribute("maubinh.NameNotMauBinh");
	}

	public String getNameMauBinhSixPair() {
		return getStringAttribute("maubinh.NameMauBinhSixPair");
	}

	public String getNameMauBinhThreeStraight() {
		return getStringAttribute("maubinh.NameMauBinhThreeStraight");
	}

	public String getNameMauBinhThreeFlush() {
		return getStringAttribute("maubinh.NameMauBinhThreeFlush");
	}

	public String getNameMauBinhSameColor12() {
		return getStringAttribute("maubinh.NameMauBinhSameColor12");
	}

	public String getNameMauBinhSixPairWithThree() {
		return getStringAttribute("maubinh.NameMauBinhSixPairWithThree");
	}

	public String getNameMauBinhStraight13() {
		return getStringAttribute("maubinh.NameMauBinhStraight13");
	}

	public String getNameMauBinhFourOfThree() {
		return getStringAttribute("maubinh.NameMauBinhFourOfThree");
	}

	public String getNameMauBinhSameColor13() {
		return getStringAttribute("maubinh.NameMauBinhSameColor13");
	}

	public String getNameSetUnknown() {
		return getStringAttribute("maubinh.NameSetUnknown");
	}

	public String getNameSetHighCard() {
		return getStringAttribute("maubinh.NameSetHighCard");
	}

	public String getNameSetOnePair() {
		return getStringAttribute("maubinh.NameSetOnePair");
	}

	public String getNameSetTwoPair() {
		return getStringAttribute("maubinh.NameSetTwoPair");
	}

	public String getNameSetThreeOfKind() {
		return getStringAttribute("maubinh.NameSetThreeOfKind");
	}

	public String getNameSetStraight() {
		return getStringAttribute("maubinh.NameSetStraight");
	}

	public String getNameSetFlush() {
		return getStringAttribute("maubinh.NameSetFlush");
	}

	public String getNameSetFullHouse() {
		return getStringAttribute("maubinh.NameSetFullHouse");
	}

	public String getNameSetFourOfKind() {
		return getStringAttribute("maubinh.NameSetFourOfKind");
	}

	public String getNameSetStraightFlush() {
		return getStringAttribute("maubinh.NameSetStraightFlush");
	}

}
