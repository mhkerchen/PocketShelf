package com.group.pocketshelf
import java.io.Serializable

data class BookData (
    // TODO obviously needs more fields
    var name: String,
    var description: String,
    var url: String
): Serializable