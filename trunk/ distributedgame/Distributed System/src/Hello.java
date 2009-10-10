import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Hello extends Remote {

	// test Server
	void testServer() throws RemoteException;

	// called by clients to register for server callbacks
	void registerForNotification(HelloClient n) throws RemoteException;

	// Go Up
	void goUp(HelloClient n) throws RemoteException;
	
	// Go Down
	void goDown(HelloClient n) throws RemoteException;
	
	// Go Left
	void goLeft(HelloClient n) throws RemoteException;
	
	// Go Right
	void goRight(HelloClient n) throws RemoteException;
}
