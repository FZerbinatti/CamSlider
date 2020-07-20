package com.francesco.camslider.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.francesco.camslider.Database.DatabaseHelper;

import com.francesco.camslider.Objects.Setting;
import com.francesco.camslider.R;

import io.github.controlwear.virtual.joystick.android.JoystickView;


public class ManualActivity extends AppCompatActivity  {

    DatabaseHelper mDatabaseHelper;

    ImageButton timed;
    private static  String STOP_LINEAR_MOTOR = "i";
    private static  String STOP_ROTATION_MOTOR = "m";
    private static  String STOP_TILTING_MOTOR = "l";
    private static  String GO_HOME_VALUE = "g";
    private static  String GO_END_VALUE = "h";
    private static  String SLIDE_LEFT_VALUE = "a";
    private static  String SLIDE_RIGHT_VALUE = "b";
    private static  String TILT_DOWN_VALUE = "c";
    private static  String TILT_UP_VALUE = "d";
    private static  String ROTATE_CCW_VALUE = "e";
    private static  String ROTATE_CW_VALUE = "f";

    private static final int JOYSTICK_FREQUENCY_VALUE = 5;
    private static final int JOYSTICK_MS = ( Math.round(((float) 1/(JOYSTICK_FREQUENCY_VALUE))*1000));


    private static final int SLIDE_FREQUENCY_VALUE = 2;
    private static final int LINEAR_MS = ( Math.round(((float) 1/(SLIDE_FREQUENCY_VALUE))*1000));


    private static final int ARDUINO_DELAY_TIME = 0;


    private static final int ITEM_COUNT = 10;
    private static final int TOTAL_DEGREE_ROTATION = 10;
    private final String TAG ="ManualActivity: ";
    TextView velocita_rotazione, rotazione, velocita_movimento, distanza, textView_tilting;
    ImageButton zeroing, arrow_left, arrow_right, hide_texts, go_end, settings, this_is_zero;
    Long velox_angolo, velox_distanza;
    Integer delay_time_angolo, delay_time_distanza;
    Boolean text_hided;
    JoystickView joystick;
    Integer linear_speed=1, rotation_speed = 1 , tilting_speed = 4;

    TextView distance_text, rotation_text , tilting_text, slide_left_text, slide_right_text, home_text, tilt_down_text, end_text, rotazione_ccw_text, rotazione_cw_text, tilt_up_text , distance_um,rotation_um, tilting_um;

    //Bluetooth setups
    String address = null , name = null;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    Set<BluetoothDevice> pairedDevices;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        mDatabaseHelper = new DatabaseHelper(getApplicationContext());

        mDatabaseHelper = new DatabaseHelper(getApplicationContext());

        end_text = findViewById(R.id.end_text);
        home_text  = findViewById(R.id.home_text);
        tilting_text  = findViewById(R.id.tilting_text);
        rotation_text = findViewById(R.id.rotation_text);
        distance_text = findViewById(R.id.distance_text);
        rotazione_ccw_text = findViewById(R.id.rotazione_ccw_text);
        tilt_down_text = findViewById(R.id.tilt_down_text);
        rotazione_cw_text = findViewById(R.id.rotazione_cw_text);
        slide_left_text = findViewById(R.id.slide_left_text);
        slide_right_text = findViewById(R.id.slide_right_text);
        tilt_up_text = findViewById(R.id.tilt_up_text);
        hide_texts = findViewById(R.id.hide_texts);
        distance_um = findViewById(R.id.distance_um);
        rotation_um= findViewById(R.id.rotation_um);
        tilting_um= findViewById(R.id.tilting_um);
        text_hided = false;

        rotazione = findViewById(R.id.textView_rotazione);
        distanza = findViewById(R.id.textView_movimento);
        textView_tilting = findViewById(R.id.textView_tilting);
        settings = findViewById(R.id.button_settings);

        Setting savedSettings =  mDatabaseHelper.getSettings();

        GO_HOME_VALUE = savedSettings.getHome_value();
        GO_END_VALUE =savedSettings.getEnd_value();
        SLIDE_LEFT_VALUE = savedSettings.getSlide_left_value();
        SLIDE_RIGHT_VALUE = savedSettings.getSlide_right_value();
        TILT_DOWN_VALUE = savedSettings.getTilt_down_value();
        TILT_UP_VALUE = savedSettings.getTilt_up_value();
        ROTATE_CCW_VALUE = savedSettings.getRotate_CCW_value();
        ROTATE_CW_VALUE = savedSettings.getRotate_CW_value();


        zeroing= findViewById(R.id. button_zeroing);
        arrow_left= findViewById(R.id.arrow_left);
        arrow_right= findViewById(R.id. arrow_right);
        go_end = findViewById(R.id.go_end);

