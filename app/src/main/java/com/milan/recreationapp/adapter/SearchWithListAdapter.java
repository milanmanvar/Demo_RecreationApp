package com.milan.recreationapp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.milan.recreationapp.R;
import com.milan.recreationapp.ReCreationApplication;
import com.milan.recreationapp.model.SearchWithModel;
import com.milan.recreationapp.view.SearchingWithClubActivity;

import java.util.ArrayList;

/**
 * Created by utsav.k on 07-04-2016.
 */
public class SearchWithListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<SearchWithModel> listItem;
    private LayoutInflater inflater;
    private ViewHolder holder;
    private SharedPreferences sharedPreferences;

    public SearchWithListAdapter(Context context,
                                 ArrayList<SearchWithModel> items) {
        this.context = context;
        this.listItem = items;
        inflater = LayoutInflater.from(this.context);
        sharedPreferences = ((ReCreationApplication) context.getApplicationContext()).sharedPreferences;
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public SearchWithModel getItem(int position) {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row_searchwith, null);
            holder.switchCompat = (Switch) convertView
                    .findViewById(R.id.row_searchwith_switch);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.switchCompat.setText(listItem.get(position).getName());
        if (sharedPreferences.getString("selectedclubs", sharedPreferences.getString("club","")).contains(listItem.get(position).getName())) {
            holder.switchCompat.setChecked(true);
        } else
            holder.switchCompat.setChecked(false);
        ((SearchingWithClubActivity) context).addToCheckList(listItem.get(position).getName(), holder.switchCompat.isChecked());

        holder.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listItem.get(position).setIsChecked(true);
                } else {
                    listItem.get(position).setIsChecked(false);
                }
                ((SearchingWithClubActivity) context).addToCheckList(listItem.get(position).getName(), isChecked);
            }
        });
        return convertView;
    }


    public class ViewHolder {
        private Switch switchCompat;
    }

}
