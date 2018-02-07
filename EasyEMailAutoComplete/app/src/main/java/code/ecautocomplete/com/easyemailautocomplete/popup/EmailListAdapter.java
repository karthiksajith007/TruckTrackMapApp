package code.ecautocomplete.com.easyemailautocomplete.popup;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import code.ecautocomplete.com.easyemailautocomplete.R;

/**
 * Created by karthik on 2/6/2018.
 */

public class EmailListAdapter extends BaseAdapter {

    private List<String> emailList;
    private Context mContext;

    public EmailListAdapter (Context mContext) {
        emailList = new ArrayList<String>();
        this.mContext = mContext;
    }

    public void setContents (List<String> emailList) {
        this.emailList.clear();
        this.emailList.addAll(emailList);
        notifyDataSetChanged();
    }
    public void clearContents () {
        this.emailList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return emailList.size();
    }

    @Override
    public String getItem(int position) {
        return emailList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.email_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.emailTextView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(emailList.get(position));
        return convertView;
    }
    private class ViewHolder {
        TextView textView;
    }
}
