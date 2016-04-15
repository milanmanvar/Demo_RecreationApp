package com.milan.recreationapp.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.milan.recreationapp.R;

/**
 * Created by milanmanvar on 05/04/16.
 */
public class BaseActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);

    }

    public void setUpActionBar(String title){
        View actionbarView = LayoutInflater.from(this).inflate(
                R.layout.actionbar_layout, null, true);


        TextView tvTitle = (TextView)actionbarView.findViewById(R.id.actionbar_layout_tv_title);
        ImageView ivMyclass = (ImageView) actionbarView.findViewById(R.id.actionbar_layout_iv_myclass);
        ivMyclass.setOnClickListener(this);
        tvTitle.setText(title);

        getActionBar().setCustomView(actionbarView);
    }


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.actionbar_layout_iv_myclass){
            Intent intent = new Intent(this,SavedClassActivity.class);
            startActivity(intent);

        }

    }

}
