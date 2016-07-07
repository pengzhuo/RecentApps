package com.emob.luck.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.emob.lib.util.StrUtils;
import com.emob.luck.common.Value;

import android.content.Context;

public class EventItem {

	public int id;   								 // 表id
	public String packageName;						 // "lock"代表锁屏， "包名"代表topTen
    public int action;								 // 记录用户行为  1:showed; 2:clicked; 3:下载；4：安装；5:closed
    public String time;								 // 记录用户行为触发点
    public int channel;
    public int pos;
    
    public static final int EVENT_TYPE_SHOW 			= 1;	// 展示
    public static final int EVENT_TYPE_CLICK 			= 2;	// 点击
    public static final int EVENT_TYPE_CLOSE 			= 5;	// 关闭
    public static final int EVENT_TYPE_REQUEST 			= 6;    // 请求
    public static final int EVENT_TYPE_REQUEST_FAILED 	= 7;    // 请求失败，无广告返回
    public static final int EVENT_TYPE_NOTREDAY 		= 8;    // 请求成功，但广告未准备好。几乎不会发生。
    
    public static final int EVENT_TYPE_GET_APPID_FAILED = 20;	// 获取appId失败
    public static final int EVENT_TYPE_CLICK_APP 		= 30;	// 点击了文件夹中的应用
    public static final int EVENT_TYPE_LOCK_DISABLE 	= 40;	// 全局锁屏开关关闭
    public static final int EVENT_TYPE_LOCK		 		= 41;	// 锁屏发生，但不满足弹窗时机
    public static final int EVENT_TYPE_NETON		 	= 42;	// 网络打开场景发生，但不满足弹窗时机
    public static final int EVENT_TYPE_LAUNCHER_ENABLE	= 50;	// 锁屏触发场景。Launcher开关开启，限制为桌面才能弹窗
    public static final int EVENT_TYPE_LAUNCHER_ENABLE_NETON	= 51;	// 网络打开触发场景。Launcher开关开启，限制为桌面才能弹窗
    public static final int EVENT_TYPE_NETON_DISABLE	= 60;	// 网络打开触发条件关闭
    public static final int EVENT_TYPE_NO_NETWORK 		= 70;	// 到锁屏弹窗时间，但无网络
    public static final int EVENT_TYPE_NO_NETWORK_REQ	= 71;	// 到请求服务器时间，但无网络
    public static final int EVENT_TYPE_NO_NETWORK_ICON	= 72;	// 到更新文件夹icon广告时间，但无网络
    public static final int EVENT_TYPE_NO_NETWORK_TOPEXIT	= 73;	// 到TopExit弹窗时间，但无网络
    public static final int EVENT_TYPE_SHORTCUT_CLICK	= 81;	// 点击了快捷方式
    public static final int EVENT_TYPE_SHORTCUT_SERVICE	= 82;	// 通过快捷方式，启动了Service
    public static final int EVENT_TYPE_SHORTCUT_GP		= 83;	// 通过快捷方式，打开了GooglePlay
    public static final int EVENT_TYPE_SHORTCUT_BROWSER	= 84;	// 通过快捷方式，因为没有GooglePlay，用浏览器GooglePlay首页
    public static final int EVENT_TYPE_REBOOT			= 91;	// 手机重启时间
    
    public static final int EVENT_TYPE_SERVICE_RESTART	= 99;
    
    public static final String SHOW_TYPE_LOCK_SCREEN = StrUtils.deCrypt("lo");	 		     //锁屏展示
    public static final String SHOW_TYPE_NETWORK_ON = StrUtils.deCrypt("ne");	 		     //锁屏展示
    public static final String SHOW_TYPE_INMOBI_NATIVE_ICON = StrUtils.deCrypt("ficon");	     //inmobi原生展示
    public static final String SHOW_TYPE_INMOBI_NATIVE_SPOT = StrUtils.deCrypt("tint");	     //inmobi原生展示
    public static final String SHOW_TYPE_INMOBI_NATIVE_BANNER = StrUtils.deCrypt("tban");	     //inmobi原生展示
    
    public EventItem(Context _context, String time, int action) {
        this.time = time;
        this.action = action;
        //this.status = status;
    }

    public EventItem() {
    	
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

    public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public int getChannel() {
		return channel;
	}
	
	public void setChannel(int channel) {
		this.channel = channel;
	}
	
	public int getPos() {
		return pos;
	}
	
	public void setPos(int pos) {
		this.pos = pos;
	}
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

    
	public JSONObject getJsonObject() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(Value.ACTIONS, action);
        object.put(Value.PACKAGENAME, packageName);
        object.put(Value.ACTION_TIME, time);
        object.put(Value.CHANNEL, channel);
        object.put(Value.POS, pos);
        return object;
    }

}
