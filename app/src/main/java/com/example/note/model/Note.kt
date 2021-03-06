package com.example.note.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Note(
    val id: String = "",
    val title: String = "",
    val note: String = "",
    val color: Color = Color.YELLOW,
    val lastChanged: Date = Date()
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Note

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

enum class Color {
    WHITE,
    BLACK,
    YELLOW,
    GREEN,
    BLUE,
    RED,
    VIOLET,
    PINK,
}