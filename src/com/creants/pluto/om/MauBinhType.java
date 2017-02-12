package com.creants.pluto.om;

/**
 * Các kiểu mậu binh (tới trắng) lớn dần theo điểm. Người chơi chiến thắng trực
 * tiếp mà không cần so từng chi. https://vi.wikipedia.org/wiki/M%E1%BA%ADu_binh
 * 
 * @author LamHa
 *
 */
public class MauBinhType {
	public static final byte BINH_LUNG = -2;

	public static final byte NOT_MAU_BINH = -1;

	// Lục phé bôn: 6 đôi
	public static final byte SIX_PAIR = 0;

	// 3 sảnh: 3 chi mỗi chi là một sảnh. Giống nhau so đến các sảnh ở các chi.
	// Có thể hoà.
	public static final byte THREE_STRAIGHT = 1;

	// 3 thùng: 3 chi mỗi chi là một thùng. Giống nhau so đến các thùng ở các
	// chi. Có thể hoà.
	public static final byte THREE_FLUSH = 2;

	// Đồng màu 2: 12 con cùng màu
	public static final byte SAME_COLOR_12 = 3;

	// 5 đôi 1 sám: có 5 đôi và 3 lá bài giống nhau
	public static final byte FIVE_PAIR_WITH_THREE = 4;

	// Sảnh rồng: sảnh 13 con không đồng chất
	public static final byte STRAIGHT_13 = 5;

	// 4 con giống nhau với 3 xám
	public static final byte FOUR_OF_THREE = 6;

	// Đồng màu 1: 13 con cùng màu ()
	public static final byte SAME_COLOR_13 = 7;

	// tứ quý
	public static final byte FOUR_OF_KIND = 8;

	// Thùng phá sảnh, dẫy đồng chất Straight Flush
	public static final byte STRAIGHT_FLUSH = 9;

}
