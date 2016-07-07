package com.emob.luck;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * Created by Mmost on 15-10-15.
 */
public class MobiBroadcast {
    public static final String INTENT_ACTION_SELF_RESTART = "com.mobi.intent.action.USER_DEFINE_LIVE";

    public static void sendBroadcastLive(Context context) {
        Intent intent = new Intent();
        intent.setAction(INTENT_ACTION_SELF_RESTART);

        //发送广播
        //1、无序广播 所有广播接收者都会接收到
        context.sendBroadcast(intent);
        //2、有序广播，接收者按照优先级接收广播事件
        //且高优先级广播有权终止该事件
//        context.sendOrderedBroadcast(intent, null);

        //使用此方法直接指定接收者 无论如何都会接收到信息
//        context.sendOrderedBroadcast(intent, null, new FinalRecivey(), null, 0, null, null);
    }

    public static void sendBroadcast(Context context, String action) {
        if (TextUtils.isEmpty(action)) {
            return;
        }

        Intent intent = new Intent();
        intent.setAction(action);

        //发送广播
        //1、无序广播 所有广播接收者都会接收到
        context.sendBroadcast(intent);

    }
}
