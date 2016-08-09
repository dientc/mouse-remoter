import java.io.IOException;
import java.net.*;

public class Broadcaster {
	
	static final int MESSAGE_SIZE = 512;
	
	private DatagramSocket m_Broadcaster;
	private int m_Port;
	
	public Broadcaster() throws SocketException {
		m_Broadcaster = new DatagramSocket();	
		m_Broadcaster.setBroadcast(true);
		m_Port = 8989;
	}
	
	public void SetPort(int Port) {
		m_Port = Port;
	}
	
	public void DoBroadcast() throws IOException {
        byte[] data = new byte[MESSAGE_SIZE];
        DatagramPacket dgramPacket = new DatagramPacket(data, data.length);
        dgramPacket.setAddress(InetAddress.getByAddress(new byte[] { (byte) 255, (byte) 255, (byte) 255, (byte) 255 }));
        dgramPacket.setPort(m_Port);
        
		String message = System.getProperty("user.name");
        System.out.println(message);
        data = message.getBytes();
        dgramPacket.setData(data);

        m_Broadcaster.send(dgramPacket);
        
	}
}
