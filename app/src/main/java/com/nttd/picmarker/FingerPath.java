package com.nttd.picmarker;

import android.graphics.Path;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class FingerPath extends Path implements Parcelable, Serializable {

    private int color;
    private int strokeWidth;
    private Path path;
    public boolean emboss;
    public boolean blur;


    public FingerPath(int color, boolean emboss, boolean blur, int strokeWidth, Path path) {
        this.color = color;
        this.emboss = emboss;
        this.blur = blur;
        this.strokeWidth = strokeWidth;
        this.path = path;
    }
    public FingerPath(int color, int strokeWidth, Path path) {
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.path = path;
    }

    protected FingerPath(Parcel in) {
        color = in.readInt();
        strokeWidth = in.readInt();
        emboss = in.readByte() != 0;
        blur = in.readByte() != 0;
    }

    public static final Creator<FingerPath> CREATOR = new Creator<FingerPath>() {
        @Override
        public FingerPath createFromParcel(Parcel in) {
            return new FingerPath(in);
        }

        @Override
        public FingerPath[] newArray(int size) {
            return new FingerPath[size];
        }
    };

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public boolean isEmboss() {
        return emboss;
    }

    public void setEmboss(boolean emboss) {
        this.emboss = emboss;
    }

    public boolean isBlur() {
        return blur;
    }

    public void setBlur(boolean blur) {
        this.blur = blur;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(color);
        parcel.writeInt(strokeWidth);
        parcel.writeByte((byte) (emboss ? 1 : 0));
        parcel.writeByte((byte) (blur ? 1 : 0));
    }
}
