import java.awt.AWTException;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Desktop {
	public static void main(String[] args) throws IOException, InterruptedException, AWTException {
		Broadcaster speaker = new Broadcaster();
		// TcpServer server = new TcpServer();
		UdpServer server = new UdpServer();
		
		for(;;) {
			ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
			Runnable periodicTask = new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						speaker.DoBroadcast();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}				
			};
			ScheduledFuture<?> result = executor.scheduleWithFixedDelay(periodicTask, 0, 200, TimeUnit.MILLISECONDS);

			/*
			// Use Tcp Server
			Socket clientConn = server.ListenForRemoter();
			System.out.println("Established to client!");
			result.cancel(true);
			
			try {
				server.ReceiveDatanExecute(clientConn);
			} catch(IOException i) {
				//System.out.println("Exeception, Continue loop!");
				continue;
			}	
			*/
			// Use Udp Server for speeding up.
			if(true == server.ListenForRemoter()) {
				System.out.println("Established to client!");
				result.cancel(true);
			}
			try {
				server.ReceiveDatanExecute();
			} catch(IOException i) {
				//System.out.println("Exeception, Continue loop!");
				continue;
			}	
		}
	}
}
