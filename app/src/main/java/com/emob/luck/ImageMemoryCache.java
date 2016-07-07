package com.emob.luck;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

import com.emob.lib.log.EmobLog;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.LruCache;


public class ImageMemoryCache {
    /**
     * 从内存读取数据速度是最快的，为了更大限度使用内存，这里使用了两层缓存。
     * 硬引用缓存不会轻易被回收，用来保存常用数据，不常用的转入软引用缓存。
     */
    private static final int SOFT_CACHE_SIZE = 15;  //软引用缓存容量
    private static LruCache<String, Bitmap> mLruCache;  //硬引用缓存
    private static LinkedHashMap<String, SoftReference<Bitmap>> mSoftCache;  //软引用缓存
    private static ImageMemoryCache mInstance = null;
    private static final String TAG = "Image";
    
    public static ImageMemoryCache getInstance(Context context) {
    	if (mInstance == null) {
    		mInstance = new ImageMemoryCache(context);
    	}
    	return mInstance;
    }
    
    public ImageMemoryCache(Context context) {
    	int cacheSize = 1024 * 1024 * 6;
    	
    	if (context!= null) {
	        int memClass = ((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
	        cacheSize = 1024 * 1024 * memClass / 4;  //硬引用缓存容量，为系统可用内存的1/4
    	}
    	
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                if (value != null)
                    return value.getRowBytes() * value.getHeight();
                else
                    return 0;
            }
                                                                                          
            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                if (oldValue != null)
                    // 硬引用缓存容量满的时候，会根据LRU算法把最近没有被使用的图片转入此软引用缓存
                    mSoftCache.put(key, new SoftReference<Bitmap>(oldValue));
            }
        };
        mSoftCache = new LinkedHashMap<String, SoftReference<Bitmap>>(SOFT_CACHE_SIZE, 0.75f, true) {
            private static final long serialVersionUID = 6040103833179403725L;
            @Override
            protected boolean removeEldestEntry(Entry<String, SoftReference<Bitmap>> eldest) {
                if (size() > SOFT_CACHE_SIZE){    
                    return true;  
                }  
                return false; 
            }
        };
    }
                                                                                  
    /**
     * 从缓存中获取图片
     */
    public Bitmap getBitmapFromCache(String url) {
        Bitmap bitmap;
        //先从硬引用缓存中获取
        synchronized (mLruCache) {
            bitmap = mLruCache.get(url);
            if (bitmap != null) {
                //如果找到的话，把元素移到LinkedHashMap的最前面，从而保证在LRU算法中是最后被删除
                mLruCache.remove(url);
                mLruCache.put(url, bitmap);
                return bitmap;
            }
        }
        //如果硬引用缓存中找不到，到软引用缓存中找
        synchronized (mSoftCache) { 
            SoftReference<Bitmap> bitmapReference = mSoftCache.get(url);
            if (bitmapReference != null) {
                bitmap = bitmapReference.get();
                if (bitmap != null) {
                    //将图片移回硬缓存
                    mLruCache.put(url, bitmap);
                    mSoftCache.remove(url);
                    return bitmap;
                } else {
                    mSoftCache.remove(url);
                }
            }
        }
        return null;
    } 
                                                                                  
    /**
     * 添加图片到缓存
     */
    public void addBitmapToCache(String url, Bitmap bitmap) {
    	EmobLog.i(TAG, "add to bitmap cache, url: "+ url);
        if (bitmap != null) {
            synchronized (mLruCache) {
            	if (mLruCache != null && mLruCache.get(url) == null) {
            		EmobLog.i(TAG, "added, url: "+ url);
            		mLruCache.put(url, bitmap);
            	}
            }
        }
    }
               
    /**
     * 删除一张图片
     * @param url
     */
    public void removeOneImg(String url) {
    	Bitmap bitmap;
        //先从硬引用缓存中获取
    	synchronized (mLruCache) {
            bitmap = mLruCache.get(url);
            if (bitmap != null) {
                //如果找到的话，把元素移到LinkedHashMap的最前面，从而保证在LRU算法中是最后被删除
                mLruCache.remove(url);
                return ;
            }
        }
        //如果硬引用缓存中找不到，到软引用缓存中找
        synchronized (mSoftCache) { 
            SoftReference<Bitmap> bitmapReference = mSoftCache.get(url);
            if (bitmapReference != null) {
                bitmap = bitmapReference.get();
                if (bitmap != null) {
                    //将图片移回硬缓存
                    mSoftCache.remove(url);
                    return;
                }
            }
        }
    	
    }
    
    public void clearCache() {
    	if (mLruCache != null) {
    		if (mLruCache.size() > 0) {
    			mLruCache.evictAll();
    		}
//    		mLruCache = null;
    	}
    	if (mSoftCache != null) {
    		mSoftCache.clear();
//    		mSoftCache = null;
    	}
    }
    
    public void removeBitmap(String url) {
    	if (TextUtils.isEmpty(url)) {
    		return;
    	}
    	if (mLruCache != null) {
	    	synchronized (mLruCache) {
	            Bitmap b = mLruCache.remove(url);
	            if (b!= null) {
	            	b.recycle();
	            }
	        }
    	}
    }
    
    public Bitmap getBitmap(String url) {  
    	EmobLog.d(TAG, "getBitmap begin, url="+url);
        // 从内存缓存中获取图片  
        Bitmap result = getBitmapFromCache(url);  
        
        if (result == null) {  
        	EmobLog.d(TAG, "getBitmap null");
            // 从网络获取  
            result = ImageGetFromHttp.downloadBitmap(url);  
            if (result != null) {  
                addBitmapToCache(url, result);  
            }  
        } else {  
            // 添加到内存缓存  
        	EmobLog.d(TAG, "getBitmap begin, not null");
            addBitmapToCache(url, result);  
        }  
        return result;  
    }  
    
//    public static void MyRecycle(Bitmap bmp) {
//		if (!bmp.isRecycled() && null != bmp) {
//			bmp = null;
//		}
//	}
}