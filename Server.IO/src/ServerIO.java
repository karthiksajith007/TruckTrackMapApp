import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ServerIO {

	public static void main (String args[]) {
		ServerSocket listener = null;
        try {
        	listener = new ServerSocket(9090);
        	System.out.println("Socket is listning to port 9090");
            while (true) {
                Socket socket = listener.accept();
                try {
                	byte []dataBytes = new byte [1024];
                	socket.getInputStream().read(dataBytes);
                	System.out.println("dataBytes="+new String (dataBytes));
                	
                    PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                    out.println(new Date().toString()+":Karthik");
                } finally {
                    socket.close();
                }
            }
        } catch (Exception exception) {
        	exception.printStackTrace();
        } finally {
        	 try {
	        	if (listener!=null) {
	        		listener.close();
	        	}
        	 } catch (Exception exception) {
             	exception.printStackTrace();
             } 
        }
	}
}
