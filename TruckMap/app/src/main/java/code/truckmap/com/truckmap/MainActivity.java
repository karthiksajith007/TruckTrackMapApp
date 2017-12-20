package code.truckmap.com.truckmap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.Manifest;
import android.widget.Toast;


/*import io.socket.client.IO;*/
/*import io.socket.client.Socket;*/
/*import io.socket.emitter.Emitter;*/


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
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

    private GoogleMap map;

    private GPSTracker gpsTracker;
    private Handler handler;
    private Timer timer;

    private double latitude, longitude;

    final LatLng HAMBURG = new LatLng(53.558, 9.927);
    final LatLng KIEL = new LatLng(53.551, 9.993);

    private Marker myLocationMarker;

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i ("Semso", "Map onCreate");
        mapView = (MapView)findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                if (map!=null){
                    Log.i ("Semso", "Map is NOT null");
                    Marker hamburg = map.addMarker(new MarkerOptions().position(HAMBURG).title("Hamburg"));
                    Marker kiel = map.addMarker(new MarkerOptions().position(KIEL).title("Kiel").snippet("Kiel is cool").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
                    myLocationMarker = map.addMarker(new MarkerOptions().position(KIEL).title("MyLocation").snippet("My location is India").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
                    hamburg.setPosition(HAMBURG);
                    kiel.setPosition(KIEL);
                } else {
                    Log.i ("Semso", "Map is null");
                }
            }
        });
       /* ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                if (map!=null){
                    Marker hamburg = map.addMarker(new MarkerOptions().position(HAMBURG).title("Hamburg"));
                    Marker kiel = map.addMarker(new MarkerOptions().position(KIEL).title("Kiel").snippet("Kiel is cool").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));

                    //hamburg.setPosition(new LatLng(1,1));
                }
            }
        });//getMap();*/


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

        /*try {
            Socket socket = new Socket("107.108.32.116", 9090);
            socket.getOutputStream().write("Hello Karthik".getBytes());
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String answer = input.readLine();

        } catch (Exception e) {

        }*/


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
                        myLocationMarker.setPosition(new LatLng(latitude, longitude));
                        myLocationMarker.
                        Toast.makeText(MainActivity.this, "latitude:"+gpsTracker.getLocation().getLatitude(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        checkLocationPermission();

        timer = new Timer ();
        timer.schedule(timerTask,0, 5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        timer.cancel();
        gpsTracker.stopUsingGPS();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location permission")
                        .setMessage("Req locatiopn perm")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        String provider = ((LocationManager)getSystemService(Context.LOCATION_SERVICE)).getBestProvider(new Criteria(), false);
                        ((LocationManager)getSystemService(Context.LOCATION_SERVICE)).requestLocationUpdates(provider, 400, 1, gpsTracker);
                    }
                    Toast.makeText(this, "onRequestPermissionsResult permission granted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "onRequestPermissionsResult permission denied.", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }
}
