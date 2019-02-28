package com.applicaster.firebasepushpluginandroid.services

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceIdService

class APInstanseIdService : FirebaseInstanceIdService() {

    private val TAG = APInstanseIdService::class.java.canonicalName

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        Log.i(TAG, "onTokenRefresh")
    }
}