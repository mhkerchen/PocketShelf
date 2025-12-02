package com.group.pocketshelf.bookapi

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DCGoogleBooks1 (
    @SerializedName("kind"      ) var kind         : String? = null,
    @SerializedName("items"     ) var items        : ArrayList<DCGoogleBooks2>? = null
) : Serializable
