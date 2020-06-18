package com.francesco.camslider;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import java.io.IOException;
import java.util.Set;
import java.util.Timer;
import java.util.UUID;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lukedeighton.wheelview.WheelView;
import com.lukedeighton.wheelview.adapter.WheelAdapter;

public class ManualActivity extends AppCompatActivity implements CounterHandler.CounterListener{

    ImageButton timed;
    private static final int STOP_ALL_MOTOR = -1;
    private static final int STOP_LINEAR_MOTOR = -2;
    private static final int STOP_ROTATION_MOTOR = -3;
    private static final int ITEM_COUNT = 10;
    private static final int TOTAL_DEGREE_ROTATION = 10;
    private final String TAG ="ManualActivity: ";
    TextView velocita_rotazione, rotazione, velocita_movimento, distanza;
    ImageButton zeroing, arrow_left, arrow_right, arrow_rotate_left, arrow_rotate_right, hide_texts;
    Long velox_angolo, velox_distanza;
    MainActivity mainActivity;
    Integer delay_time_angolo, delay_time_distanza;
    Boolean text_hided;

    TextView textView01, textView02, textView03, textView04, textView05, textView06, textView07;

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
        timed = findViewById(R.id.bottom_timed);
        final WheelView wheelviewLineare = (WheelView) findViewById(R.id.wheelviewLineare);
        final WheelView wheelviewRotazione = (WheelView) findViewById(R.id.wheelviewRotazione);

        textView01 = findViewById(R.id.textView01);
        textView02 = findViewById(R.id.textView02);
        textView03 = findViewById(R.id.textView03);
        textView04 = findViewById(R.id.textView04);
        textView05 = findViewById(R.id.textView05);
        textView06 = findViewById(R.id.textView06);
        textView07 = findViewById(R.id.textView07);
        hide_texts = findViewById(R.id.hide_texts);
        text_hided = false;

        velocita_movimento = findViewById(R.id.textView_velocitaMovimento);
        velocita_rotazione = findViewById(R.id.textView_velocitaRotazione);
        rotazione = findViewById(R.id.textView_rotazione);
        distanza = findViewById(R.id.textView_movimento);

        zeroing= findViewById(R.id. button_zeroing);
        arrow_left= findViewById(R.id.arrow_left);
        arrow_right= findViewById(R.id. arrow_right);
        arrow_rotate_left= findViewById(R.id. arrow_rotate_left);
        arrow_rotate_right= findViewById(R.id. arrow_rotate_right);

        wheelviewRotazione.setWheelItemCount(ITEM_COUNT);
        wheelviewLineare.setWheelItemCount(ITEM_COUNT);


        velox_angolo =1L;
        velox_distanza=1L;
        delay_time_distanza=1000;
        delay_time_angolo=1000;

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
                    textView01.setVisibility(View.INVISIBLE);
                    textView02.setVisibility(View.INVISIBLE);
                    textView03.setVisibility(View.INVISIBLE);
                    textView04.setVisibility(View.INVISIBLE);
                    textView05.setVisibility(View.INVISIBLE);
                    textView06.setVisibility(View.INVISIBLE);
                    textView07.setVisibility(View.INVISIBLE);
                }else {
                    text_hided = false;
                    textView01.setVisibility(View.VISIBLE);
                    textView02.setVisibility(View.VISIBLE);
                    textView03.setVisibility(View.VISIBLE);
                    textView04.setVisibility(View.VISIBLE);
                    textView05.setVisibility(View.VISIBLE);
                    textView06.setVisibility(View.VISIBLE);
                    textView07.setVisibility(View.VISIBLE);
                }
            }
        });

        timed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManualActivity.this, MainActivity.class);

