package com.francesco.camslider;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lukedeighton.wheelview.Circle;
import com.lukedeighton.wheelview.WheelView;
import com.lukedeighton.wheelview.adapter.WheelAdapter;
import com.lukedeighton.wheelview.adapter.WheelArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final int ITEM_COUNT = 12;
    private static final int TOTAL_DEGREE_ROTATION = 60;
    private final String TAG ="MainActivity: ";
    NumberPicker minuti, secondi;
    SeekBar distanza;
    TextView textview_distanza, text_distanza, text_angolo;
    Integer timer_tot_sec, int_distanza_inizio, int_distanza_fine =0;
    Button start, inizio, fine;
    Boolean state_inizio, hasSetStart, hasSetEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WheelView wheelView = (WheelView) findViewById(R.id.wheelview);
        wheelView.setWheelItemCount(ITEM_COUNT);
        final ShapeDrawable[] shapeDrawables = new ShapeDrawable[ITEM_COUNT];
        textview_distanza = findViewById(R.id.textView_distanza);
        text_distanza = findViewById(R.id.text_distanza);
        text_angolo = findViewById(R.id.text_angolo);

        state_inizio = true;
        hasSetEnd=false;
        hasSetStart=false;
        int_distanza_fine=0;
        int_distanza_inizio=0;


        //Buttons
        start = findViewById(R.id.inizia_sessione);
        inizio = findViewById(R.id.button_inizio);
        fine = findViewById(R.id.button_fine);



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

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


                if (state_inizio){
                    hasSetStart=true;
                    int_distanza_inizio = distanza.getProgress();
                    Log.d(TAG, "onStopTrackingTouch: "+int_distanza_inizio);
                }else {
                    hasSetEnd=true;
                    int_distanza_fine = distanza.getProgress();
                    Log.d(TAG, "onStopTrackingTouch: "+int_distanza_fine);
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

        wheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectListener() {
            @Override
            public void onWheelItemSelected(WheelView parent,  Drawable itemDrawable, int position) {
                //the adapter position that is closest to the selection angle and it's drawable
                Log.d(TAG, "onWheelItemSelected: position:" + position);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer_tot_sec = (minuti.getValue())*60 + secondi.getValue();
                int diff_distance = int_distanza_fine-int_distanza_inizio;
                Log.d(TAG, "onClick: timer_tot_sec:" + timer_tot_sec);
                if (timer_tot_sec < 10){
                    Toast.makeText(MainActivity.this, "Minimo possibile: 10 secondi",
                            Toast.LENGTH_LONG).show();
                    if(diff_distance<0){
                        Toast.makeText(MainActivity.this, "L'inizio dev'essere prima della fine",
                                Toast.LENGTH_LONG).show();
                    }
                }
                if (timer_tot_sec > 10 && diff_distance >0){
                    Toast.makeText(MainActivity.this, "Inviando impostazioni, inizio sessione",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        inizio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state_inizio=true;
                text_angolo.setText(getResources().getString(R.string.angolo_camera_di_inizio));
                text_distanza.setText(getResources().getString(R.string.distanza_inizio));
                Log.d(TAG, "onClick: int_distanza_inizio:"+int_distanza_inizio);
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
                // mi treovo nella parte di dichiarazione finale
                state_inizio=false;
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

    }

    private int positionToDegree(int position){
        if (position == 0){
            return TOTAL_DEGREE_ROTATION;}
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
