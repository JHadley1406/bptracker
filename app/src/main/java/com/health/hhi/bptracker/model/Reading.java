package com.health.hhi.bptracker.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Josiah Hadley on 12/11/2015.
 */
public class Reading implements Parcelable {

    public int getSystolic() {
        return systolic;
    }

    public void setSystolic(int systolic) {
        this.systolic = systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(int diastolic) {
        this.diastolic = diastolic;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    private int systolic;
    private int diastolic;
    private int pulse;
    private long date;

    public Reading(int systolic, int diastolic, int pulse, long date){
        this.setSystolic(systolic);
        this.setDiastolic(diastolic);
        this.setPulse(pulse);
        this.setDate(date);
    }

    public Reading(Parcel in){
        this.setSystolic(in.readInt());
        this.setDiastolic(in.readInt());
        this.setPulse(in.readInt());
        this.setDate(in.readLong());
    }

    @Override
    public int describeContents(){ return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(this.getSystolic());
        dest.writeInt(this.getDiastolic());
        dest.writeInt(this.getPulse());
        dest.writeLong(this.getDate());
    }

    public static final Parcelable.Creator<Reading> CREATOR = new Parcelable.Creator<Reading>(){
        public Reading createFromParcel(Parcel in){ return new Reading(in);}

        public Reading[] newArray(int size){ return new Reading[size]; }
    };
}
