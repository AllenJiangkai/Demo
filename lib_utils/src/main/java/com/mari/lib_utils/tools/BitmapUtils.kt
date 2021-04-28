package com.mari.lib_utils.tools

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import java.io.*

/**
 * @author Alan_Xiong
 * @desc: bitmap 工具类
 * @time 2019-08-11 23:05
 */
class BitmapUtils {
    companion object {

        private const val SHOT_FOLDER = "CouPong"
        /**
         * 将view转bitmap
         *
         * @param v
         * @return
         */
        fun loadBitmapFromView(v: View): Bitmap {
            val bmp = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
            val c = Canvas(bmp)
            c.drawColor(Color.WHITE)
            //如果不设置canvas画布为白色，则生成透明
            v.layout(
                v.left, v.top, v.right,
                v.bottom
            )
            v.draw(c)
            return bmp
        }

        /**
         * 保存截屏图片至相册
         *
         * @param context
         * @param bmp
         */
        fun saveShotBitmapToGallery(
            bmp: Bitmap,
            pathName: String
        ): Boolean { // 首先保存图片
            val appDir = File(pathName)
            if (!appDir.exists()) {
                appDir.mkdir()
            }
            val fileName = System.currentTimeMillis().toString() + ".jpg"
            val file = File(appDir, fileName)
            return try {
                val fos = FileOutputStream(file)
                //通过io流的方式来压缩保存图片
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
                fos.close()
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }

        /**
         * io 转bitmap
         *
         * @param is
         * @return
         */
        fun inputStream2Bitmap(inputStream: InputStream?): Bitmap? {
            var bitmap: Bitmap? = null
            if (inputStream == null) {
                throw RuntimeException("stream is null")
            } else {
                try {
                    val outStream = ByteArrayOutputStream()
                    val buffer = ByteArray(10240)
                    var len: Int
                    while (inputStream.read(buffer).also { len = it } != -1) {
                        outStream.write(buffer, 0, len)
                    }
                    outStream.close()
                    inputStream.close()
                    val data = outStream.toByteArray()
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return bitmap
        }

        /**
         * Drawable 转  bitmap
         *
         * @param img
         */
        fun drawable2Bitmap(img: Drawable?): Bitmap {
            var bd: BitmapDrawable? = null
            try {
                bd = img as BitmapDrawable?
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return bd!!.bitmap
        }

        /**
         * bitmap 转  Drawable
         *
         * @param bitmap
         */
        fun bitmap2Drawable(bitmap: Bitmap?): Drawable? {
            return try {
                BitmapDrawable(null, bitmap)
            } catch (e: Exception) {
                null
            }
        }

        // 将Bitmap转换成InputStream
        fun bitmap2InputStream(bm: Bitmap): InputStream {
            val baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            return ByteArrayInputStream(baos.toByteArray())
        }

        /**************** Bitmap转换相关的操作  */
        /**
         * bitmap转字节数组
         *
         * @param bitmap
         * @return 字节数组
         */
        fun bitmap2Byte(bitmap: Bitmap): ByteArray? {
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos) //将图片以PNG的格式指定的字节流
            return bos.toByteArray()
        }
    }
}