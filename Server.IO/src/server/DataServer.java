package server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class DataServer {
	
	interface IDataServer {
		boolean onDataReceived (String data);
	}
	
	private IDataServer interfaceServer;
	
	private ServerSocket serverSocket;
	
	public DataServer (IDataServer interfaceServer) {
		this.interfaceServer = interfaceServer;
	}

	public void startServer (int portNumber) {
		serverSocket = null;
        try {
        	serverSocket = new ServerSocket(portNumber);
        	System.out.println("Socket is listning to port "+portNumber);
        	
            while (true) {
                Socket socket = serverSocket.accept();
                try {
                	String data = getData (socket);
                	System.out.println("data from client="+data);
                	
                	if (interfaceServer.onDataReceived(data)) {
                		writeResponse (socket.getOutputStream());	
                	} else {
                		System.out.println("Auth rejected from client.");	
                	}
                } finally {
                    //socket.close();
                }
            }
        } catch (Exception exception) {
        	exception.printStackTrace();
        } finally {
        	 closeSocket (); 
        }
	}
	
	public void stopServer () {
		closeSocket ();
	}
	
	private void closeSocket () {
		try {
        	if (serverSocket!=null) {
        		serverSocket.close();
        	}
    	 } catch (Exception exception) {
         	exception.printStackTrace();
         }
	}
	
	private String getData (Socket socket) throws IOException {
		byte []dataBytes = new byte [1024];
    	socket.getInputStream().read(dataBytes);
    	return new String (dataBytes);
	}
	private void writeResponse (OutputStream outputStream) {
		PrintWriter out = new PrintWriter(outputStream, true);
        out.println(new Date().toString()+":ACK");
	}
}
