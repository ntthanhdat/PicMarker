package com.nttd.picmarker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nttd.picmarker.fragment.SecFragment;

public class SteganographyActivity extends AppCompatActivity {


    EditText messenger, password;
    Button submitbttn, cancelbttn;
    TextView textstatus;
    Switch switchAES;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stegano);
        switchAES = findViewById(R.id.switchAES);
        messenger =  findViewById(R.id.editTextMessenger);
        password = findViewById(R.id.editTextPassword);
        submitbttn= findViewById(R.id.button_save);
        cancelbttn= findViewById(R.id.button_cancel);
        textstatus=  findViewById(R.id.text_status);

        Intent intent1 = getIntent();
        Bundle data = intent1.getExtras();


        switchAES.setChecked(false);
        password.setVisibility(View.GONE);
        textstatus.setText( "Maximum character is "+ data.getString("Limit"));

        switchAES.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        password.setVisibility(switchAES.isChecked() ? View.VISIBLE : View.GONE);
                    }
                });

        submitbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //gui intent tra ve
                Intent i =new Intent();
                i.putExtra("Messenger", messenger.getText().toString());
                if(switchAES.isChecked()){
                    i.putExtra("Password", password.getText().toString());
                }
                setResult(Activity.RESULT_OK,i);
                finish();
            }
        });
        cancelbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });



    }
    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

}