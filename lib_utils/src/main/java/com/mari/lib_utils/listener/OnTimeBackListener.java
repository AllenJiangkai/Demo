package com.mari.lib_utils.listener;

/**
  * @author Alan_Xiong
  * @desc:
  * @time: 2019/7/26 13:40
  */
public interface OnTimeBackListener {

    void onTick(int hour, int minute, int second);

    void onFinish();
}
