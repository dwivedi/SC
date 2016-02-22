package com.samsung.ssc.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by d.ashish on 13-01-2016.
 */
public class LMSLeaveDataModal implements Parcelable {

    public int LeaveMasterID;
    public String LeaveSubject;
    public long AppliedDate;
    public int CreatedBy;
    public String CreatedByUserName;
    public long CreatedDate;
    public int CurrentStatus;
    public int ModifiedBy;
    public String ModifiedByUserName;
    public long ModifiedDate;
    public int NumberOfLeave;
    public int PendingWith;
    public String PendingWithUserName;
    public String Remarks;
    public int LMSLeaveTypeMasterID;
    public String LeaveTypeCode;
    public String LeaveType;
    public int LeavesTaken;
     public ArrayList<LMSLeaveDateModal> leaveDateModals ;
    public ArrayList<LMSLeaveStatusLogModal> leaveStatusLogModals;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.LeaveMasterID);
        dest.writeString(this.LeaveSubject);
        dest.writeLong(this.AppliedDate);
        dest.writeInt(this.CreatedBy);
        dest.writeString(this.CreatedByUserName);
        dest.writeLong(this.CreatedDate);
        dest.writeInt(this.CurrentStatus);
        dest.writeInt(this.ModifiedBy);
        dest.writeString(this.ModifiedByUserName);
        dest.writeLong(this.ModifiedDate);
        dest.writeInt(this.NumberOfLeave);
        dest.writeInt(this.PendingWith);
        dest.writeString(this.PendingWithUserName);
        dest.writeString(this.Remarks);
        dest.writeInt(this.LMSLeaveTypeMasterID);
         dest.writeString(this.LeaveTypeCode);
        dest.writeString(this.LeaveType);
        dest.writeInt(this.LeavesTaken);
        dest.writeTypedList(leaveDateModals);
        dest.writeTypedList(leaveStatusLogModals);
    }

    public LMSLeaveDataModal() {
    }

    protected LMSLeaveDataModal(Parcel in) {
        this.LeaveMasterID = in.readInt();
        this.LeaveSubject = in.readString();
        this.AppliedDate = in.readLong();
        this.CreatedBy = in.readInt();
        this.CreatedByUserName = in.readString();
        this.CreatedDate = in.readLong();
        this.CurrentStatus = in.readInt();
        this.ModifiedBy = in.readInt();
        this.ModifiedByUserName = in.readString();
        this.ModifiedDate = in.readLong();
        this.NumberOfLeave = in.readInt();
        this.PendingWith = in.readInt();
        this.PendingWithUserName = in.readString();
        this.Remarks = in.readString();
        this.LMSLeaveTypeMasterID = in.readInt();
         this.LeaveTypeCode = in.readString();
        this.LeaveType = in.readString();
        this.LeavesTaken = in.readInt();
        this.leaveDateModals = in.createTypedArrayList(LMSLeaveDateModal.CREATOR);
        this.leaveStatusLogModals = in.createTypedArrayList(LMSLeaveStatusLogModal.CREATOR);
    }

    public static final Parcelable.Creator<LMSLeaveDataModal> CREATOR = new Parcelable.Creator<LMSLeaveDataModal>() {
        public LMSLeaveDataModal createFromParcel(Parcel source) {
            return new LMSLeaveDataModal(source);
        }

        public LMSLeaveDataModal[] newArray(int size) {
            return new LMSLeaveDataModal[size];
        }
    };
}


