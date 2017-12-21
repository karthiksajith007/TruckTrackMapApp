package code.truckmap.com.truckmap;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.Manifest;
import android.widget.TextView;
import android.widget.Toast;


/*import io.socket.client.IO;*/
/*import io.socket.client.Socket;*/
/*import io.socket.emitter.Emitter;*/


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements TaskScheduler.ITaskScheduler, HttpRequestThread.IHttpRequestThread/*, SocketNetworkService.ISocketNetworkService*/, OnMapReadyCallback {

    private GPSTracker gpsTracker;

    private Handler handler;
    private TaskScheduler taskScheduler;

    private enum UserAuthStatus {Valid, InValid, Requesting, NotAvailable};

    private Marker currentLocationMarker;
    private GoogleMap googleMap;
    private LatLng updatedLatLng;
    private UserAuthStatus userAuthStatus;

    public final int LOCATION_PERMISSION_REQUEST = 999;

    private SocketNetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getMapView ().onCreate(savedInstanceState);
        getMapView ().getMapAsync(this);
        gpsTracker = new GPSTracker(this);
        handler = new Handler();
        taskScheduler = new TaskScheduler(this, 10000, 0);
        if (checkLocationPermissionAndRequestIfNot()) {
            taskScheduler.startScheduler();
        }
        userAuthStatus = UserAuthStatus.Requesting;
        networkService = new SocketNetworkService("107.108.32.116", 9090);
        new HttpRequestThread (this, "http://107.108.32.116:8080/TruckServer/authrequest.json", 0).doRequestAsync();

        updateUserStatus ();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    public void onHttpResponse(JSONObject responseJson, int requestID) {
        if (responseJson == null) {
            userAuthStatus = UserAuthStatus.NotAvailable;
        } else if (JSonLogics.isResponseAuthValid(responseJson)) {
            userAuthStatus = UserAuthStatus.Valid;
            networkService.openConnection();
        } else {
            userAuthStatus = UserAuthStatus.InValid;
        }
        updateUserStatus ();
    }

    @Override
    public void onScheduledTask (long scheduleID) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Location location = gpsTracker.getLocation();
                if (location != null) {
                    getStatusText().setText("Location status : Location updated.");
                    updatedLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    updateLocation (updatedLatLng);
                    Toast.makeText(MainActivity.this, "Location updated at ("+updatedLatLng.latitude+","+updatedLatLng.longitude+")", Toast.LENGTH_SHORT).show();
                } else {
                    updatedLatLng = null;
                    getStatusText().setText("Location status : Cannot find updated location. Please check if phone's Location service is Turned On.");
                }
            }
        });
        if (updatedLatLng != null) {
            if (userAuthStatus == UserAuthStatus.Valid) {
                if (networkService!=null && networkService.getIsConnected()) {
                    JSONObject jsonObject = JSonLogics.getJSonObjectForItems(new JSonLogics.JSonItem("latitude", String.valueOf(updatedLatLng.latitude)),
                            new JSonLogics.JSonItem("longitude", String.valueOf(updatedLatLng.longitude)),
                            new JSonLogics.JSonItem("status_car", "READY"));
                    if (jsonObject != null) {
                        String response = networkService.sendData(jsonObject);
                        Log.i("response", "response=" + response);
                    }
                }
                if (networkService!=null && !networkService.getIsConnected()) {
                    networkService.openConnection();
                }
                updateNetworkStatus();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        String provider = ((LocationManager)getSystemService(Context.LOCATION_SERVICE)).getBestProvider(new Criteria(), false);
                        ((LocationManager)getSystemService(Context.LOCATION_SERVICE)).requestLocationUpdates(provider, 400, 1, gpsTracker);
                    }
                    taskScheduler.startScheduler();
                    getStatusText ().setText("Location status : Error.No permission for Location.");
                } else {
                    getStatusText ().setText("Location status : Error. Permission rejected by user.");
                }
                return;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMapView ().onResume();
        taskScheduler.resumeScheduler();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getMapView ().onPause();
        taskScheduler.pauseScheduler();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getMapView ().onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        networkService.closeConnection();
        getMapView ().onDestroy();
        taskScheduler.stopScheduler();
        gpsTracker.stopUsingGPS();
    }

    private void updateLocation (LatLng latLng) {
        if (googleMap == null) {
            return ;
        }
        if (currentLocationMarker == null) {
            currentLocationMarker = googleMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
        }
        currentLocationMarker.setPosition(latLng);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private void updateUserStatus () {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (userAuthStatus == UserAuthStatus.Valid) {
                    geUuserAuthStatusTextView ().setText("User auth status: Valid.");
                } else if (userAuthStatus == UserAuthStatus.InValid) {
                    geUuserAuthStatusTextView ().setText("User auth status: Invalid.");
                } else if (userAuthStatus == UserAuthStatus.Requesting) {
                    geUuserAuthStatusTextView ().setText("User auth status: Requesting.");
                } else if (userAuthStatus == UserAuthStatus.NotAvailable) {
                    geUuserAuthStatusTextView ().setText("User auth status: Not available.");
                } else {
                    geUuserAuthStatusTextView ().setText("User auth status: Unknown.");
                }
            }
        });
    }

    private void updateNetworkStatus () {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (networkService.getIsConnected()) {
                    getNetworkConnectionStatusTextview ().setText("Network status: Connected.");
                } else {
                    getNetworkConnectionStatusTextview ().setText("Network status: Not connected.");
                }
            }
        });
    }

    private boolean checkLocationPermissionAndRequestIfNot() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            getStatusText ().setText("Location status : Cannot find location. Please check if Location is Turned On.");
            return false;
        } else {
            getStatusText ().setText("Location status : Location permission present.");
            return true;
        }
    }

    private MapView getMapView () {
        return (MapView)findViewById(R.id.map);
    }
    private TextView getStatusText () {
        return (TextView)findViewById(R.id.statusTextView);
    }
    private TextView getNetworkConnectionStatusTextview () {
        return (TextView)findViewById(R.id.networkConnectionStatusTextview);
    }
    private TextView geUuserAuthStatusTextView () {
        return (TextView)findViewById(R.id.userAuthStatusTextView);
    }
}
