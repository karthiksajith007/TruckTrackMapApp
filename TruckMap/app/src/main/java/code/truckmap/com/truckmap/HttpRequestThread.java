package code.truckmap.com.truckmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by karthik on 12/21/2017.
 */

public class HttpRequestThread extends Thread {

    public interface IHttpRequestThread {
        void onHttpResponse (JSONObject responseJson, int requestID);
    }

    private IHttpRequestThread iHttpRequestThread;
    private String url;
    private int requestID;

    public HttpRequestThread (IHttpRequestThread iHttpRequestThread, String url, int requestID) {
        this.iHttpRequestThread = iHttpRequestThread;
        this.url = url;
        this.requestID = requestID;
    }

    public void doRequestAsync() {
        start();
    }

    @Override
    public void run() {
        super.run();
        JSONObject responseJson = null;
        try {
            responseJson = HttpClient.requestPostForJSonObject(url);
        } catch (IOException e) {
            e.printStackTrace();
            responseJson = null;
        } catch (JSONException e) {
            e.printStackTrace();
            responseJson = null;
        }
        if (iHttpRequestThread!=null) {
            iHttpRequestThread.onHttpResponse(responseJson, requestID);
        }
    }
}
