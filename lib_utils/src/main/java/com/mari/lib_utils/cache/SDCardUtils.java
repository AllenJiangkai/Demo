package com.mari.lib_utils.cache;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SDCardUtils {

    private SDCardUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断SD卡是否可用
     *
     * @return true : 可用<br>false : 不可用
     */
    public static boolean isSDCardEnable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }



    /**
     * 获取SD卡Data路径
     *
     * @return SD卡Data路径，即："/data/"
     */
    public static String getDataPath() {
        if (!isSDCardEnable()) {
            throw new RuntimeException("sdcard disabled!");
        }
        return Environment.getDataDirectory().getPath() + File.separator;
    }
    /**
     * 获取SD卡Data路径
     *
     * @return SD卡Data路径，即："/data/"
     */
    public static String getImageDataPath() {
        if (!isSDCardEnable()) {
            throw new RuntimeException("sdcard disabled!");
        }
        return Environment.getDataDirectory().getPath() + File.separator+"/IMG/";
    }

    /**
     * 在缓存路径下创建指定图片名称的文件
     * tips:通过Context获取的路径都带有包名
     *
     * @param context
     * @param fileName
     */
    public static void savePic(Context context, String fileName) {
        if (!isSDCardEnable()) {
            throw new RuntimeException("sdcard disabled!");
        }
        String path = context.getExternalCacheDir().getAbsolutePath();
        String filePath = path + fileName;
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取外部存储Music路径，如果这个目录不存在，系统会自动帮你创建
     * /storage/emulated/0/Android/data/包名/files/Music/
     *
     * @param context
     * @return /storage/emulated/0/Android/data/包名/files/Music/
     */
    public static String getExternalMusicDir(Context context) {
        if (!isSDCardEnable()) {
            throw new RuntimeException("sdcard disabled!");
        }
        return context.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + File.separator;
    }

    /**
     * 获取外部存储Movies路径，如果这个目录不存在，系统会自动帮你创建
     * /storage/emulated/0/Android/data/包名/files/Movies/
     *
     * @param context
     * @return /storage/emulated/0/Android/data/包名/files/Movies/
     */
    public static String getExternalMovieDir(Context context) {
        if (!isSDCardEnable()) {
            throw new RuntimeException("sdcard disabled!");
        }
        return context.getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath() + File.separator;
    }

    /**
     * 获取外部存储Download路径，如果这个目录不存在，系统会自动帮你创建
     * /storage/emulated/0/Android/data/包名/files/Download/
     *
     * @param context
     * @return /storage/emulated/0/Android/data/包名/files/Download/
     */
    public static String getExternalDownloadDir(Context context) {
        if (!isSDCardEnable()) {
            throw new RuntimeException("sdcard disabled!");
        }
        return context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator;
    }

    /**
     * 获取外部存储Pictures路径，如果这个目录不存在，系统会自动帮你创建
     * /storage/emulated/0/Android/data/包名/files/Pictures/
     *
     * @param context
     * @return /storage/emulated/0/Android/data/包名/files/Pictures/
     */
    public static String getExternalPicturesDir(Context context) {
        if (!isSDCardEnable()) {
            throw new RuntimeException("sdcard disabled!");
        }
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator;
    }

    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsDir(File file) {
        // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    public static boolean saveImage(String filePath, Bitmap bitmap){
        try {
            OutputStream os = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.close();
            os.flush();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 获取SD卡信息
     *
     * @return SDCardInfo
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static SDCardInfo getSDCardInfo() {
        SDCardInfo sd = new SDCardInfo();
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            sd.isExist = true;
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            sd.totalBlocks = sf.getBlockCountLong();
            sd.blockByteSize = sf.getBlockSizeLong();
            sd.availableBlocks = sf.getAvailableBlocksLong();
            sd.availableBytes = sf.getAvailableBytes();
            sd.freeBlocks = sf.getFreeBlocksLong();
            sd.freeBytes = sf.getFreeBytes();
            sd.totalBytes = sf.getTotalBytes();
        }
        return sd;
    }

    private static class SDCardInfo {
        boolean isExist;
        long totalBlocks;
        long freeBlocks;
        long availableBlocks;

        long blockByteSize;

        long totalBytes;
        long freeBytes;
        long availableBytes;

        @Override
        public String toString() {
            return "SDCardInfo{" +
                    "isExist=" + isExist +
                    ", totalBlocks=" + totalBlocks +
                    ", freeBlocks=" + freeBlocks +
                    ", availableBlocks=" + availableBlocks +
                    ", blockByteSize=" + blockByteSize +
                    ", totalBytes=" + totalBytes +
                    ", freeBytes=" + freeBytes +
                    ", availableBytes=" + availableBytes +
                    '}';
        }
    }
}
