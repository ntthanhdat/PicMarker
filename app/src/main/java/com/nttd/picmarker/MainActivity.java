package com.nttd.picmarker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.nttd.picmarker.R;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private PaintView paintView;
    public static final int PICK_IMAGE = 102;
    private Bitmap nBitmap;
    private String strFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SeekBar seekbar = findViewById(R.id.seek_bar);
        seekbar.setMax(200);
        seekbar.setProgress(10);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                paintView.BRUSH_SIZE = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        paintView = findViewById(R.id.paint_view);
        paintView.COLOR_PEN = Color.BLACK;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.active_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId()){
            case R.id.properties:
                startActivity(new Intent(MainActivity.this, PropertiesActivity.class));
                break;
                ///cac case khac
            case R.id.photo:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
                return true;

            case R.id.save:
                paintView.save();
                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_undo:
                paintView.imageReverse();
                if(nBitmap != null) {
                    float centreX = (paintView.mCanvas.getWidth()-nBitmap.getWidth()) / 2;
                    float centreY = (paintView.mCanvas.getHeight()-nBitmap.getHeight()) / 2;
                    Bitmap.Config config;
                    config = nBitmap.getConfig();
                    //paintView.mCanvas.drawColor(-1);
                    paintView.mCanvas.drawBitmap(nBitmap, centreX, centreY, null);
                }
                //Toast.makeText(this, "Reversed!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_redo:
                paintView.redo();
                return true;

            case R.id.exit:
                // on below line we are finishing activity.
                MainActivity.this.finish();

                // on below line we are exiting our activity
                System.exit(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            try{
                Uri url_value = data.getData();

                String dir = url_value.toString();
                strFileName = dir.substring(dir.lastIndexOf("/") + 1);

                Bitmap tempBitmap;
                try {
                    InputStream img = getContentResolver().openInputStream(url_value);
                    tempBitmap = BitmapFactory.decodeStream(img);
                    Bitmap.Config config;
                    if(tempBitmap.getConfig() != null){
                        config = tempBitmap.getConfig();
                    }else{
                        config = Bitmap.Config.ARGB_8888;
                    }
                    paintView.clear();
                    nBitmap = Bitmap.createScaledBitmap(tempBitmap,paintView.mCanvas.getWidth(),paintView.mCanvas.getHeight(),false);

                    float h = paintView.mCanvas.getHeight();
                    float w = paintView.mCanvas.getWidth();
                    float centreX = (paintView.mCanvas.getWidth()-nBitmap.getWidth()) / 2;
                    float centreY = (paintView.mCanvas.getHeight()-nBitmap.getHeight()) / 2;

                    paintView.init2(nBitmap.getWidth(),nBitmap.getHeight());
                    paintView.mCanvas.drawBitmap(nBitmap, centreX,centreY, null);
                    paintView.mBitmap.setConfig(nBitmap.getConfig());
                    paintView.pen();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{

        }
    }
}