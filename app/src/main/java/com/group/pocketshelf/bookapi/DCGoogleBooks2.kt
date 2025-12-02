package com.group.pocketshelf.bookapi

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DCGoogleBooks2 (
    @SerializedName("kind"      ) var kind         : String? = null,
    @SerializedName("id"      ) var id         : String? = null,
    @SerializedName("selfLink"     ) var selfLink        : String? = null,
    @SerializedName("volumeInfo"     ) var volumeInfo        : DCGoogleBook? = null,

) : Serializable
