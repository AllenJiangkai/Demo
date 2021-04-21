package com.coupang.common.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NetworkReceiver : BroadcastReceiver() {
    private lateinit var mINetListener: INetListener

    override fun onReceive(context: Context?, intent: Intent?) {
        var state = context?.let { NetUtil().getNetType(it) }
        when(state){
            NetUtil.NetType.NONE ->mINetListener?.changeNoNet()
            NetUtil.NetType.WIFI ->mINetListener?.changeWifi()
            NetUtil.NetType.NET_2G, NetUtil.NetType.NET_3G, NetUtil.NetType.NET_4G, NetUtil.NetType.NET_5G ->mINetListener?.changeMobile()
        }
    }

    fun setListener(iNetListener: INetListener) {
        this.mINetListener = iNetListener
    }

    interface INetListener {
        fun changeNoNet()
        fun changeMobile()
        fun changeWifi()
    }
}