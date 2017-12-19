package code.truckmap.com.truckmap;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;


/*import io.socket.client.IO;*/
/*import io.socket.client.Socket;*/
/*import io.socket.emitter.Emitter;*/


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    private GoogleMap map;

    private GPSTracker gpsTracker;
    private Handler handler;
    private Timer timer;

    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        if (map!=null){
            Marker hamburg = map.addMarker(new MarkerOptions().position(HAMBURG)
                    .title("Hamburg"));
            Marker kiel = map.addMarker(new MarkerOptions()
                    .position(KIEL)
                    .title("Kiel")
                    .snippet("Kiel is cool")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
        }

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


        gpsTracker = new GPSTracker(this);
        handler = new Handler();
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        /*if(flag){
                            pastDistance.setLatitude(gpsTracker.getLocation().getLatitude());
                            pastDistance.setLongitude(gpsTracker.getLocation().getLongitude());
                            flag = false;
                        }else{
                            currentDistance.setLatitude(gpsTracker.getLocation().getLatitude());
                            currentDistance.setLongitude(gpsTracker.getLocation().getLongitude());
                            flag = comapre_LatitudeLongitude();
                        }*/
                        latitude = gpsTracker.getLocation().getLatitude();
                        longitude = gpsTracker.getLocation().getLongitude();
                        Toast.makeText(MainActivity.this, "latitude:"+gpsTracker.getLocation().getLatitude(), Toast.LENGTH_SHORT).show();

                    }
                });


            }
        };

        timer.schedule(timerTask,0, 5000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        gpsTracker.stopUsingGPS();
    }
}
