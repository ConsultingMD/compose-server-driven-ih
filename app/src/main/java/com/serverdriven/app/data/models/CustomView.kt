package com.serverdriven.app.data.models

import com.google.gson.annotations.SerializedName

data class CustomView(
    @SerializedName("__typename")
    val typeName: TypeView = TypeView.UNKNOWN,
    val modifier: CustomModifier = CustomModifier(),
    @SerializedName("objects")
    val children: List<CustomView> = emptyList(),
    val horizontalArrangement: String? = "default",
    val verticalAlignment: String? = "default",
    val value: String? = "default",
)