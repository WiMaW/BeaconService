package pl.wmwdev.beaconservice.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Action (
    val name: String = "",
    val tekst: String = "",
    val image: String = "",
    val audio: String = "",
    val video: String = ""
) : Parcelable