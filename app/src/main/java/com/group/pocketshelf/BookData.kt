package com.group.pocketshelf
import java.io.Serializable
import com.google.gson.annotations.SerializedName

data class BookData (
    // can be populated from API
    @SerializedName("title"     ) var title         : String? = null,
    @SerializedName("author"    ) var author        : String? = null,
    @SerializedName("language"  ) var language      : String? = null,
    @SerializedName("synopsis"  ) var synopsis      : String? = null,
    @SerializedName("page_count") var page_count    : String? = null,
    @SerializedName("isbn"      ) var isbn          : String? = null,

    // user generated
    @SerializedName("tags"      ) var tags          : ArrayList<String>? = null,
    @SerializedName("rating"    ) var rating        : String? = null,
    @SerializedName("notes"     ) var notes         : String? = null,

    // determinant
    @SerializedName("cover_is_url") var cover_is_url: Boolean = true,
    @SerializedName("img_url"   ) var img_url       : String? = null,
    @SerializedName("img_path"  ) var img_path      : String? = null,
) : Serializable