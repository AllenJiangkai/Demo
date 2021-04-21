package com.mari.lib_utils.tools.time

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * package: com.easytools.tools.DateUtils
 * author: gyc
 * description:与日期操作相关的工具类
 * time: create at 2017/1/15 15:41
 */
object DateUtils {
    /**
     * 英文简写如：2010
     */
    var FORMAT_Y = "yyyy"
    /**
     * 英文简写如：12:01
     */
    var FORMAT_HM = "HH:mm"
    /**
     * 英文简写如：1-12 12:01
     */
    var FORMAT_MDHM = "MM-dd HH:mm"
    /**
     * 英文简写（默认）如：2010-12-01
     */
    var FORMAT_YMD = "yyyy-MM-dd"
    /**
     * 英文全称  如：2010-12-01 23:15
     */
    var FORMAT_YMDHM = "yyyy-MM-dd HH:mm"
    /**
     * 获得默认的日期格式：yyyy-MM-dd HH:mm:ss
     * @return  默认的格式
     */
    /**
     * 英文全称  如：2010-12-01 23:15:06
     */
    var datePattern = "yyyy-MM-dd HH:mm:ss"
    /**
     * 精确到毫秒的完整时间    如：yyyy-MM-dd HH:mm:ss.S
     */
    var FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.S"
    /**
     * 精确到毫秒的完整时间    如：yyyy-MM-dd HH:mm:ss.S
     */
    var FORMAT_FULL_SN = "yyyyMMddHHmmssS"
    /**
     * 中文简写  如：2010年12月01日
     */
    var FORMAT_YMD_CN = "yyyy年MM月dd日"
    /**
     * 中文简写  如：2010年12月01日 12时
     */
    var FORMAT_YMDH_CN = "yyyy年MM月dd日 HH时"
    /**
     * 中文简写  如：2010年12月01日 12时12分
     */
    var FORMAT_YMDHM_CN = "yyyy年MM月dd日 HH时mm分"
    /**
     * 中文全称  如：2010年12月01日 23时15分06秒
     */
    var FORMAT_YMDHMS_CN = "yyyy年MM月dd日  HH时mm分ss秒"
    /**
     * 精确到毫秒的完整中文时间
     */
    var FORMAT_FULL_CN = "yyyy年MM月dd日  HH时mm分ss秒SSS毫秒"
    var calendar: Calendar? = null
    private const val FORMAT = "yyyy-MM-dd HH:mm:ss"
    /**
     * 将字符串按照给定格式做解析，如format为空则使用默认格式："yyyy-MM-dd HH:mm:ss"
     *
     * @param str    待解析的字符串
     * @param format 给定的解析格式，若为null则使用"yyyy-MM-dd HH:mm:ss"解析
     * @return Date对象
     */
    /**
     * 将字符串按照给定格式做解析，使用默认格式："yyyy-MM-dd HH:mm:ss"
     *
     * @param str 待解析的字符串
     * @return Date对象
     */
    @JvmOverloads
    fun str2Date(str: String?, format: String?): Date? {
        var dataFormat = format
        if (str == null || str.isEmpty()) {
            return null
        }
        if (dataFormat == null || dataFormat.length == 0) {
            dataFormat = FORMAT
        }
        var date: Date? = null
        try {
            val sdf = SimpleDateFormat(dataFormat)
            date = sdf.parse(str)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }
    /**
     * 将字符串按照给定格式做解析，如format为空则使用默认格式："yyyy-MM-dd HH:mm:ss"
     *
     * @param str    待解析的字符串
     * @param format 给定的解析格式，若为null则使用"yyyy-MM-dd HH:mm:ss"解析
     * @return Calendar对象
     */
    /**
     * 将字符串按照给定格式做解析，使用默认格式："yyyy-MM-dd HH:mm:ss"
     *
     * @param str 待解析的字符串
     * @return Calendar对象
     */
    @JvmOverloads
    fun str2Calendar(str: String?, format: String? = null): Calendar? {
        val date = str2Date(str, format)
            ?: return null
        val c = Calendar.getInstance()
        c.time = date
        return c
    }
    /**
     * Calendar转为字符串，
     *
     * @param c
     * @param format 使用的格式，为null则使用默认格式：yyyy-MM-dd HH:mm:ss
     * @return
     */
    /**
     * Calendar转为字符串，使用默认格式：yyyy-MM-dd HH:mm:ss
     *
     * @param c
     * @return
     */
    @JvmOverloads
    fun calendar2Str(c: Calendar?, format: String? = null): String? {
        return if (c == null) {
            null
        } else date2Str(c.time, format)
    }
    /**
     * Date转为字符串
     *
     * @param d
     * @param format 格式，为null则使用默认格式：yyyy-MM-dd HH:mm:ss
     * @return
     */
    /**
     * Date转为字符串，使用默认格式：yyyy-MM-dd HH:mm:ss
     *
     * @param d
     * @return
     */
    @JvmOverloads
    fun date2Str(
        d: Date?,
        format: String? = null
    ): String? { // yyyy-MM-dd HH:mm:ss
        var format = format
        if (d == null) {
            return null
        }
        if (format == null || format.length == 0) {
            format = FORMAT
        }
        val sdf = SimpleDateFormat(format)
        return sdf.format(d)
    }

    /**
     * 返回当前的日期和时间
     *
     * @return 如：2019-01-04 13:51:27
     */
    val curDateTime: String?
        get() {
            val c = Calendar.getInstance()
            return calendar2Str(
                c,
                datePattern
            )
        }

    /**
     * 返回当前的日期
     *
     * @return 如：2019-01-04
     */
    val curDate: String?
        get() {
            val c = Calendar.getInstance()
            return calendar2Str(
                c,
                FORMAT_YMD
            )
        }

    /**
     * 将给定时间以"yyyy-MM-dd"的格式进行转换
     * @param time
     * @return
     */
    fun getDateByTime(time: Long): String {
        val sdf =
            SimpleDateFormat(FORMAT_YMD,Locale.CANADA)
        val date = Date(time)
        return sdf.format(date)
    }

    /**
     * 通过给定格式，将指定时间进行转换
     * @param time
     * @param format
     * @return
     */
    fun getDateByFormat(time: Long, format: String): String {
        val sdf = SimpleDateFormat(format, Locale.CANADA)
        val date = Date(time)
        return sdf.format(date)
    }

    /**
     * 获得当前日期的字符串格式
     * @param format    格式化的类型
     * @return  返回格式化之后的事件
     */
    fun getCurDate(format: String?): String? {
        val c = Calendar.getInstance()
        return calendar2Str(c, format)
    }

    /**
     * 返回当前时间字符串
     *
     * @param time 当前的时间
     * @return  格式到秒
     */
    fun getMillon(time: Long): String {
        return SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(time)
    }

    /**
     * 返回给定天的字符串
     *
     * @param time  当前的时间
     * @return  当前的天
     */
    fun getDay(time: Long): String {
        return SimpleDateFormat("yyyy-MM-dd").format(time)
    }

    /**
     * 返回给定时间的毫秒字符串
     *
     * @param time 时间
     * @return 返回一个毫秒
     */
// 格式到毫秒
    fun getSMillon(time: Long): String {
        return SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(time)
    }

    /**
     * 在日期上增加数个整月
     * @param date 日期
     * @param n 要增加的月数
     * @return   增加数个整月
     */
    fun addMonth(date: Date, n: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.MONTH, n)
        return cal.time
    }

