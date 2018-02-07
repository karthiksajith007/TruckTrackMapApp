package code.emailauto.com.easyemailautocomplete.async;

import android.os.AsyncTask;


/**
 * Created by karthik on 2/7/2018.
 */

public class EmailVerificationAsync extends AsyncTask <Void, Boolean, Boolean> {

    public interface IEmailVerificationAsync {
        void onEmailDeliverableVerification (EmailDeliverableVerificationStatus emailDeliverableVerificationStatus, String emailVerified);
    }

    public enum EmailDeliverableVerificationStatus {Valid, Invalid};

    private IEmailVerificationAsync iEmailVerificationAsync;
    private String emailToVerify;

    public EmailVerificationAsync (IEmailVerificationAsync iEmailVerificationAsync, String emailToVerify) {
        this.iEmailVerificationAsync = iEmailVerificationAsync;
        this.emailToVerify = emailToVerify;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        /*KickBoxApi kickBoxApi = new KickBoxApi("test_7aa5acb0ec450fbeabba2b927999feb0508a4bbd380c684ca16d7ffde174b4e3");
        return kickBoxApi.verify("your_email@gmail.com");*/
        return false;
    }

    @Override
    protected void onPostExecute(Boolean verification) {
        super.onPostExecute(verification);
        if (iEmailVerificationAsync != null) {
            EmailDeliverableVerificationStatus emailDeliverableVerificationStatus = verification==true ? EmailDeliverableVerificationStatus.Valid
                    : EmailDeliverableVerificationStatus.Invalid;
            iEmailVerificationAsync.onEmailDeliverableVerification(emailDeliverableVerificationStatus, emailToVerify);
        }
    }
}
