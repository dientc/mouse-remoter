package com.remote.dien.mouseremoter;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class PhoneClient {
	static final int SERVER_PORT = 8080;
	
	Socket m_ClientSocket;
	DataOutputStream m_Out;
	
	public PhoneClient(String RemoteAddr) throws UnknownHostException, IOException {
		m_ClientSocket = new Socket(RemoteAddr, SERVER_PORT);
		
		Log.d("PhoneClient ", "Just connected to "
				 + m_ClientSocket.getRemoteSocketAddress());
		OutputStream outToServer = m_ClientSocket.getOutputStream();
		m_Out = new DataOutputStream(outToServer);
		m_Out.writeUTF("Hello from vyvanveo "
			                      + m_ClientSocket.getLocalSocketAddress());
		InputStream inFromServer = m_ClientSocket.getInputStream();
		DataInputStream in = new DataInputStream(inFromServer);
		Log.d("PhoneClient ", "Server says " + in.readUTF());
		
		// data transfer
		//m_Out.writeUTF("start transfer data ...");
	}
	
	public void Send(String Message) throws IOException {
		m_Out.writeUTF(Message);
	}
	
	
	public void Send(byte[] Payload) throws IOException {
		m_Out.write(Payload);
	}
	
	public void Close() throws IOException {
		m_ClientSocket.close();
	}
}
