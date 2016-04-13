package com.milan.recreationapp.view;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.milan.recreationapp.R;
import com.milan.recreationapp.ReCreationApplication;
import com.milan.recreationapp.adapter.ExpandableListAdapter;
import com.milan.recreationapp.model.ClubTimeTable_New;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by utsav.k on 09-04-2016.
 */
public class SavedClassActivity extends Activity {

    public ExpandableListView listView;
    public boolean isDeleteVisible = false;
    private LinearLayout lTimeTable;
    private ArrayList<ClubTimeTable_New> list;
    private ReCreationApplication application;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private Map<String, List<ClubTimeTable_New>> expandableListDetail;
    private int lastExpandedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_class);
//        lTimeTable = (LinearLayout) findViewById(R.id.my_class_detail_l);
        listView = (ExpandableListView) findViewById(R.id.listView2);
        application = ((ReCreationApplication) getApplication());
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);
        setUpActionBar("My Class");
//        list = application.getDatabase().getSavedClubTimeTable();
//        String temp = "";
//        for (int i = 0; i < list.size(); i++) {
//            View vMorning = getLayoutInflater().inflate(R.layout.row_timetable_title, null);
//            TextView txtTitle = (TextView) vMorning.findViewById(R.id.row_timetable_txtTitle);
//
//            txtTitle.setText(list.get(i).getDay().toUpperCase());
//
//            if (!temp.equalsIgnoreCase(txtTitle.getText().toString().trim()))
//                lTimeTable.addView(vMorning);
//            temp = txtTitle.getText().toString().trim();
//
//            final View vBody = getLayoutInflater().inflate(R.layout.row_timetable, null);
//            TextView txtName = (TextView) vBody.findViewById(R.id.row_timetable_txtName);
//            txtName.setText(list.get(i).getTime() + " " + list.get(i).getClassName());
//
//            ImageView imgRemove = (ImageView) vBody.findViewById(R.id.row_timetable_imgRemove);
//            imgRemove.setVisibility(View.GONE);
//            final int finalI = i;
//            vBody.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent iClass = new Intent(SavedClassActivity.this, ClubClassDetailActivity.class);
//                    iClass.putExtra("clubdaytime", list.get(finalI));
//                    startActivity(iClass);
//                }
//            });
//            vBody.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    application.getDatabase().removeToMyClass(list.get(finalI).getId());
//                    lTimeTable.removeView(vBody);
//                    return false;
//                }
//            });
//            lTimeTable.addView(vBody);
//        }

//        MyClassListAdapter mAdapter = new MyClassListAdapter(this);
//        for (int i = 1; i < list.size(); i++) {
//            mAdapter.addItem(list.get(i));
//            if (!temp.equalsIgnoreCase(list.get(i).getDay().toString().trim()))
//                mAdapter.addSectionHeaderItem(list.get(i));
//            temp = list.get(i).getDay();
//        }
//        listView.setAdapter(mAdapter);
        setDataSet();
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true; // This way the expander cannot be collapsed
            }
        });

    }

    private void setUpActionBar(final String title) {
        View actionbarView = LayoutInflater.from(this).inflate(
                R.layout.actionbar_layout_for_alert, null, true);

        TextView tvTitle = (TextView) actionbarView.findViewById(R.id.actionbar_layout_tv_title);
        tvTitle.setText(title);
        TextView tvClose = (TextView) actionbarView.findViewById(R.id.actionbar_layout_tv_close);
        final TextView tvAdd = (TextView) actionbarView.findViewById(R.id.actionbar_layout_tv_add);
        tvClose.setVisibility(View.INVISIBLE);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvAdd.setText("Edit");
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableListAdapter != null) {
                    if (tvAdd.getText().toString().equalsIgnoreCase("edit")) {
                        isDeleteVisible = true;
                        expandableListAdapter.notifyDataSetChanged();
                        tvAdd.setText("Done");
                    } else {
                        isDeleteVisible = false;
                        expandableListAdapter.notifyDataSetChanged();
                        tvAdd.setText("Edit");
                    }
                }
            }
        });

        getActionBar().setCustomView(actionbarView);
    }

    public void deleteSavedClass(ClubTimeTable_New club) {
        application.getDatabase().removeToMyClass(club.getId());
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        Uri deleteUri = null;
        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, club.getEventId());
        int rows = getContentResolver().delete(deleteUri, null, null);
        Log.i("Rows deleted: ", "" + rows);
        setDataSet();
    }

    private void setDataSet() {
        expandableListDetail = new LinkedHashMap<String, List<ClubTimeTable_New>>();
        expandableListTitle = application.getDatabase().getSavedClubDay();
        for (int i = 0; i < expandableListTitle.size(); i++) {
            List<ClubTimeTable_New> tList = application.getDatabase().getSavedClubTimeTableData(expandableListTitle.get(i));
            expandableListDetail.put(expandableListTitle.get(i), tList);
        }
        expandableListTitle = new ArrayList<String>(
                expandableListDetail.keySet());
        expandableListAdapter = new ExpandableListAdapter(this,
                expandableListTitle, expandableListDetail);
        listView.setAdapter(expandableListAdapter);
        for (int i = 0; i < expandableListTitle.size(); i++)
            listView.expandGroup(i);
    }

    private void deleteEvent() {

    }
}
