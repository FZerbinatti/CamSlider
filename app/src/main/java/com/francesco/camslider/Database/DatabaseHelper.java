package com.francesco.camslider.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.francesco.camslider.Objects.Setting;
import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {

    static final String TABLE_SETTINGS = "SETTINGS";
    private static final String DB_NAME = "BREA_DB";
    private static final int DB_VERSION = 1;


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
    }


    private void createTables(SQLiteDatabase db){
        Log.d(TAG, "createTables: ");

        //creating tables
        String CREATE_TABLE_MOVIES = "CREATE TABLE " + TABLE_SETTINGS +
                "(" + "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "DISTANCE" + " TEXT ,  "
                + "DISTANCE_UM" + " TEXT ,  "
                + "ROTATION" + " TEXT , "
                + "ROTATION_UM" + " TEXT , "
                + "TILTING" + " TEXT , "
                + "TILTING_UM" + " TEXT , "

                + "SLIDE_LEFT" + " TEXT , "
                + "SLIDE_LEFT_VALUE" + " CHAR , "
                + "SLIDE_RIGHT" + " TEXT , "
                + "SLIDE_RIGHT_VALUE" + " TEXT , "

                + "TILT_DOWN" + " TEXT , "
                + "TILT_DOWN_VALUE" + " TEXT , "
                + "TILT_UP" + " TEXT , "
                + "TILT_UP_VALUE" + " TEXT , "

                + "ROTATE_CCW" + " TEXT , "
                + "ROTATE_CCW_VALUE" + " TEXT , "
                + "ROTATE_CW" + " TEXT , "
                + "ROTATE_CW_VALUE" + " TEXT , "

                + "GO_HOME" + " TEXT , "
                + "GO_HOME_VALUE" + " TEXT , "
                + "GO_END" + " TEXT , "
                + "GO_END_VALUE" + " TEXT , "

                + "SLIDE_FREQUENCY_VALUE" + " TEXT , "
                + "JOYSTICK_FREQUENCY_VALUE" + " TEXT   );";

        db.execSQL(CREATE_TABLE_MOVIES);

        ContentValues contVals = new ContentValues();

        contVals.put("DISTANCE", "Distance");
        contVals.put("DISTANCE_UM", " cm");
        contVals.put("ROTATION", "Rotation");
        contVals.put("ROTATION_UM", " °");
        contVals.put("TILTING", "Tilting");
        contVals.put("TILTING_UM", " °");

        contVals.put("SLIDE_LEFT", "Slide Left");
        contVals.put("SLIDE_LEFT_VALUE", "a");
        contVals.put("SLIDE_RIGHT", "Slide Right");
        contVals.put("SLIDE_RIGHT_VALUE", "b");

        contVals.put("TILT_DOWN", "Tilt Down");
        contVals.put("TILT_DOWN_VALUE", "c");
        contVals.put("TILT_UP", "Tilt Up");
        contVals.put("TILT_UP_VALUE", "d");

        contVals.put("ROTATE_CCW", "Rotate CCW");
        contVals.put("ROTATE_CCW_VALUE", "e");
        contVals.put("ROTATE_CW", "Rotate CW");
        contVals.put("ROTATE_CW_VALUE", "f");

        contVals.put("GO_HOME", "Home");
        contVals.put("GO_HOME_VALUE", "g");
        contVals.put("GO_END", "go End");
        contVals.put("GO_END_VALUE", "h");

        contVals.put("SLIDE_FREQUENCY_VALUE", "2");
        contVals.put("JOYSTICK_FREQUENCY_VALUE", "5");

        db.insert(TABLE_SETTINGS, null, contVals);

    }

    public void addMovie (  ){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contVals = new ContentValues();

        contVals.put("DISTANCE", "Distance");
        contVals.put("DISTANCE_UM", "Rotation");
        contVals.put("ROTATION", "Tilting");
        contVals.put("ROTATION_UM", "cm");
        contVals.put("TILTING", " °");
        contVals.put("TILTING_UM", " °");

        contVals.put("SLIDE_LEFT", "Slide Left");
        contVals.put("SLIDE_RIGHT", "Slide Right");
        contVals.put("TILT_DOWN", "Tilt Down");
        contVals.put("TILT_UP", "Tilt Up");
        contVals.put("ROTATE_CCW", "Rotate CCW");
        contVals.put("ROTATE_CW", "Rotate CW");
        contVals.put("GO_HOME", "Home");
        contVals.put("GO_END", "go End");

        contVals.put("SLIDE_LEFT_VALUE", "a");
        contVals.put("SLIDE_RIGHT_VALUE", "b");
        contVals.put("TILT_DOWN_VALUE", "c");
        contVals.put("TILT_UP_VALUE", "d");
        contVals.put("ROTATE_CCW_VALUE", "e");
        contVals.put("ROTATE_CW_VALUE", "f");
        contVals.put("GO_HOME_VALUE", "g");
        contVals.put("GO_END_VALUE", "h");
        contVals.put("SLIDE_FREQUENCY_VALUE", "2");
        contVals.put("JOYSTICK_FREQUENCY_VALUE", "5");



        db.insert(TABLE_SETTINGS, null, contVals);
        db.close();
    }


    public void updateSettings(Setting setting){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("DISTANCE", setting.getDistance());
        cv.put("DISTANCE_UM", setting.getDistance_um());
        cv.put("ROTATION", setting.getRotation());
        cv.put("ROTATION_UM", setting.getRotation_um());
        cv.put("TILTING", setting.getTilting());
        cv.put("TILTING_UM", setting.getTilting_um());

        cv.put("SLIDE_LEFT", setting.getSlide_left());
        cv.put("SLIDE_LEFT_VALUE", setting.getSlide_left_value());
        cv.put("SLIDE_RIGHT", setting.getSlide_right());
        cv.put("SLIDE_RIGHT_VALUE", setting.getSlide_right_value());

        cv.put("TILT_DOWN", setting.getTilt_down());
        cv.put("TILT_DOWN_VALUE", setting.getTilt_down_value());
        cv.put("TILT_UP", setting.getTilt_up());
        cv.put("TILT_UP_VALUE", setting.getTilt_up_value());

        cv.put("ROTATE_CCW", setting.getRotate_CCW());
        cv.put("ROTATE_CCW_VALUE", setting.getRotate_CCW_value());
        cv.put("ROTATE_CW", setting.getRotate_CW());
        cv.put("ROTATE_CW_VALUE", setting.getRotate_CW_value());

        cv.put("GO_HOME", setting.getHome());
        cv.put("GO_HOME_VALUE", setting.getHome_value());
        cv.put("GO_END", setting.getEnd());
        cv.put("GO_END_VALUE", setting.getEnd_value());
        cv.put("SLIDE_FREQUENCY_VALUE", setting.getTop_value());
        cv.put("JOYSTICK_FREQUENCY_VALUE", setting.getJoystick_frequency_value());

        db.update(TABLE_SETTINGS, cv, null, null);
        db.close();

    }







    public Setting getSettings ( ){
        SQLiteDatabase db = this.getReadableDatabase();

        Setting setting = new Setting();

        String selectQuery = "SELECT ID, DISTANCE,DISTANCE_UM,ROTATION,ROTATION_UM,TILTING,TILTING_UM," +
                "SLIDE_LEFT,SLIDE_LEFT_VALUE,SLIDE_RIGHT,SLIDE_RIGHT_VALUE," +
                "TILT_DOWN,TILT_DOWN_VALUE,TILT_UP,TILT_UP_VALUE," +
                "ROTATE_CCW,ROTATE_CCW_VALUE,ROTATE_CW,ROTATE_CW_VALUE," +
                "GO_HOME,GO_HOME_VALUE,GO_END,GO_END_VALUE," +
                "SLIDE_FREQUENCY_VALUE,JOYSTICK_FREQUENCY_VALUE FROM SETTINGS WHERE ID = 1";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {

                setting = new Setting(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                                              cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10),
                                              cursor.getString(11), cursor.getString(12), cursor.getString(13), cursor.getString(14), cursor.getString(15),
                                              cursor.getString(16), cursor.getString(17), cursor.getString(18), cursor.getString(19), cursor.getString(20),
                                              cursor.getString(21), cursor.getString(22), cursor.getString(23), cursor.getString(24)
                );

                Log.d(TAG, "getSettings: CCW " +setting.getRotate_CCW());
                Log.d(TAG, "getSettings: CCW value "+ setting.getRotate_CCW_value());



            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return setting;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTables(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        onCreate(sqLiteDatabase);
    }


}
