package com.samsung.ssc.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by d.ashish on 15-01-2016.
 */
public class LMSLeaveTypeModal implements Parcelable {

    public int LMSLeaveTypeMasterID;
    public String LeaveType;
    public String LeaveTypeCode;
    public int MaxLimit;
    public int ConsecutiveLeaves;
    public int LeavesTaken;
    public int LeaveBalance;


    public LMSLeaveTypeModal() {

    }


    public LMSLeaveTypeModal(Parcel in) {
        this.LMSLeaveTypeMasterID = in.readInt();
        this.LeaveType = in.readString();
        this.LeaveTypeCode = in.readString();
        this.MaxLimit = in.readInt();
        this.ConsecutiveLeaves = in.readInt();
        this.LeavesTaken = in.readInt();
        this.LeaveBalance = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.LMSLeaveTypeMasterID);
        parcel.writeString(this.LeaveType);
        parcel.writeString(this.LeaveTypeCode);
        parcel.writeInt(this.MaxLimit);
        parcel.writeInt(this.ConsecutiveLeaves);
        parcel.writeInt(this.LeavesTaken);
        parcel.writeInt(this.LeaveBalance);
    }


    public static final Parcelable.Creator<LMSLeaveTypeModal> CREATOR = new Creator<LMSLeaveTypeModal>() {

        @Override
        public LMSLeaveTypeModal[] newArray(int size) {
            return new LMSLeaveTypeModal[size];
        }

        @Override
        public LMSLeaveTypeModal createFromParcel(Parcel source) {
            return new LMSLeaveTypeModal(source);
        }
    };

}
