import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HelloClient extends Remote {

	public String testClient() throws RemoteException;

	// get X
	int getX() throws RemoteException;

	// get Y
	int getY() throws RemoteException;

	// set X
	void setX(int tempX) throws RemoteException;

	// set Y
	void setY(int tempY) throws RemoteException;

	// get ID;
	int getID() throws RemoteException;

	// set ID
	void setID(int tempID) throws RemoteException;

	// get ClientMap
	int[][] getClientMap() throws RemoteException;

	// set ClientMap
	void setClientMap(int[][] tempClientMap) throws RemoteException;
	
	// set Map size
	void setMapSize(int tempMapSize) throws RemoteException;
	
	// show Message from Server
	void showMessage(String msg) throws RemoteException;
	
	// show map
	void showMap() throws RemoteException;
	
}
