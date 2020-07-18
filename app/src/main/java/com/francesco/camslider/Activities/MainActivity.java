package com.francesco.camslider.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.francesco.camslider.R;
import com.francesco.camslider.GraphicLibraries.TextDrawable;
import com.lukedeighton.wheelview.WheelView;
import com.lukedeighton.wheelview.adapter.WheelAdapter;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity {

    private final String TAG ="MainActivity: ";
    NumberPicker minuti, secondi;

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

    private static final int JOYSTICK_Hz = 5;
    private static final int JOYSTICK_MS = ( Math.round(((float) 1/(JOYSTICK_Hz))*1000));


    private static final int LINEAR_Hz = 2;
    private static final int LINEAR_MS = ( Math.round(((float) 1/(LINEAR_Hz))*1000));


    private static final int ARDUINO_DELAY_TIME = 0;



    TextView textview_distanza, text_distanza;
    Integer timer_tot_sec, int_distanza_inizio, int_distanza_fine =0;
    Button start, inizio, fine;
    ImageButton manual;
    Boolean distanza_state_inizio, angolo_state_inizio,  hasSetStart, hasSetEnd;
    Integer lastAngle ;
    ImageButton  arrow_left, arrow_right;
    Button linear_speed_1,linear_speed_2,linear_speed_3,linear_speed_4;
    Integer linear_speed=1;


    //Bluetooth setups
    String address = null , name = null;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    Set<BluetoothDevice> pairedDevices;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text_distanza = findViewById(R.id.text_distanza);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        distanza_state_inizio = true;
        angolo_state_inizio = true;
        hasSetEnd=false;
        hasSetStart=false;
        JoystickView joystick;
        joystick = (JoystickView) findViewById(R.id.joystickView_auto);

        //Bluetooth
        try {
            initializeBluetooth();
        } catch (Exception e) {
           // Log.d(TAG, "onCreate: ERRORE INCREDIBILE while SETTING UP BLUETOOTH");
        }

        //Buttons
        start = findViewById(R.id.settings_save);
        inizio = findViewById(R.id.button_inizio);
        fine = findViewById(R.id.button_fine);
        manual = findViewById(R.id.bluetooth);
        arrow_left= findViewById(R.id.auto_arrow_left);
        arrow_right= findViewById(R.id.auto_arrow_right);

        linear_speed_1 = findViewById(R.id.linear_speed_1);
        linear_speed_2 = findViewById(R.id.linear_speed_2);
        linear_speed_3 = findViewById(R.id.linear_speed_3);
        linear_speed_4 = findViewById(R.id.linear_speed_4);


        //      inizio.setBackgroundResource((R.drawable.button_blue_background));


        inizio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

  //              inizio.setBackgroundResource((R.drawable.button_white_background));
  //              fine.setBackgroundResource((R.drawable.button_blue_background));

                distanza_state_inizio = true;
                angolo_state_inizio = true;
                text_distanza.setText(getResources().getString(R.string.distanza_inizio));
               // Log.d(TAG, "onClick: int_distanza_inizio:"+int_distanza_inizio);

            }
        });

        fine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

     //           fine.setBackgroundResource((R.drawable.button_white_background));
     //           inizio.setBackgroundResource((R.drawable.button_blue_background));
                // mi treovo nella parte di dichiarazione finale
                distanza_state_inizio =    false;
                angolo_state_inizio = false;
                text_distanza.setText(getResources().getString(R.string.distanza_fine));


            }
        });

        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this, ManualActivity.class);
               /*if (btSocket != null) {
                   try {
                       btSocket.close();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }*/
                startActivity(intent);
            }
        });


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
                    mHandler.postDelayed(this, LINEAR_MS);
                }
            };

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
                    sendBluetoothMessage(ROTATE_RIGHT);
                }else if(angle > 45 && angle < 135){
                    sendBluetoothMessage(TILT_UP);
                }else if(angle > 135 && angle < 225){
                    sendBluetoothMessage(ROTATE_LEFT);
                }else if (angle > 225 && angle < 315){
                    sendBluetoothMessage(TILT_DOWN);
                }

            }
        },JOYSTICK_MS);


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

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initializeBluetooth() throws IOException {
        bluetooth_connect_device();
        start=(Button)findViewById(R.id.settings_save);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (linear_speed==1){
                    sendBluetoothMessage('p');
                }else if (linear_speed==2){
                    sendBluetoothMessage('q');
                }else if (linear_speed==2){
                    sendBluetoothMessage('r');
                }else if (linear_speed==2){
                    sendBluetoothMessage('s');
                }

            }
        });



    }

    private void bluetooth_connect_device() throws IOException {
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
                btSocket.getOutputStream().write(String.valueOf(i).getBytes());
            }

        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        //savedInstanceState.putInt("MyBoolean", timer_tot_sec);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
/*        boolean myBoolean = savedInstanceState.getBoolean("MyBoolean");
        double myDouble = savedInstanceState.getDouble("myDouble");
        int myInt = savedInstanceState.getInt("MyInt");
        String myString = savedInstanceState.getString("MyString");*/
    }




}
