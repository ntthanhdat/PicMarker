package com.nttd.picmarker;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.nttd.picmarker.encode.EncodeTask;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {


    private static PaintView paintView;


    public static final int PICK_IMAGE = 102;
    public static final int RESULT_MESSENGER = 122;

    private Bitmap nBitmap;
    private String strFileName;
    //slight foot
    private ViewGroup mBrushPanel;
    private ViewGroup mBrushColors;

    private ViewGroup mBrushTypePanel;
    private ViewGroup mBrushType;
MyApplication myApp =(MyApplication) this.getApplication();

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
            Color.rgb(255,   255, 255), //  white
            Color.rgb( 51, 204, 255), // NAVY BLUE
            Color.rgb(102, 255, 204), // BRIGHT GREEN
    };


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///tao bang mau

        mBrushPanel = findViewById(R.id.brush_panel);
        mBrushColors = findViewById(R.id.brush_colors);
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

        //tao brush type
        mBrushTypePanel =findViewById(R.id.brush_choose_panel);
        mBrushType = findViewById(R.id.brush_type);
        mBrushTypePanel.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
        {
            @Override
            public boolean onPreDraw()
            {
                mBrushTypePanel.getViewTreeObserver().removeOnPreDrawListener(this);
                mBrushTypePanel.setTranslationY(isLandscape() ?
                        -mBrushTypePanel.getHeight() : mBrushTypePanel.getHeight());
                return false;
            }
        });

        createBrushTypePanelContent();
        //het brush type

        ///tao seek bar
        SeekBar seekbar = findViewById(R.id.seek_bar);
        seekbar.setMax(200);
        seekbar.setMin(20);
        seekbar.setProgress(10);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                paintView.BRUSH_SIZE = i;
                paintView.setStrokeWidth();
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
        //khoi phuc net ve tu sau khi chuyen activity
        if(myApp.getPathIndex()!=0){
            paintView.paths=myApp.getPaths();
            paintView.deletedPaths=myApp.getDel_paths();
            paintView.setPathIndex(myApp.getPathIndex());
            paintView.setDelPathIndex(myApp.getDelpathIndex());
            paintView.drawPath();
            //paintView.mBitmap=myApp.getBitmap();
            setBitmapPaintview(myApp.getBitmap());
           // System.out.println("da dc luu");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.active_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
//menu chinh
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
            case R.id.action_brush_type:
                if(mBrushTypePanel.getTranslationY() == 0){
                    hideBrushTypePanel();
                }
                else{
                    showBrushTypePanel();
                }
                break;
            case R.id.action_undo:
                paintView.imageReverse();
                if(nBitmap != null) {
                    float centreX = (paintView.mCanvas.getWidth()-nBitmap.getWidth()) / 2;
                    float centreY = (paintView.mCanvas.getHeight()-nBitmap.getHeight()) / 2;
                    Bitmap.Config config;
                    config = nBitmap.getConfig();
                    paintView.mCanvas.drawBitmap(nBitmap, centreX, centreY, null);
                }
                break;
            case R.id.action_redo:
                paintView.redo();
                break;

                //open
            case R.id.photo:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
                return true;

            case R.id.save:

               //lay thong tin ma hoa
                //doc du lieu ma hoa

                if(myApp.getEncoded()){
                    String messenger1 = myApp.getMessenger();
                    String password = myApp.getPassword();
                    Boolean AES= myApp.getAES();
                    Boolean ELSB= myApp.getELSB();
                    System.out.println("messenger da luu: "+messenger1+" ");
                    EncodeTask encodeTask = new EncodeTask(
                            paintView.getFilepath(),
                            messenger1,
                            password,
                            AES,
                            ELSB,
                            paintView.mBitmap);

                    encodeTask.SteganographyProcess();
                    FileUtils.scanFile(this, FileUtils.uriToFilePath(this , EncodeTask.getResultUri()));
                    System.out.println("encode thanh cong ");
                    Toast.makeText(this, "Encoded! Saved!", Toast.LENGTH_SHORT).show();
                }else{
                   paintView.save();
                    Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                }


                return true;

            case R.id.properties:
                startActivity(new Intent(MainActivity.this, PropertiesActivity.class));
                break;
            case R.id.help:
                startActivity(new Intent(MainActivity.this, HelpActivity.class));
                break;


            case R.id.exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                   builder.setMessage("Do you want to save picture?")
                        .setTitle("Are you sure you want to exit?")
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    MainActivity.this.finish();
                    System.exit(0);
                }
            })
                    .setNegativeButton("no", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                            System.exit(0);
                        }
                    })
                .setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"cancel",Toast.LENGTH_SHORT).show();
                    }
                });
             AlertDialog dialog = builder.create();


                dialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        myApp.setPaths(paintView.paths);
        myApp.setDel_paths(paintView.deletedPaths);
        myApp.setPathIndex(paintView.getPathIndex());
        myApp.setDelpathIndex(paintView.getDelPathIndex());

        myApp.setBitmap(paintView.mBitmap);
        //System.out.println("luu");
    }

