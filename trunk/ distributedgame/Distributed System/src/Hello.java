import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Hello extends Remote {

	// int throwInt() throws RemoteException;

	// String sayHello() throws RemoteException;
	
	// void joinGame() throws RemoteException;
	
	// void move(int id, int direction) throws RemoteException; 
	
	String up() throws RemoteException;
	
	String down() throws RemoteException;
	
	String left() throws RemoteException;
	
	String right() throws RemoteException;
	
	
	
}
