package code.truckmap.com.truckmap;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by karthik on 12/20/2017.
 */

public class SocketNetworkService /*implements TaskScheduler.ITaskScheduler*/{

    /*public interface ISocketNetworkService {
        void onConnectionUpdateStatus (SocketNetworkConnectionStatus socketNetworkConnectionStatus);
    }*/
    /*public enum SocketNetworkConnectionStatus {Connected, Connecting, Disconnected};*/

    private Socket socket;

    private String ipAddr;
    private int port;

    private boolean isConnecting;
    private boolean isConnected;
    //private TaskScheduler taskScheduler;

    public SocketNetworkService(String ipAddr, int port) {
        this.ipAddr = ipAddr;
        this.port = port;
        isConnecting = false;
        isConnected = false;
        /*taskScheduler = new TaskScheduler(this, 500, 0);
        taskScheduler.startScheduler();*/
    }
    /*@Override
    synchronized public void onScheduledTask(long scheduleID) {
        if (getIsConnected()) {
            if (!isPinging) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        isPinging = true;
                        sendData("PingRing");
                        isPinging = false;
                    }
                }).start();
            }
        }
    }*/

    synchronized public void openConnection () {
        isConnected = false;
        if (!isConnecting) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    isConnecting = true;
                    try {
                        socket = new Socket(ipAddr, port);
                        isConnected = true;
                    } catch (IOException e) {
                        isConnected = false;
                        e.printStackTrace();
                    }
                    isConnecting = false;
                }
            }).start();
        }
    }

    public boolean closeConnection () {
        boolean isSuccess;
        try {
            if (socket!=null) {
                socket.close();
            }
            //taskScheduler.stopScheduler();
            isConnected = false;
            isSuccess = true;
        } catch (IOException e) {
            e.printStackTrace();
            isSuccess = false;
        }
        return isSuccess;
    }

    public String sendData (double latitude, double longitude) {
        return sendData (latitude+","+longitude);
    }

    public String sendData (JSONObject jsonObject) {
        return sendData (jsonObject.toString());
    }

    synchronized public String sendData (String stringData) {
        String answer;
        try {
            if (socket!=null) {
                socket.getOutputStream().write(stringData.getBytes());
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                answer = input.readLine();
                isConnected = true;
            } else {
                answer = null;
                isConnected = false;
            }
        } catch (Exception exception) {
            Log.i ("response", "ip="+ipAddr+":"+ port);
            exception.printStackTrace();
            answer = null;
            isConnected = false;
        }
        return answer;
    }

    public boolean getIsConnected () {
        boolean isConnected = false;
        if (socket!=null && socket.isConnected() && this.isConnected) {
            isConnected = true;
        }
        return isConnected;
    }
}
