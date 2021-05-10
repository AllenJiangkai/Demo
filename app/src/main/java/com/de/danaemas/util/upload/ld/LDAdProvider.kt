package com.de.danaemas.util.upload.ld

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import java.io.IOException
import java.util.concurrent.LinkedBlockingQueue

/**
 *
 * @ProjectName:    Business
 * @Package:        com.mari.uang.util.upload.ld
 * @ClassName:      LDAdProvider
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/4/21 4:04 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/4/21 4:04 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
object LDAdProvider {
    @Throws(Exception::class)
    fun getAdvertisingIdInfo(context: Context): AdInfo? {
        check(Looper.myLooper() != Looper.getMainLooper()) { "Cannot be called from the main thread" }
        try {
            val pm = context.packageManager
            pm.getPackageInfo("com.android.vending", 0)
        } catch (var11: Exception) {
            throw var11
        }
        val connection = AdvertisingConnection()
        val intent = Intent("com.google.android.gms.ads.identifier.service.START")
        intent.setPackage("com.google.android.gms")
        try {
            if (context.bindService(
                    intent,
                    connection,
                    Context.BIND_AUTO_CREATE
                )
            ) {
                val var5: AdInfo
                var5 = try {
                    val adInterface =
                        AdvertisingInterface(connection.binder!!)
                    val adInfo =
                        AdInfo(adInterface.id, adInterface.isLimitAdTrackingEnabled(true))
                    adInfo
                } catch (var12: Exception) {
                    throw IOException("Google Play connection failed")
                } finally {
                    context.unbindService(connection)
                }
                return var5
            }
        } catch (var14: Exception) {
        }
        throw IOException("Google Play connection failed")
    }

    private class AdvertisingInterface internal constructor(private val binder: IBinder) :
        IInterface {
        override fun asBinder(): IBinder {
            return binder
        }

        @get:Throws(RemoteException::class)
        val id: String?
            get() {
                val data = Parcel.obtain()
                val reply = Parcel.obtain()
                val id: String?
                id = try {
                    data.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService")
                    binder.transact(1, data, reply, 0)
                    reply.readException()
                    reply.readString()
                } finally {
                    reply.recycle()
                    data.recycle()
                }
                return id
            }

        @Throws(RemoteException::class)
        fun isLimitAdTrackingEnabled(paramBoolean: Boolean): Boolean {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()
            val limitAdTracking: Boolean
            limitAdTracking = try {
                data.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService")
                data.writeInt(if (paramBoolean) 1 else 0)
                binder.transact(2, data, reply, 0)
                reply.readException()
                0 != reply.readInt()
            } finally {
                reply.recycle()
                data.recycle()
            }
            return limitAdTracking
        }

    }

    private class AdvertisingConnection : ServiceConnection {
        var retrieved = false
        private val queue: LinkedBlockingQueue<IBinder?>
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            try {
                queue.put(service)
            } catch (var4: Exception) {
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {}

        @get:Throws(InterruptedException::class)
        val binder: IBinder?
            get() = if (retrieved) {
                throw IllegalStateException()
            } else {
                retrieved = true
                queue.take()
            }

        init {
            queue = LinkedBlockingQueue<IBinder?>(1)
        }
    }

    class AdInfo internal constructor(
        val id: String?,
        val isLimitAdTrackingEnabled: Boolean
    )
}