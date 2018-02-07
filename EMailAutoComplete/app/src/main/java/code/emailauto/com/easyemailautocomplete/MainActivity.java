package code.emailauto.com.easyemailautocomplete;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import code.emailauto.com.easyemailautocomplete.async.EmailVerificationAsync;
import code.emailauto.com.easyemailautocomplete.ui.EmailEdittext;


public class MainActivity extends AppCompatActivity implements EmailEdittext.IEmailEdittext, EmailVerificationAsync.IEmailVerificationAsync {

    final public static List<String> emailStorageList = Arrays.asList("gmail.com", "yahoo.com", "ymail.com", "reddiff.com",
                                                                                "outlook.com", "aol.com", "yandex.com", "protonmail.com" , "zoho.com", "mail.com");

    private MainActivityPresenter mainActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityPresenter = new MainActivityPresenter(this);
        mainActivityPresenter.getEmailEditText().setiEmailEdittext(this);
    }

    @Override
    public void onEMailValidityChanged(EmailEdittext.EMailValidityState emailValidityState) {
        if (emailValidityState == EmailEdittext.EMailValidityState.Valid) {
            String email = mainActivityPresenter.getEmailEditText().getText().toString();
            //new EmailVerificationAsync (this, email).execute();
            mainActivityPresenter.setEmailValidityStatusMessage("Vaild email!!");
        } else if (emailValidityState == EmailEdittext.EMailValidityState.InValid) {
            mainActivityPresenter.setEmailValidityStatusMessage("Invaild email. Please correct");
        }
    }
    @Override
    public void onEmailDeliverableVerification(EmailVerificationAsync.EmailDeliverableVerificationStatus emailDeliverableVerificationStatus, String emailVerified) {
        Log.i ("007", "email: "+emailVerified);
        Log.i ("007", "status: "+emailDeliverableVerificationStatus.name());
    }
}
