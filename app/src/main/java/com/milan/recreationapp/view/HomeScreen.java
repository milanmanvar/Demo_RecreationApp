package com.milan.recreationapp.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.milan.recreationapp.R;
import com.milan.recreationapp.ReCreationApplication;

/**
 * Created by milanmanvar on 18/03/16.
 */
public class HomeScreen extends Activity {


    private TextView txtClubName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        txtClubName = (TextView) findViewById(R.id.home_clubName);

    }

    @Override
    protected void onResume() {
        super.onResume();
        txtClubName.setText(((ReCreationApplication) getApplication()).sharedPreferences.getString("club", "").toUpperCase() + "\n TIMETABLE");
    }

    public void onHomeMenuClick(View v) {
        switch (v.getId()) {
            case R.id.lTimeTable:
                Intent intent = new Intent(HomeScreen.this, ClubTimeTableActivity.class);
                startActivity(intent);
                break;
            case R.id.lMyClass:
                Intent iSaved = new Intent(HomeScreen.this, SavedClassActivity.class);
                startActivity(iSaved);
                break;
            case R.id.lOurClub:
                Intent iClub = new Intent(HomeScreen.this, OurClubActivity.class);
                startActivity(iClub);
                break;
            case R.id.lFindClass:
                Intent iFind = new Intent(HomeScreen.this, FindClassActivity.class);
                startActivity(iFind);
                break;
        }
    }

}