    /**
     * 在日期上增加天数
     * @param date 日期
     * @param n 要增加的天数
     * @return   增加之后的天数
     */
    fun addDay(date: Date, n: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DATE, n)
        return cal.time
    }

    /**
     * 获取距现在某一小时的时刻
     *
     * @param format 格式化时间的格式
     * @param h 距现在的小时 例如：h=-1为上一个小时，h=1为下一个小时
     * @return  获取距现在某一小时的时刻
     */
    fun getNextHour(format: String?, h: Int): String {
        val sdf = SimpleDateFormat(format)
        val date = Date()
        date.time = date.time + h * 60 * 60 * 1000
        return sdf.format(date)
    }

    /**
     * 获取时间戳
     * @return 获取时间戳
     */
    val timeStamp: String
        get() {
            val df =
                SimpleDateFormat(FORMAT_FULL)
            val calendar = Calendar.getInstance()
            return df.format(calendar.time)
        }

    /**
     * 功能描述：返回月
     *
     * @param date Date 日期
     * @return 返回月份
     */
    fun getMonth(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.MONTH) + 1
    }

    /**
     * 功能描述：返回日
     *
     * @param date Date 日期
     * @return 返回日份
     */
    fun getDay(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    /**
     * 功能描述：返回小时
     *
     * @param date 日期
     * @return 返回小时
     */
    fun getHour(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    /**
     * 功能描述：返回分
     *
     * @param date 日期
     * @return 返回分钟
     */
    fun getMinute(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.MINUTE)
    }

    /**
     * 返回秒
     *
     * @param date Date 日期
     * @return 秒数
     */
    fun getSecond(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.SECOND)
    }

    /**
     * 功能描述：返回毫秒
     *
     * @param date 日期
     * @return 返回毫
     */
    fun getMillis(date: Date): Long {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.timeInMillis
    }

    /**
     * 按默认格式的字符串距离今天的天数
     *
     * @param date 日期字符串
     * @return 按默认格式的字符串距离今天的天数
     */
    fun countDays(date: String): Int {
        val t = Calendar.getInstance().time.time
        val c = Calendar.getInstance()
        c.time = parse(date)
        val t1 = c.time.time
        return (t / 1000 - t1 / 1000).toInt() / 3600 / 24
    }

    /**
     * 按用户格式字符串距离今天的天数
     *
     * @param date 日期字符串
     * @param format 日期格式
     * @return  按用户格式字符串距离今天的天数
     */
    fun countDays(date: String, format: String): Int {
        val t = Calendar.getInstance().time.time
        val c = Calendar.getInstance()
        c.time = parse(date, format)
        val t1 = c.time.time
        return (t / 1000 - t1 / 1000).toInt() / 3600 / 24
    }

    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return  提取字符串日期
     */
    @JvmOverloads
    fun parse(
        strDate: String,
        pattern: String = datePattern
    ): Date? {
        val df = SimpleDateFormat(pattern,Locale.CANADA)
        return try {
            df.parse(strDate)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }
}