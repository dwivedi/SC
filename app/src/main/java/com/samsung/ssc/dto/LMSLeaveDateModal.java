package com.samsung.ssc.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by d.ashish on 13-01-2016.
 */
public class  LMSLeaveDateModal implements Parcelable {

    public int LMSLeaveDetailID;
    public int LeaveMasterID;
    public int CurrentStatus;
    public boolean IsHalfDay;
    public long LeaveDate;
    public String LeaveDateText;
    public int CreatedBy;
    public int ModifiedBy;
    public String ModifiedByUserName;
    public long ModifiedDate;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.LMSLeaveDetailID);
        dest.writeInt(this.LeaveMasterID);
        dest.writeInt(this.CurrentStatus);
        dest.writeByte(IsHalfDay ? (byte) 1 : (byte) 0);
        dest.writeLong(this.LeaveDate);
        dest.writeString(this.LeaveDateText);
        dest.writeInt(this.CreatedBy);
        dest.writeInt(this.ModifiedBy);
        dest.writeString(this.ModifiedByUserName);
        dest.writeLong(this.ModifiedDate);
    }

    public LMSLeaveDateModal() {
    }

    protected LMSLeaveDateModal(Parcel in) {
        this.LMSLeaveDetailID = in.readInt();
        this.LeaveMasterID = in.readInt();
        this.CurrentStatus = in.readInt();
        this.IsHalfDay = in.readByte() != 0;
        this.LeaveDate = in.readLong();
        this.LeaveDateText = in.readString();
        this.CreatedBy = in.readInt();
        this.ModifiedBy = in.readInt();
        this.ModifiedByUserName = in.readString();
        this.ModifiedDate = in.readLong();
    }

    public static final Parcelable.Creator<LMSLeaveDateModal> CREATOR = new Parcelable.Creator<LMSLeaveDateModal>() {
        public LMSLeaveDateModal createFromParcel(Parcel source) {
            return new LMSLeaveDateModal(source);
        }

        public LMSLeaveDateModal[] newArray(int size) {
            return new LMSLeaveDateModal[size];
        }
    };
}
