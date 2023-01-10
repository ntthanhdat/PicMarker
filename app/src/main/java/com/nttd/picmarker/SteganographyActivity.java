package com.nttd.picmarker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    Switch switchAES, switchELSM;
    MyApplication myApplication= new MyApplication();
    int limit_char=myApplication.getNumberOfByte();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stegano);
        switchAES = findViewById(R.id.switchAES);
        switchELSM = findViewById(R.id.switchELSM);
        messenger =  findViewById(R.id.editTextMessenger);
        password = findViewById(R.id.editTextPassword);
        submitbttn= findViewById(R.id.button_save);
        cancelbttn= findViewById(R.id.button_cancel);
        textstatus=  findViewById(R.id.text_status);
        switchAES.setChecked(false);
        password.setVisibility(View.GONE);
        switchELSM.setVisibility(View.GONE);
        if(switchELSM.isChecked()){
            textstatus.setText("Character limit: "+ (limit_char / 8 - 32));
        }else{
            textstatus.setText("Character limit: "+ (limit_char * 4 / 3 / 8 - 32));
        }
        switchAES.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        password.setVisibility(switchAES.isChecked() ? View.VISIBLE : View.GONE);
                    }
                });
        messenger.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                submitbttn.setEnabled(messenger.getText().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        submitbttn.setEnabled(messenger.getText().length() > 0);
        submitbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //gui intent tra ve
                Intent i =new Intent();
                i.putExtra("Messenger", messenger.getText().toString());
                if(switchAES.isChecked()){
                    i.putExtra("Password", password.getText().toString());
                }
                i.putExtra("AES", switchAES.isChecked());
                i.putExtra("ELSB", switchELSM.isChecked());
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
    @Override
    public void onSaveInstanceState(Bundle b) {
        super.onSaveInstanceState(b);
        b.putString("Messenger", messenger.getText().toString());
        b.putString("Password", password.getText().toString());
    }
}