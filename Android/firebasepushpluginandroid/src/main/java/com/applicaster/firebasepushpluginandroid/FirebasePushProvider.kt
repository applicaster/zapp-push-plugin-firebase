package com.applicaster.firebasepushpluginandroid

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import com.applicaster.plugin_manager.push_plugin.PushContract
import com.applicaster.plugin_manager.push_plugin.helper.PushPluginsType
import com.applicaster.plugin_manager.push_plugin.listeners.PushTagLoadedI
import com.applicaster.plugin_manager.push_plugin.listeners.PushTagRegistrationI

class FirebasePushProvider : PushContract {

    private val TAG = FirebasePushProvider::class.java.canonicalName

    private var pluginsParamsMap: MutableMap<*, *>? = null

    override fun initPushProvider(context: Context?) {

        if (Build.VERSION.SDK_INT >= 26) {
            val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

            val paramChannelName = CHANNEL_NAME_KEY
            val channel = NotificationChannel(
                FIREBASE_DEFAULT_CHANNEL_ID,
                if (paramChannelName.isEmpty()) FIREBASE_DEFAULT_CHANNEL_ID else paramChannelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationManager?.createNotificationChannel(channel)
        }
    }

    override fun registerPushProvider(context: Context?, registerID: String?) {
        initPushProvider(context)
    }

    override fun getPluginType(): PushPluginsType {
        return PushPluginsType.applicaster
    }

    override fun addTagToProvider(
        context: Context?,
        tag: MutableList<String>?,
        pushTagRegistrationListener: PushTagRegistrationI?
    ) {

    }

    override fun setPluginParams(params: MutableMap<Any?, Any?>?) {
        pluginsParamsMap = params
    }

    override fun removeTagToProvider(
        context: Context?,
        tag: MutableList<String>?,
        pushTagRegistrationListener: PushTagRegistrationI?
    ) {

    }

    override fun getTagList(context: Context?, listener: PushTagLoadedI?) {

    }

    private fun getPluginParamByKey(key: String): String {
        if (pluginsParamsMap?.containsKey(key) == true)
            return pluginsParamsMap?.get(key).toString()
        return ""
    }
}