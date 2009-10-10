import java.rmi.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.*;

public class Client implements HelloClient {

	static int X, Y;

	static int ID;

	static int[][] ClientMap;

	static int MapSize;

	static int TreasureNumber;

	static Boolean Gamebegin;

	Client() {
		X = 0;
		Y = 0;
		ID = 0;
		MapSize = 0;
		TreasureNumber = 0;
		Gamebegin = false;
		try {
			UnicastRemoteObject.exportObject(this, 0);
		} catch (RemoteException re) {
			re.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		String host = (args.length < 1) ? null : args[0];
		try {
			Registry registry = LocateRegistry.getRegistry(host);
			Hello stub = (Hello) registry.lookup("Hello");
			Client client = new Client();
			stub.registerForNotification(client);

			while (Gamebegin != true) {

			}

			// stub.testServer();

			char userEntry; // User's entry

			while ((userEntry = getChar()) != 'Q') {
				if (userEntry == 'w') {
					stub.goUp(client);
				}
				if (userEntry == 's') {
					stub.goDown(client);
				}
				if (userEntry == 'a') {
					stub.goLeft(client);
				}
				if (userEntry == 'd') {
					stub.goRight(client);
				}
			}

		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}

	// method getChar(): retrieve a single char from System.in
	static public char getChar() throws IOException {
		char ch = (char) System.in.read();
		flushInput(); // This clears out System.in
		return ch;
	}

	// method flushInput: reads System.in until newline
	static public void flushInput() throws IOException {
		while ((char) System.in.read() != '\n')
			; // do nothing
	}

	@Override
	public String testClient() throws RemoteException {
		System.out.println("Call back OK!");
		return "Callback OK!";
	}

	@Override
	public int getX() throws RemoteException {
		return X;
	}

	@Override
	public int getY() throws RemoteException {
		return Y;
	}

	@Override
	public void setX(int tempX) throws RemoteException {
		X = tempX;
	}

	@Override
	public void setY(int tempY) throws RemoteException {
		Y = tempY;
	}

	@Override
	public int getID() throws RemoteException {
		return ID;
	}

	@Override
	public void setID(int tempID) throws RemoteException {
		ID = tempID;
	}

	@Override
	public int[][] getClientMap() throws RemoteException {
		return ClientMap;
	}

	@Override
	public void setClientMap(int[][] tempClientMap) throws RemoteException {

		ClientMap = tempClientMap.clone();
	}

	@Override
	public void showMessage(String msg) throws RemoteException {
		System.out.println(msg);
	}

	@Override
	public void setMapSize(int tempMapSize) throws RemoteException {
		MapSize = tempMapSize;
	}

	@Override
	public void showMap() throws RemoteException {
		for (int i = 0; i < MapSize; i++) {
			for (int j = 0; j < MapSize; j++) {
				System.out.print(ClientMap[i][j]);
				System.out.print("   ");
			}
			System.out.println();
		}
		System.out.println("Enter characters. Enter a Q to quit");
	}

	@Override
	public int getTreasureNumber() throws RemoteException {
		return TreasureNumber;
	}

	@Override
	public void setTreasureNumber(int tempTreasureNumber)
			throws RemoteException {
		TreasureNumber = tempTreasureNumber;
	}

	@Override
	public void setGamebegin(Boolean tempGamebegin) throws RemoteException {
		Gamebegin = tempGamebegin;
	}

}
