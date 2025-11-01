package com.tps.challenge.network.model

import com.google.gson.annotations.SerializedName
import com.tps.challenge.database.entities.PlaceholderEntity

/**
 * Store remote data model.
 */
data class StoreResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("cover_img_url")
    val coverImgUrl: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("delivery_fee")
    val deliveryFeeCents: String
)

fun List<StoreResponse>.toEntity(): List<PlaceholderEntity>{
    return this.map { storeResponse ->
        PlaceholderEntity(id = storeResponse.id, name = storeResponse.name, description =  storeResponse.description,
            coverImgUrl = storeResponse.coverImgUrl, status = storeResponse.status, deliveryFeeCents = storeResponse.deliveryFeeCents)
    }
}
