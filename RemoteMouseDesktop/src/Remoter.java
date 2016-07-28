import java.io.IOException;
import java.net.SocketException;

public class Remoter {
	public static void main(String[] args) throws IOException, InterruptedException {
		Broadcaster test = new Broadcaster();
		while(true) {
			test.DoBroadcast();
			Thread.sleep(1000);
		}
	}
}
