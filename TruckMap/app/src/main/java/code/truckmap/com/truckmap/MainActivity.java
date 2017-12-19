package code.truckmap.com.truckmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


/*import io.socket.client.IO;*/
/*import io.socket.client.Socket;*/
/*import io.socket.emitter.Emitter;*/


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*try {
            final Socket socket = IO.socket("http://localhost");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    socket.emit("foo", "hi");
                    socket.disconnect();
                }

            }).on("event", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }

            });
            socket.connect();
        } catch (Exception exception) {
            exception.printStackTrace();
        }*/

        try {
            Socket socket = new Socket("107.108.32.116", 9090);
            socket.getOutputStream().write("Hello Karthik".getBytes());
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String answer = input.readLine();

        } catch (Exception e) {

        }
    }
}
