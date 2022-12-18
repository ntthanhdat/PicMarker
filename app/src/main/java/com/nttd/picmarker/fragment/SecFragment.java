package com.nttd.picmarker.fragment;

import android.app.Activity;
import android.content.Intent;
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

import com.nttd.picmarker.R;
import com.nttd.picmarker.SteganographyActivity;

import java.io.InputStream;

public class SecFragment extends PreferenceFragmentCompat {

    public static final int GET_MESSENGER =1;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_security, rootKey);


        Preference swich_stegano =findPreference("switch_ste");
        swich_stegano.setSummary("Has no Messenger");
        ActivityResultLauncher<Intent> SteganoActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent intent1 = result.getData();
                            Bundle data = intent1.getExtras();
                            String messenger =data.getString("messenger");
                            ///lam gi do
                            System.out.println("tra ve thanh cong: "+messenger);

                            swich_stegano.setSummary("Has Messenger");

                        }else{
                            System.out.println("tra ve khong thanh cong");
                        }
                    }
                });



        swich_stegano.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {


                Intent requestStegano = new Intent(getContext(), SteganographyActivity.class);
                requestStegano.putExtra("Limit", "100");
               //startActivityForResult(requestStegano, GET_MESSENGER);
                SteganoActivityResultLauncher.launch(requestStegano);
                return true;
            }
        });
    }
}