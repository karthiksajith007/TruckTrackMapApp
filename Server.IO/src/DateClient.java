import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class DateClient {

    /**
     * Runs the client as an application.  First it displays a dialog
     * box asking for the IP address or hostname of a host running
     * the date server, then connects to it and displays the date that
     * it serves.
     */
    public static void main(String[] args) throws IOException {
        String serverAddress = "localhost";//JOptionPane.showInputDialog("Enter IP Address of a machine that is\n" +"running the date service on port 9090:");
        Socket socket = new Socket(serverAddress, 9090);
        socket.getOutputStream().write("Hello Karthik".getBytes());
        
        BufferedReader input =
            new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String answer = input.readLine();
        
        System.out.println(answer);
        //JOptionPane.showMessageDialog(null, answer);
        
        socket.close();
        System.exit(0);
    }
}