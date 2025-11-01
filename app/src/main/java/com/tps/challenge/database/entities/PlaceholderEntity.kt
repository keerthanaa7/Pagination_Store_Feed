package com.tps.challenge.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.tps.challenge.Store

/**
 * A placeholder entity.
 */
@Entity(tableName = "stores")
data class PlaceholderEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val coverImgUrl: String,
    val status: String,
    val deliveryFeeCents: String
)

fun PlaceholderEntity.toDomain(): Store {
    return Store(
        id = this.id,
        name = this.name,
        description = this.description,
        coverImgUrl = this.coverImgUrl,
        status = this.status,
        deliveryFeeCents = this.deliveryFeeCents
    )
}