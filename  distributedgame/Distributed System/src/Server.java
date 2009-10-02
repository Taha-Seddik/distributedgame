import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server implements Hello {

	// Number of Clients
	private static int NumOfClient;

	// To hold the registered clients
	private static Vector ClientList = null;

	// for the first regitry
	private static Boolean FirstRegistry;

	// for the registry time
	private static Boolean RegistryTime;

	// The Global Map
	public static int[][] map;

	// The Size of the Map
	public static int N;

	// The No. of Treasures
	public static int M;

	public Server() throws RemoteException {
		super();
		NumOfClient = 0;
		ClientList = new Vector();
		FirstRegistry = false;
		RegistryTime = false;
		initMap();
		initTreasure();
	}

	public static void main(String args[]) {
		Hello stub = null;
		Registry registry = null;

		try {
			Server obj = new Server();
			stub = (Hello) UnicastRemoteObject.exportObject(obj, 0);
			registry = LocateRegistry.getRegistry();
			registry.bind("Hello", stub);
			System.err.println("Server ready");

			showMap();
		} catch (Exception e) {
			try {
				registry.unbind("Hello");
				registry.bind("Hello", stub);
				System.err.println("Server ready");
			} catch (Exception ee) {
				System.err.println("Server exception: " + ee.toString());
				ee.printStackTrace();
			}
		}
	}

	public static void initMap() {
		System.out.println("Please input N -- size of the map:");
		N = getInt();
		map = new int[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				map[i][j] = 0;
			}
		}
	}

	public static void initTreasure() {
		System.out.println("Please input M -- number of treasures:");
		M = getInt();

		// We need to get M random positions
		for (int i = 0; i < M; i++) {
			Random r = new Random();
			int X = r.nextInt(N - 1);
			int Y = r.nextInt(N - 1);
			map[X][Y]--;
		}
	}

	public static int getInt() {
		int result = 0;
		try {
			BufferedReader is = new BufferedReader(new InputStreamReader(
					System.in));
			result = Integer.parseInt(is.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void showMap() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				System.out.print(map[i][j]);
				System.out.print("   ");
			}
			System.out.println();
		}
	}

	public static Timer timer;

	public void registryTime(int seconds) {
		timer = new Timer();
		System.out.println("registry time begin");
		RegistryTime = true;
		timer.schedule(new RemindTask(), seconds * 1000);
	}

	class RemindTask extends TimerTask {
		public void run() {
			RegistryTime = false;
			System.out.println("registry time over");
		}
	}

	@Override
	public void registerForNotification(HelloClient n) throws RemoteException {

		if (FirstRegistry == false) {
			FirstRegistry = true;
			registryTime(30);
		}

		if (FirstRegistry == true && RegistryTime == true) {
			NumOfClient++;
			Random r = new Random();
			int X = r.nextInt(N - 1);
			int Y = r.nextInt(N - 1);
			while (map[X][Y] != 0) {
				X = r.nextInt(N - 1);
				Y = r.nextInt(N - 1);
			}
			n.setX(X);
			n.setY(Y);
			n.setID(NumOfClient);
			ClientList.addElement(n);
			n.showMessage("Registry OK!");
			map[X][Y] = NumOfClient;
			n.setMapSize(N);
			updateAllClientMap();
			showMap();
		}

		if (FirstRegistry == true && RegistryTime == false) {
			n.showMessage("Registry time is over!");
		}
	}

	@Override
	public void testServer() throws RemoteException {
		for (Enumeration clients = ClientList.elements(); clients
				.hasMoreElements();) {
			HelloClient thingToNotify = (HelloClient) clients.nextElement();
			System.out.println(thingToNotify.testClient());
		}
	}

	@Override
	public void updateAllClientMap() throws RemoteException {
		for (Enumeration clients = ClientList.elements(); clients
				.hasMoreElements();) {
			HelloClient thingToNotify = (HelloClient) clients.nextElement();
			thingToNotify.setClientMap(map);
			thingToNotify.showMap();
		}
	}

	@Override
	public void goUp(HelloClient n) throws RemoteException {
		// TODO Auto-generated method stub

	}

}
