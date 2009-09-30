import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HelloClient extends Remote{
	public void notify(Integer reason) throws RemoteException;
	
	public String testClient() throws RemoteException;
}
