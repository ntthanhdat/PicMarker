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
import com.scottyab.aescrypt.AESCrypt;

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
                System.out.println("decode...");

                    try {

                        DecodeTask decodeTask = new DecodeTask();
                        decodeTask.SteganographyDecodeProcess();
                        if (myApplication.getDecoded()) {
                            if (switchAES.isChecked()) {
                                try {
                                    String msg;
                                    //AESHelper aesHelper = new AESHelper();
                                    String pass=password.getText().toString().trim();
                                    if(pass.length()<16){
                                        pass=pass.concat("aaaaaaaaaaaaaaaa");
                                    }
                                    char[] data=pass.toCharArray();
                                    pass=String.copyValueOf(data,0,16);
                                    System.out.println(pass);
                                    //msg = aesHelper.decrypt(pass, myApplication.getMessenger()); //password.getText().toString().trim()
//                                    SympleCrypto sympleCrypto=new SympleCrypto();
//                                    msg = sympleCrypto.decrypt(pass, myApplication.getMessenger());
                                    msg= AESCrypt.decrypt(pass,myApplication.getMessenger());
                                    decodestatus.setText(msg);
                                } catch (Exception e) {
                                    decodestatus.setText("Can't decoded");
                                    e.printStackTrace();
                                }

                            } else {
                                decodestatus.setText(myApplication.getMessenger());
                                System.out.println(myApplication.getMessenger());
                            }


                        }
                    }catch(Exception e1){
                        e1.printStackTrace();
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
