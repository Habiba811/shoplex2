package com.trueandtrust.shoplex.model.pojo

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import com.trueandtrust.shoplex.model.enumurations.Plan
import java.util.*

data class Premium(val plan: Plan? = null,
                   val premiumDays: Int = 0,
                   @ServerTimestamp val premiumStart: Date? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        plan = Plan.valueOf(parcel.readString().toString()),
        parcel.readInt(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(plan?.name)
        parcel.writeInt(premiumDays)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Premium> {
        override fun createFromParcel(parcel: Parcel): Premium {
            return Premium(parcel)
        }

        override fun newArray(size: Int): Array<Premium?> {
            return arrayOfNulls(size)
        }
    }
}