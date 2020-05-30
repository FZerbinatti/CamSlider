package com.francesco.camslider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.lukedeighton.wheelview.WheelView;
import com.lukedeighton.wheelview.adapter.WheelAdapter;

public class ManualActivity extends AppCompatActivity {

    ImageButton timed;
    private static final int ITEM_COUNT = 12;
    private static final int TOTAL_DEGREE_ROTATION = 60;
    private final String TAG ="ManualActivity: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        timed = findViewById(R.id.bottom_timed);
        final WheelView wheelviewLineare = (WheelView) findViewById(R.id.wheelviewLineare);
        final WheelView wheelviewRotazione = (WheelView) findViewById(R.id.wheelviewRotazione);
        final MainActivity mainActivity= new MainActivity();


        wheelviewRotazione.setWheelItemCount(ITEM_COUNT);
        wheelviewLineare.setWheelItemCount(ITEM_COUNT);


        final ShapeDrawable[] shapeDrawables = new ShapeDrawable[ITEM_COUNT];

        timed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManualActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        for (int i =0; i<ITEM_COUNT; i++){
            shapeDrawables[i] = new ShapeDrawable( new OvalShape());
            shapeDrawables[i].getPaint().setColor(getResources().getColor(R.color.transparent));

        }


        // ---------------------------------------------------------------------------------------------------------------------- wheel rotazione ---------------------------------------------------------------------------------------------------------------------


        wheelviewRotazione.setAdapter(new WheelAdapter() {
            @Override
            public Drawable getDrawable(int position) {
                //return drawable here - the position can be seen in the gifs above
                Drawable[] drawable = new Drawable[] {

                        shapeDrawables[position], new TextDrawableNoNumbers(String.valueOf(mainActivity.positionToDegree(position)))

                };
                return new LayerDrawable(drawable);

            }

            @Override
            public int getCount() {
                //return the count
                return ITEM_COUNT;
            }
        });

        wheelviewRotazione   .setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectListener() {
            int num_precedente =0;
            @Override
            public void onWheelItemSelected(WheelView parent,  Drawable itemDrawable, int position) {
                //the adapter position that is closest to the selection angle and it's drawable
                Log.d(TAG, "onWheelItemSelected: position:" + position);

/*                if (angolo_state_inizio){
                    Log.d(TAG, "onWheelItemSelected: inizio con angolo");
                    sendBluetoothMessage(2000+(position*(TOTAL_DEGREE_ROTATION/ITEM_COUNT)));
                }else {
                    Log.d(TAG, "onWheelItemSelected: fine con angolo");
                    sendBluetoothMessage(3000+(position*(TOTAL_DEGREE_ROTATION/ITEM_COUNT)));
                }*/

            }
        });

        // ---------------------------------------------------------------------------------------------------------------------- wheel lineare ---------------------------------------------------------------------------------------------------------------------


        wheelviewLineare.setAdapter(new WheelAdapter() {
            @Override
            public Drawable getDrawable(int position) {
                //return drawable here - the position can be seen in the gifs above
                Drawable[] drawable = new Drawable[] {

                        shapeDrawables[position], new TextDrawableNoNumbers(String.valueOf(mainActivity.positionToDegree(position)))

                };
                return new LayerDrawable(drawable);

            }

            @Override
            public int getCount() {
                //return the count
                return ITEM_COUNT;
            }
        });

        wheelviewLineare   .setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectListener() {
            int num_precedente =0;
            @Override
            public void onWheelItemSelected(WheelView parent,  Drawable itemDrawable, int position) {
                //the adapter position that is closest to the selection angle and it's drawable
                Log.d(TAG, "onWheelItemSelected: position:" + position);

/*                if (angolo_state_inizio){
                    Log.d(TAG, "onWheelItemSelected: inizio con angolo");
                    sendBluetoothMessage(2000+(position*(TOTAL_DEGREE_ROTATION/ITEM_COUNT)));
                }else {
                    Log.d(TAG, "onWheelItemSelected: fine con angolo");
                    sendBluetoothMessage(3000+(position*(TOTAL_DEGREE_ROTATION/ITEM_COUNT)));
                }*/

            }
        });


    }

}
