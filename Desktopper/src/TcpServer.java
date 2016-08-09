import java.io.IOException;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.io.*;
import java.net.*;

import javax.xml.bind.DatatypeConverter;

public class TcpServer {
	private int m_Port;
	private ServerSocket m_Server;
	private String m_PrivateKey;
	private Robot m_Robot;
	Dimension m_ScreenSize;
	int[] m_Ordinators; // [0] Ox, [1] Oy
	
	public TcpServer() throws IOException, AWTException {
		m_Port = 8080;
		m_PrivateKey = new String("vyvanveo");
		m_Server = new ServerSocket(m_Port);
		m_Robot = new Robot();
		m_ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		m_Ordinators = new int[] {(int)(m_ScreenSize.getWidth()/2), (int)(m_ScreenSize.getHeight()/2)};
	}
	
	public Socket ListenForRemoter() throws IOException {
		Socket connectionSocket = null;
		
		while(true) {
			connectionSocket = m_Server.accept();
			// Check if the client is a mouse remoter
			DataInputStream inFromClient = new DataInputStream(connectionSocket.getInputStream());
			// if string input contain the key then it is a mouse remoter -> break
			String hiMessage = inFromClient.readUTF();
			System.out.println(hiMessage);
			if(hiMessage.toLowerCase().indexOf(m_PrivateKey) != -1)
			{
				DataOutputStream out = new DataOutputStream(connectionSocket.getOutputStream());
				out.writeUTF("Thank you for connecting to "
							+ connectionSocket.getLocalSocketAddress());
				break;
			} else {
				connectionSocket.close();
			}
		}
		
		return connectionSocket;
	}
	
	public void ReceiveDatanExecute(Socket ConnSocket) throws IOException, AWTException {
		try {
			System.out.println("Start collect data...");
			DataInputStream inFromClient = new DataInputStream(ConnSocket.getInputStream());
			// if string input contain the key then it is a mouse remoter -> break
			while(true) {
				byte[] data = new byte[Utilities.PAYLOAD_SIZE]; 
				inFromClient.readFully(data);
				ProcessData(data);
			}
		} catch (IOException i) {
			// maybe, connection is off.
			// throw exception for main catching
			System.out.println("ReceiveDatanExecute EXCEPTION!");
		}		
	}
	
	private void ProcessData(byte[] Data) throws AWTException {
		switch(Data[0]) {
		case Utilities.CLICK:
		{
            m_Robot.mousePress(InputEvent.BUTTON1_MASK);
            m_Robot.mouseRelease(InputEvent.BUTTON1_MASK);
            m_Robot.mousePress(InputEvent.BUTTON1_MASK);
            m_Robot.mouseRelease(InputEvent.BUTTON1_MASK);
		} break;
		
		case Utilities.MOVE:
		{
			byte b1 = (byte) Data[1];
			byte b2 = (byte) Data[2];
			short x = (short) (b1 << 8 | b2 & 0xFF);
			b1 = (byte) Data[3];
			b2 = (byte) Data[4];
			short y = (short) (b1 << 8 | b2 & 0xFF);
			
			//Update ordinators
			if((m_Ordinators[0] + x) > m_ScreenSize.getWidth())
			{
				m_Ordinators[0] = (int)m_ScreenSize.getWidth();
			} else if ((m_Ordinators[0] + x) < 0)
			{
				m_Ordinators[0] = 0;
			} else {
				m_Ordinators[0] += x;
			}
			
			if((m_Ordinators[1] + y) > m_ScreenSize.getWidth())
			{
				m_Ordinators[1] = (int)m_ScreenSize.getWidth();
			} else if ((m_Ordinators[1] + y) < 0)
			{
				m_Ordinators[1] = 0;
			} else {
				m_Ordinators[1] += y;
			}
			
			m_Robot.mouseMove(m_Ordinators[0], m_Ordinators[1]);
		} break;
		}
	}
}
