import java.awt.AWTException;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Remoter {
	public static void main(String[] args) throws IOException, InterruptedException, AWTException {
		Broadcaster speaker = new Broadcaster();
		DesktopServer server = new DesktopServer();
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
			executor.scheduleAtFixedRate(periodicTask, 0, 1, TimeUnit.SECONDS);

			Socket clientConn = server.ListenForRemoter();
			executor.shutdown();

			try {
				server.DataTransferAndExecute(clientConn);
			} catch(IOException i) {
				continue;
			}			
		}
	}
}
