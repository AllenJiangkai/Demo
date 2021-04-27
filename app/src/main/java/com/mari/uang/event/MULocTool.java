package com.mari.uang.event;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.coupang.common.utils.ContextUtils;
import com.coupang.common.utils.spf.SpConfig;
import com.mari.uang.util.PermissionUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MULocTool {
    
    private static MULocTool locTool;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location locationInfo;
    
    public static MULocTool getInstance(){
        if (locTool == null){
            locTool = new MULocTool();
        }
        return locTool;
    }
    
    
    private MULocTool(){
        locationManager = (LocationManager) ContextUtils.getApplication().getSystemService(Context.LOCATION_SERVICE);
    }


    public void startLocation(MULocCall locCall){

        try {
            if (PermissionUtil.INSTANCE.checkPermission(ContextUtils.getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)){
                return;
            }

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    locationInfo = location;
                    SpConfig.INSTANCE.setLocation_latitude(String.valueOf(location.getLatitude()));
                    SpConfig.INSTANCE.setLocation_longitude(String.valueOf(location.getLongitude()));

                    location(location, locCall);
                    stopLocation();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            reqLoc(locationListener);
        }catch (Exception e){

        }


    }
    
    public void startLocation(LocationListener locationListener){
        try{
            if (PermissionUtil.INSTANCE.checkPermission(ContextUtils.getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)){
                return;
            }
            this.locationListener = locationListener;
            reqLoc(locationListener);
        }catch (Exception e){

        }

    }

    /**********
     * 解析定位信息
     */
    public static void location(Location location, MULocCall callBack) {
        Geocoder gc = new Geocoder(ContextUtils.getApplication(), Locale.getDefault());
        Log.e("onLocationChanged", "onLocationChanged: " + location.getLatitude() + "   " + location.getLongitude());
        List<Address> locationList = null;
        try {
            locationList = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            callBack.error(e.getMessage());
            e.printStackTrace();
            Log.e("onLocationChanged", "locationList:error "+ e.getMessage());
        }
        JSONObject addressJson = new JSONObject();
        String addressDetail = "";
        if(locationList != null && locationList.size() > 0) {

            Log.e("onLocationChanged", "locationList: " + locationList.size());
            Address address = locationList.get(0);//得到Address实例
            if(address != null){
                addressJson.put("country_name", address.getCountryName());//国家
                addressJson.put("country_code", address.getCountryCode());//国家Code
                addressJson.put("admin_area", address.getAdminArea());//省
                addressJson.put("locality", address.getLocality());//市
                addressJson.put("sub_admin_area", address.getSubAdminArea());//区
                addressJson.put("feature_name", address.getFeatureName());//街道

                for(int i = 0; address.getAddressLine(i) != null; i++){
                    addressJson.put("address" + i, address.getAddressLine(i));
                }
                addressDetail = address.getAddressLine(0) + "";
            }
        } else {
            callBack.error("Location info is null");
        }
        if (callBack != null) {
            callBack.location(location, addressDetail, addressJson.toJSONString());
        }
    }


    @SuppressLint("MissingPermission")
    public void stopLocation(){
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
    
    
    @SuppressLint("MissingPermission")
    private void reqLoc(LocationListener locationListener){
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1000, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 1000, locationListener);
    }
    
}
