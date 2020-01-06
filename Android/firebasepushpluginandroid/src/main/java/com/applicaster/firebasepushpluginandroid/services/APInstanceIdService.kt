package com.applicaster.firebasepushpluginandroid.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class APInstanceIdService : FirebaseMessagingService() {

    private val TAG = APInstanceIdService::class.java.simpleName

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i(TAG, "onTokenRefresh")
    }
}