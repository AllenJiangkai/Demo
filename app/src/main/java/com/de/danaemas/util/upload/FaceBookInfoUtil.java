package com.de.danaemas.util.upload;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.alibaba.fastjson.JSONArray;
import com.de.danaemas.BuildConfig;

import java.io.File;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @ProjectName: Business
 * @Package: com.mari.uang.util.upload
 * @ClassName: FaceBookInfoUtil
 * @Description: java类作用描述
 * @Author: jtao
 * @CreateDate: 2021/5/1 5:17 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/1 5:17 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class FaceBookInfoUtil {

    // getAvailableBlocks/getBlockSize deprecated but required pre-API v18
    @SuppressWarnings("deprecation")
    private static long refreshAvailableExternalStorage() {
        double availableExternalStorageGB = 0;
        try {
            if (externalStorageExists()) {
                File path = Environment.getExternalStorageDirectory();
                StatFs stat = new StatFs(path.getPath());
                availableExternalStorageGB =
                        (long)stat.getAvailableBlocks() * (long)stat.getBlockSize();
            }
            availableExternalStorageGB = convertBytesToGB(availableExternalStorageGB);
        } catch (Exception e) {
            // Swallow
        }
        return (long) availableExternalStorageGB;
    }



    public static String getExtinfo(Context context){
        JSONArray extinfoArr = new JSONArray();
        extinfoArr.add("a2");
        extinfoArr.add(context.getPackageName());
        extinfoArr.add(BuildConfig.VERSION_CODE);
        extinfoArr.add(BuildConfig.VERSION_NAME);
        extinfoArr.add(Build.VERSION.RELEASE);
        extinfoArr.add(Build.MODEL);
        Locale locale;
        try {
            locale = context.getResources().getConfiguration().locale;
        } catch (Exception e) {
            locale = Locale.getDefault();
        }
        extinfoArr.add(locale.getLanguage() + "_" + locale.getCountry());
        TimeZone tz = TimeZone.getDefault();
        extinfoArr.add(tz.getDisplayName(tz.inDaylightTime(new Date()), TimeZone.SHORT));


        TelephonyManager telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
        extinfoArr.add(telephonyManager.getNetworkOperatorName());

        int width = 0;
        int height = 0;
        double density = 0;
        try {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (wm != null) {
                Display display = wm.getDefaultDisplay();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                display.getMetrics(displayMetrics);
                width = displayMetrics.widthPixels;
                height = displayMetrics.heightPixels;
                density = displayMetrics.density;
            }
        } catch (Exception e) {
            // Swallow
        }
        extinfoArr.add(width);
        extinfoArr.add(height);
        extinfoArr.add(String.format("%.2f", density));

        extinfoArr.add(Math.max(Runtime.getRuntime().availableProcessors(), 1));

        extinfoArr.add(refreshTotalExternalStorage());
        extinfoArr.add(refreshAvailableExternalStorage());

        extinfoArr.add(tz.getID());
        return extinfoArr.toJSONString();
    }

    // getAvailableBlocks/getBlockSize deprecated but required pre-API v18
    @SuppressWarnings("deprecation")
    private static long refreshTotalExternalStorage() {
        double totalExternalStorageGB = 0;
        try {
            if (externalStorageExists()) {
                File path = Environment.getExternalStorageDirectory();
                StatFs stat = new StatFs(path.getPath());
                totalExternalStorageGB = (long)stat.getBlockCount() * (long)stat.getBlockSize();
            }
            totalExternalStorageGB = convertBytesToGB(totalExternalStorageGB);
        } catch (Exception e) {
            // Swallow
        }
        return (long) totalExternalStorageGB;
    }

    private static long convertBytesToGB(double bytes) {
        return Math.round(bytes / (1024.0 * 1024.0 * 1024.0));
    }

    /**
     * @return whether there is external storage:
     */
    private static boolean externalStorageExists() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }



}
