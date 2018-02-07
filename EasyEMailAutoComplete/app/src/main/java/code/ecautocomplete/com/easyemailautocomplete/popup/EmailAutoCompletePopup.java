package code.ecautocomplete.com.easyemailautocomplete.popup;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import code.ecautocomplete.com.easyemailautocomplete.R;

/**
 * Created by karthik on 2/6/2018.
 */

public class EmailAutoCompletePopup implements AdapterView.OnItemClickListener {



    public interface IEmailAutoCompletePopup {
        void onPopupItemSelected (String stringItem);
    }

    private View attachmentLayout;

    private ListPopupWindow mPopupWindow;

    private EmailListAdapter emailListAdapter;

    private IEmailAutoCompletePopup iEmailAutoCompletePopup;

    public EmailAutoCompletePopup (Context context, View attachmentLayout, IEmailAutoCompletePopup emailAutoCompletePopup) {
        this.iEmailAutoCompletePopup = emailAutoCompletePopup;
        this.attachmentLayout = attachmentLayout;
        emailListAdapter = new EmailListAdapter(context);
        mPopupWindow = new ListPopupWindow(context);
        mPopupWindow.setAdapter(emailListAdapter);
        mPopupWindow.setAnchorView(attachmentLayout);
        mPopupWindow.setModal(false);
        mPopupWindow.setAdapter(emailListAdapter);
        mPopupWindow.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (iEmailAutoCompletePopup != null) {
            mPopupWindow.dismiss();
            iEmailAutoCompletePopup.onPopupItemSelected(emailListAdapter.getItem(position));
        }
    }
    public ListPopupWindow getPopupWindow () {
        return mPopupWindow;
    }

    public void addContents (List<String> emailList) {
        emailListAdapter.setContents(emailList);
    }
    public void clearContents () {
        emailListAdapter.clearContents();
    }
}
