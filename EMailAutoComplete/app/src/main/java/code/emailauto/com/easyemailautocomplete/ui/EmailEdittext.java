package code.emailauto.com.easyemailautocomplete.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import java.util.List;

import code.emailauto.com.easyemailautocomplete.EmailActivityLogics;
import code.emailauto.com.easyemailautocomplete.MainActivity;
import code.emailauto.com.easyemailautocomplete.popup.EmailAutoCompletePopup;

/**
 * Created by karthik on 2/7/2018.
 */

@SuppressLint("AppCompatCustomView")
public class EmailEdittext extends AutoCompleteTextView implements TextWatcher, EmailAutoCompletePopup.IEmailAutoCompletePopup {

    public interface IEmailEdittext {
        void onEMailValidityChanged (EMailValidityState emailValidityState);
    }

    public enum EMailValidityState {Valid, InValid};

    private EmailAutoCompletePopup emailAutoCompletePopup;
    private IEmailEdittext iEmailEdittext;

    private EMailValidityState emailValidityState;

    public EmailEdittext(Context context) {
        super(context);
    }

    public EmailEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmailEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        emailAutoCompletePopup = new EmailAutoCompletePopup(getContext(), this, EmailEdittext.this);
        emailValidityState = EMailValidityState.InValid;
    }

    @Override
    public void onPopupItemSelected(String stringItem) {
        margeEmail (stringItem);
    }

    @Override
    public void onTextChanged(CharSequence emailCharSequence, int start, int before, int count) {
        if (emailAutoCompletePopup != null) {
            List<String> emailList = EmailActivityLogics.getMatchingEmails(emailCharSequence.toString());
            if (!emailList.isEmpty()) {
                emailAutoCompletePopup.show();
                emailAutoCompletePopup.addContents(emailList);
                requestFocus();
            } else {
                emailAutoCompletePopup.dismiss();
                emailAutoCompletePopup.clearContents();
            }
            if (iEmailEdittext != null) {
                if (EmailActivityLogics.isEmailValid(emailCharSequence.toString())) {
                    emailValidityState = EMailValidityState.Valid;
                    iEmailEdittext.onEMailValidityChanged(EMailValidityState.Valid);
                } else {
                    emailValidityState = EMailValidityState.InValid;
                    iEmailEdittext.onEMailValidityChanged(EMailValidityState.InValid);
                }
            }
        }
    }

    public void margeEmail (String email) {
        String editTextContent = getText().toString();
        String []emailSplit = editTextContent.split("@");
        if (emailSplit.length == 2) {
            StringBuffer emailBuffer = new StringBuffer();
            emailBuffer.append(emailSplit[0]).append("@").append(email);
            setText(emailBuffer.toString());
        } else if (emailSplit.length == 1) {
            StringBuffer emailBuffer = new StringBuffer();
            emailBuffer.append(emailSplit[0]).append("@").append(email);
            setText(emailBuffer.toString());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {}

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    public IEmailEdittext getiEmailEdittext() {
        return iEmailEdittext;
    }

    public void setiEmailEdittext(IEmailEdittext iEmailEdittext) {
        this.iEmailEdittext = iEmailEdittext;
    }

    public EMailValidityState getEMailValidityState () {
        return emailValidityState;
    }
}
