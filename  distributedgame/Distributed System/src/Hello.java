import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Hello extends Remote {

	// Initial the Map
	// void initMap() throws RemoteException, IOException;

	// Initial the Treasure
	// void initTreasure() throws RemoteException, IOException;
	
	// test Server
	void testServer() throws RemoteException;
	
	// called by clients to register for server callbacks
    void registerForNotification(HelloClient n) throws RemoteException;
}
