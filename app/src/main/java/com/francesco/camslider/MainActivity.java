package com.francesco.camslider;

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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lukedeighton.wheelview.WheelView;
import com.lukedeighton.wheelview.adapter.WheelAdapter;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final int ITEM_COUNT = 12;
    private static final int TOTAL_DEGREE_ROTATION = 60;
    private final String TAG ="MainActivity: ";
    NumberPicker minuti, secondi;
    SeekBar distanza;
    TextView textview_distanza, text_distanza, text_angolo;
    Integer timer_tot_sec, int_distanza_inizio, int_distanza_fine =0;
    Button start, inizio, fine;
    ImageButton manual;
    Boolean distanza_state_inizio, angolo_state_inizio,  hasSetStart, hasSetEnd;

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
        final WheelView wheelView = (WheelView) findViewById(R.id.wheelviewLineare);
        wheelView.setWheelItemCount(ITEM_COUNT);
        final ShapeDrawable[] shapeDrawables = new ShapeDrawable[ITEM_COUNT];
        textview_distanza = findViewById(R.id.textView_distanza);
        text_distanza = findViewById(R.id.text_distanza);
        text_angolo = findViewById(R.id.text_angolo);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        distanza_state_inizio = true;
        angolo_state_inizio = true;
        hasSetEnd=false;
        hasSetStart=false;
        int_distanza_fine=0;
        int_distanza_inizio=0;

        //Bluetooth
        try {
            initializeBluetooth();
        } catch (Exception e) {
           // Log.d(TAG, "onCreate: ERRORE INCREDIBILE while SETTING UP BLUETOOTH");
        }


        //Buttons
        start = findViewById(R.id.inizia_sessione);
        inizio = findViewById(R.id.button_inizio);
        fine = findViewById(R.id.button_fine);
        manual = findViewById(R.id.bluetooth);


        inizio.setBackgroundResource((R.drawable.button_blue_background));


        // Number Pickers
        minuti = findViewById(R.id.numberPicker_minuti);
        secondi = findViewById(R.id.numberPicker_secondi);
        minuti.setMinValue(0);
        minuti.setMaxValue(59);
        secondi.setMinValue(0);
        secondi.setMaxValue(59);




        // Seekbar
        distanza = findViewById(R.id.seekBar_distanza);
        distanza.setMax(200);
        distanza.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                textview_distanza.setText(from_int_to_StringFloat(i));
                if (distanza_state_inizio){
                    sendBluetoothMessage(i);
                }else {
                    sendBluetoothMessage(i+1000);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


                if (distanza_state_inizio){
                    hasSetStart=true;
                    int_distanza_inizio = distanza.getProgress();
                  //  Log.d(TAG, "onStopTrackingTouch: "+int_distanza_inizio);
                }else {
                    hasSetEnd=true;
                    int_distanza_fine = distanza.getProgress();
                  //  Log.d(TAG, "onStopTrackingTouch: "+int_distanza_fine);
                }

            }
        });

        for (int i =0; i<ITEM_COUNT; i++){
            shapeDrawables[i] = new ShapeDrawable( new OvalShape());
            shapeDrawables[i].getPaint().setColor(getResources().getColor(R.color.transparent));

        }


        wheelView.setAdapter(new WheelAdapter() {
            @Override
            public Drawable getDrawable(int position) {
                //return drawable here - the position can be seen in the gifs above
                Drawable[] drawable = new Drawable[] {

                        shapeDrawables[position],
                        new TextDrawable(String.valueOf(positionToDegree(position)))
                };
                return new LayerDrawable(drawable);

            }

            @Override
            public int getCount() {
                //return the count
                return ITEM_COUNT;
            }
        });

