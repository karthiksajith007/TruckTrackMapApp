package server;

public class ServerMain {
	
	private final int portNumber = 9090;
	
	public ServerMain () {
		new DataServer ().startServer(portNumber);
	}

	public static void main (String args[]) {		
		new ServerMain ();
	}
}
