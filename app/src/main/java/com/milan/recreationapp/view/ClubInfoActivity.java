package com.milan.recreationapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.milan.recreationapp.R;
import com.milan.recreationapp.ReCreationApplication;
import com.milan.recreationapp.model.ClubModel_New;

import java.util.ArrayList;

/**
 * Created by utsav.k on 05-04-2016.
 */
public class ClubInfoActivity extends BaseActivity {

    private TextView txtAddress;
    private LinearLayout lHoursDetail;
    private ClubModel_New clubData;
    private ArrayList<ClubModel_New> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_info);
        lHoursDetail = (LinearLayout) findViewById(R.id.location_detail_lHoursDetail);
        txtAddress = (TextView) findViewById(R.id.location_detail_txtAddress);
        if (this.getIntent().hasExtra("clubdata")) {
            clubData = (ClubModel_New) this.getIntent().getSerializableExtra("clubdata");
        }
        if (clubData != null) {
            txtAddress.setText(clubData.getAddress() + "\n" + clubData.getPhone());
            setUpActionBar(clubData.getName());
            list = ((ReCreationApplication) getApplication()).getDatabase().getClubDataFromName(clubData.getName());
            String temp = "";
            for (int i = 0; i < list.size(); i++) {

                View v = getLayoutInflater().inflate(R.layout.row_hours_title, null);
                TextView txtTitle = (TextView) v.findViewById(R.id.row_hours_title);
                txtTitle.setText(list.get(i).getHoursTitle());

                if (!temp.equalsIgnoreCase(txtTitle.getText().toString().trim()))
                    lHoursDetail.addView(v);
                temp = txtTitle.getText().toString().trim();

                View vBody = getLayoutInflater().inflate(R.layout.row_hours, null);
                TextView txtDay = (TextView) vBody.findViewById(R.id.row_day);
                TextView txtHours = (TextView) vBody.findViewById(R.id.row_hours);
                txtDay.setText(list.get(i).getDays());
                txtHours.setText(list.get(i).getHours());
                lHoursDetail.addView(vBody);
            }
        }
    }

    @Override
    public void setUpActionBar(String title) {

            //View actionbarView = LayoutInflater.from(this).inflate(
             //       R.layout.actionbar_layout_for_alert, null, true);

        getActionBar().setCustomView(R.layout.actionbar_layout_for_alert);
            TextView tvTitle = (TextView) findViewById(R.id.actionbar_layout_tv_title);
            tvTitle.setText(title);
            TextView tvClose = (TextView) findViewById(R.id.actionbar_layout_tv_close);
            final TextView tvAdd = (TextView) findViewById(R.id.actionbar_layout_tv_add);
            tvClose.setVisibility(View.VISIBLE);
            tvClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        tvAdd.setVisibility(View.GONE);
            tvAdd.setText("Edit");




    }
}
