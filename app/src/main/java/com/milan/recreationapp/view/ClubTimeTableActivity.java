package com.milan.recreationapp.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.milan.recreationapp.R;
import com.milan.recreationapp.ReCreationApplication;
import com.milan.recreationapp.adapter.ClubDetailTimeTableListAdapter;
import com.milan.recreationapp.model.ClubModel_New;
import com.milan.recreationapp.model.ClubTimeTable_New;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by milanmanvar on 05/04/16.
 */
public class ClubTimeTableActivity extends BaseActivity {

    private ListView list;
    private ArrayList<ClubTimeTable_New> clubTimeTables;
    private String selectedClub;
    private int selectedClubPos;
    private Button btnClub;
    private ReCreationApplication reCreationApplication;
    private PopupMenu clubPopUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubtitmetable);



        list = (ListView) findViewById(R.id.clubtimetable_list);
        btnClub = (Button) findViewById(R.id.clubtimetable_btnClub);

        reCreationApplication = (ReCreationApplication) getApplicationContext();

//        clubTimeTables = Utils.parseClubDetailXML(reCreationApplication.sharedPreferences.getString("club" + selectedClubPos, ""));
        clubPopUp = new PopupMenu(this, btnClub);
        clubPopUp.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                btnClub.setText(item.getTitle());
                SharedPreferences.Editor editor = ((ReCreationApplication) getApplication()).sharedPreferences.edit();
                editor.putString("club", item.getTitle().toString());
                editor.putInt("clubposition", item.getOrder());
                editor.commit();
                setData();
                return true;
            }
        });
        final ArrayList<ClubModel_New> clubs = reCreationApplication.getDatabase().getClubList();
        for (int i = 0; i < clubs.size(); i++)
            clubPopUp.getMenu().add(i, i + 1, i + 1, clubs.get(i).getName());
        btnClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clubPopUp.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    private void setData() {
        selectedClub = reCreationApplication.sharedPreferences.getString("club", "");
        selectedClubPos = reCreationApplication.sharedPreferences.getInt("clubposition", 0);
        clubTimeTables = reCreationApplication.getDatabase().getClubTimeTableFromName(selectedClub);
        btnClub.setText(selectedClub);
        setUpActionBar(selectedClub+"");
        findViewById(R.id.actionbar_layout_iv_myclass).setVisibility(View.GONE);
        ArrayList<ClubTimeTable_New> temp = clubTimeTables;
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).getDay().equalsIgnoreCase(getCurrentDay())) {
                ClubTimeTable_New c = clubTimeTables.remove(i);
                clubTimeTables.add(0, c);
                int tempI = i + 1;
                ClubTimeTable_New c1 = clubTimeTables.remove(tempI);
                clubTimeTables.add(1, c1);
//                tempI = tempI;
                ClubTimeTable_New c2 = clubTimeTables.remove(tempI);
                clubTimeTables.add(c2);
                break;
            }
        }
        list.setAdapter(new ClubDetailTimeTableListAdapter(this, clubTimeTables));
    }

    private String getCurrentDay() {
        String weekDay;
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        weekDay = dayFormat.format(calendar.getTime());
        Log.e("Week day:", "" + weekDay);
        return weekDay;
    }
}
