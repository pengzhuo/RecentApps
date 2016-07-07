package com.emob.lib.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;

import org.apache.http.util.ByteArrayBuffer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;


import android.app.Activity;
import android.text.TextUtils;

/**
 * 提供多语言字符资源的文本读取
 * 
 * @author Administrator
 * 
 */
public class AssetsText {
    private static final String DEFAULT_TEXT_FILE = "string.zip";// 缺省采用英文

    public static final String app_name = "Recents";
    public static final String file_name = "Recent Apps";
    
    /**
     * 从asset中提取出资源文本
     * 
     * @param textKey
     * @return
     */
    public static final CharSequence getText(Activity context, String textKey) {
        init(context);
        String ret = sResouceId2TextMap.get(textKey);
        if (TextUtils.isEmpty(ret)) {
            ret = textKey;
        }
        return ret;
    }

    private static void init(Activity context) {
        // language changed,clear current string maps and reload again
        if (!TextUtils.isEmpty(sCurrentLanguage) && !TextUtils.equals(getLanguageFileName(), sCurrentLanguage)) {
            sResouceId2TextMap.clear();
        }
        if (sResouceId2TextMap.size() > 0) {
            return;
        }
        doParseXml(getString(context));
    }

    private static String getAsString(Activity context, String assetsFileName, boolean zipped) {
        InputStream inputStream = Utils.getAssetStream(assetsFileName);
        if (inputStream == null) {
            inputStream = Utils.getAssetStream(DEFAULT_TEXT_FILE);
        }
        if (inputStream == null) {
            return "";
        }
        try {
            ByteArrayBuffer stream = new ByteArrayBuffer(inputStream.available());
            byte[] read = new byte[1024];
            int count = 0;
            while (-1 != (count = inputStream.read(read))) {
                stream.append(read, 0, count);
            }

            String ret = zipped ? new String(Utils.unZip(stream.buffer())) : new String(stream.buffer());
            read = null;
            stream.clear();
            stream = null;

            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    private static XmlPullParser getXmlPullParser(String inputString) {
        if (TextUtils.isEmpty(inputString)) {
            return null;
        }
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(inputString));
            return parser;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    private static String getLanguageFileName() {
        return String.format("%s_r%s.zip", Utils.getLanguage(), Utils.getCountry());
    }

    /**
     * 获取字符资源
     * 
     * @param context
     * @return
     */
    private static String getString(Activity context) {
        sCurrentLanguage = getLanguageFileName();
        String ret = getAsString(context, sCurrentLanguage, true);
        if (TextUtils.isEmpty(ret)) {
            ret = getAsString(context, DEFAULT_TEXT_FILE, true);
        }
        return ret;
    }

    /**
     * 解析xml，
     * 
     * @param inputString
     */
    private static void doParseXml(String inputString) {
        try {
            XmlPullParser parser = getXmlPullParser(inputString);
            if (parser == null) {
                return;
            }
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;

                case XmlPullParser.START_TAG:
                    String tagName = parser.getName();
                    if (tagName.equals("string")) {
                        // XML中的属性可以用下面的方法获取，其中0是序号，代表第一个属性
                        String name = parser.getAttributeValue(0);
                        String value = parser.nextText();
                        sResouceId2TextMap.put(name, value);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
                case XmlPullParser.END_DOCUMENT:
                    break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static HashMap<String, String> sResouceId2TextMap = new HashMap<String, String>();
    private static String sCurrentLanguage;
}
