package com.nttd.picmarker.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nttd.picmarker.DecodeActivity;
import com.nttd.picmarker.MainActivity;
import com.nttd.picmarker.MyApplication;
import com.nttd.picmarker.PropertiesActivity;
import com.nttd.picmarker.R;
import com.nttd.picmarker.SteganographyActivity;

import java.io.InputStream;

public class SecFragment extends PreferenceFragmentCompat {
    MyApplication myApplication=new MyApplication();
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_security, rootKey);


        Preference swich_stegano =findPreference("switch_ste");
        //summary

        if(myApplication.getEncoded()){

            Boolean AES= myApplication.getAES();
            Boolean ELSB= myApplication.getELSB();

            swich_stegano.setSummary("Encoded"+ (AES? ", AES" : "")+(ELSB? ", ELSB" : "")+ ".");

        }else{
            swich_stegano.setSummary("Has no messenger encoded.");
        }


        ActivityResultLauncher<Intent> SteganoActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent intent1 = result.getData();
                            Bundle data = intent1.getExtras();
                            String messenger = data.getString("Messenger");
                            String password = data.getString("Password");
                            boolean isAES = data.getBoolean("AES");
                            boolean isELSB  = data.getBoolean("ELSB");


                            ///luu du lieu ma hoa vao global
                            myApplication.setMessenger(messenger);
                            myApplication.setPassword(password);
                            myApplication.setAES(isAES);
                            myApplication.setELSB(isELSB);
                            myApplication.setEncoded(true);
                            swich_stegano.setSummary("Encoded"+ (isAES? ", AES" : "")+(isELSB? ", ELSM" : "")+ ".");
                            if(myApplication.getEncoded())
                            System.out.println("thanh cong: "+messenger);
                        }else{
                            myApplication.setEncoded(false);
                            System.out.println("tra ve khong thanh cong");

                        }
                    }
                });



        swich_stegano.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {


                Intent requestStegano = new Intent(getContext(), SteganographyActivity.class);
                SteganoActivityResultLauncher.launch(requestStegano);
                return true;
            }
        });

        Preference decode =findPreference("decode");
        decode.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {


                Intent requestDecode = new Intent(getContext(), DecodeActivity.class);
                startActivity(requestDecode);
                return true;
            }
        });
    }


}