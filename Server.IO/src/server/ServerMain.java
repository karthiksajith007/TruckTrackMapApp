package server;

import server.DataServer.IDataServer;

public class ServerMain implements IDataServer {
	
	private final int portNumber = 9090;
	
	public ServerMain () {
		new DataServer (this).startServer(portNumber);
	}

	@Override
	public boolean onDataReceived(String data) {
		return true;
	}

	public static void main (String args[]) {		
		new ServerMain ();
	}
}
