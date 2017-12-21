package code.truckmap.com.truckmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by karthik on 12/21/2017.
 */

public class HttpRequestThread extends Thread {

    public interface IHttpRequestThread {
        void onHttpResponse (JSONObject responseJson, int requestID);
    }

    private WeakReference <IHttpRequestThread> iHttpRequestThreadRef;
    private String url;
    private int requestID;

    public HttpRequestThread (IHttpRequestThread iHttpRequestThread, String url, int requestID) {
        this.iHttpRequestThreadRef = new WeakReference<IHttpRequestThread>(iHttpRequestThread);
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

        IHttpRequestThread iHttpRequestThread = getListener ();
        if (iHttpRequestThread!=null) {
            iHttpRequestThread.onHttpResponse(responseJson, requestID);
        }
    }

    private IHttpRequestThread getListener () {
        IHttpRequestThread iHttpRequestThread = null;
        if (iHttpRequestThreadRef != null) {
            iHttpRequestThread = iHttpRequestThreadRef.get();
        }
        return iHttpRequestThread;
    }
}