        joystick = (JoystickView) findViewById(R.id.joystickView);

        velox_angolo =1L;
        velox_distanza=1L;
        delay_time_distanza=1000;
        delay_time_angolo=1000;

        populateFileds();

        //Bluetooth
        try {
            initializeBluetooth();
        } catch (Exception e) {
             Log.d(TAG, "onCreate: Bluetooth related error");
        }


        hide_texts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!text_hided){
                    text_hided = true;

                    rotazione_ccw_text.setVisibility(View.INVISIBLE);
                    tilt_down_text  .setVisibility(View.INVISIBLE);
                    rotazione_cw_text .setVisibility(View.INVISIBLE);
                    slide_left_text .setVisibility(View.INVISIBLE);
                    slide_right_text.setVisibility(View.INVISIBLE);
                    tilt_up_text.setVisibility(View.INVISIBLE);

                }else {
                    text_hided = false;

                    rotazione_ccw_text.setVisibility(View.VISIBLE);
                    tilt_down_text  .setVisibility(View.VISIBLE);
                    rotazione_cw_text .setVisibility(View.VISIBLE);
                    slide_left_text .setVisibility(View.VISIBLE);
                    slide_right_text.setVisibility(View.VISIBLE);
                    tilt_up_text.setVisibility(View.VISIBLE);

                }
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ManualActivity.this, SettingsActivity.class);
                startActivity(intent);

            }
        });

        go_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBluetoothMessage(GO_END_VALUE);
            }
        });


        // ---------------------------------------------------------------------------------------------------------------------- joystick ---------------------------------------------------------------------------------------------------------------------

        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
               // Log.d(TAG, "onMove: angle: "+angle + " - strenght: " + strength);

                if (angle == 0 || strength == 0){
                    sendBluetoothMessage(STOP_ROTATION_MOTOR);
                    sendBluetoothMessage(STOP_TILTING_MOTOR);
                }

                if (angle > 0 && angle < 45 || angle > 315 ){
                   sendBluetoothMessage(ROTATE_CW_VALUE);
                    rotazione.setText(String.valueOf(Integer.valueOf(rotazione.getText().toString())+1));
                 }else if(angle > 45 && angle < 135){
                   sendBluetoothMessage(TILT_UP_VALUE);
                    textView_tilting.setText(String.valueOf(Integer.valueOf(textView_tilting.getText().toString())+1));
                 }else if(angle > 135 && angle < 225){
                   sendBluetoothMessage(ROTATE_CCW_VALUE);
                    rotazione.setText(String.valueOf(Integer.valueOf(rotazione.getText().toString())-1));
                 }else if (angle > 225 && angle < 315){
                   sendBluetoothMessage(TILT_DOWN_VALUE);
                    textView_tilting.setText(String.valueOf(Integer.valueOf(textView_tilting.getText().toString())-1));
                 }



            }
        },JOYSTICK_MS);



        // ---------------------------------------------------------------------------------------------------------------------- arrows ---------------------------------------------------------------------------------------------------------------------

        arrow_right.setOnTouchListener(new View.OnTouchListener() {

            private Handler mHandler;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "onTouch: pressed");
                        sendBluetoothMessage(SLIDE_RIGHT_VALUE);
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, ARDUINO_DELAY_TIME);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "onTouch: unpressed");

                        sendBluetoothMessage(STOP_LINEAR_MOTOR);
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        break;
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override public void run() {
                  distanza.setText(String.valueOf(Integer.valueOf(distanza.getText().toString())+1));
                   //int local_velox = 2;
                   //delay_time_distanza = ( Math.round(((float) 1/(local_velox))*1000));
                  mHandler.postDelayed(this, LINEAR_MS);
                }
            };

        });


            arrow_left.setOnTouchListener(new View.OnTouchListener() {

                private Handler mHandler;

                @Override public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Log.d(TAG, "onTouch: pressed");
                            sendBluetoothMessage(SLIDE_LEFT_VALUE);
                            if (mHandler != null) return true;
                            mHandler = new Handler();
                            mHandler.postDelayed(mAction, ARDUINO_DELAY_TIME);
                            break;
                        case MotionEvent.ACTION_UP:
                            Log.d(TAG, "onTouch: unpressed");
                            sendBluetoothMessage(STOP_LINEAR_MOTOR);
                            if (mHandler == null) return true;
                            mHandler.removeCallbacks(mAction);
                            mHandler = null;
                            break;
                    }
                    return false;
                }

                Runnable mAction = new Runnable() {
                    @Override public void run() {

                        int local_distance =  Integer.valueOf(distanza.getText().toString());
                        if (local_distance>0){
                            distanza.setText(String.valueOf(local_distance-1));
                        }

                        //int local_velox = 2;
                        //sendBluetoothMessage(11000+ local_velox);
                        //delay_time_distanza = ( Math.round(((float) 1/(local_velox))*1000));
                        mHandler.postDelayed(this, LINEAR_MS);
                    }
                };

            });








        zeroing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBluetoothMessage(GO_HOME_VALUE);
                final int value = Integer.valueOf(distanza.getText().toString());
                Thread t=new Thread(){
                    @Override
                    public void run(){
                        final int[] count = {value};
                        while(count[0] >1){

                            try {
                                Thread.sleep(30);  //1000ms = 1 sec

                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        count[0]--;
                                        distanza.setText(String.valueOf(count[0]));
                                    }
                                });

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                };

                t.start();


                final int valueAngle = Integer.valueOf(rotazione.getText().toString());

                Thread t2=new Thread(){
                    @Override
                    public void run(){
                        final int[] count = {valueAngle};
                        while(count[0] >1 || count[0]<-1){

                            try {
                                Thread.sleep(30);  //1000ms = 1 sec

                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        if (count[0]>0){
                                            count[0]--;
                                        }else {
                                            count[0]++;
                                        }

                                        rotazione.setText(String.valueOf(count[0]));
                                    }
                                });

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                };

                t2.start();

                final int valueTilt = Integer.valueOf(textView_tilting.getText().toString());

                Thread t3=new Thread(){
                    @Override
                    public void run(){
                        final int[] count = {valueAngle};
                        while(count[0] >1 || count[0]<-1){

                            try {
                                Thread.sleep(30);  //1000ms = 1 sec

                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        if (count[0]>0){
                                            count[0]--;
                                        }else {
                                            count[0]++;
                                        }

                                        textView_tilting.setText(String.valueOf(count[0]));
                                    }
                                });

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                };

                t3.start();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Setting savedSettings =  mDatabaseHelper.getSettings();

        GO_HOME_VALUE = savedSettings.getHome_value();
        GO_END_VALUE =savedSettings.getEnd_value();
        SLIDE_LEFT_VALUE = savedSettings.getSlide_left_value();
        SLIDE_RIGHT_VALUE = savedSettings.getSlide_right_value();
        TILT_DOWN_VALUE = savedSettings.getTilt_down_value();
        TILT_UP_VALUE = savedSettings.getTilt_up_value();
        ROTATE_CCW_VALUE = savedSettings.getRotate_CCW_value();
        ROTATE_CW_VALUE = savedSettings.getRotate_CW_value();


    }

    private void populateFileds() {

        Setting setting = mDatabaseHelper.getSettings();
        distance_text.setText(setting.getDistance());
        rotation_text.setText(setting.getRotation());
        tilt_up_text.setText(setting.getTilt_up());
        distance_um.setText(setting.getDistance_um());
        rotation_um.setText(setting.getRotation_um());
        tilting_um.setText(setting.getTilting_um());
        tilting_text.setText(setting.getTilting());
        slide_left_text.setText(setting.getSlide_left());
        slide_right_text.setText(setting.getSlide_right());
        home_text.setText(setting.getHome());
        tilt_down_text.setText(setting.getTilt_down());
        end_text.setText(setting.getEnd());
        rotazione_ccw_text.setText(setting.getRotate_CCW());
        rotazione_cw_text.setText(setting.getRotate_CW());
        tilt_up_text.setText(setting.getTilt_up());

    }




    @SuppressLint("ClickableViewAccessibility")
    private void initializeBluetooth() throws IOException
    {
        bluetooth_connect_device();
    }

    private void bluetooth_connect_device() throws IOException
    {
        try
        {
            myBluetooth = BluetoothAdapter.getDefaultAdapter();
            address = myBluetooth.getAddress();
            pairedDevices = myBluetooth.getBondedDevices();
            if (pairedDevices.size()>0)
            {
                for(BluetoothDevice bt : pairedDevices)
                {
                    address=bt.getAddress().toString();
                    name = bt.getName().toString();


                }
            }

        }
        catch(Exception we){}
        myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
        btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
        btSocket.connect();
        Toast.makeText(getApplicationContext(),"Bluetooth Connesso a: "+name+"\n Bluetooth Address: " + address , Toast.LENGTH_SHORT).show();
        try {

        }
        catch(Exception e){}
    }

    public void sendBluetoothMessage(String m) {

        char i = m.charAt(0);

        try
        {
            if (btSocket!=null)
            {

                Log.d(TAG, "sendBluetoothMessage: SENDING DATA VIA BLUETOOTH, CODE: "+i);
               // Log.d(TAG, "sendBluetoothMessage: SENDING DATA VIA BLUETOOTH, CODED: " +i.toString().getBytes());
                btSocket.getOutputStream().write(String.valueOf(i).getBytes());
            }

        }
        catch (Exception e)
        {

            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


}
