package com.mei.imageloader;

import com.mei.imageloader.bean.LoaderResult;
import com.mei.imageloader.cache.DiskLruCache;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import androidx.annotation.NonNull;
import androidx.collection.LruCache;

/**
 * @author mxb
 * @date 2020/10/19
 * @desc 图片加载入口类
 * @desired
 */
public class ImageLoader {

    private static final String TAG = "ImageLoader";

    /**
     * 图片在每个节点的缓存下标，因为每个节点只存一张图片，所以每个节点的图片下标固定为0
     */
    private final static int DISK_CACHE_INDEX = 0;

    // 磁盘缓存的最大容量
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 50;

    //
    private static final int IO_BUFFER_SIZE = 8 * 1024;

    private static final int MSG_POST_RESULT = 110;

    // ImageView的tag key值
    private static final int TAG_KEY_URI = R.id.image_view_tag_key;

    // 磁盘缓存是否创建
    private boolean mIsDiskLruCacheCreated = false;

    private final Context mContext;

    // 内存缓存
    private LruCache<String, Bitmap> mMemoryCache;

    // 磁盘缓存
    private DiskLruCache mDiskLruCache;

    private ImageResizer mImageResizer = new ImageResizer();

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            LoaderResult result = (LoaderResult) msg.obj;
            if (result == null) {
                return;
            }
            ImageView imageView = result.imageView;
            String tag = (String) imageView.getTag(TAG_KEY_URI);
            if (TextUtils.equals(tag, result.uri)) {
                imageView.setImageBitmap(result.bitmap);
            } else {
                Log.i(TAG, "set image bitmap,but url has changed,ignored!");
            }
        }
    };


    private ImageLoader(Context context) {
        mContext = context.getApplicationContext();
        int maxCacheMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxCacheMemory / 8;
        Log.i(TAG, "ImageLoader: maxCacheMemory=" + (maxCacheMemory / 1024) + "MB;cacheSize=" + (
                cacheSize / 1024) + "MB");
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(@NonNull String key, @NonNull Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024; // 单位kb
            }
        };
        File diskCacheDir = getDiskCacheDir(mContext, "bitmap");
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs();
        }
        if (getUsableSpace(diskCacheDir) > DISK_CACHE_SIZE) {
            try {
                mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 构建ImageLoader对象
     */

    public static ImageLoader sImageLoader;

    public static ImageLoader getInstance(Context context) {
        if (sImageLoader == null) {
            synchronized (ImageLoader.class) {
                if (sImageLoader == null) {
                    sImageLoader = new ImageLoader(context);
                }
            }
        }
        return sImageLoader;
    }

    /**
     * 添加图片到缓存
     *
     * @param key    图片到key值
     * @param bitmap 图片对象
     */
    private void addMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 从缓存获取图片
     *
     * @param uri 图片的url
     */
    private Bitmap getBitmapFromMemoryCache(String uri) {
        String key = hashKeyFromUrl(uri);
        return mMemoryCache.get(key);
    }

    /**
     * 加载图片到指定的ImageView中
     *
     * @param uri       图片url
     * @param imageView 图片地址
     */
    public void bindBitmap(String uri, ImageView imageView) {
        bindBitmap(uri, imageView, 0, 0);
    }

    /**
     * 加载图片到指定的ImageView中
     *
     * @param uri       图片url
     * @param imageView 图片地址
     * @param reqWidth  期望的图片宽度
     * @param reqHeight 期望的图片高度
     */
    public void bindBitmap(String uri, ImageView imageView, int reqWidth, int reqHeight) {
        Log.i(TAG, "bindBitmap: imageView=" + imageView + ";uri=" + uri);
        Log.i(TAG, "bindBitmap: reqWidth=" + reqWidth + ";reqHeight=" + reqHeight);
        if (imageView == null || TextUtils.isEmpty(uri)) {
            return;
        }
        imageView.setImageResource(R.color.design_dark_default_color_error);
        // 设置tag
        imageView.setTag(TAG_KEY_URI, uri);

        // 1. 从缓存加载图片
        Bitmap bitmap = loadBitmapFromCache(uri);
        if (bitmap != null) {
            Log.i(TAG, "bindBitmap: 从缓存中获取到图片:" + bitmap);
            imageView.setImageBitmap(bitmap);
            return;
        }
        Runnable loadBitmapTask = () -> {
            Bitmap bitmap1 = loadBitmap(uri, reqWidth, reqHeight);
            if (bitmap1 != null) {
                LoaderResult result = new LoaderResult();
                result.bitmap = bitmap1;
                result.uri = uri;
                result.imageView = imageView;
                mHandler.obtainMessage(MSG_POST_RESULT, result).sendToTarget();
            }
        };
        ThreadPoolManager.run(loadBitmapTask);
    }

    /**
     * 下载图片
     *
     * @param uri       图片url
     * @param reqWidth  期望的图片宽度
     * @param reqHeight 期望的图片高度
     */
    public Bitmap loadBitmap(String uri, int reqWidth, int reqHeight) {
        // 1. 从缓存中获取图片
        Bitmap bitmap = loadBitmapFromCache(uri);
        if (bitmap != null) {
            Log.d(TAG, "loadBitmapFromCache,url= " + uri);
            return bitmap;
        }
        try {
            // 2. 从磁盘缓存中查找图片
            bitmap = loadBitmapFromDiskCache(uri, reqWidth, reqHeight);
            if (bitmap != null) {
                Log.d(TAG, "loadBitmapFromDiskCache url=" + uri);
            }

            // 3. 从网络下来图片
            bitmap = loadBitmapFromHttp(uri, reqWidth, reqHeight);
            Log.d(TAG, "loadBitmapFromHttp,url=" + uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 4. 经过上面的步骤之后，图片加载还是失败，则直接下载图片到内存中
        if (bitmap == null && !mIsDiskLruCacheCreated) {
            Log.w(TAG, "loadBitmap, encounter error,DiskLruCache is not created.");
            bitmap = downloadBitmapFromUrl(uri);
        }

        return bitmap;
    }

    /**
     * 从网络下载图片，并缓存到本地磁盘中，在从磁盘把图片加载到内存中来
     *
     * @param url       图片url地址
     * @param reqWidth  期望的图片宽度
     * @param reqHeight 期望的图片高度
     */
    private Bitmap loadBitmapFromHttp(String url, int reqWidth, int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("can not visit network from UI Thread.");
        }
        if (mDiskLruCache == null) {
            return null;
        }
        String key = hashKeyFromUrl(url);
        DiskLruCache.Editor edit = mDiskLruCache.edit(key);
        if (edit != null) {
            OutputStream outputStream = edit.newOutputStream(DISK_CACHE_INDEX);
            // 把图片下载到指定的文件下
            if (downloadUrlToStream(url, outputStream)) {
                edit.commit();// 图片下载成功后，提交保存
            } else {
                edit.abort();
            }
            mDiskLruCache.flush();
        }
        // 从磁盘中，把刚才下载好的图片加载到内存中来
        return loadBitmapFromDiskCache(url, reqWidth, reqHeight);
    }

    /**
     * 下载图片到指定的输出流中
     *
     * @param urlString    图片url地址
     * @param outputStream 图片保存路径的输出流
     */
    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (in != null) {
                    in.close();
                }

                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 从磁盘中加载图片
     *
     * @param url       图片url
     * @param reqWidth  期望的图片宽度
     * @param reqHeight 期望的图片高度
     */
    private Bitmap loadBitmapFromDiskCache(String url, int reqWidth, int reqHeight)
            throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.w(TAG, "load bitmap from UI Thread,it's not recommended!");
        }

        if (mDiskLruCache == null) {
            return null;
        }
        Bitmap bitmap = null;
        String key = hashKeyFromUrl(url);
        DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
        if (snapshot != null) {
            FileInputStream inputStream = (FileInputStream) snapshot
                    .getInputStream(DISK_CACHE_INDEX);
            FileDescriptor fd = inputStream.getFD();
            bitmap = mImageResizer.decodeFromFile(fd, reqWidth, reqHeight);

            // 加载到内存中的图片，缓存到内存中
            if (bitmap != null) {
                addMemoryCache(key, bitmap);
            }
        }
        return bitmap;
    }

    /**
     * 直接下载图片到内存中，图片不会压缩，直接是原图
     *
     * @param urlString 图片url
     */
    private Bitmap downloadBitmapFromUrl(String urlString) {
        Bitmap bitmap = null;
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 获取可用的空间
     */
    @SuppressLint({"UsableSpace", "ObsoleteSdkInt"})
    private long getUsableSpace(File diskCacheDir) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return diskCacheDir.getUsableSpace();
        }
        StatFs statFs = new StatFs(diskCacheDir.getPath());
        return statFs.getBlockSize() * statFs.getAvailableBlocks();
    }

    /**
     * 获取图片缓存目录
     *
     * @param uniqueName 图片缓存文件夹
     */
    public File getDiskCacheDir(Context context, String uniqueName) {
        boolean available = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        String cachePath;
        if (available) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        Log.i(TAG, "getDiskCacheDir: cachePath=" + cachePath);
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 从缓存中加载图片
     */
    private Bitmap loadBitmapFromCache(String uri) {
        if (mMemoryCache == null) {
            return null;
        }
        String key = hashKeyFromUrl(uri);

        return mMemoryCache.get(key);
    }

    /**
     * 根据图片url，生成图片key值
     */
    private String hashKeyFromUrl(String url) {
        String cacheKey;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(url.getBytes());
            cacheKey = bytesToHexString(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    /**
     * 把字节转换成字符串
     *
     * @param bytes 字节数值
     */
    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

}
