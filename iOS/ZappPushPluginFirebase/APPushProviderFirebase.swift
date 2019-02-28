//
//  APPushProviderFirebase.swift
//  Pods
//
//  Created by Egor Brel on 2/22/19.
//

import ZappPlugins
import ZappPushPluginsSDK
import UserNotifications
import FirebaseMessaging
import FirebaseCore

open class APPushProviderFirebase: ZPPushProvider {

    override open func getKey() -> String {
        return "firebase"
    }

    override open func configureProvider() -> Bool {
        if (FirebaseApp.app() == nil) {
            FirebaseApp.configure()
        }
        if #available(iOS 10.0, *) {
            UNUserNotificationCenter.current().delegate = self
            let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
            UNUserNotificationCenter.current().requestAuthorization(
                options: authOptions,
                completionHandler: {_, _ in })
        } else {
            let settings: UIUserNotificationSettings = UIUserNotificationSettings(types: [.alert, .badge, .sound],
                                                                                  categories: nil)
            UIApplication.shared.registerUserNotificationSettings(settings)
        }
        UIApplication.shared.registerForRemoteNotifications()
        return true
    }
}

@available(iOS 10, *)
extension APPushProviderFirebase : UNUserNotificationCenterDelegate {
    
    public func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        completionHandler([.alert, .badge, .sound])
    }
    
    public func userNotificationCenter(_ center: UNUserNotificationCenter,
                                didReceive response: UNNotificationResponse,
                                withCompletionHandler completionHandler: @escaping () -> Void) {
        completionHandler()
    }
}
