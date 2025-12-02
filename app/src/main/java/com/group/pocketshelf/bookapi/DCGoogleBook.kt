package com.group.pocketshelf.bookapi
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DCGoogleBook (
    @SerializedName("title"      ) var title         : String? = null,
    @SerializedName("description"      ) var description         : String? = null,
    @SerializedName("authors"     ) var authors        : ArrayList<String>? = null,
    @SerializedName("pageCount"     ) var pageCount        : Int? = null,
    @SerializedName("imageLinks"     ) var imageLinks        : Map<String, String>? = null,
) : Serializable
