import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.io.*;

public class Client {

	private Client() {
	}

	public static void main(String[] args) throws IOException {
		String host = (args.length < 1) ? null : args[0];
		try {
			Registry registry = LocateRegistry.getRegistry(host);
			Hello stub = (Hello) registry.lookup("Hello");

			
		   /*String response = stub.sayHello();
			 System.out.println("response: " + response); int test =
			 stub.throwInt(); System.out.println(test);

			char userEntry; // User's entry
			System.out.println("Enter characters. Enter a Q to quit");

			while ((userEntry = getChar()) != 'Q') {
				if (userEntry == 'w') {
					//System.out.println(stub.up());
				}
				System.out.println("User entered " + userEntry);
			}*/

		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}

	// *******************************************************
	// method getChar(): retrieve a single char from System.in
	// *******************************************************
	static public char getChar() throws IOException {
		char ch = (char) System.in.read();
		flushInput(); // This clears out System.in
		return ch;
	}

	// *******************************************************
	// method flushInput: reads System.in until newline
	// *******************************************************
	static public void flushInput() throws IOException {
		while ((char) System.in.read() != '\n')
			; // do nothing
	}

}