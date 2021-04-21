package com.mari.lib_utils.tools.front

import com.mari.lib_utils.tools.BigDecimalUtils

/**
 * 粉丝数量等
 */
object FansUtils {

    /**
     * 获取数量文案
     */
    fun getCountString(count: Int): String {
        return if (count >= 10000) {
            BigDecimalUtils.div(count.toString(), "10000", 1) + "w"
        } else {
            count.toString()
        }
    }

    /**
     * @param front 前缀文案
     * 获取数量文案
     */
    fun getFrontCountString(front: String, count: Int): String {
        return if (count >= 10000) {
            StringBuffer(front).append(BigDecimalUtils.div(count.toString(), "10000", 1) + "w")
                .toString()
        } else {
            StringBuffer(front).append(count)
                .toString()
        }
    }

    /**
     * @param before 后缀文案
     * 获取数量文案
     */
    fun getBeforeCountString(before: String, count: Int): String {
        return if (count >= 10000) {
            StringBuffer(BigDecimalUtils.div(count.toString(), "10000", 1) + "w").append(before)
                .toString()
        } else {
            StringBuffer(count).append(before)
                .toString()
        }
    }
}