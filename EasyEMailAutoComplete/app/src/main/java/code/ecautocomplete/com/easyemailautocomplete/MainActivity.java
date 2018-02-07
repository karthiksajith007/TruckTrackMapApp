package code.ecautocomplete.com.easyemailautocomplete;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import code.ecautocomplete.com.easyemailautocomplete.popup.EmailAutoCompletePopup;

public class MainActivity extends AppCompatActivity implements EmailAutoCompletePopup.IEmailAutoCompletePopup, TextWatcher {

    private EmailAutoCompletePopup emailAutoCompletePopup;

    private MainActivityPresenter mainActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*KickBoxApi kickBoxApi = new KickBoxApi("KEY");
        boolean valid = kickBoxApi.verify("your_email@gmail.com");*/

        mainActivityPresenter = new MainActivityPresenter(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                emailAutoCompletePopup = new EmailAutoCompletePopup(MainActivity.this, mainActivityPresenter.getEmailEditText (), MainActivity.this);
            }
        }, 500);

        mainActivityPresenter.getEmailEditText ().addTextChangedListener(this);
    }

    @Override
    public void onPopupItemSelected(String stringItem) {
        mainActivityPresenter.margeEmail(stringItem);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence emailCharSequence, int start, int before, int count) {
        if (emailAutoCompletePopup != null) {
            List<String> emailList = EmailActivityLogics.getMatchingEmails(emailCharSequence.toString());
            if (!emailList.isEmpty()) {
                emailAutoCompletePopup.getPopupWindow().show();
                emailAutoCompletePopup.addContents(emailList);
                mainActivityPresenter.getEmailEditText ().requestFocus();
            } else {
                emailAutoCompletePopup.getPopupWindow().dismiss();
                emailAutoCompletePopup.clearContents();
            }
            if (EmailActivityLogics.isEmailValid(emailCharSequence.toString())) {
                mainActivityPresenter.getMessageTextView ().setText("Vaild email!!");
            } else {
                mainActivityPresenter.getMessageTextView ().setText("Invaild email. Please correct");
            }
        }
    }
    @Override
    public void afterTextChanged(Editable s) {}
}
