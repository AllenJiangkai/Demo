package com.coupang.common.base

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.coupang.common.utils.shortToast
import com.coupang.common.widget.dialog.LoadingDialogFgm
import com.coupang.common.widget.dialog.ext.lifecycleOwner

/**
 * @author Allen
 * @date 2020-05-27.
 * description：
 */
abstract class BaseSimpleActivity : AppCompatActivity() {

    private val loadingDialogFgm: LoadingDialogFgm by lazy {
        LoadingDialogFgm().apply {
            cancelOnTouchOutside(false)
            cancelable(false)
            lifecycleOwner(this@BaseSimpleActivity)
        }
    }
    protected var pageCreateTime = System.currentTimeMillis()

    private lateinit var activityRoot: View

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        hideSystemActionBar()
        setStatusBarTranslucent(false)
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        activityRoot = getActivityRoot()
        initView()
        registerObserver()
        initData()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("base_register_global_layout_listener_again", true)
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun initView()
    abstract fun registerObserver()
    abstract fun initData()


    fun setStatusBarTranslucent(isHideNavigation: Boolean = true) {
        if (isHideNavigation) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.or(
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                    )
                )
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN.or(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
                        .or(View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT
                window.navigationBarColor = Color.TRANSPARENT
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN.or(View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT
            }
        }

    }

    fun hideSystemActionBar() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    open fun keyboardVisibleStatus(visible: Boolean) {

    }

    fun showLoadingDialog() {
        if (!loadingDialogFgm.isShowing) {
            loadingDialogFgm.show(supportFragmentManager, "loading")
        }
    }

    fun hideLoadingDialog() {
        if (loadingDialogFgm.isShowing) {
            loadingDialogFgm.dismiss()
        }
    }


    open fun showToast(tips: String) {
        shortToast(tips)
    }

    private fun getActivityRoot(): View {
        return (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
    }

    fun registerLiveDataCommonObserver(vararg viewModels: BaseViewModel<*>) {
        viewModels.forEach { vm ->
            vm.showToast.observe(this, Observer {
                showToast(it)
            })
            vm.showHttpLoading.observe(this, Observer {
                if (it) {
                    this@BaseSimpleActivity.showLoadingDialog()
                } else {
                    this@BaseSimpleActivity.hideLoadingDialog()
                }
            })
        }
    }
}