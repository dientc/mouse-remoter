import java.io.IOException;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.*;
import java.net.*;

public class DesktopServer {
	private int m_Port;
	private ServerSocket m_Server;
	private String m_PrivateKey;
	private Robot m_Robot;
	
	public DesktopServer() throws IOException, AWTException {
		m_Port = 8080;
		m_PrivateKey = new String("vyvanveo");
		m_Server = new ServerSocket(m_Port);
		m_Robot = new Robot();
	}
	
	public Socket ListenForRemoter() throws IOException {
		Socket connectionSocket = null;
		
		while(true) {
			connectionSocket = m_Server.accept();
			// Check if the client is a mouse remoter
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			// if string input contain the key then it is a mouse remoter -> break
			if(inFromClient.readLine().toLowerCase().indexOf(m_PrivateKey) != -1)
			{
				break;
			} else {
				connectionSocket.close();
			}
		}
		
		return connectionSocket;
	}
	
	public void DataTransferAndExecute(Socket ConnSocket) throws IOException {
		try {
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(ConnSocket.getInputStream()));
			ProcessData(inFromClient.readLine());
		} catch (IOException i) {
			// maybe, connection is off.
			// throw exception for main catching
		}		
	}
	
	private void ProcessData(String Data) {
		// work with data vs Robot
	}
}
