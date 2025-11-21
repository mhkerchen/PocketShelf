package com.group.pocketshelf

import java.io.Serializable
data class ShelfData (
    var name: String,
    var books: List<BookData>

): Serializable