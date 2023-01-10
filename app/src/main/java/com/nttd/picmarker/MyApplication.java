package com.nttd.picmarker;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;

import com.nttd.picmarker.encode.EncodeTask;

import java.io.File;
import java.util.ArrayList;

public class MyApplication extends Application {

    private static ArrayList<FingerPath> paths;
    private static ArrayList<FingerPath> del_paths;
    private static int pathIndex;
    private static int delpathIndex;
    private static Bitmap bitmap;
    private static Bitmap bitmapgoc;

    private static File file;
    private static String messenger;
    private static String password ;
    private static Boolean AES;
    private static Boolean ELSB;
    private static Boolean Encoded=false;
    private static Boolean Decoded=false;
    private static Uri uri;
  //  private static int BitLenght;

    public MyApplication() {
        //System.out.println("gia tri da luu: "+pathIndex);

    }
    public static Boolean getELSB() {
        return ELSB;
    }

    public static void setELSB(Boolean ELSB) {
        MyApplication.ELSB = ELSB;
    }

    public static String getMessenger() {
        return messenger;
    }

    public static void setMessenger(String messenger) {
        MyApplication.messenger = messenger;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        MyApplication.password = password;
    }

    public static Boolean getAES() {
        return AES;
    }

    public static void setAES(Boolean AES) {
        MyApplication.AES = AES;
    }


    public static Boolean getEncoded() {
        return Encoded;
    }

    public static void setEncoded(Boolean encoded) {
        Encoded = encoded;
    }

    public int getNumberOfByte(){
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int numberOfPixels = w * h;
        return numberOfPixels;
    }


    public static void setPaths(ArrayList<FingerPath> paths) {
        MyApplication.paths = paths;
    }
    public static ArrayList<FingerPath> getPaths() {
        return paths;
    }
    public static ArrayList<FingerPath> getDel_paths() {
        return del_paths;
    }

    public static void setDel_paths(ArrayList<FingerPath> del_paths) {
        MyApplication.del_paths = del_paths;
    }

    public static int getPathIndex() {
        return pathIndex;
    }

    public static void setPathIndex(int pathIndex) {
        MyApplication.pathIndex = pathIndex;
    }

    public static int getDelpathIndex() {
        return delpathIndex;
    }

    public static void setDelpathIndex(int delpathIndex) {
        MyApplication.delpathIndex = delpathIndex;
    }

    public static File getFile() {
        return file;
    }

    public static void setFile(File file) {
        MyApplication.file = file;
    }

    public static Bitmap getBitmap() {
        return bitmap;
    }

    public static void setBitmap(Bitmap bitmap) {
        MyApplication.bitmap = bitmap;
    }

    public static Uri getUri() {
        return uri;
    }

    public static void setUri(Uri uri) {
        MyApplication.uri = uri;
    }

    public static Boolean getDecoded() {
        return Decoded;
    }

    public static void setDecoded(Boolean decoded) {
        Decoded = decoded;
    }

    public static Bitmap getBitmapgoc() {
        return bitmapgoc;
    }

    public static void setBitmapgoc(Bitmap bitmapgoc) {
        MyApplication.bitmapgoc = bitmapgoc;
    }
}
