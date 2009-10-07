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

import sun.org.mozilla.javascript.internal.Synchronizer;

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

	// The No. of last Treasures
	public static int LastTreasures;

	public Server() throws RemoteException {
		super();
		NumOfClient = 0;
		ClientList = new Vector();
		FirstRegistry = false;
		RegistryTime = false;
		initMap();
		initTreasure();
		LastTreasures = M;
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
			int X = r.nextInt(N);
			int Y = r.nextInt(N);
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
			int Counter = 0;
			while (Counter < ClientList.size()) {
				try {
					updateAllClientMap();
					for (Enumeration clients = ClientList.elements(); clients
							.hasMoreElements();) {
						HelloClient thingToNotify = (HelloClient) clients
								.nextElement();
						thingToNotify.setGamebegin(true);
						Counter++;
					}
				} catch (Exception e) {
					e.printStackTrace();
					ClientList.remove(Counter);
				}
			}
		}
	}

	static public void judge() {
		int MaxTreasure = 0;
		int Counter = 0;
		while (Counter < ClientList.size()) {
			try {
				for (Enumeration clients = ClientList.elements(); clients
						.hasMoreElements();) {
					HelloClient thingToNotify = (HelloClient) clients
							.nextElement();
					thingToNotify.setGamebegin(false);
					if (thingToNotify.getTreasureNumber() > MaxTreasure)
						MaxTreasure = thingToNotify.getTreasureNumber();
				}

				for (Enumeration clients = ClientList.elements(); clients
						.hasMoreElements();) {
					HelloClient thingToNotify = (HelloClient) clients
							.nextElement();
					if (thingToNotify.getTreasureNumber() < MaxTreasure) {
						thingToNotify.showMessage("You LOSER!");
					} else {
						thingToNotify.showMessage("Hello Lucky Gay!");
					}
				}
				Counter++;
			} catch (Exception e) {
				e.printStackTrace();
				ClientList.remove(Counter);
			}
		}
	}

	@Override
	public void registerForNotification(HelloClient n) throws RemoteException {

		if (FirstRegistry == false) {
			FirstRegistry = true;
			registryTime(20);
		}

		if (FirstRegistry == true && RegistryTime == true) {
			NumOfClient++;
			Random r = new Random();
			int X = r.nextInt(N);
			int Y = r.nextInt(N);
			while (map[X][Y] != 0) {
				X = r.nextInt(N);
				Y = r.nextInt(N);
			}
			n.setX(X);
			n.setY(Y);
			n.setID(NumOfClient);
			ClientList.addElement(n);
			n.showMessage("Registry OK!");
			map[X][Y] = NumOfClient;
			n.setMapSize(N);
			// updateAllClientMap();
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
	public void updateAllClientMap() {
		int Counter = 0;
		while (Counter < ClientList.size()) {
			try {
				for (Enumeration clients = ClientList.elements(); clients
						.hasMoreElements();) {
					System.out.println("Counter in FOr"+Counter);
					HelloClient thingToNotify = (HelloClient) clients
							.nextElement();
					thingToNotify.setClientMap(map);
					thingToNotify.showMap();
					Counter++;
				}
			} catch (Exception e) {
				ClientList.remove(Counter);
			}
		}
	}

	@Override
	public void goUp(HelloClient n) throws RemoteException {
		// TODO ½áÊøÌõ¼þ
		System.out.println("UP");
		int tempX = n.getX();
		int tempY = n.getY();
		int tempTreasureNumber = n.getTreasureNumber();
		int tempID = n.getID();
		if (tempX == 0) {
		} else if (map[tempX - 1][tempY] <= 0) {
			
				LastTreasures = LastTreasures - Math.abs(map[tempX - 1][tempY]);
				n.setX(tempX - 1);
				n.setY(tempY);
				n.setTreasureNumber(tempTreasureNumber
						+ Math.abs(map[tempX - 1][tempY]));
				map[tempX - 1][tempY] = tempID;
				map[tempX][tempY] = 0;
				updateAllClientMap();
				showMap();
			
		}

		if (LastTreasures == 0)
			judge();

	}

	@Override
	public void goDown(HelloClient n) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("Down");

		int tempX = n.getX();
		int tempY = n.getY();
		int tempTreasureNumber = n.getTreasureNumber();
		int tempID = n.getID();
		if (tempX == N - 1) {
		} else if (map[tempX + 1][tempY] <= 0) {
			

				LastTreasures = LastTreasures - Math.abs(map[tempX + 1][tempY]);
				n.setX(tempX + 1);
				n.setY(tempY);
				n.setTreasureNumber(tempTreasureNumber
						+ Math.abs(map[tempX + 1][tempY]));
				map[tempX + 1][tempY] = tempID;
				map[tempX][tempY] = 0;
				updateAllClientMap();
				showMap();
			
		}

		if (LastTreasures == 0)
			judge();

	}

	@Override
	public void goLeft(HelloClient n) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("Left");

		int tempX = n.getX();
		int tempY = n.getY();
		int tempTreasureNumber = n.getTreasureNumber();
		int tempID = n.getID();
		if (tempY == 0) {
		} else if (map[tempX][tempY - 1] <= 0) {
			

				LastTreasures = LastTreasures - Math.abs(map[tempX][tempY - 1]);
				n.setX(tempX);
				n.setY(tempY - 1);
				n.setTreasureNumber(tempTreasureNumber
						+ Math.abs(map[tempX][tempY - 1]));
				map[tempX][tempY - 1] = tempID;
				map[tempX][tempY] = 0;
				updateAllClientMap();
				showMap();
			
		}

		if (LastTreasures == 0)
			judge();

	}

	@Override
	public void goRight(HelloClient n) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("Right");

		int tempX = n.getX();
		int tempY = n.getY();
		int tempTreasureNumber = n.getTreasureNumber();
		int tempID = n.getID();
		if (tempY == N - 1) {
		} else if (map[tempX][tempY + 1] <= 0) {
			
				n.setX(tempX);
				n.setY(tempY + 1);
				LastTreasures = LastTreasures - Math.abs(map[tempX][tempY + 1]);
				n.setTreasureNumber(tempTreasureNumber
						+ Math.abs(map[tempX][tempY + 1]));
				map[tempX][tempY + 1] = tempID;
				map[tempX][tempY] = 0;
				updateAllClientMap();
				showMap();
			
		}

		if (LastTreasures == 0)
			judge();
	}

}
