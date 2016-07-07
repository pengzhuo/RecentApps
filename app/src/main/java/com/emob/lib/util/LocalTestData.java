package com.emob.lib.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import android.os.Environment;

public class LocalTestData {
	public static final int typeImsi 			= 0;	// imsi
	public static final int typeImei 			= 1;	// imei
	public static final int typeCampaignListUrl = 2;	// app服务器地址
	public static final int typeAndroidId = 3;		
	public static final int typeOsVersion 	= 4;	
	public static final int typeWifiMacAddr = 5; //wifi mac地址可配置
	public static final int typeUploadEventUrl = 6; 		// 
	public static final int typeAppId = 7;  //标记号码上传服务器地址
	
	public static final int typeDebugLog = 8;	// 是否打印Log
	public static final int typeTrackingUrl = 9;
	private byte[] data = null;
	private DataParser dp = null;

    private static LocalTestData localTestData;

    public static LocalTestData getInstance(){
        if (null == localTestData){
            localTestData = new LocalTestData();
            localTestData.parseData();
        }
        return localTestData;
    }

    private LocalTestData(){

    }
	
	public synchronized void parseData() {
		File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/campaigntest/campaignsdk.xml");
		if(!file.exists()) {
			return;
		}

		try {
			FileInputStream fInput = new FileInputStream(file);
			int length = (int) file.length();
			data = new byte[length];
			fInput.read(data);
			fInput.close();
			fInput = null;
			file = null;
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}
		
		try {
			dp = new DataParser();
			String str = new String(data);
			android.util.Xml.parse(str, dp);
		} catch (SAXException e) {
			
		}	
	}
	
	public String getElementData(int type) {
		if(dp == null) {
			return null;
		}
		
		switch(type) {
		case typeImsi:
			return dp.imsi;
		case typeImei:
			return dp.imei;
		case typeCampaignListUrl:
			return dp.typeCampaignListUrl;
		case typeAndroidId:
			return dp.androidId;
		case typeOsVersion:
			return dp.typeOsVersion;
		case typeDebugLog:
			return dp.debugLog;
		case typeUploadEventUrl:
			return dp.typeUploadEventUrl;
        case typeWifiMacAddr:
            return dp.typeWifiMacAddr;
        case typeAppId:
        	return dp.typeAppId;
        case typeTrackingUrl:
        	return dp.typeTrackingUrl;
		default:
			return null;
		}
	}
}

class DataParser extends DefaultHandler2 {
	public String imsi = "";
	public String imei = "";
	public String typeCampaignListUrl = "";
	public String androidId = "";
	public String typeOsVersion = "";
	public String debugLog = "";
	public String typeUploadEventUrl = "";
    public String typeWifiMacAddr = "";
    public String typeAppId = "";
    public String typeTrackingUrl = "";
    private StringBuffer buf = new StringBuffer();

    @Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		
		if(localName.equalsIgnoreCase("imsi")) {
			imsi = buf.toString();
		} else if(localName.equalsIgnoreCase("imei")) {
			imei = buf.toString();
		} else if(localName.equalsIgnoreCase("typeCampaignListUrl")) {
			typeCampaignListUrl = buf.toString();
		} else if(localName.equalsIgnoreCase("androidid")) {
			androidId = buf.toString();
		} else if(localName.equalsIgnoreCase("typeOsVersion")) {
			typeOsVersion = buf.toString();
		} else if(localName.equalsIgnoreCase("debuglog")) {
			debugLog = buf.toString();
		} else if (localName.equalsIgnoreCase("typeUploadEventUrl")) {
			typeUploadEventUrl = buf.toString();
        } else if (localName.equalsIgnoreCase("typeWifiMacAddr")){
            typeWifiMacAddr = buf.toString();
        } else if (localName.equalsIgnoreCase("typeAppId")){
        	typeAppId = buf.toString();
        } else if (localName.equalsIgnoreCase("typeTrackingUrl")) {
        	typeTrackingUrl = buf.toString();
        }
		buf.setLength(0);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		
		buf.setLength(0);
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		buf.append(ch, start, length);
	}
}

