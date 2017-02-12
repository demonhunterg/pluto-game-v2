package com.creants.pluto.om.card;

/**
 * Đối tượng lá bài, sử dụng chung cho các game bài
 * 
 * @author LamHa
 * 
 */
public class Card implements Comparable<Card> {

	/**
	 * qui dinh so loai nuoc tren cung 1 con bai. Voi bai tay thong thuong thi
	 * co Co - Ro - Chuon - Bich.
	 */
	private static final int TYPE_NUMBER = 4;
	/**
	 * Id của quân bài - 0 -> 51. Bắt đầu từ 2 bích
	 */
	private final byte id;
	/**
	 * Tên con bài được qui định bằng số.
	 */
	private final int cardNumber;
	/**
	 * Nước của con bài 0: bí 1: chuồn 2: rô 3: cơ.
	 */
	private final int cardType;
	/**
	 * Màu của con bài: 0: đen, 1: đỏ.
	 */
	private final int color;

	private boolean isOpen = false; // Trạng thái mở hay úp

	/**
	 * Tạo 1 lá bài mới.
	 *
	 * @param Id
	 *            bài
	 */
	public Card(byte id) {
		this.id = id;
		cardNumber = id / 4 + 1;
		cardType = id % TYPE_NUMBER;
		color = cardType / 2;
	}

	/**
	 * Lấy giá trị của quân bài
	 *
	 * @return ID con bài: 0 -> 51
	 */
	public byte getId() {
		return id;
	}

	/**
	 * Lấy số điểm của lá bài
	 *
	 * @return STT từ 1 -> 13. Bắt đầu từ heo kết thúc là ách đối với bài mậu
	 *         binh
	 */
	public int getCardNumber() {
		return cardNumber;
	}

	/**
	 * Lấy chất của con bài 0: bí, 1: chuồn, 2: rô, 3: cơ.
	 */
	public int getCardType() {
		return cardType;
	}

	/**
	 * Kiểm tra là lá bài đen
	 *
	 * @return true: là bích hoặc chuồn, false: cơ hoặc rô
	 */
	public boolean isTypeBlack() {
		return cardType < 2;
	}

	/**
	 * Kiem tra xem card1 co lon hon card2 hay khong.
	 *
	 * @param card1
	 * @param card2
	 * @return true neu card1 > card 2 trong tien len, con lai return false.
	 */
	public static final boolean isHigher(Card card1, Card card2) {
		return card1.compareTo(card2) > 0;
	}

	public int getColor() {
		return color;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isHide) {
		isOpen = isHide;
	}

	/**
	 * tra ve -1 neu nho hon, 0 la bang, 1 la lon hon.
	 *
	 * @param o
	 *            objec can so sanh
	 * @return ket qua so sanh
	 */
	@Override
	public int compareTo(Card o) {
		Byte thisID = Byte.valueOf(this.getId());
		Byte oID = Byte.valueOf(o.getId());
		return thisID.compareTo(oID);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Card other = (Card) obj;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 79 * hash + this.id;
		return hash;
	}

	public String getName() {
		String cardName = null;
		switch (cardNumber) {
		case 1:
			cardName = "Heo";
			break;
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
			cardName = String.valueOf(cardNumber + 1);
			break;
		case 10:
			cardName = "J";
			break;
		case 11:
			cardName = "Q";
			break;
		case 12:
			cardName = "K";
			break;
		case 13:
			cardName = "A";
			break;

		default:
			break;
		}

		cardName += " ";
		switch (cardType) {
		case 0:
			cardName += "Bích";
			break;
		case 1:

			cardName += "Chuồn";
			break;
		case 2:

			cardName += "Rô";
			break;
		case 3:
			cardName += "Cơ";
			break;

		default:
			break;
		}

		return cardName;
	}

	@Override
	public String toString() {

		// return "Card{" + "id=" + id + ", cardNumber=" + cardNumber + ",
		// cardType=" + cardType + ", color=" + color
		// + '}';

		return getName();
	}
}
