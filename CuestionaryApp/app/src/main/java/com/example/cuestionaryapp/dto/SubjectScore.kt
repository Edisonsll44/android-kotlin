package com.example.cuestionaryapp.dto;

import android.os.Parcel
import android.os.Parcelable

data class SubjectScore(
    val subjectName: String,
    val score: Int,
    val idItem: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(subjectName)
        parcel.writeInt(score)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubjectScore> {
        override fun createFromParcel(parcel: Parcel): SubjectScore {
            return SubjectScore(parcel)
        }

        override fun newArray(size: Int): Array<SubjectScore?> {
            return arrayOfNulls(size)
        }
    }
}

object ScoresManager {
    var scoresList: ArrayList<SubjectScore> = arrayListOf()
}
