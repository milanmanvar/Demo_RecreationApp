package com.milan.recreationapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.milan.recreationapp.R;
import com.milan.recreationapp.model.ClubTimeTable_New;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by utsav.k on 13-04-2016.
 */
public class MyClassListAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<ClubTimeTable_New> mData = new ArrayList<ClubTimeTable_New>();
    private TreeSet<String> sectionHeader = new TreeSet<String>();

    private LayoutInflater mInflater;

    public MyClassListAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final ClubTimeTable_New item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final ClubTimeTable_New item) {
        mData.add(item);
        sectionHeader.add(item.getDay().toString().trim());
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(mData.get(position).getDay().toString().trim()) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ClubTimeTable_New getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.row_timetable, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.row_timetable_txtName);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.row_timetable_title, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.row_timetable_txtTitle);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (rowType == TYPE_ITEM)
            holder.textView.setText(mData.get(position).getTime() + " " + mData.get(position).getClassName());
        else
            holder.textView.setText(mData.get(position).getDay());

        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
    }

}
