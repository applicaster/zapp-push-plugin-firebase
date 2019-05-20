package com.applicaster.firebasepushpluginandroid

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.applicaster.plugin_manager.push_plugin.PushContract
import com.applicaster.plugin_manager.push_plugin.helper.PushPluginsType
import com.applicaster.plugin_manager.push_plugin.listeners.PushTagLoadedI
import com.applicaster.plugin_manager.push_plugin.listeners.PushTagRegistrationI
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.suspendCoroutine

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
           launch{ registerAll(tag, pushTagRegistrationListener)}
    }


    override fun removeTagToProvider(
            context: Context?,
            tag: MutableList<String>?,
            pushTagRegistrationListener: PushTagRegistrationI?
    ) {
        launch{ unregisterAll(tag, pushTagRegistrationListener)}
    }

    override fun setPluginParams(params: MutableMap<Any?, Any?>?) {
        pluginsParamsMap = params
    }

    override fun getTagList(context: Context?, listener: PushTagLoadedI?) {

    }

    //region Private methods

    private fun getPluginParamByKey(key: String): String {
        if (pluginsParamsMap?.containsKey(key) == true)
            return pluginsParamsMap?.get(key).toString()
        return ""
    }

    suspend private fun registerAll(tag: MutableList<String>?,
                                    pushTagRegistrationListener: PushTagRegistrationI?){
        var totalResult = true;
        tag?.forEach {
            var result = async{register(it) }.await()
            totalResult = totalResult && result
        }
        pushTagRegistrationListener?.pushRregistrationTagComplete(PushPluginsType.applicaster, totalResult)
    }

    suspend private fun register(topic : String): Boolean =
            suspendCoroutine { cont ->
                FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnCompleteListener { task ->
                    cont.resume(task.isSuccessful)
                }
            }

    suspend private fun unregisterAll(tag: MutableList<String>?,
                                    pushTagRegistrationListener: PushTagRegistrationI?){
        var totalResult = true;
        tag?.forEach {
            var result = async{unregister(it) }.await()
            totalResult = totalResult && result
        }
        pushTagRegistrationListener?.pushRregistrationTagComplete(PushPluginsType.applicaster, totalResult)
    }

    suspend private fun unregister(topic : String): Boolean =
            suspendCoroutine { cont ->
                FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).addOnCompleteListener { task ->
                    cont.resume(task.isSuccessful)
                }
            }

    //endregion
}