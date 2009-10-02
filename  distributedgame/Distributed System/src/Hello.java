import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Hello extends Remote {
	
	// test Server
	void testServer() throws RemoteException;
	
	// called by clients to register for server callbacks
    void registerForNotification(HelloClient n) throws RemoteException;
    

}
