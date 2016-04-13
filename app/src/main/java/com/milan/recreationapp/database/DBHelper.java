package com.milan.recreationapp.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.milan.recreationapp.model.ClubModel_New;
import com.milan.recreationapp.model.ClubTimeTable_New;
import com.milan.recreationapp.model.SearchWithModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("DefaultLocale")
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "reCreation.sqlite";
    public static final int DATABASE_VERSION = 1;
    private final String TABLE_CLUB_DETAIL = "ClubsDetail";
    private final String TABLE_CLUB_DETAIL_COL_ID = "_id";
    private final String TABLE_CLUB_DETAIL_COL_CLUB_NAME = "ClubName";
    private final String TABLE_CLUB_DETAIL_COL_CLASS_NAME = "ClassName";
    private final String TABLE_CLUB_DETAIL_COL_CLASS_INSTRUCTOR = "Instructor";
    private final String TABLE_CLUB_DETAIL_COL_CLASS_DURATION = "Duration";
    private final String TABLE_CLUB_DETAIL_COL_CLASS_TIME = "Time";
    private final String TABLE_CLUB_DETAIL_COL_CLASS_DAY = "Day";
    private final String TABLE_CLUB_DETAIL_COL_CLASS_TYPE = "Classtype";
    private final String TABLE_CLUB_DETAIL_COL_CLASS_DESCRIPTION = "Description";
    private final String TABLE_CLUB_DETAIL_COL_CLASS_LOCATION = "Location";
    private final String TABLE_CLUB_DETAIL_COL_IS_SAVED = "IsSaved";
    private final String TABLE_CLUB_DETAIL_COL_EVENDID = "EventId";

    private final String TABLE_CLUB = "Club";
    private final String TABLE_CLUB_COL_ID = "_id";
    private final String TABLE_CLUB_COL_CLUB_NAME = "ClubName";
    private final String TABLE_CLUB_COL_ADDRESS = "Address";
    private final String TABLE_CLUB_COL_PHONE = "Phone";
    private final String TABLE_CLUB_COL_LATITUDE = "Latitude";
    private final String TABLE_CLUB_COL_LONGITUDE = "Longitude";
    private final String TABLE_CLUB_COL_HOURSTITLE = "HoursTitle";
    private final String TABLE_CLUB_COL_DAYS = "Days";
    private final String TABLE_CLUB_COL_HOURS = "Hours";
    private final String TABLE_CLUB_COL_IS_24 = "is24Hour";


    private SQLiteDatabase myDataBase;
    private Context myContext;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("Db versions:", "Older:" + oldVersion + "--> New:" + newVersion);
    }

    // ---Create the database---
    public void createDataBase() throws IOException {

        // ---Check whether database is already created or not---
        boolean dbExist = checkDataBase();

        if (!dbExist) {
            this.getReadableDatabase();
            try {
                // ---If not created then copy the database---
                copyDataBase();
                this.close();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    // --- Check whether database already created or not---
    private boolean checkDataBase() {
        try {
            final String myPath = "/data/data/" + myContext.getPackageName()
                    + "/databases/" + DATABASE_NAME;
            final File f = new File(myPath);
            if (f.exists())
                return true;
            else
                return false;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }

    }

    // --- Copy the database to the output stream---
    private void copyDataBase() throws IOException {

        final InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

        final String outFileName = "/data/data/" + myContext.getPackageName()
                + "/databases/" + DATABASE_NAME;

        final OutputStream myOutput = new FileOutputStream(outFileName);

        final byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public SQLiteDatabase openDataBase() throws SQLException {

        // --- Open the database---
        final String myPath = "/data/data/" + myContext.getPackageName()
                + "/databases/" + DATABASE_NAME;

        myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        return myDataBase;
    }

    public void closeDatabase() {
        myDataBase.close();
        SQLiteDatabase.releaseMemory();
    }

    public void insertClubData(String clubName, String className, String instructor, String duration, String time, String day, String classtype, String description, String location) {
        myDataBase.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(TABLE_CLUB_DETAIL_COL_CLUB_NAME, clubName);
            values.put(TABLE_CLUB_DETAIL_COL_CLASS_NAME, className);
            values.put(TABLE_CLUB_DETAIL_COL_CLASS_INSTRUCTOR, instructor);
            values.put(TABLE_CLUB_DETAIL_COL_CLASS_DURATION, duration);
            values.put(TABLE_CLUB_DETAIL_COL_CLASS_TIME, time);
            values.put(TABLE_CLUB_DETAIL_COL_CLASS_DAY, day);
            values.put(TABLE_CLUB_DETAIL_COL_CLASS_TYPE, classtype);
            values.put(TABLE_CLUB_DETAIL_COL_CLASS_DESCRIPTION, description);
            values.put(TABLE_CLUB_DETAIL_COL_CLASS_LOCATION, location);
            values.put(TABLE_CLUB_DETAIL_COL_IS_SAVED, 0);
            values.put(TABLE_CLUB_DETAIL_COL_EVENDID, 0);
            long id = myDataBase.insert(TABLE_CLUB_DETAIL, null, values);
            Log.e("id:" + id, "" + clubName);
            myDataBase.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            myDataBase.endTransaction();
        }
    }

    public void deleteTableData() {
        String sql1 = String.format("DELETE FROM '%s'", TABLE_CLUB_DETAIL);
        myDataBase.execSQL(sql1);

        String sql2 = String.format("DELETE FROM '%s'", TABLE_CLUB);
        myDataBase.execSQL(sql2);
    }

    public Cursor getSampleClubData() {
        Cursor cursor = myDataBase.query(TABLE_CLUB, new String[]{"*"},
                null,
                null, null, null, null, null);
        return cursor;
    }

    public void insertClub(String name, String address, String phone, double lat, double lng, String title, String days, String hours, boolean is24) {
        myDataBase.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(TABLE_CLUB_COL_CLUB_NAME, name);
            values.put(TABLE_CLUB_COL_ADDRESS, address);
            values.put(TABLE_CLUB_COL_PHONE, phone);
            values.put(TABLE_CLUB_COL_LATITUDE, lat);
            values.put(TABLE_CLUB_COL_LONGITUDE, lng);
            values.put(TABLE_CLUB_COL_HOURSTITLE, title);
            values.put(TABLE_CLUB_COL_DAYS, days);
            values.put(TABLE_CLUB_COL_HOURS, hours);
            values.put(TABLE_CLUB_COL_IS_24, is24);
            myDataBase.insert(TABLE_CLUB, null, values);
            myDataBase.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            myDataBase.endTransaction();
        }
    }

    public ArrayList<ClubTimeTable_New> getClubTimeTableFromName(String name) {
        ArrayList<ClubTimeTable_New> timeTables = new ArrayList<>();
        String sql = "SELECT * FROM ClubsDetail WHERE ClubName = '%s'  GROUP BY Day ORDER BY _id";
        Cursor cursor = null;
        try {
            cursor = myDataBase.rawQuery(String.format(sql, name), null);
            Log.e("cursor:", "" + cursor.getCount());
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                ClubTimeTable_New clubTimeTable;
                do {
                    clubTimeTable = new ClubTimeTable_New();
                    clubTimeTable.setId(cursor.getInt(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_ID)));
                    clubTimeTable.setDay(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_DAY)));
                    clubTimeTable.setLocation(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_LOCATION)));
                    clubTimeTable.setClassName(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_NAME)));
                    clubTimeTable.setClassType(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_TYPE)));
                    clubTimeTable.setClubName(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLUB_NAME)));
                    clubTimeTable.setDesc(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_DESCRIPTION)));
                    clubTimeTable.setDuration(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_DURATION)));
                    clubTimeTable.setInstructor(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_INSTRUCTOR)));
                    clubTimeTable.setTime(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_TIME)));
                    clubTimeTable.setIsSaved(cursor.getInt(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_IS_SAVED)));
                    clubTimeTable.setEventId(cursor.getLong(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_EVENDID)));

                    timeTables.add(clubTimeTable);

                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return timeTables;
    }

    public ArrayList<ClubTimeTable_New> getClubTimeTableLikeName(String name, String clubs, String classtype) {
        ArrayList<ClubTimeTable_New> timeTables = new ArrayList<>();
        String sql = "SELECT * FROM ClubsDetail WHERE ClassName like '%s' AND ClubName IN ('%s') AND Classtype IN ('%s')  ORDER BY _id";
        Cursor cursor = null;
        try {
            cursor = myDataBase.rawQuery(String.format(sql, "%" + name + "%", clubs.replace(",", "','"), classtype.replace(",", "','")), null);
            Log.e("cursor:", "" + cursor.getCount());
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                ClubTimeTable_New clubTimeTable;
                do {
                    clubTimeTable = new ClubTimeTable_New();
                    clubTimeTable.setId(cursor.getInt(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_ID)));
                    clubTimeTable.setDay(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_DAY)));
                    clubTimeTable.setLocation(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_LOCATION)));
                    clubTimeTable.setClassName(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_NAME)));
                    clubTimeTable.setClassType(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_TYPE)));
                    clubTimeTable.setClubName(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLUB_NAME)));
                    clubTimeTable.setDesc(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_DESCRIPTION)));
                    clubTimeTable.setDuration(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_DURATION)));
                    clubTimeTable.setInstructor(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_INSTRUCTOR)));
                    clubTimeTable.setTime(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_TIME)));
                    clubTimeTable.setIsSaved(cursor.getInt(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_IS_SAVED)));
                    clubTimeTable.setEventId(cursor.getLong(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_EVENDID)));
                    timeTables.add(clubTimeTable);

                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return timeTables;
    }

    public ArrayList<ClubTimeTable_New> getClubTimeTableDetailFromNameAndDay(String name, String day) {
        ArrayList<ClubTimeTable_New> timeTables = new ArrayList<>();
        String sql = "SELECT * FROM ClubsDetail WHERE ClubName = '%s' AND Day = '%s'  ORDER BY _id";
//        String sql = "SELECT * FROM " + TABLE_CLUB_DETAIL + " WHERE " + TABLE_CLUB_DETAIL_COL_CLUB_NAME + "= '" + name + "' AND " + TABLE_CLUB_DETAIL_COL_CLASS_DAY + " ='" + day + "'";
        Cursor cursor = null;
        try {
            cursor = myDataBase.rawQuery(String.format(sql, name, day), null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                ClubTimeTable_New clubTimeTable;
                do {
                    clubTimeTable = new ClubTimeTable_New();
                    clubTimeTable.setId(cursor.getInt(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_ID)));
                    clubTimeTable.setDay(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_DAY)));
                    clubTimeTable.setLocation(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_LOCATION)));
                    clubTimeTable.setClassName(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_NAME)));
                    clubTimeTable.setClassType(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_TYPE)));
                    clubTimeTable.setClubName(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLUB_NAME)));
                    clubTimeTable.setDesc(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_DESCRIPTION)));
                    clubTimeTable.setDuration(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_DURATION)));
                    clubTimeTable.setInstructor(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_INSTRUCTOR)));
                    clubTimeTable.setTime(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_TIME)));
                    clubTimeTable.setIsSaved(cursor.getInt(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_IS_SAVED)));
                    clubTimeTable.setEventId(cursor.getLong(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_EVENDID)));
                    timeTables.add(clubTimeTable);

                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return timeTables;
    }


    public ArrayList<ClubModel_New> getClubList() {
        ArrayList<ClubModel_New> timeTables = new ArrayList<>();
        String sql = "SELECT * FROM Club GROUP BY ClubName ORDER BY _id";
        Cursor cursor = null;
        try {
            cursor = myDataBase.rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                ClubModel_New club;
                do {
                    club = new ClubModel_New();
                    club.setId(cursor.getInt(cursor.getColumnIndex(TABLE_CLUB_COL_ID)));
                    club.setName(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_COL_CLUB_NAME)));
                    club.setAddress(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_COL_ADDRESS)));
                    club.setDays(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_COL_DAYS)));
                    club.setHours(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_COL_HOURS)));
                    club.setHoursTitle(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_COL_HOURSTITLE)));
                    club.setPhone(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_COL_PHONE)));
                    club.setLat(cursor.getDouble(cursor.getColumnIndex(TABLE_CLUB_COL_LATITUDE)));
                    club.setLng(cursor.getDouble(cursor.getColumnIndex(TABLE_CLUB_COL_LONGITUDE)));
                    club.setIs24Hour(cursor.getInt(cursor.getColumnIndex(TABLE_CLUB_COL_IS_24)) == 1 ? true : false);
                    timeTables.add(club);

                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return timeTables;
    }

    public ArrayList<SearchWithModel> getClubListForSearch() {
        ArrayList<SearchWithModel> timeTables = new ArrayList<>();
        String sql = "SELECT * FROM Club GROUP BY ClubName ORDER BY _id";
        Cursor cursor = null;
        try {
            cursor = myDataBase.rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                SearchWithModel club;
                do {
                    club = new SearchWithModel();
                    club.setName(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_COL_CLUB_NAME)));
                    club.setIsChecked(false);
                    timeTables.add(club);

                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return timeTables;
    }


    public ArrayList<ClubModel_New> getClubDataFromName(String name) {
        ArrayList<ClubModel_New> timeTables = new ArrayList<>();
        String sql = "SELECT * FROM Club WHERE ClubName = '%s' ORDER BY _id";
        Cursor cursor = null;
        try {
            cursor = myDataBase.rawQuery(String.format(sql, name), null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                ClubModel_New club;
                do {
                    club = new ClubModel_New();
                    club.setId(cursor.getInt(cursor.getColumnIndex(TABLE_CLUB_COL_ID)));
                    club.setName(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_COL_CLUB_NAME)));
                    club.setAddress(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_COL_ADDRESS)));
                    club.setDays(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_COL_DAYS)));
                    club.setHours(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_COL_HOURS)));
                    club.setHoursTitle(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_COL_HOURSTITLE)));
                    club.setPhone(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_COL_PHONE)));
                    club.setLat(cursor.getDouble(cursor.getColumnIndex(TABLE_CLUB_COL_LATITUDE)));
                    club.setLng(cursor.getDouble(cursor.getColumnIndex(TABLE_CLUB_COL_LONGITUDE)));
                    club.setIs24Hour(cursor.getInt(cursor.getColumnIndex(TABLE_CLUB_COL_IS_24)) == 1 ? true : false);
                    timeTables.add(club);

                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return timeTables;
    }

    public void saveToMyClass(int id) {
        String queryUpdate = "UPDATE ClubsDetail SET IsSaved='1' WHERE _id='%d' ";
        try {
            myDataBase.beginTransaction();
            String sql = String.format(queryUpdate, id);
            myDataBase.execSQL(sql);
            myDataBase.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            myDataBase.endTransaction();
        }
    }

    public void removeToMyClass(int id) {
        String queryUpdate = "UPDATE ClubsDetail SET IsSaved='0', EventId='0' WHERE _id='%d' ";
        try {
            myDataBase.beginTransaction();
            String sql = String.format(queryUpdate, id);
            myDataBase.execSQL(sql);
            myDataBase.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            myDataBase.endTransaction();
        }
    }

    public void saveEventId(int id, long eventId) {
        String queryUpdate = "UPDATE ClubsDetail SET EventId='%d' WHERE _id='%d' ";
        try {
            myDataBase.beginTransaction();
            String sql = String.format(queryUpdate, eventId, id);
            Log.e("event update sql:",""+sql);
            myDataBase.execSQL(sql);
            myDataBase.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            myDataBase.endTransaction();
        }
    }

    public List<String> getSavedClubDay() {
        List<String> timeTables = new ArrayList<>();
        String sql = "SELECT * FROM ClubsDetail WHERE IsSaved = '1' GROUP BY Day  ORDER BY _id";
        Cursor cursor = null;
        try {
            cursor = myDataBase.rawQuery(sql, null);
            Log.e("cursor:", "" + cursor.getCount());
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
//                    clubTimeTable = new ClubTimeTable_New();
//                    clubTimeTable.setId(cursor.getInt(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_ID)));
//                    clubTimeTable.setDay(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_DAY)));

                    timeTables.add(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_DAY)));

                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return timeTables;
    }


    public ArrayList<ClubTimeTable_New> getSavedClubTimeTable() {
        ArrayList<ClubTimeTable_New> timeTables = new ArrayList<>();
        String sql = "SELECT * FROM ClubsDetail WHERE IsSaved = '1'  ORDER BY _id";
        Cursor cursor = null;
        try {
            cursor = myDataBase.rawQuery(sql, null);
            Log.e("cursor:", "" + cursor.getCount());
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                ClubTimeTable_New clubTimeTable;
                do {
                    clubTimeTable = new ClubTimeTable_New();
                    clubTimeTable.setId(cursor.getInt(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_ID)));
                    clubTimeTable.setDay(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_DAY)));
                    clubTimeTable.setLocation(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_LOCATION)));
                    clubTimeTable.setClassName(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_NAME)));
                    clubTimeTable.setClassType(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_TYPE)));
                    clubTimeTable.setClubName(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLUB_NAME)));
                    clubTimeTable.setDesc(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_DESCRIPTION)));
                    clubTimeTable.setDuration(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_DURATION)));
                    clubTimeTable.setInstructor(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_INSTRUCTOR)));
                    clubTimeTable.setTime(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_TIME)));
                    clubTimeTable.setIsSaved(cursor.getInt(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_IS_SAVED)));
                    timeTables.add(clubTimeTable);

                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return timeTables;
    }



    public List<ClubTimeTable_New> getSavedClubTimeTableData(String day) {
        List<ClubTimeTable_New> timeTables = new ArrayList<>();
        String sql = "SELECT * FROM ClubsDetail WHERE IsSaved = '1' AND Day = '%s' ORDER BY _id";
        Cursor cursor = null;
        try {
            cursor = myDataBase.rawQuery(String.format(sql,day), null);
            Log.e("cursor:", "" + cursor.getCount());
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                ClubTimeTable_New clubTimeTable;
                do {
                    clubTimeTable = new ClubTimeTable_New();
                    clubTimeTable.setId(cursor.getInt(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_ID)));
                    clubTimeTable.setDay(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_DAY)));
                    clubTimeTable.setLocation(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_LOCATION)));
                    clubTimeTable.setClassName(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_NAME)));
                    clubTimeTable.setClassType(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_TYPE)));
                    clubTimeTable.setClubName(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLUB_NAME)));
                    clubTimeTable.setDesc(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_DESCRIPTION)));
                    clubTimeTable.setDuration(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_DURATION)));
                    clubTimeTable.setInstructor(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_INSTRUCTOR)));
                    clubTimeTable.setTime(cursor.getString(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_CLASS_TIME)));
                    clubTimeTable.setIsSaved(cursor.getInt(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_IS_SAVED)));
                    clubTimeTable.setEventId(cursor.getLong(cursor.getColumnIndex(TABLE_CLUB_DETAIL_COL_EVENDID)));
                    timeTables.add(clubTimeTable);

                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return timeTables;
    }
}
