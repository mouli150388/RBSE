package com.tutorix.tutorialspoint.models;

import android.os.Parcel;
import android.os.Parcelable;

public class QUizResultModel implements Parcelable {
    public String userid;
    public String currentTime;
    public String quiz_id;
    public int score;
    public int selectedAns;
    public int wrongAnswers;
    public int Minute;
    public int Sec;

    public QUizResultModel()
    {

    }
    protected QUizResultModel(Parcel in) {
        userid = in.readString();
        currentTime = in.readString();
        quiz_id = in.readString();
        score = in.readInt();
        selectedAns = in.readInt();
        wrongAnswers = in.readInt();
        Minute = in.readInt();
        Sec = in.readInt();
    }

    public static final Creator<QUizResultModel> CREATOR = new Creator<QUizResultModel>() {
        @Override
        public QUizResultModel createFromParcel(Parcel in) {
            return new QUizResultModel(in);
        }

        @Override
        public QUizResultModel[] newArray(int size) {
            return new QUizResultModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userid);
        dest.writeString(currentTime);
        dest.writeString(quiz_id);
        dest.writeInt(score);
        dest.writeInt(selectedAns);
        dest.writeInt(wrongAnswers);
        dest.writeInt(Minute);
        dest.writeInt(Sec);
    }
}
