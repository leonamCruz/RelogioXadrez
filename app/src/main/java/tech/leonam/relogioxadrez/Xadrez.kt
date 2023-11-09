package tech.leonam.relogioxadrez

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Xadrez(
    var segOne: Double,
    var segTwo: Double,
    var adicional: Double
) : Parcelable
