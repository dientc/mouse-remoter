import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class UdpServer {
	private int m_Port;
	private DatagramSocket m_Server;
	private String m_PrivateKey;
	private Robot m_Robot;
	Dimension m_ScreenSize;
	int[] m_Ordinators; // [0] Ox, [1] Oy
	
	public UdpServer() throws IOException, AWTException {
		System.out.println("Use UDP Server");
		m_Port = 8080;
		m_PrivateKey = new String("vyvanveo");
		m_Server = new DatagramSocket(m_Port);
		m_Robot = new Robot();
		m_ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		m_Ordinators = new int[] {(int)(m_ScreenSize.getWidth()/2), (int)(m_ScreenSize.getHeight()/2)};
	}
	
	public boolean ListenForRemoter() throws IOException {
		byte[] receivedData = new byte[512];
		byte[] sendData = new byte[512];
		boolean status = false;
		DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
		m_Server.receive(receivedPacket);

		String hiMessage = new String(receivedPacket.getData());
		System.out.println(hiMessage);
		InetAddress IPAddress = receivedPacket.getAddress();
        int port = receivedPacket.getPort();
		if(hiMessage.toLowerCase().indexOf(m_PrivateKey) != -1)
		{			
            String response = "Successful! Accepted connection.";
            sendData = response.getBytes();
            status = true;
		} else {
			String response = "Failed! Connection is refused.";
            sendData = response.getBytes();
            status = false;
		}

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
        m_Server.send(sendPacket);
        
		return status;
	}
	
	public void ReceiveDatanExecute() throws IOException, AWTException {
		try {
			System.out.println("Start collect data...");
			
			while(true) {
				byte[] receivedData = new byte[Utilities.PAYLOAD_SIZE];
				DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
				m_Server.receive(receivedPacket);

				receivedData = receivedPacket.getData();

				ProcessData(receivedData);
			}
		} catch (IOException i) {
			System.out.println("ReceiveDatanExecute EXCEPTION!");
		}		
	}
	
	private void ProcessData(byte[] Data) throws AWTException, IOException {
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
		
		case Utilities.GOODBYE:
		{
			throw new IOException("Client disconect!");
		}
		
		}
	}
}
