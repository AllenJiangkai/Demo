package com.mari.lib_utils.tools.time

import android.os.CountDownTimer
import com.mari.lib_utils.listener.OnTimeBackListener

/**
 * @author Alan_Xiong
 * @desc:  倒计时工具
 * @time: 2019/7/26 11:42
 */
class CommonCountTimer
/**
 * @param millisInFuture    The number of millis in the future from the call
 * to [.start] until the countdown is done and [.onFinish]
 * is called.
 * @param countDownInterval The interval along the way to receive
 * [.onTick] callbacks.
 */(
    millisInFuture: Long,
    countDownInterval: Long,
    private val mListener: OnTimeBackListener?
) : CountDownTimer(millisInFuture, countDownInterval) {
    override fun onTick(millisUntilFinished: Long) {
        var millIsUntilFinished = millisUntilFinished
        millIsUntilFinished /= 1000
        val hours = (millisUntilFinished / (60 * 60)).toInt()
        val leftSeconds = (millisUntilFinished % (60 * 60)).toInt()
        val minutes = leftSeconds / 60
        val seconds = leftSeconds % 60
        mListener?.onTick(hours, minutes, seconds)
    }

    override fun onFinish() {
        mListener!!.onFinish()
    }

}