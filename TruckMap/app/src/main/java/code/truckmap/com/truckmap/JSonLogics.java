package code.truckmap.com.truckmap;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by karthik on 12/20/2017.
 */

public class JSonLogics {

    public static class JSonItem {
        public String key;
        public String value;
        public JSonItem (String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    public static JSONObject getJSonObjectForItems (JSonItem... jSonItemArray) {
        JSONObject jsonObject = new JSONObject();
        for (JSonItem jSonItem : jSonItemArray) {
            try {
                jsonObject.put(jSonItem.key, jSonItem.value);
            } catch (JSONException e) {
                e.printStackTrace();
                jsonObject = null;
                break;
            }
        }
        return jsonObject;
    }

    public static boolean isResponseAuthValid (JSONObject jsonObject) {
        boolean isValid;
        //{"data":{"car_id":1,"driver_id":100,"order":1,"status":"READY","uid":"some string"},"status":true}
        try {
            String statusString = jsonObject.getJSONObject("data").getString("status");
            if (statusString.equals("READY")) {
                isValid = true;
            } else {
                isValid = false;
            }
            Log.i("HttpClient", "statusString="+statusString);
        } catch (JSONException e) {
            e.printStackTrace();
            isValid = false;
        }
        return isValid;
    }
}
