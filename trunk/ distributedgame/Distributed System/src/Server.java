import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class Server implements Hello {

	// To hold the registered clients
	private static Vector clientList = null;

	public Server() throws RemoteException {
		super();
		clientList = new Vector();
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

			initMap();
			initTreasure();
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					System.out.print(map[i][j]);
					System.out.print("   ");
				}
				System.out.println();
			}

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

	// The Global Map
	public static int[][] map;

	// The Size of the Map
	public static int N;

	// The No. of Treasures
	public static int M;

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
			map[X][Y]++;
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

	@Override
	public void registerForNotification(HelloClient n) throws RemoteException {
		clientList.addElement(n);
		System.out.println("Fuck again!");
	}

	@Override
	public void testServer() throws RemoteException {
		System.out.println("Whah");
		for (Enumeration clients = clientList.elements(); clients
				.hasMoreElements();) {
			HelloClient thingToNotify = (HelloClient) clients.nextElement();
			System.out.println(thingToNotify.testClient());
		}

	}

}
