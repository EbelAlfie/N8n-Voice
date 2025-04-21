package com.app.customerservice.presentation

import android.app.Application
import com.app.customerservice.BuildConfig
import com.app.customerservice.domain.model.UserModel
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.model.User

class App: Application() {

  override fun onCreate() {
    super.onCreate()

    val user = User(
      id = User.id,
      name = User.userName
    )

    val token = ""

    StreamVideoBuilder(
      context = this,
      apiKey = BuildConfig.STREAM_KEY,
      token = StreamVideo.devToken(user.id),
      user = user
    )
      .build()
  }

  companion object {
    val User: UserModel = UserModel(
      id = "customer-service",
      userName = "Customer Service"
    )

//    UserModel(
//      id = "cacing",
//      userName = "Cacing Tanah"
//    )
  }
}