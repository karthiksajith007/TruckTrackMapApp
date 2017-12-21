package server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class DataServer {
	
	private ServerSocket serverSocket;
	
	public DataServer () {
	}

	public void startServer (int portNumber) {
		serverSocket = null;
        try {
        	serverSocket = new ServerSocket(portNumber);
        	System.out.println("Socket is listning to port "+portNumber);

        	while (true) {
        		System.out.println("Waiting for client...");
	            Socket socket = serverSocket.accept();
            	System.out.println("Client connected.");
	            while (true) {
	                try {
	                	String data = getData (socket);
	                	if (data == null) {
	                		socket.close();
	                		break;
	                	}
	                	System.out.println("data from client="+data);                	
	                	writeResponse (socket.getOutputStream());
	                } catch (Exception exception) {
	                	break;
	                }
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
    	if (dataBytes.length == 0) {
    		return null;
    	}
    	return new String (dataBytes);
	}
	private void writeResponse (OutputStream outputStream) {
		PrintWriter out = new PrintWriter(outputStream, true);
        out.println(new Date().toString()+":ACK");
	}
}
