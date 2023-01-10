package com.nttd.picmarker.encode;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.nttd.picmarker.AESHelper;
import com.nttd.picmarker.FileUtils;
import com.nttd.picmarker.MyApplication;
import com.nttd.picmarker.SympleCrypto;
import com.scottyab.aescrypt.AESCrypt;

import java.io.File;
import java.util.Objects;

public class EncodeTask {
    private String mFilePath;
    private String mMessenger;
    private String mPassword;
    private boolean isAES ;
    private boolean isELSB ;
    private Bitmap mBitmap;
    private static final int MAX_DECODABLE_LENGTH = 0x00FFFFFF;
    private static Uri resultUri;

    private static final int LSB = 1;

    public EncodeTask(String mFilePath, String mMessenger, String mPassword, boolean isAES, boolean isELSB, Bitmap mBitmap) {
        this.mFilePath = mFilePath;
        this.mMessenger = mMessenger;
        this.mPassword = mPassword;
        this.isAES = isAES;
        this.isELSB = isELSB;
        this.mBitmap = mBitmap;
    }

    public String getMessenger() {
        return mMessenger;
    }

    public static Uri getResultUri() {
        return resultUri;
    }

    public int getNumberOfPixels(){
        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();
        int numberOfPixels = w * h;
        return numberOfPixels;
    }
    public int getRequiredLength(){
        byte[] data = getMessenger().getBytes();

        int requiredLength = data.length * 8 + 32;
        return requiredLength;
    }
//xu ly chinh
    public void SteganographyProcess(){

        int numberOfPixels = getNumberOfPixels();
        //chon che do ma hoa AES thi se thay doi messenger
        if(isAES){

            try {
                String pass=mPassword.trim();
                if(pass.length()<16){
                    pass=pass.concat("aaaaaaaaaaaaaaaa");
                }
                char[] data=pass.toCharArray();
                pass=String.copyValueOf(data,0,16);
                System.out.println(pass);
                //AESHelper aesHelper= new AESHelper();
                //mMessenger=aesHelper.encrypt(pass, mMessenger.trim()); //mPassword.trim()
                //SympleCrypto sympleCrypto=new SympleCrypto();
                // mMessenger = sympleCrypto.encrypt(pass, mMessenger.trim());
                mMessenger= AESCrypt.encrypt(pass,mMessenger);
                System.out.println(mMessenger);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        int requiredLength = getRequiredLength();
        if ( requiredLength> numberOfPixels) {
            throw new IllegalArgumentException("Message is too long to fit into pixels.");
        }
//giau cac bit vao pixel, luu duoi dang array
        int[] encodedPixels = encode(getPixels(mBitmap, requiredLength),getMessenger());


        setPixels(mBitmap, encodedPixels);

//luu bitmap
        resultUri = FileUtils.saveBitmap(mBitmap);
        //FileUtils.scanFile(this, FileUtils.uriToFilePath(new , resultUri));
        Log.d("Steganography.Encode", "saved: "+resultUri.toString());

        //FileUtils.SaveImage(mBitmap, new File(mFilePath));

    }
    public static int[] encode(int[] pixels, String message) {
        Log.d("Steganography.Encode", "Encode Begin");
        byte[] data = message.getBytes();

        //Insert length into data
        {
            byte[] dataWithLength = new byte[4 + data.length];

            int dataLength = data.length;
            for (int i = 0; i < 4; i++) {
                dataWithLength[i] = (byte)(dataLength & 0xff);
                dataLength >>>= 8;
            }

            System.arraycopy(data, 0, dataWithLength, 4, data.length);
            data = dataWithLength;
        }
        int pixelIndex = 0;

        //For each byte of data, insert its 8 bits into the LSB of 8 pixels.
        for (byte b : data) {
            for (int i = 0; i < 8; i++, pixelIndex++) {
                //Remove LSB from pixel
                pixels[pixelIndex] &= ~LSB;
                //OR LSB of byte into pixel
                pixels[pixelIndex] |= b & LSB;
                //Bit-shift byte into next position
                b >>>= 1;
            }
        }

        Log.d("Steganography.Encode", "Encode End");

        return pixels;
    }
        public static int[] getPixels(Bitmap bitmap, int requiredLength) {
            int[] bounds = getMinimumAreaBounds(requiredLength, bitmap.getWidth());

            int[] pixels = new int[bounds[0] * bounds[1]];
            bitmap.getPixels(pixels, 0, bounds[0], 0, 0, bounds[0], bounds[1]);

            return pixels;
        }
        public static void setPixels(Bitmap bitmap, int[] pixels) {
            int[] bounds = getMinimumAreaBounds(pixels.length, bitmap.getWidth());
            bitmap.setPixels(pixels, 0, bounds[0], 0, 0, bounds[0], bounds[1]);
        }

        /**
         * Gets the area required for numberOfBytes to fit into an image width width of imageWidth.
         * @param requiredLength Number of pixels required for message to fit in image.
         * @param imageWidth Width of image.
         * @return [width, height]
         */
        private static int[] getMinimumAreaBounds(int requiredLength, int imageWidth) {
            if (requiredLength < imageWidth) {
                return new int[] {requiredLength, 1};
            } else {
                return new int[] {imageWidth, (int) Math.ceil(((double) requiredLength) / ((double) imageWidth)) };
            }
        }
}
