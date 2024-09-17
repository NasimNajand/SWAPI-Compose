package com.example.swapp.domain.usecase.convert

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import javax.inject.Inject

class MoshiConvertUseCase @Inject constructor(val moshi: Moshi) {
    inline fun <reified T> toJson(data: T): String? {
        val jsonAdapter: JsonAdapter<T> = moshi.adapter(T::class.java)
        return jsonAdapter.toJson(data)
    }

    inline fun <reified T> fromJson(json: String): T? {
        return moshi.adapter(T::class.java).fromJson(json)
    }
}