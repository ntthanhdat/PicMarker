package com.nttd.picmarker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nttd.picmarker.decode.DecodeTask;

public class DecodeActivity extends AppCompatActivity {
    Button ok, cancel;
    Switch switchAES;
    EditText password;
    TextView decodestatus;
    MyApplication myApplication=new MyApplication();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode);
        switchAES=findViewById(R.id.switchAESDecode);
        password=findViewById(R.id.editTextPasswordDecode);
        ok= findViewById(R.id.button_decode);
        cancel= findViewById(R.id.button_cancel_decode);
        decodestatus=  findViewById(R.id.decode_status);
        password.setVisibility(View.GONE);


        switchAES.setChecked(false);
        password.setVisibility(View.GONE);
        switchAES.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        password.setVisibility(switchAES.isChecked() ? View.VISIBLE : View.GONE);
                    }
                });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //gui intent tra ve
//                Intent i =new Intent();
//                i.putExtra("Messenger", messenger.getText().toString());
//                if(switchAES.isChecked()){
//                    i.putExtra("Password", password.getText().toString());
//                }
//                i.putExtra("AES", switchAES.isChecked());
//                i.putExtra("ELSB", switchELSM.isChecked());
//                setResult(Activity.RESULT_OK,i);
//                finish();

                System.out.println("decode...");
                DecodeTask decodeTask=new DecodeTask();
                decodeTask.SteganographyDecodeProcess();
                if(myApplication.getDecoded()){
                    decodestatus.setText(myApplication.getMessenger());

                }else{
                    decodestatus.setText( "Not decoded");
                }


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
