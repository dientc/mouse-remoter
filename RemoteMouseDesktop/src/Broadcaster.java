import java.io.IOException;
import java.net.*;

public class Broadcaster {
	
	static final int MESSAGE_SIZE = 512;
	
	private DatagramSocket m_Broadcaster;
	private int m_Port;
	private String m_Message;
	
	public Broadcaster() throws SocketException {
		m_Broadcaster = new DatagramSocket();	
		m_Broadcaster.setBroadcast(true);
		m_Port = 12345;
		m_Message = new String("Hi, mouse server!");
	}
	
	public void SetPort(int Port) {
		m_Port = Port;
	}
	
	public void DoBroadcast() throws IOException {
        byte[] data = new byte[MESSAGE_SIZE];
        DatagramPacket dgramPacket = new DatagramPacket(data, data.length);
        dgramPacket.setAddress(InetAddress.getByAddress(new byte[] { (byte) 255, (byte) 255, (byte) 255, (byte) 255 }));
        dgramPacket.setPort(m_Port);
        System.out.println(m_Message);
        data = m_Message.getBytes();
        dgramPacket.setData(data);

        m_Broadcaster.send(dgramPacket);
        
	}
}
