package code.emailauto.com.easyemailautocomplete;

import android.app.Activity;
import android.text.Editable;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import code.emailauto.com.easyemailautocomplete.ui.EmailEdittext;

/**
 * Created by karthik on 2/6/2018.
 */

public class MainActivityPresenter {

    private Activity mActivity;

    public MainActivityPresenter (Activity mActivity) {
        this.mActivity = mActivity;
    }

    void setEmailValidityStatusMessage (String message) {
        getMessageTextView ().setText(message);
    }

    public EmailEdittext getEmailEditText () {
        return (EmailEdittext)mActivity.findViewById(R.id.emailEditText);
    }
    public TextView getMessageTextView () {
        return (TextView)mActivity.findViewById(R.id.messageTextView);
    }
}