//nhan ket qua mo hinh anh
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

                    myApp.setBitmapgoc(tempBitmap);
                    Bitmap.Config config;
                    if(tempBitmap.getConfig() != null){
                        config = tempBitmap.getConfig();
                    }else{
                        config = Bitmap.Config.ARGB_8888;
                    }
                    paintView.clear();
                    nBitmap = Bitmap.createScaledBitmap(tempBitmap,paintView.mCanvas.getWidth(),paintView.mCanvas.getHeight(),false);

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
    private void setBitmapPaintview(Bitmap tempBitmap){
        paintView.clear();
        nBitmap = Bitmap.createScaledBitmap(tempBitmap,paintView.mCanvas.getWidth(),paintView.mCanvas.getHeight(),false);

        float centreX = (paintView.mCanvas.getWidth()-nBitmap.getWidth()) / 2;
        float centreY = (paintView.mCanvas.getHeight()-nBitmap.getHeight()) / 2;

        paintView.init2(nBitmap.getWidth(),nBitmap.getHeight());
        paintView.mCanvas.drawBitmap(nBitmap, centreX,centreY, null);
        paintView.mBitmap.setConfig(nBitmap.getConfig());
        paintView.pen();
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
            //hideBrushPanel();
        }
    };

    private void showBrushPanel()
    {
        if(mBrushTypePanel.getTranslationY() == 0){
            hideBrushTypePanel();
        }
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

    //chuc nang thay doi loai but
    private void createBrushTypePanelContent()
    {

        TableRow tableRow = null;
        tableRow = new TableRow(this);

        mBrushType.addView(tableRow, new TableLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        tableRow.addView(createToolTypeButton(tableRow, R.drawable.ic_action_brush, 1));
        tableRow.addView(createToolTypeButton(tableRow, R.drawable.ic_blur, 2));
        tableRow.addView(createToolTypeButton(tableRow, R.drawable.ic_emboss, 3));


    }

    private ImageButton createToolTypeButton(ViewGroup parent, int drawableResId, int index)
    {
        ImageButton button = (ImageButton)getLayoutInflater().inflate(R.layout.button_brush_spot, parent, false);
        button.setImageResource(drawableResId);
        button.setOnClickListener(mButtonClick1);
        if(index != -1){
            button.setTag(Integer.valueOf(index));
        }
        return button;
    }

    private View.OnClickListener mButtonClick1 = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            //tahy doi hinh dang cua icon tren menu
            switch(((Integer)v.getTag()).intValue()){
                case 1:
                    paintView.normal();
                    break;
                case 2:
                    paintView.blur();
                    break;
                case 3:
                    paintView.emboss();
                    break;
                default:
                    paintView.normal();
                    break;

            }
            hideBrushTypePanel();
        }
    };

    private void showBrushTypePanel()
    {
        if(mBrushPanel.getTranslationY() == 0){
            hideBrushPanel();
        }
        mBrushTypePanel.animate()
                .translationY(0)
                .start();
    }

    private void hideBrushTypePanel()
    {
        mBrushTypePanel.animate()
                .translationY(isLandscape() ?
                        -mBrushTypePanel.getHeight() : mBrushTypePanel.getHeight())
                .start();
    }
    //het chuc nang thay doi loai but
}