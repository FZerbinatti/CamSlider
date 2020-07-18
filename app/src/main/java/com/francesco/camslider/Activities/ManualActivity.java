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
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
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
    private static final char STOP_LINEAR_MOTOR = 'i';
    private static final char STOP_ROTATION_MOTOR = 'm';
    private static final char STOP_TILTING_MOTOR = 'l';
    private static final char HOMING_SEQUENCE = 'g';
    private static final char GO_END = 'h';
    private static final char SLIDE_LEFT = 'a';
    private static final char SLIDE_RIGHT = 'b';
    private static final char TILT_DOWN = 'c';
    private static final char TILT_UP = 'd';
    private static final char ROTATE_LEFT = 'e';
    private static final char ROTATE_RIGHT = 'f';
    private static final char THIS_IS_ZERO = 'z';

    private static final int JOYSTICK_Hz = 5;
    private static final int JOYSTICK_MS = ( Math.round(((float) 1/(JOYSTICK_Hz))*1000));


    private static final int LINEAR_Hz = 2;
    private static final int LINEAR_MS = ( Math.round(((float) 1/(LINEAR_Hz))*1000));


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
    Button linear_speed_1,linear_speed_2,linear_speed_3,linear_speed_4;
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
        this_is_zero = findViewById(R.id.button_this_is_zero);

/*      linear_speed_1 = findViewById(R.id.linear_speed_1);
        linear_speed_2 = findViewById(R.id.linear_speed_2);
        linear_speed_3 = findViewById(R.id.linear_speed_3);
        linear_speed_4 = findViewById(R.id.linear_speed_4);*/

/*        velocita_movimento = findViewById(R.id.textView_velocitaMovimento);
        velocita_rotazione = findViewById(R.id.textView_velocitaRotazione);*/
        rotazione = findViewById(R.id.textView_rotazione);
        distanza = findViewById(R.id.textView_movimento);
        textView_tilting = findViewById(R.id.textView_tilting);
        settings = findViewById(R.id.button_settings);


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
            // Log.d(TAG, "onCreate: ERRORE INCREDIBILE while SETTING UP BLUETOOTH");
        }

        final ShapeDrawable[] shapeDrawables = new ShapeDrawable[ITEM_COUNT];

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
                sendBluetoothMessage(GO_END);
            }
        });




        for (int i =0; i<ITEM_COUNT; i++){
            shapeDrawables[i] = new ShapeDrawable( new OvalShape());
            shapeDrawables[i].getPaint().setColor(getResources().getColor(R.color.transparent));

        }

/*
        // ---------------------------------------------------------------------------------------------------------------------- bottoni speed ---------------------------------------------------------------------------------------------------------------------

        linear_speed_1.setBackgroundResource(R.drawable.button_blue_background);

        linear_speed_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linear_speed_1.setBackgroundResource(R.drawable.button_blue_background);
                linear_speed_2.setBackgroundResource(R.drawable.button_white_background);
                linear_speed_3.setBackgroundResource(R.drawable.button_white_background);
                linear_speed_4.setBackgroundResource(R.drawable.button_white_background);

                linear_speed = 1;
            }
        });
        linear_speed_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linear_speed_1.setBackgroundResource(R.drawable.button_white_background);
                linear_speed_2.setBackgroundResource(R.drawable.button_blue_background);
                linear_speed_3.setBackgroundResource(R.drawable.button_white_background);
                linear_speed_4.setBackgroundResource(R.drawable.button_white_background);

                linear_speed = 2;
            }
        });
        linear_speed_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linear_speed_1.setBackgroundResource(R.drawable.button_white_background);
                linear_speed_2.setBackgroundResource(R.drawable.button_white_background);
                linear_speed_3.setBackgroundResource(R.drawable.button_blue_background);
                linear_speed_4.setBackgroundResource(R.drawable.button_white_background);

                linear_speed = 3;
            }
        });
        linear_speed_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linear_speed_1.setBackgroundResource(R.drawable.button_white_background);
                linear_speed_2.setBackgroundResource(R.drawable.button_white_background);
                linear_speed_3.setBackgroundResource(R.drawable.button_white_background);
                linear_speed_4.setBackgroundResource(R.drawable.button_blue_background);

                linear_speed = 4;
            }
        });
*/

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
                   sendBluetoothMessage(ROTATE_RIGHT);
                    rotazione.setText(String.valueOf(Integer.valueOf(rotazione.getText().toString())+1));
                 }else if(angle > 45 && angle < 135){
                   sendBluetoothMessage(TILT_UP);
                    textView_tilting.setText(String.valueOf(Integer.valueOf(textView_tilting.getText().toString())+1));
                 }else if(angle > 135 && angle < 225){
                   sendBluetoothMessage(ROTATE_LEFT);
                    rotazione.setText(String.valueOf(Integer.valueOf(rotazione.getText().toString())-1));
                 }else if (angle > 225 && angle < 315){
                   sendBluetoothMessage(TILT_DOWN);
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
                        sendBluetoothMessage(SLIDE_RIGHT);
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
                            sendBluetoothMessage(SLIDE_LEFT);
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


            this_is_zero.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendBluetoothMessage(THIS_IS_ZERO);
                }
            });






        zeroing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBluetoothMessage(HOMING_SEQUENCE);
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
                    Toast.makeText(getApplicationContext(),"Connected", Toast.LENGTH_SHORT).show();

                }
            }

        }
        catch(Exception we){}
        myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
        btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
        btSocket.connect();
        Toast.makeText(getApplicationContext(),"bluetooth_connect_device: "+"BT Name: "+name+"\nBT Address: " , Toast.LENGTH_SHORT).show();
        try {

        }
        catch(Exception e){}
    }

    public void sendBluetoothMessage(char i) {

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
