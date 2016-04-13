package com.milan.recreationapp.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.milan.recreationapp.R;
import com.milan.recreationapp.ReCreationApplication;
import com.milan.recreationapp.adapter.SearchWithListAdapter;
import com.milan.recreationapp.model.SearchWithModel;

import java.util.ArrayList;

/**
 * Created by utsav.k on 07-04-2016.
 */
public class SearchingWithClubActivity extends Activity {


    private ListView list;
    private ArrayList<SearchWithModel> clubs;
    private ArrayList<String> checkedClubs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);
        setUpActionBar("Search with");
        checkedClubs = new ArrayList<>();
        clubs = ((ReCreationApplication) getApplication()).getDatabase().getClubListForSearch();
        list = (ListView) findViewById(R.id.listView);
        list.setAdapter(new SearchWithListAdapter(this, clubs));

    }

    private void setUpActionBar(String title) {
        View actionbarView = LayoutInflater.from(this).inflate(
                R.layout.actionbar_layout_with_close, null, true);

        TextView tvTitle = (TextView) actionbarView.findViewById(R.id.actionbar_layout_tv_title);
        tvTitle.setText(title);
        TextView tvClose = (TextView) actionbarView.findViewById(R.id.actionbar_layout_tv_close);

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                String selected = TextUtils.join(",", checkedClubs);
                i.putExtra("selectedclubs", selected);
                setResult(RESULT_OK, i);
                SharedPreferences.Editor e = ((ReCreationApplication) getApplication()).sharedPreferences.edit();
                e.putString("selectedclubs", selected);
                e.commit();
                finish();
            }
        });
        getActionBar().setCustomView(actionbarView);
    }

    public void addToCheckList(String club, boolean checked) {
        if (checked) {
            if (!checkedClubs.contains(club))
                checkedClubs.add(club);
        } else {
            if (checkedClubs.contains(club))
                checkedClubs.remove(club);
        }
    }
}
