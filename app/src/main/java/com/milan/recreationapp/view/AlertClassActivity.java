package com.milan.recreationapp.view;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.milan.recreationapp.R;
import com.milan.recreationapp.ReCreationApplication;
import com.milan.recreationapp.model.ClubTimeTable_New;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by utsav.k on 12-04-2016.
 */
public class AlertClassActivity extends Activity {

    private ClubTimeTable_New timeTable_new;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private int selectedDay, hour, min, timeBefore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_class);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        if (this.getIntent().hasExtra("clubalert")) {
            timeTable_new = (ClubTimeTable_New) this.getIntent().getSerializableExtra("clubalert");
            setUpActionBar(timeTable_new.getClassName() + " alert");
            if (timeTable_new.getDay().contains("sunday"))
                selectedDay = 1;
            else if (timeTable_new.getDay().contains("monday"))
                selectedDay = 2;
            else if (timeTable_new.getDay().contains("tuesday"))
                selectedDay = 3;
            else if (timeTable_new.getDay().contains("wednesday"))
                selectedDay = 4;
            else if (timeTable_new.getDay().contains("thursday"))
                selectedDay = 5;
            else if (timeTable_new.getDay().contains("friday"))
                selectedDay = 6;
            else if (timeTable_new.getDay().contains("saturday"))
                selectedDay = 7;
            getHourAndMin();
        }
    }

    private void getHourAndMin() {
        try {
            String time = timeTable_new.getTime().toString().trim().substring(0, timeTable_new.getTime().toString().trim().length() - 2).replace(".", ":");
            String ampm = timeTable_new.getTime().toString().trim().substring(timeTable_new.getTime().toString().trim().length() - 2, timeTable_new.getTime().toString().trim().length()).toString().toUpperCase();
            SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
            Date date = parseFormat.parse(time + " " + ampm);
            String parsedDate = displayFormat.format(date);
            hour = Integer.parseInt(parsedDate.split(":")[0]);
            min = Integer.parseInt(parsedDate.split(":")[1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setUpActionBar(final String title) {
        View actionbarView = LayoutInflater.from(this).inflate(
                R.layout.actionbar_layout_for_alert, null, true);

        TextView tvTitle = (TextView) actionbarView.findViewById(R.id.actionbar_layout_tv_title);
        tvTitle.setText(title);
        TextView tvClose = (TextView) actionbarView.findViewById(R.id.actionbar_layout_tv_close);
        TextView tvAdd = (TextView) actionbarView.findViewById(R.id.actionbar_layout_tv_add);

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.alert_radioAtTime:
                        timeBefore = 0;
                        break;
                    case R.id.alert_radioFiveMin:
                        timeBefore = 5;
                        break;
                    case R.id.alert_radioFifteenMin:
                        timeBefore = 15;
                        break;
                    case R.id.alert_radioThirtyMin:
                        timeBefore = 30;
                        break;
                    case R.id.alert_radioOneHours:
                        timeBefore = 60;
                        break;
                    case R.id.alert_radioTwoHours:
                        timeBefore = 120;
                        break;
                }
                addEvent();
                Toast.makeText(AlertClassActivity.this, "Event added successfully", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        getActionBar().setCustomView(actionbarView);
    }

    public void addEvent() {
        Calendar calDate = new GregorianCalendar();
        calDate.set(Calendar.DAY_OF_WEEK, selectedDay);
        calDate.set(Calendar.HOUR_OF_DAY, hour);
        calDate.set(Calendar.MINUTE, min);
        calDate.set(Calendar.SECOND, 0);
        calDate.set(Calendar.MILLISECOND, 0);
        Log.e("time milli:", "" + calDate.getTimeInMillis());
        try {
            ContentResolver cr = this.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, calDate.getTimeInMillis());
            values.put(CalendarContract.Events.DTEND, calDate.getTimeInMillis() + (Integer.parseInt(timeTable_new.getDuration())) * 60 * 1000);
            values.put(CalendarContract.Events.TITLE, timeTable_new.getClassName());
            values.put(CalendarContract.Events.EVENT_LOCATION, timeTable_new.getLocation());
            values.put(CalendarContract.Events.DESCRIPTION, timeTable_new.getDesc());
            values.put(CalendarContract.Events.CALENDAR_ID, 1);
//            values.put(CalendarContract.Events.RRULE, "FREQ=WEEKLY");
            values.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance()
                    .getTimeZone().getID());
            System.out.println(Calendar.getInstance().getTimeZone().getID());
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

            // Save the eventId into the Task object for possible future delete.
            long _eventId;
            _eventId = Long.parseLong(uri.getLastPathSegment());
            // Add a 5 minute, 1 hour and 1 day reminders (3 reminders)
            setReminder(cr, _eventId, timeBefore);

            ((ReCreationApplication) getApplication()).getDatabase().saveEventId(timeTable_new.getId(), _eventId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // routine to add reminders with the event
    public void setReminder(ContentResolver cr, long eventID, int timeBefore) {
        try {
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Reminders.MINUTES, timeBefore);
            values.put(CalendarContract.Reminders.EVENT_ID, eventID);
            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            Uri uri = cr.insert(CalendarContract.Reminders.CONTENT_URI, values);
//            Cursor c = CalendarContract.Reminders.query(cr, eventID,
//                    new String[]{CalendarContract.Reminders.MINUTES});
//            if (c.moveToFirst()) {
//                System.out.println("calendar"
//                        + c.getInt(c.getColumnIndex(CalendarContract.Reminders.MINUTES)));
//            }
//            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
