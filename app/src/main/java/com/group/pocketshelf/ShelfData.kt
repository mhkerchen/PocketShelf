package com.group.pocketshelf
import java.io.Serializable
import com.google.gson.annotations.SerializedName

data class ShelfData (
    @SerializedName("name"      ) var name         : String? = null,
    @SerializedName("books"     ) var books        : ArrayList<String>? = null
) : Serializable