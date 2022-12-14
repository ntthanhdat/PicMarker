package com.nttd.picmarker;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

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
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.nttd.picmarker.R;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private PaintView paintView;
    public static final int PICK_IMAGE = 102;
    private Bitmap nBitmap;
    private String strFileName;
    //slight foot
    private ViewGroup mBrushPanel;
    private ViewGroup   mBrushColors;
    private SeekBar     mBrushStroke;


    static int[] COLORS = {
            Color.rgb(255,  51, 255), // DARK PINK
            Color.rgb(255, 230, 102), // LIGHT YELLOW
            Color.rgb(148,  66,  50), // DARK MAROON
            Color.rgb(186, 123,  68), // LIGHT MAROON
            Color.rgb(252,  20,  20), // RED
            Color.rgb(102, 255, 255), // LIGHT BLUE

            Color.rgb( 70,  78, 202), // DARK BLUE
            Color.rgb(190, 255,  91), // LIGHT GREEN
            Color.rgb( 15, 230,   0), // DARK GREEN
            Color.rgb(123,   0, 230), // JAMBLI
            Color.rgb(255, 187,  50), // ORANGE
            Color.rgb(  7,   5,   0), // BLACK

            Color.rgb(129, 128, 127), // GRAY
            Color.rgb(255,   4, 139), // PINK RED
            Color.rgb( 51, 204, 255), // NAVY BLUE
            Color.rgb(102, 255, 204), // BRIGHT GREEN
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///tao bang mau

        mBrushPanel = (ViewGroup)findViewById(R.id.brush_panel);
        mBrushColors = (ViewGroup)findViewById(R.id.brush_colors);
        mBrushPanel.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
        {
            @Override
            public boolean onPreDraw()
            {
                mBrushPanel.getViewTreeObserver().removeOnPreDrawListener(this);
                mBrushPanel.setTranslationY(isLandscape() ?
                        -mBrushPanel.getHeight() : mBrushPanel.getHeight());
                return false;
            }
        });

        createBrushPanelContent();
        //het tao bang mau

        ///tao seek bar
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
        ///het tao seek bar
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
            case R.id.action_brush:
                if(mBrushPanel.getTranslationY() == 0){
                    hideBrushPanel();
                }
                else{
                    showBrushPanel();
                }
                break;
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
//chuc nang tao bang mau
    private void createBrushPanelContent()
    {
        TableRow tableRow = null;
        final int rowLimit = isLandscape() ? 16 : 8;
        for(int i = 0; i < COLORS.length; i++){
            if((i % rowLimit) == 0){
                tableRow = new TableRow(this);
                mBrushColors.addView(tableRow, new TableLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            }
            tableRow.addView(createToolButton(tableRow, R.drawable.ic_paint_splot, i));
        }
    }

    private ImageButton createToolButton(ViewGroup parent, int drawableResId, int index)
    {
        ImageButton button = (ImageButton)getLayoutInflater().inflate(R.layout.button_paint_spot, parent, false);
        button.setImageResource(drawableResId);
        button.setOnClickListener(mButtonClick);
        if(index != -1){
            button.setTag(Integer.valueOf(index));
            button.setColorFilter(COLORS[index]);
        }
        return button;
    }

    private View.OnClickListener mButtonClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            paintView.COLOR_PEN = COLORS[((Integer)v.getTag()).intValue()];
            paintView.pen();
            //mDrawingView.setDrawingColor(COLORS[((Integer)v.getTag()).intValue()]);
            hideBrushPanel();
        }
    };

    private void showBrushPanel()
    {
        mBrushPanel.animate()
                .translationY(0)
                .start();
    }

    private void hideBrushPanel()
    {
        mBrushPanel.animate()
                .translationY(isLandscape() ?
                        -mBrushPanel.getHeight() : mBrushPanel.getHeight())
                .start();
    }
    private boolean isLandscape()
    {
        return false;
    }
//het chuc nang tao bang mau
}