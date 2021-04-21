package com.mari.lib_utils.tools

import android.view.View
import android.view.animation.*

/**
  * @author Alan_Xiong
  *
  * @desc: 动画相关的工具类
  * @time 2020/5/28 11:11 AM
  */
class XAnimationUtils private constructor() {


    companion object {

        /**
         * 设置View的渐显动画
         *
         * @param view
         * @param duration
         */
        fun setShowAnimation(view: View?, duration: Int) {
            if (null == view || duration < 0) {
                return
            }
            if (null != SHOW_ANIM) {
                SHOW_ANIM!!.cancel()
            }
            SHOW_ANIM = AlphaAnimation(0.0f, 1.0f)
            SHOW_ANIM!!.duration = duration.toLong()
            SHOW_ANIM!!.fillAfter = true
            SHOW_ANIM!!.setAnimationListener(object :
                Animation.AnimationListener {
                override fun onAnimationStart(arg0: Animation) {
                    view.visibility = View.VISIBLE
                }

                override fun onAnimationRepeat(arg0: Animation) {}
                override fun onAnimationEnd(arg0: Animation) {}
            })
            view.startAnimation(SHOW_ANIM)
        }

        /**
         * 默认动画持续时间
         */
        const val DEFAULT_ANIMATION_DURATION: Long = 400

        /**
         * 获取一个旋转的补间动画（补间动画：该类的Animation可以完成控件的旋转、移动、伸缩、淡入淡出等特效）
         *
         * @param fromDegrees       开始角度
         * @param toDegrees         结束角度
         * @param pivotXType        旋转中心点X轴坐标相对类型
         * @param pivotXValue       旋转中心点X轴坐标
         * @param pivotYType        旋转中心点Y轴坐标相对类型
         * @param pivotYValue       旋转中心点Y轴坐标
         * @param durationMillis    持续时间
         * @param animationListener 动画监听器
         * @return 一个旋转动画
         */
        private fun getRotateAnimation(
            fromDegrees: Float,
            toDegrees: Float,
            pivotXType: Int,
            pivotXValue: Float,
            pivotYType: Int,
            pivotYValue: Float,
            durationMillis: Long,
            animationListener: Animation.AnimationListener?
        ): RotateAnimation {
            val rotateAnimation = RotateAnimation(
                fromDegrees,
                toDegrees, pivotXType, pivotXValue, pivotYType, pivotYValue
            )
            rotateAnimation.duration = durationMillis
            if (animationListener != null) {
                rotateAnimation.setAnimationListener(animationListener)
            }
            return rotateAnimation
        }

        /**
         * 获取一个根据视图自身中心点旋转的补间动画
         *
         * @param durationMillis    动画持续时间
         * @param animationListener 动画监听器
         * @return 一个根据中心点旋转的动画
         */
        private fun getRotateAnimationByCenter(
            durationMillis: Long,
            animationListener: Animation.AnimationListener?
        ): RotateAnimation {
            return getRotateAnimation(
                0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, durationMillis,
                animationListener
            )
        }

        /**
         * 获取一个根据中心点旋转的动画
         *
         * @param duration 动画持续时间
         * @return 一个根据中心点旋转的动画
         */
        fun getRotateAnimationByCenter(duration: Long): RotateAnimation {
            return getRotateAnimationByCenter(
                duration,
                null
            )
        }

        /**
         * 获取一个根据视图自身中心点旋转的动画
         *
         * @param animationListener 动画监听器
         * @return 一个根据中心点旋转的动画
         */
        fun getRotateAnimationByCenter(animationListener: Animation.AnimationListener?): RotateAnimation {
            return getRotateAnimationByCenter(
                DEFAULT_ANIMATION_DURATION,
                animationListener
            )
        }

        /**
         * 获取一个根据中心点旋转的动画
         *
         * @return 一个根据中心点旋转的动画，默认持续时间为DEFAULT_ANIMATION_DURATION
         */
        val rotateAnimationByCenter: RotateAnimation
            get() = getRotateAnimationByCenter(
                DEFAULT_ANIMATION_DURATION,
                null
            )

        /**
         * 获取一个透明度渐变动画
         *
         * @param fromAlpha         开始时的透明度
         * @param toAlpha           结束时的透明度都
         * @param durationMillis    持续时间
         * @param animationListener 动画监听器
         * @return 一个透明度渐变动画
         */
        fun getAlphaAnimation(
            fromAlpha: Float,
            toAlpha: Float,
            durationMillis: Long,
            animationListener: Animation.AnimationListener?
        ): AlphaAnimation {
            val alphaAnimation = AlphaAnimation(fromAlpha, toAlpha)
            alphaAnimation.duration = durationMillis
            if (animationListener != null) {
                alphaAnimation.setAnimationListener(animationListener)
            }
            return alphaAnimation
        }

        /**
         * 获取一个透明度渐变动画
         *
         * @param fromAlpha      开始时的透明度
         * @param toAlpha        结束时的透明度都
         * @param durationMillis 持续时间
         * @return 一个透明度渐变动画
         */
        fun getAlphaAnimation(
            fromAlpha: Float,
            toAlpha: Float,
            durationMillis: Long
        ): AlphaAnimation {
            return getAlphaAnimation(
                fromAlpha,
                toAlpha,
                durationMillis,
                null
            )
        }

        /**
         * 获取一个透明度渐变动画
         *
         * @param fromAlpha         开始时的透明度
         * @param toAlpha           结束时的透明度都
         * @param animationListener 动画监听器
         * @return 一个透明度渐变动画，默认持续时间为DEFAULT_ANIMATION_DURATION
         */
        fun getAlphaAnimation(
            fromAlpha: Float,
            toAlpha: Float,
            animationListener: Animation.AnimationListener?
        ): AlphaAnimation {
            return getAlphaAnimation(
                fromAlpha,
                toAlpha,
                DEFAULT_ANIMATION_DURATION,
                animationListener
            )
        }

        /**
         * 获取一个透明度渐变动画
         *
         * @param fromAlpha 开始时的透明度
         * @param toAlpha   结束时的透明度都
         * @return 一个透明度渐变动画，默认持续时间为DEFAULT_ANIMATION_DURATION
         */
        fun getAlphaAnimation(fromAlpha: Float, toAlpha: Float): AlphaAnimation {
            return getAlphaAnimation(
                fromAlpha,
                toAlpha,
                DEFAULT_ANIMATION_DURATION,
                null
            )
        }

        /**
         * 获取一个由完全显示变为不可见的透明度渐变动画
         *
         * @param durationMillis    持续时间
         * @param animationListener 动画监听器
         * @return 一个由完全显示变为不可见的透明度渐变动画
         */
        fun getHiddenAlphaAnimation(
            durationMillis: Long,
            animationListener: Animation.AnimationListener?
        ): AlphaAnimation {
            return getAlphaAnimation(
                1.0f,
                0.0f,
                durationMillis,
                animationListener
            )
        }

        /**
         * 获取一个由完全显示变为不可见的透明度渐变动画
         *
         * @param durationMillis 持续时间
         * @return 一个由完全显示变为不可见的透明度渐变动画
         */
        fun getHiddenAlphaAnimation(durationMillis: Long): AlphaAnimation {
            return getHiddenAlphaAnimation(
                durationMillis,
                null
            )
        }

        /**
         * 获取一个由完全显示变为不可见的透明度渐变动画
         *
         * @param animationListener 动画监听器
         * @return 一个由完全显示变为不可见的透明度渐变动画，默认持续时间为DEFAULT_ANIMATION_DURATION
         */
        fun getHiddenAlphaAnimation(animationListener: Animation.AnimationListener?): AlphaAnimation {
            return getHiddenAlphaAnimation(
                DEFAULT_ANIMATION_DURATION,
                animationListener
            )
        }

        /**
         * 获取一个由完全显示变为不可见的透明度渐变动画
         *
         * @return 一个由完全显示变为不可见的透明度渐变动画，默认持续时间为DEFAULT_ANIMATION_DURATION
         */
        val hiddenAlphaAnimation: AlphaAnimation
            get() = getHiddenAlphaAnimation(
                DEFAULT_ANIMATION_DURATION,
                null
            )

        /**
         * 获取一个由不可见变为完全显示的透明度渐变动画
         *
         * @param durationMillis    持续时间
         * @param animationListener 动画监听器
         * @return 一个由不可见变为完全显示的透明度渐变动画
         */
        fun getShowAlphaAnimation(
            durationMillis: Long,
            animationListener: Animation.AnimationListener?
        ): AlphaAnimation {
            return getAlphaAnimation(
                0.0f,
                1.0f,
                durationMillis,
                animationListener
            )
        }

        /**
         * 获取一个由不可见变为完全显示的透明度渐变动画
         *
         * @param durationMillis 持续时间
         * @return 一个由不可见变为完全显示的透明度渐变动画
         */
        fun getShowAlphaAnimation(durationMillis: Long): AlphaAnimation {
            return getAlphaAnimation(
                0.0f,
                1.0f,
                durationMillis,
                null
            )
        }

        /**
         * 获取一个由不可见变为完全显示的透明度渐变动画
         *
         * @param animationListener 动画监听器
         * @return 一个由不可见变为完全显示的透明度渐变动画，默认持续时间为DEFAULT_ANIMATION_DURATION
         */
        fun getShowAlphaAnimation(animationListener: Animation.AnimationListener?): AlphaAnimation {
            return getAlphaAnimation(
                0.0f,
                1.0f,
                DEFAULT_ANIMATION_DURATION,
                animationListener
            )
        }

        /**
         * 获取一个由不可见变为完全显示的透明度渐变动画
         *
         * @return 一个由不可见变为完全显示的透明度渐变动画，默认持续时间为DEFAULT_ANIMATION_DURATION
         */
        val showAlphaAnimation: AlphaAnimation
            get() = getAlphaAnimation(
                0.0f,
                1.0f,
                DEFAULT_ANIMATION_DURATION,
                null
            )

        /**
         * 获取一个缩小动画
         *
         * @param durationMillis    时间
         * @param animationListener 监听
         * @return 一个缩小动画
         */
        fun getLessenScaleAnimation(
            durationMillis: Long,
            animationListener: Animation.AnimationListener?
        ): ScaleAnimation {
            val scaleAnimation = ScaleAnimation(
                1.0f, 0.0f, 1.0f,
                0.0f, ScaleAnimation.RELATIVE_TO_SELF.toFloat(),
                ScaleAnimation.RELATIVE_TO_SELF.toFloat()
            )
            scaleAnimation.duration = durationMillis
            scaleAnimation.setAnimationListener(animationListener)
            return scaleAnimation
        }

        /**
         * 获取一个缩小动画
         *
         * @param durationMillis 时间
         * @return 一个缩小动画
         */
        fun getLessenScaleAnimation(durationMillis: Long): ScaleAnimation {
            return getLessenScaleAnimation(
                durationMillis,
                null
            )
        }

        /**
         * 获取一个缩小动画，使用默认执行时间
         *
         * @param animationListener 监听
         * @return 返回一个缩小的动画
         */
        fun getLessenScaleAnimation(animationListener: Animation.AnimationListener?): ScaleAnimation {
            return getLessenScaleAnimation(
                DEFAULT_ANIMATION_DURATION,
                animationListener
            )
        }

        /**
         * 获取一个放大动画
         *
         * @param durationMillis    时间
         * @param animationListener 监听
         * @return 返回一个放大的效果
         */
        private fun getAmplificationAnimation(
            durationMillis: Long,
            animationListener: Animation.AnimationListener?
        ): ScaleAnimation {
            val scaleAnimation = ScaleAnimation(
                0.0f, 1.0f, 0.0f,
                1.0f, ScaleAnimation.RELATIVE_TO_SELF.toFloat(),
                ScaleAnimation.RELATIVE_TO_SELF.toFloat()
            )
            scaleAnimation.duration = durationMillis
            scaleAnimation.setAnimationListener(animationListener)
            return scaleAnimation
        }

        /**
         * 获取一个放大动画
         *
         * @param durationMillis 时间
         * @return 返回一个放大的效果
         */
        fun getAmplificationAnimation(durationMillis: Long): ScaleAnimation {
            return getAmplificationAnimation(
                durationMillis,
                null
            )
        }

        /**
         * 获取一个放大动画，使用默认动画时间
         *
         * @param animationListener 监听
         * @return 返回一个放大的效果
         */
        fun getAmplificationAnimation(animationListener: Animation.AnimationListener?): ScaleAnimation {
            return getAmplificationAnimation(
                DEFAULT_ANIMATION_DURATION,
                animationListener
            )
        }

        var HIDE_ANIM //渐隐动画
                : AlphaAnimation? = null
        var SHOW_ANIM //渐显动画
                : AlphaAnimation? = null

        /**
         * 设置View的渐隐动画
         *
         * @param view
         * @param duration
         */
        fun setHideAnimation(view: View?, duration: Int) {
            if (null == view || duration < 0) {
                return
            }
            if (null != HIDE_ANIM) {
                HIDE_ANIM!!.cancel()
            }
            // 监听动画结束的操作
            HIDE_ANIM = AlphaAnimation(1.0f, 0.0f)
            HIDE_ANIM!!.duration = duration.toLong()
            HIDE_ANIM!!.fillAfter = true
            HIDE_ANIM!!.setAnimationListener(object :
                Animation.AnimationListener {
                override fun onAnimationStart(arg0: Animation) {}
                override fun onAnimationRepeat(arg0: Animation) {}
                override fun onAnimationEnd(arg0: Animation) {
                    view.visibility = View.GONE
                }
            })
            view.startAnimation(HIDE_ANIM)
        }

        /**
         * View设置VISIABLE后的底部弹出动画
         * @param view
         */
        fun slidInBottom(view: View) {
            val showAnim = TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f
            )
            showAnim.duration = 200
            showAnim.interpolator = DecelerateInterpolator()
            view.startAnimation(showAnim)
        }

        /**
         * View设置GONE后的底部滑出动画
         * @param view
         */
        fun slidOutBottom(view: View) {
            val goneAnim = TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f
            )
            goneAnim.duration = 200
            goneAnim.interpolator = DecelerateInterpolator()
            view.startAnimation(goneAnim)
        }
    }

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }
}