package com.leysoft.products.adapter.out.persistence.exposed

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone

object Products : Table("products.products") {
    val id = varchar("id", 255)
    val name = varchar("name", 255)
    val stock = double("stock")
    val createdAt = timestampWithTimeZone("created_at")

    override val primaryKey = PrimaryKey(id)
}
