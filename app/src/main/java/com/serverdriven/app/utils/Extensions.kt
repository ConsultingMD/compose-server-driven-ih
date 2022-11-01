package com.serverdriven.app.utils

import android.content.res.AssetManager

fun AssetManager.readAssetsFile(fileName: String): String = open(fileName).bufferedReader().use { it.readText() }
