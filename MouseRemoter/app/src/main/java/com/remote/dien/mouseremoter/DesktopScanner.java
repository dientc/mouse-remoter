package com.remote.dien.mouseremoter;

import android.util.Log;

import java.io.IOException;
import java.net.*;
import java.util.LinkedHashSet;
import java.util.Vector;

public class DesktopScanner {
	private int m_Port;
	private DatagramSocket m_Scanner;
	private InetAddress m_RemoteDesktopAddr;
	
	public DesktopScanner() throws SocketException, UnknownHostException {
		m_Port = 8989;
	    m_Scanner = new DatagramSocket(null);
		m_Scanner.setReuseAddress(true);
		m_Scanner.setSoTimeout(10000);
		m_Scanner.setBroadcast(true);
		m_Scanner.bind(new InetSocketAddress(m_Port));
	}
	
	public Vector ScanDesktop() throws IOException {
		Vector remoteAddrs = new Vector();
		byte[] data = new byte[512];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		while(true) {
			try {
				m_Scanner.receive(packet);
			} catch (IOException i) {
				Log.d("receive", "exception");
			}

			String message = new String(packet.getData(), 0, packet.getLength());
			Log.d("Scanner ", packet.getAddress().getHostAddress() + " : " + message);
			if(message.toLowerCase().indexOf("desktop") != -1)
			{
				remoteAddrs.add(packet.getAddress().getHostAddress());
			}
			
			// Stop when catching to 10 units. HARDCODE
			if (remoteAddrs.size() == 10) {
				break;
			}
		}

		return (new Vector(new LinkedHashSet(remoteAddrs)));
	}
	
}
