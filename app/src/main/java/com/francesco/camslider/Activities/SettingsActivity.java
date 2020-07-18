package com.francesco.camslider.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.francesco.camslider.Database.DatabaseHelper;
import com.francesco.camslider.Objects.Setting;
import com.francesco.camslider.R;

public class SettingsActivity extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;
    Button save;
    EditText settings_distance, settings_Rotation, settings_tilting, settings_um_1, settings_um_2, settings_um_3;
    EditText f1,f2,f3,f4,f5,f6,f7,f8;
    EditText v1,v2,v3,v4,v5,v6,v7,v8,v9,v10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mDatabaseHelper = new DatabaseHelper(getApplicationContext());
        save = findViewById(R.id.settings_save);

        settings_distance = findViewById(R.id.settings_distance);
        settings_um_1 = findViewById(R.id.settings_um_1);
        settings_Rotation = findViewById(R.id.settings_Rotation);
        settings_um_2 = findViewById(R.id.settings_um_2);
        settings_tilting = findViewById(R.id.settings_tilting);
        settings_um_3 = findViewById(R.id.settings_um_3);

        f1 = findViewById(R.id.settings_f1);
        f2 = findViewById(R.id.settings_f2);
        f3 = findViewById(R.id.settings_f3);
        f4 = findViewById(R.id.settings_f4);
        f5 = findViewById(R.id.settings_f5);
        f6 = findViewById(R.id.settings_f6);
        f7 = findViewById(R.id.settings_f7);
        f8 = findViewById(R.id.settings_f8);

        v1 = findViewById(R.id.settings_slide_v1);
        v2 = findViewById(R.id.settings_slide_v2);
        v3 = findViewById(R.id.settings_slide_v3);
        v4 = findViewById(R.id.settings_slide_v4);
        v5 = findViewById(R.id.settings_slide_v5);
        v6 = findViewById(R.id.settings_slide_v6);
        v7 = findViewById(R.id.settings_slide_v7);
        v8 = findViewById(R.id.settings_slide_v8);
        v9 = findViewById(R.id.settings_slide_v9);
        v10 = findViewById(R.id.settings_slide_v10);



        hideSoftKeyboard();

        populateFields();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabaseHelper.updateSettings(new Setting(
                        settings_distance.getText().toString(), settings_um_1.getText().toString(), settings_Rotation.getText().toString(),
                        settings_um_2.getText().toString(), settings_tilting.getText().toString(), settings_um_3.getText().toString(),
                        f1.getText().toString(), v1.getText().toString(), f2.getText().toString(), v2.getText().toString(), f3.getText().toString(), v3.getText().toString(),
                        f4.getText().toString(), v4.getText().toString(), f5.getText().toString(), v5.getText().toString(), f6.getText().toString(), v6.getText().toString(),
                        f7.getText().toString(), v7.getText().toString(), f8.getText().toString(), v8.getText().toString(), v9.getText().toString(), v10.getText().toString()

                ));

                Toast.makeText(SettingsActivity.this, "Updated.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SettingsActivity.this, ManualActivity.class);
                startActivity(intent);

            }
        });
    }

    private void populateFields() {

        Setting setting = mDatabaseHelper.getSettings();

        settings_distance.setText(setting.getDistance()  );
        settings_um_1.setText(setting.getDistance_um()  );
        settings_Rotation .setText(setting.getRotation()  );
        settings_um_2 .setText(setting.getRotation_um()  );
        settings_tilting .setText(setting.getTilting()  );
        settings_um_3 .setText(setting.getTilting_um()  );

        f1 .setText(setting.getSlide_left());
        f2 .setText(setting.getSlide_right());
        f3 .setText(setting.getTilt_down());
        f4 .setText(setting.getTilt_up());
        f5 .setText(setting.getRotate_CCW());
        f6 .setText(setting.getRotate_CW());
        f7 .setText(setting.getHome());
        f8 .setText(setting.getEnd());

        v1 .setText(setting.getSlide_left_value() );
        v2 .setText(setting.getSlide_right_value()  );
        v3 .setText(setting.getTilt_down_value()  );
        v4 .setText(setting.getTilt_up_value() );
        v5 .setText(setting.getRotate_CCW_value()  );
        v6 .setText(setting.getRotate_CW_value()  );
        v7 .setText(setting.getHome_value()  );
        v8 .setText(setting.getEnd_value()  );
        v9 .setText(setting.getTop_value()  );
        v10.setText(setting.getJoystick_frequency_value()  );

    }

    private void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }


}
