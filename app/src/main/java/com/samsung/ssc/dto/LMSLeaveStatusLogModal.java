package com.samsung.ssc.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by d.ashish on 22-01-2016.
 */
public class LMSLeaveStatusLogModal implements Parcelable {

    public int LMSStatusLogID;
    public int LeaveMasterID;
    public int CreatedBy;
    public String CreatedByUserName;
    public long CreatedDate;
    public int CurrentStatus;
    public String Remarks;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.LMSStatusLogID);
        dest.writeInt(this.LeaveMasterID);
        dest.writeInt(this.CreatedBy);
        dest.writeString(this.CreatedByUserName);
        dest.writeLong(this.CreatedDate);
        dest.writeInt(this.CurrentStatus);
        dest.writeString(this.Remarks);
    }

    public LMSLeaveStatusLogModal() {
    }

    protected LMSLeaveStatusLogModal(Parcel in) {
        this.LMSStatusLogID = in.readInt();
        this.LeaveMasterID = in.readInt();
        this.CreatedBy = in.readInt();
        this.CreatedByUserName = in.readString();
        this.CreatedDate = in.readLong();
        this.CurrentStatus = in.readInt();
        this.Remarks = in.readString();
    }

    public static final Parcelable.Creator<LMSLeaveStatusLogModal> CREATOR = new Parcelable.Creator<LMSLeaveStatusLogModal>() {
        public LMSLeaveStatusLogModal createFromParcel(Parcel source) {
            return new LMSLeaveStatusLogModal(source);
        }

        public LMSLeaveStatusLogModal[] newArray(int size) {
            return new LMSLeaveStatusLogModal[size];
        }
    };
}
