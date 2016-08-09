package com.remote.dien.mouseremoter;
// Make payload 3 bytes
// 1st byte: control command: RIGHT MOUSE, LEFT MOUSE, SCROLL, MOVE
// 2nd byte: Ox
// 3rd byte: Oy

public class Utilities {
	public static final int PAYLOAD_SIZE = 5;
	public static final byte CLICK = 0x01;
	public static final byte MOVE = 0x02;
	public static final byte GOODBYE = 0x0A;

	public static void PrepareData(byte[] Data) {
		int x = 0;
		int y = 0;

		Data[0] = MOVE;
		Data[1] = (byte)(x & 0xFF);
		Data[2] = (byte)(y & 0xFF);
		Data[3] = (byte)(x & 0xFF);
		Data[4] = (byte)(y & 0xFF);
	}
}
