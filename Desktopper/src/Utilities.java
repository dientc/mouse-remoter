// Make payload 34 bits
// 2 bits: control command: RIGHT MOUSE, LEFT MOUSE, SCROLL, MOVE
// 16 bits: Ox
// 16 bits: Oy

public class Utilities {
	public static final int PAYLOAD_SIZE = 5;
	public static final byte CLICK = 0x01;
	public static final byte MOVE = 0x02;
	public static final byte GOODBYE = 0x0A;
}
