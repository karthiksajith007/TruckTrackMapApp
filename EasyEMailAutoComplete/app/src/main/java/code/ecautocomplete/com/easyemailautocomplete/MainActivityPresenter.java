package code.ecautocomplete.com.easyemailautocomplete;

import android.app.Activity;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by karthik on 2/6/2018.
 */

public class MainActivityPresenter {

    private Activity mActivity;

    public MainActivityPresenter (Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void margeEmail (String email) {
        String editTextContent = getEmailEditText ().getText().toString();
        String []emailSplit = editTextContent.split("@");
        if (emailSplit.length == 2) {
            StringBuffer emailBuffer = new StringBuffer();
            emailBuffer.append(emailSplit[0]).append("@").append(email);
            getEmailEditText ().setText(emailBuffer.toString());
        }
    }

    public EditText getEmailEditText () {
        return (EditText)mActivity.findViewById(R.id.emailEditText);
    }
    public TextView getMessageTextView () {
        return (TextView)mActivity.findViewById(R.id.messageTextView);
    }
}