/*
        wheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectListener() {
            int num_precedente =0;
            @Override
            public void onWheelItemSelected(WheelView parent,  Drawable itemDrawable, int position) {
                //the adapter position that is closest to the selection angle and it's drawable
             //   Log.d(TAG, "onWheelItemSelected: position:" + position);

                if (angolo_state_inizio){
                   // Log.d(TAG, "onWheelItemSelected: inizio con angolo");
                    sendBluetoothMessage(2000+(position*(TOTAL_DEGREE_ROTATION/ITEM_COUNT)));
                }else {
                  //  Log.d(TAG, "onWheelItemSelected: fine con angolo");
                    sendBluetoothMessage(3000+(position*(TOTAL_DEGREE_ROTATION/ITEM_COUNT)));
                }

            }
        });
*/


        wheelView.setOnWheelAngleChangeListener(new WheelView.OnWheelAngleChangeListener() {
            @Override
            public void onWheelAngleChange(float angle) {
              //  Log.d(TAG, "onWheelAngleChange: angle: "+Math.round(angle));
                int rounded = Math.round(angle);
                //the new angle of the wheel
                if (angolo_state_inizio){
                //    Log.d(TAG, "onWheelItemSelected: inizio con angolo");
                    sendBluetoothMessage(2360+(rounded));
                }else {
                 //   Log.d(TAG, "onWheelItemSelected: fine con angolo");
                    sendBluetoothMessage(3360+(rounded));
                }



            }
        });



        inizio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inizio.setBackgroundResource((R.drawable.button_blue_background));
                fine.setBackgroundResource((R.drawable.button_white_background));

                distanza_state_inizio = true;
                angolo_state_inizio = true;
                text_angolo.setText(getResources().getString(R.string.angolo_camera_di_inizio));
                text_distanza.setText(getResources().getString(R.string.distanza_inizio));
               // Log.d(TAG, "onClick: int_distanza_inizio:"+int_distanza_inizio);
                if (hasSetStart){
                    textview_distanza.setText(from_int_to_StringFloat(int_distanza_inizio));
                    distanza.setProgress(int_distanza_inizio);

                }else {
                    textview_distanza.setText("0.00");
                    distanza.setProgress(0);
                }

            }
        });

        fine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fine.setBackgroundResource((R.drawable.button_blue_background));
                inizio.setBackgroundResource((R.drawable.button_white_background));
                // mi treovo nella parte di dichiarazione finale
                distanza_state_inizio =    false;
                angolo_state_inizio = false;
                text_angolo.setText(getResources().getString(R.string.angolo_camera_di_fine));
                text_distanza.setText(getResources().getString(R.string.distanza_fine));
                //resetto il visualizzatore di distanza
                if (hasSetEnd){
                    textview_distanza.setText(from_int_to_StringFloat(int_distanza_fine));
                    distanza.setProgress(int_distanza_fine);
                }else {
                    textview_distanza.setText("2.00");
                    distanza.setProgress(200);
                }

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

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initializeBluetooth() throws IOException {
        bluetooth_connect_device();
        start=(Button)findViewById(R.id.inizia_sessione);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer_tot_sec = (minuti.getValue())*60 + secondi.getValue();
                int diff_distance = int_distanza_fine-int_distanza_inizio;
              //  Log.d(TAG, "onClick: timer_tot_sec:" + timer_tot_sec);
                if (timer_tot_sec < 10){
                    Toast.makeText(MainActivity.this, "Minimo possibile: 10 secondi",
                            Toast.LENGTH_LONG).show();
                    if(diff_distance<0){
                        Toast.makeText(MainActivity.this, "L'inizio dev'essere prima della fine",
                                Toast.LENGTH_LONG).show();
                    }
                }
                if (timer_tot_sec > 10 && diff_distance >0){

                    //Bluetooth shit
                //    Log.d(TAG, "onClick: sending bluetooth message: timer_tot_sec, CODE:"+timer_tot_sec);
                    sendBluetoothMessage(timer_tot_sec+4000);
                    //sendBluetoothMessage(1);

                    //Bluetooth shit

                    Toast.makeText(MainActivity.this, "Inviando impostazioni, inizio sessione",
                            Toast.LENGTH_LONG).show();
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

    public void sendBluetoothMessage(Integer i) {
        try
        {
            if (btSocket!=null)
            {
                Log.d(TAG, "sendBluetoothMessage: SENDING DATA VIA BLUETOOTH, CODE: "+i);
                btSocket.getOutputStream().write(i.toString().getBytes());
            }

        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public int positionToDegree(int position){
        if (position == 0){
            return 0;}
        else {
            return position*(TOTAL_DEGREE_ROTATION/ITEM_COUNT);
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

    public String from_int_to_StringFloat(int distance_cm){
        return String.valueOf(distance_cm/100+"."+String.format("%02d", distance_cm%100));
    }



}
