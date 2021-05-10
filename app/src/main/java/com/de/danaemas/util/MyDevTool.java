package com.de.danaemas.util;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @ProjectName: My Application
 * @Package: com.kejutan.dk.utils
 * @ClassName: DevicesUtil
 * @Description: java类作用描述
 * @Author: jtao
 * @CreateDate: 2021/1/13 1:24 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/1/13 1:24 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MyDevTool {

    //获取当前app的版本名称
    public static String getAppVersion(Context context) {
        return getAppVersion(context, true);
    }

    public static String getAppVersion(Context context, boolean isApi) {
        String appVersion = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo;
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            appVersion = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersion;
    }

    //获取当前app的版本号
    public static int getAppVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo;
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getInstalledTime(Context context){
        String installedTime = "";
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long firstInstallTime = packageInfo.firstInstallTime;//应用第一次安装的时间
            installedTime = sdf.format(firstInstallTime);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return installedTime;
    }

    public static String getDeviceId(Context context){
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                if (tm == null || tm.getDeviceId() == null) {
                    String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    if (androidId != null) {
                        return androidId;
                    } else {
                        String serial = null;
                        String m_szDevIDShort = "35" +
                                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                                Build.USER.length() % 10; //13 位
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                serial = android.os.Build.getSerial();
                            } else {
                                serial = Build.SERIAL;
                            }
                            //API>=9 使用serial号
                            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
                        } catch (Exception exception) {
                            //serial需要一个初始化
                            serial = "serial"; // 随便一个初始化
                        }
                        //使用硬件信息拼凑出来的15位号码
                        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
                    }
                } else {
                    return tm.getDeviceId();
                }
            }
        } catch (Exception e) {
            String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (androidId != null) {
                return androidId;
            } else {
                String serial = null;
                String m_szDevIDShort = "35" +
                        Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                        Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                        Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                        Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                        Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                        Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                        Build.USER.length() % 10; //13 位
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        serial = android.os.Build.getSerial();
                    } else {
                        serial = Build.SERIAL;
                    }
                    //API>=9 使用serial号
                    return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
                } catch (Exception exception) {
                    //serial需要一个初始化
                    serial = "serial"; // 随便一个初始化
                }
                //使用硬件信息拼凑出来的15位号码
                return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
            }
        }
        return null;
    }

    public static String getDeviceName(){
        try {
            return android.os.Build.MODEL;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getOsVersion(){
        try {
            return android.os.Build.VERSION.RELEASE;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Location getLocationInfo(Context context){
        try {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED) {
                if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    return location != null ? location : getLngAndLatWithNetwork(context);
                } else {
//                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
                    Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    return location;
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Location getLngAndLatWithNetwork(Context context) {
        Location location = null;
        try {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
                location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return location;
    }

    public static String getAddress(Context context, Location location){
        if (location == null) {
            return null;
        }
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude,
                    longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String baseAddress = "";
                String endAddress = "";
                if (address != null && address.getMaxAddressLineIndex() > 2) {
                    baseAddress = address.getAddressLine(0);
                    endAddress = address.getAddressLine(1);
                }
                return baseAddress + endAddress;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "获取失败";
    }

    public static String getIPAddress(){
        return getIPAddress(true);
    }
    public static String getIPAddress(final boolean useIPv4) {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            LinkedList<InetAddress> adds = new LinkedList<>();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                // To prevent phone of xiaomi return "10.0.2.15"
                if (!ni.isUp() || ni.isLoopback())
                    continue;
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    adds.addFirst(addresses.nextElement());
                }
            }
            for (InetAddress add : adds) {
                if (!add.isLoopbackAddress()) {
                    String hostAddress = add.getHostAddress();
                    boolean isIPv4 = hostAddress.indexOf(':') < 0;
                    if (useIPv4) {
                        if (isIPv4)
                            return hostAddress;
                    } else {
                        if (!isIPv4) {
                            int index = hostAddress.indexOf('%');
                            return index < 0
                                    ? hostAddress.toUpperCase()
                                    : hostAddress.substring(0, index).toUpperCase();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isDeviceRooted(){
        String su = "su";
        String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
                "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/"};
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSimulator(Context context){
        String url = "tel:" + "123456";
        Intent intent = new Intent();
        intent.setData(Uri.parse(url));
        intent.setAction(Intent.ACTION_DIAL);
        // 是否可以处理跳转到拨号的 Intent
        boolean canResolveIntent = intent.resolveActivity(context.getPackageManager()) != null;

        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.toLowerCase().contains("vbox")
                || Build.FINGERPRINT.toLowerCase().contains("test-keys")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("MuMu")
                || Build.MODEL.contains("virtual")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.SERIAL.equalsIgnoreCase("android")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT)
                || ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName().toLowerCase().equals("android")
                || !canResolveIntent;
    }

    public static String getIMSI(Context context){
        try{
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                return tm.getSubscriberId();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getMacAddress(Context context){
        return getMacAddress(context, (String[]) null);
    }

    public static String getMacAddress(Context context, final String... excepts) {
        String macAddress = getMacAddressByWifiInfo(context);
        if (isAddressNotInExcepts(macAddress, excepts)) {
            return macAddress;
        }
        macAddress = getMacAddressByNetworkInterface();
        if (isAddressNotInExcepts(macAddress, excepts)) {
            return macAddress;
        }
        macAddress = getMacAddressByInetAddress();
        if (isAddressNotInExcepts(macAddress, excepts)) {
            return macAddress;
        }
        // root的情况       macAddress = getMacAddressByFile();
        if (isAddressNotInExcepts(macAddress, excepts)) {
            return macAddress;
        }
        return "";
    }
    public static InetAddress getInetAddress() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                // To prevent phone of xiaomi return "10.0.2.15"
                if (!ni.isUp())
                    continue;
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        if (hostAddress.indexOf(':') < 0)
                            return inetAddress;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getMacAddressByInetAddress() {
        try {
            InetAddress inetAddress = getInetAddress();
            if (inetAddress != null) {
                NetworkInterface ni = NetworkInterface.getByInetAddress(inetAddress);
                if (ni != null) {
                    byte[] macBytes = ni.getHardwareAddress();
                    if (macBytes != null && macBytes.length > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (byte b : macBytes) {
                            sb.append(String.format("%02x:", b));
                        }
                        return sb.substring(0, sb.length() - 1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }
    public static boolean isAddressNotInExcepts(final String address, final String... excepts) {
        if (excepts == null || excepts.length == 0) {
            return !"02:00:00:00:00:00".equals(address);
        }
        for (String filter : excepts) {
            if (address.equals(filter)) {
                return false;
            }
        }
        return true;
    }

    public static String getMacAddressByNetworkInterface() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (ni == null || !ni.getName().equalsIgnoreCase("wlan0"))
                    continue;
                byte[] macBytes = ni.getHardwareAddress();
                if (macBytes != null && macBytes.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (byte b : macBytes) {
                        sb.append(String.format("%02x:", b));
                    }
                    return sb.substring(0, sb.length() - 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    public static String getMacAddressByWifiInfo(Context context) {
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifi != null) {
                WifiInfo info = wifi.getConnectionInfo();
                if (info != null)
                    return info.getMacAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }


    public static String getElectricQuantity(Context context){
        BatteryManager manager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) + "";
        }
        return "";
    }

    public static String getBrand(){
        return android.os.Build.BRAND;
    }
    public static String getIMEI(Context context){
        try{
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                return tm.getDeviceId();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static long getRamTotalSize(Context context){
        long systemTotalMemorySize = 0;
        try {
            //获得ActivityManager服务->获得MemoryInfo对象
            ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            //获得系统可用内存，保存在MemoryInfo对象上
            mActivityManager.getMemoryInfo(memoryInfo);
            long memSize = memoryInfo.totalMem;
            systemTotalMemorySize = memSize;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return systemTotalMemorySize;
    }
    public static long getRomTotalSize(){
        long romTotalSize = 0;
        try {
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            romTotalSize = blockSize * totalBlocks;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return romTotalSize;
    }
    public static String getSimOperatorByMnc(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //noinspection ConstantConditions
        String operator = tm.getSimOperator();
        if (operator == null)
            return "";
        switch (operator) {
            case "46000":
            case "46002":
            case "46007":
            case "46020":
                return "中国移动";
            case "46001":
            case "46006":
            case "46009":
                return "中国联通";
            case "46003":
            case "46005":
            case "46011":
                return "中国电信";
            default:
                return operator;
        }
    }

    public static long getRomAvailableSize(){
        long romAvailableSize = 0;
        try {

            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            romAvailableSize = blockSize * availableBlocks;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return romAvailableSize;
    }
    public static long getSDTotalSize(){
        long sdTotalSize = 0;
        try {
            if (!hasSDCard()) {
//                Toast.makeText(context, "内存卡不存在", Toast.LENGTH_SHORT).show();
                return sdTotalSize;
            }
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            sdTotalSize = blockSize * totalBlocks;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sdTotalSize;
    }

    /**
     * 内存卡是否存在
     *
     * @return
     */
    public static boolean hasSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && !Environment.isExternalStorageRemovable();
    }

    public static long getSDAvailableSize(){
        long sdAvailableSize = 0;
        try {
            if (!hasSDCard()) {
                return sdAvailableSize;
//                Toast.makeText(context, "内存卡不存在", Toast.LENGTH_SHORT).show();
            }
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            sdAvailableSize = blockSize * availableBlocks;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sdAvailableSize;
    }

    public static boolean isLocationEnabled(Context context){
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }


}
