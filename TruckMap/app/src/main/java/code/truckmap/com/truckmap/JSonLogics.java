package code.truckmap.com.truckmap;

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
}
