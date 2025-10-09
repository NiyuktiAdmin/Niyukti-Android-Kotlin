package com.example.niyuktikotlin.models

import android.os.Parcel
import android.os.Parcelable

data class CourseModel(
    val id: String,
    val title: String,
    val description: String,
    val actualPrice: Int,
    val discountedPrice: Int,
    val isActive: Boolean,
    val discountPercent: Int,
    val imageResId: Int,
    val category: String,
    val tags: List<String> = emptyList(),
    val syllabusIds: List<String> = emptyList(),
    val testsIds: List<String> = emptyList(),
    val plansIds: List<String> = emptyList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readString() ?: "",
        title = parcel.readString() ?: "",
        description = parcel.readString() ?: "",
        actualPrice = parcel.readInt(),
        discountedPrice = parcel.readInt(),
        isActive = parcel.readByte() != 0.toByte(),
        discountPercent = parcel.readInt(),
        imageResId = parcel.readInt(),
        category = parcel.readString() ?: "",
        tags = parcel.createStringArrayList() ?: emptyList(),
        syllabusIds = parcel.createStringArrayList() ?: emptyList(),
        testsIds = parcel.createStringArrayList() ?: emptyList(),
        plansIds = parcel.createStringArrayList() ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeInt(actualPrice)
        parcel.writeInt(discountedPrice)
        parcel.writeByte(if (isActive) 1 else 0)
        parcel.writeInt(discountPercent)
        parcel.writeInt(imageResId)
        parcel.writeString(category)
        parcel.writeStringList(tags)
        parcel.writeStringList(syllabusIds)
        parcel.writeStringList(testsIds)
        parcel.writeStringList(plansIds)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<CourseModel> {
        override fun createFromParcel(parcel: Parcel): CourseModel {
            return CourseModel(parcel)
        }

        override fun newArray(size: Int): Array<CourseModel?> {
            return arrayOfNulls(size)
        }
    }
}