/*                if (btSocket != null) {
                    try {
                        btSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*/
                startActivity(intent);
                finish();
            }
        });


        for (int i =0; i<ITEM_COUNT; i++){
            shapeDrawables[i] = new ShapeDrawable( new OvalShape());
            shapeDrawables[i].getPaint().setColor(getResources().getColor(R.color.transparent));

        }



        // ---------------------------------------------------------------------------------------------------------------------- arrows ---------------------------------------------------------------------------------------------------------------------

        arrow_right.setOnTouchListener(new View.OnTouchListener() {

            private Handler mHandler;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "onTouch: pressed");
                        sendBluetoothMessage(10000);
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 1);
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
                   int local_velox = Integer.valueOf(velocita_movimento.getText().toString());
                   delay_time_distanza = ( Math.round(((float) 1/(local_velox))*1000));
                  mHandler.postDelayed(this, delay_time_distanza);
                }
            };

        });


            arrow_left.setOnTouchListener(new View.OnTouchListener() {

                private Handler mHandler;

                @Override public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Log.d(TAG, "onTouch: pressed");
                            sendBluetoothMessage(11000);
                            if (mHandler != null) return true;
                            mHandler = new Handler();
                            mHandler.postDelayed(mAction, 1);
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

                        int local_velox = Integer.valueOf(velocita_movimento.getText().toString());
                        //sendBluetoothMessage(11000+ local_velox);
                        delay_time_distanza = ( Math.round(((float) 1/(local_velox))*1000));
                        mHandler.postDelayed(this, delay_time_distanza);
                    }
                };

            });




        arrow_rotate_right.setOnTouchListener(new View.OnTouchListener() {

            private Handler mHandler;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "onTouch: pressed");
                        sendBluetoothMessage(12000);
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 1);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "onTouch: unpressed");
                        sendBluetoothMessage(STOP_ROTATION_MOTOR);
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        break;
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override public void run() {
                    rotazione.setText(String.valueOf(Integer.valueOf(rotazione.getText().toString())+1));
                    int local_angle = Integer.valueOf(velocita_rotazione.getText().toString());
                    //sendBluetoothMessage(12000+ local_angle);
                    delay_time_angolo = ( Math.round(((float) 1/(local_angle))*1000));
                    mHandler.postDelayed(this, delay_time_angolo);
                }
            };

        });

        arrow_rotate_left.setOnTouchListener(new View.OnTouchListener() {

            private Handler mHandler;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "onTouch: pressed");
                        sendBluetoothMessage(13000);
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 1);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "onTouch: unpressed");
                        sendBluetoothMessage(STOP_ROTATION_MOTOR);
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        break;
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override public void run() {
                    rotazione.setText(String.valueOf(Integer.valueOf(rotazione.getText().toString())-1));
                    int local_angle = Integer.valueOf(velocita_rotazione.getText().toString());
                    //sendBluetoothMessage(13000+ local_angle);
                    delay_time_angolo = ( Math.round(((float) 1/(local_angle))*1000));
                    mHandler.postDelayed(this, delay_time_angolo);
                }
            };

        });

        zeroing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBluetoothMessage(STOP_ALL_MOTOR);
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










            }
        });









        // ---------------------------------------------------------------------------------------------------------------------- wheel rotazione ---------------------------------------------------------------------------------------------------------------------


        wheelviewRotazione.setAdapter(new WheelAdapter() {
            @Override
            public Drawable getDrawable(int position) {
                //return drawable here - the position can be seen in the gifs above
                Drawable[] drawable = new Drawable[] {

                        shapeDrawables[position], new TextDrawableNoNumbers(String.valueOf((position+1)))

                };
                return new LayerDrawable(drawable);

            }

            @Override
            public int getCount() {
                //return the count
                return ITEM_COUNT;
            }
        });

        wheelviewRotazione.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectListener() {
            @Override
            public void onWheelItemSelected(WheelView parent, Drawable itemDrawable, int position) {
                Log.d(TAG, "onWheelItemSelected: position:" + position);
                int incrementedPosition = position+1;
                velocita_rotazione.setText(""+incrementedPosition);
                sendBluetoothMessage(11000+incrementedPosition);
            }
        });


        // angle change listener
        /*

                wheelviewRotazione.setOnWheelAngleChangeListener(new WheelView.OnWheelAngleChangeListener() {
                    @Override
                    public void onWheelAngleChange(float angle) {
                        int rounded_rotazione  = Math.round(angle);
          //              Log.d(TAG, "onWheelAngleChange: angle: "+rounded_rotazione);
                        //the new angle of the wheel
                        if (rounded_rotazione<0){
                            velocita_rotazione.setText("0");
                        }else {
                            velox_angolo = Math.round(rounded_rotazione*0.1 );
                            velocita_rotazione.setText( String.valueOf( velox_angolo ));

                            if (velox_angolo>0){


                                new CounterHandler.Builder()
                                        .incrementalView(arrow_rotate_right)
                                        .decrementalView(arrow_rotate_left)
                                        .minRange(0) // cant go any less than -50
                                        .maxRange(200) // cant go any further than 50
                                        .code(2)
                                        .isCycle(true) // 49,50,-50,-49 and so on
                                        .counterDelay( delay_time_angolo  ) // speed of counter
                                        .counterStep(1)  // steps e.g. 0,2,4,6...
                                        .listener(ManualActivity.this) // to listen counter results and show them in app
                                        .build();

                            }
                        }

                    }
                });
        */

        // ---------------------------------------------------------------------------------------------------------------------- wheel lineare ---------------------------------------------------------------------------------------------------------------------


        wheelviewLineare.setAdapter(new WheelAdapter() {
            @Override
            public Drawable getDrawable(int position) {
                Drawable[] drawable = new Drawable[] {
                        shapeDrawables[position], new TextDrawableNoNumbers(String.valueOf((position+1)))
                };
                return new LayerDrawable(drawable);
            }

            @Override
            public int getCount() {
                //return the count
                return ITEM_COUNT;
            }
        });


        wheelviewLineare.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectListener() {
            @Override
            public void onWheelItemSelected(WheelView parent,  Drawable itemDrawable, int position) {
                //the adapter position that is closest to the selection angle and it's drawable
                   Log.d(TAG, "onWheelItemSelected: position:" + position);
                    int incrementedPosition = position+1;
                   velocita_movimento.setText(""+incrementedPosition);
                    sendBluetoothMessage(10000+incrementedPosition);
            }
        });

        // angle change listener
        /*        wheelviewLineare.setOnWheelAngleChangeListener(new WheelView.OnWheelAngleChangeListener() {
                    @Override
                    public void onWheelAngleChange(float angle) {
                        int rounded_movimento = Math.round(angle);
                        //Log.d(TAG, "onWheelAngleChange: angle: "+rounded_movimento);
                        //the new angle of the wheel
                        if (rounded_movimento<1){
                            velocita_movimento.setText("0");

                        }else{

                            velox_distanza = Math.round(rounded_movimento*0.1 );

                            velocita_movimento.setText( String.valueOf( velox_distanza ));

                            if (velox_distanza>0){
                                delay_time_distanza = ( Math.round(((float) 1/(velox_distanza))*1000));

                                new CounterHandler.Builder()
                                        .incrementalView(arrow_right)
                                        .decrementalView(arrow_left)
                                        .minRange(0) // cant go any less than -50
                                        .maxRange(100) // cant go any further than 50
                                        .code(1)
                                        .isCycle(true) // 49,50,-50,-49 and so on
                                        .counterDelay( delay_time_distanza  ) // speed of counter
                                        .counterStep(1)  // steps e.g. 0,2,4,6...
                                        .listener(ManualActivity.this) // to listen counter results and show them in app
                                        .build();

                            }
                        }
                    }
                });*/


    }


    @Override
    public void onIncrement(View view, long number, int code) {

        if (code == 1) {
            distanza.setText(String.valueOf(number));
        }else if (code == 2 ){
            rotazione.setText(String.valueOf(number));
        }

    }

    @Override
    public void onDecrement(View view, long number, int code) {
        if (code == 1) {
            distanza.setText(String.valueOf(number));
        }else if (code == 2 ){
            rotazione.setText(String.valueOf(number));
        }
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

    public void sendBluetoothMessage(Integer i) {
/*        if (i==-1){
            Log.d(TAG, "sendBluetoothMessage: STOP ALL MOTORS");
        } else if (i==-2){
            Log.d(TAG, "sendBluetoothMessage: STOP LINEAR MOTORS");
        }  else if (i==-3){
        Log.d(TAG, "sendBluetoothMessage: STOP ROTATE MOTORS");
        }*/
        try
        {
            if (btSocket!=null)
            {

                Log.d(TAG, "sendBluetoothMessage: SENDING DATA VIA BLUETOOTH, CODE: "+i);
               // Log.d(TAG, "sendBluetoothMessage: SENDING DATA VIA BLUETOOTH, CODED: " +i.toString().getBytes());
                btSocket.getOutputStream().write(i.toString().getBytes());
            }

        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


}
