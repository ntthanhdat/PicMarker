package com.nttd.picmarker.decode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.nttd.picmarker.MyApplication;

import java.io.InputStream;

public class DecodeTask {

    /**
     * Max length to attempt to decode.
     */
    private static final int MAX_DECODABLE_LENGTH = 0x00FFFFFF;

    private static final int LSB = 1;
    private  MyApplication myApplication = new MyApplication();

    public  void SteganographyDecodeProcess(){ //sua thanh bitmap

        //Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
        Bitmap bitmap = myApplication.getBitmapgoc();
        String messenger = decode(getPixels(bitmap));

        myApplication.setMessenger(messenger);


    }
    public static int[] getPixels(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pixels = new int[w*h];
        bitmap.getPixels(pixels, 0, w, 0, 0, w, h);

        return pixels;
    }
    public String decode(int[] pixels) {
        Log.d("Steganography.Decode", "Decode Begin");

        int pixelIndex = 0;

        //Decode length;
        int length = decodeBitsFromPixels(pixels, 32, pixelIndex);
        pixelIndex += 32;

        if (length < 0 || length > MAX_DECODABLE_LENGTH) {
            myApplication.setDecoded(false);
            throw new IllegalArgumentException("Failed to decode. Are you sure the image is encoded?");

        }

        byte[] data = new byte[length];

        for (int byteIndex = 0; byteIndex < length; byteIndex++, pixelIndex+=8) {
            data[byteIndex] = (byte) decodeBitsFromPixels(pixels, 8, pixelIndex);
        }

        Log.d("Steganography.Decode", "Decode End");
        myApplication.setDecoded(true);
        return new String(data);
    }

    /**
     * Decodes specified number of bits out of pixels, returns them as a single integer.
     * @param pixels Pixels as (A)RGB integers: R=0xFF0000, G=0x00FF00, B=0x0000FF
     * @param numberOfBits Number of bits to decode.
     * @param pixelIndex Index to start at.
     * @return Decoded integer.
     */
    private static int decodeBitsFromPixels(int[] pixels,
                                            int numberOfBits,
                                            int pixelIndex) {

        int decodedValue = 0;

        for (int i = 0; i < numberOfBits; i++, pixelIndex++) {
            //Get bit
            int bit = pixels[pixelIndex] & LSB;
            //Shift into position
            decodedValue |= bit << i;
        }

        return decodedValue;

    }
}