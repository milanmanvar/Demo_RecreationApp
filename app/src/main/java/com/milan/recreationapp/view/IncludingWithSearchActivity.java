package com.milan.recreationapp.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.milan.recreationapp.R;
import com.milan.recreationapp.ReCreationApplication;

/**
 * Created by utsav.k on 07-04-2016.
 */
public class IncludingWithSearchActivity extends Activity {

    private Switch switchMorning, switchLunch, switchEvening;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchincludewith);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);
        setUpActionBar("Include with");
        switchMorning = (Switch) findViewById(R.id.searchinclude_switchMorning);
        switchLunch = (Switch) findViewById(R.id.searchinclude_switchLunch);
        switchEvening = (Switch) findViewById(R.id.searchinclude_switchEvening);
        switchMorning.setChecked(((ReCreationApplication) getApplication()).sharedPreferences.getBoolean("morning", true));
        switchLunch.setChecked(((ReCreationApplication) getApplication()).sharedPreferences.getBoolean("lunchtime", true));
        switchEvening.setChecked(((ReCreationApplication) getApplication()).sharedPreferences.getBoolean("evening", true));

    }

    private String getSelectedType() {
        StringBuilder result = new StringBuilder();
        SharedPreferences.Editor e = ((ReCreationApplication) getApplication()).sharedPreferences.edit();
        if (switchMorning.isChecked()) {
            result.append("morning");
            result.append(",");
            e.putBoolean("morning", true);
        } else
            e.putBoolean("morning", false);

        if (switchLunch.isChecked()) {
            result.append("lunchtime");
            result.append(",");
            e.putBoolean("lunchtime", true);
        } else
            e.putBoolean("lunchtime", false);
        if (switchEvening.isChecked()) {
            result.append("evening");
            result.append(",");
            e.putBoolean("evening", true);
        } else
            e.putBoolean("evening", false);
        e.commit();

        return result.length() > 0 ? result.substring(0, result.length() - 1) : "";
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
                String selected = getSelectedType();
                Intent i = new Intent();
                i.putExtra("selectedtype", selected);
                setResult(RESULT_OK, i);
                SharedPreferences.Editor e = ((ReCreationApplication) getApplication()).sharedPreferences.edit();
                e.putString("selectedtype", selected);
                e.commit();
                finish();
            }
        });
        getActionBar().setCustomView(actionbarView);
    }
}
