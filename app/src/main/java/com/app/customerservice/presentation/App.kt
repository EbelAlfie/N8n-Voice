package com.app.customerservice.presentation

import android.app.Application
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.model.User

class App: Application() {

  override fun onCreate() {
    super.onCreate()

    val user = User(
      id = "cacing",
      name = "Cacing Tanah"
    )

    val token = ""

    StreamVideoBuilder(
      context = this,
      apiKey = BuildConfig.STREAM_KEY,
      token = token,
      user = user
    )
      .build()
  }
}