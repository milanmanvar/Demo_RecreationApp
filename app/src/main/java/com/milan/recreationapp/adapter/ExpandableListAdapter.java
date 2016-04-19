package com.milan.recreationapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.milan.recreationapp.R;
import com.milan.recreationapp.ReCreationApplication;
import com.milan.recreationapp.model.ClubTimeTable_New;
import com.milan.recreationapp.view.ClubClassDetailActivity;
import com.milan.recreationapp.view.SavedClassActivity;

import java.util.List;
import java.util.Map;

public class ExpandableListAdapter extends BaseExpandableListAdapter {


    private Context context;
    private List<String> expandableListTitle;
    private Map<String, List<ClubTimeTable_New>> expandableListDetail;
    private ReCreationApplication application;

    public ExpandableListAdapter(Context context,
                                 List<String> expandableListTitle,
                                 Map<String, List<ClubTimeTable_New>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        application = (ReCreationApplication) context.getApplicationContext();
    }

    public void setListData(List<String> title) {
        this.expandableListTitle = title;
    }

    @Override
    public ClubTimeTable_New getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail
                .get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final ClubTimeTable_New expandedListText = getChild(listPosition,
                expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.row_saved_class, null);
        }
        TextView expandedListTextViewName = (TextView) convertView
                .findViewById(R.id.row_timetable_txtName);

        ImageView imgEdit = (ImageView) convertView
                .findViewById(R.id.row_timetable_imgRemove);
        if (((SavedClassActivity) context).isDeleteVisible)
            imgEdit.setVisibility(View.VISIBLE);
        else
            imgEdit.setVisibility(View.GONE);
        expandedListTextViewName.setText(expandedListText.getTime() + " " + expandedListText.getClassName());
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SavedClassActivity) context).deleteSavedClass(expandedListText);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iClass = new Intent(context, ClubClassDetailActivity.class);
                iClass.putExtra("clubdaytime", expandedListText);
                context.startActivity(iClass);
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail
                .get(this.expandableListTitle.get(listPosition)).size();
    }


    @Override
    public String getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.row_timetable_title, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.row_timetable_txtTitle);
        listTitleTextView.setText(listTitle.toString().trim().substring(0, 1).toUpperCase() + listTitle.toString().trim().substring(1));
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition,
                                     int expandedListPosition) {
        return true;
    }


}