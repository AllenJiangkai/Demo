//package com.mari.uang.module.web
//
//import android.Manifest
//import android.annotation.TargetApi
//import android.app.Dialog
//import android.content.ActivityNotFoundException
//import android.content.Context
//import android.content.Intent
//import android.graphics.Bitmap
//import android.net.Uri
//import android.net.http.SslError
//import android.os.Build
//import android.os.Environment
//import android.provider.MediaStore
//import android.text.TextUtils
//import android.view.View
//import android.view.ViewGroup
//import android.webkit.*
//import android.widget.LinearLayout
//import androidx.annotation.RequiresApi
//import androidx.core.content.ContextCompat
//import androidx.core.content.FileProvider
//import com.alibaba.fastjson.JSONObject
//import com.coupang.common.base.BaseSimpleActivity
//import com.coupang.common.user.UserManager.sessionid
//import com.coupang.common.user.UserManager.uid
//import com.coupang.common.utils.ContextUtils
//import com.coupang.common.utils.strings
//import com.just.agentweb.AgentWeb
//import com.just.agentweb.WebChromeClient
//import com.just.agentweb.WebViewClient
//import com.mari.uang.BuildConfig
//import com.mari.uang.R
//import com.mari.uang.config.ConstantConfig.TITLE_KEY
//import com.mari.uang.config.ConstantConfig.WEB_URL_KEY
//import com.mari.uang.module.web.MUHttpConstants.H5_SERVICE_URL
//import com.mari.uang.util.MyDevTool
//import com.mari.uang.util.PermissionUtil
//import com.yanzhenjie.permission.Action
//import kotlinx.android.synthetic.main.activity_web.*
//import java.io.File
//import java.lang.ref.WeakReference
//import java.net.URLEncoder
//import java.util.*
//
//class WebActivity :BaseSimpleActivity(){
//
//    private val GET_IMAGE_CODE = 10001
//
//    private var mAgentWeb: AgentWeb? = null
//
//    var mActionArray = arrayOf("mailto:", "geo:", "tel:", "sms:")
//
//    private var backMsg: String? = null
//    private var rightCallPhone: String? = null
//
//
//    private var mTitleStr: String? = null
//    private var mUrl: String? = null
//    private var mUploadMessage: ValueCallback<Uri>? = null
//    private var mUploadCallbackAboveL: ValueCallback<Array<Uri>>? = null
//    private var imageUri: Uri? = null
//    private var registCallBack: RegisterCallBack? = null
//
//    private val filePermissions = arrayOf(
//        Manifest.permission.CAMERA,
//        Manifest.permission.READ_EXTERNAL_STORAGE,
//        Manifest.permission.WRITE_EXTERNAL_STORAGE
//    )
//    private val callPhonePermissions = arrayOf(Manifest.permission.CALL_PHONE)
//
//
//    override fun getLayoutId(): Int {
//        return R.layout.activity_web
//    }
//
//    override fun initView() {
//        initViewData()
//    }
//
//    override fun registerObserver() {
//    }
//
//    override fun initData() {
//    }
//
//    fun initViewData() {
//        //通过XML配置参数 nav_graph.xml  取参数
////        url = WebFragmentArgs.fromBundle(getArguments()).getUrl();
//        getParams()
//        mAgentWeb = AgentWeb.with(this)
//            .setAgentWebParent(
//                parentView,
//                LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT
//                )
//            )
//            .useDefaultIndicator(ContextCompat.getColor(this, R.color.transparent))
//            .setWebViewClient(webViewClient)
//            .additionalHttpHeader(mUrl, getHeader())
//            .setWebChromeClient(mWebChromeClient)
//            .createAgentWeb()
//            .ready()
//            .go(mUrl)
//        //        mAgentWeb.getWebCreator().getWebView().loadUrl(mUrl,WebUtil.getHeader());
//        mAgentWeb?.apply {
//            jsInterfaceHolder
//                .addJavaObject("nativeMethod", AndroidFragmentInterface(this, this))
//            initSetting(this.webCreator.webView, this@WebActivity)
//        }
//
//
//        title_bar.apply {
//            setTitle(mTitleStr)
//            onClickRightListener {
//                callPhone(rightCallPhone)
//            }
//        }
//
//        //查看 Cookies
////        String cookie s= AgentWebConfig.getCookiesByUrl(targetUrl);
//        //同步Cookie
////        AgentWebConfig.syncCookie("http://www.jd.com","ID=XXXX");
//    }
//
//    private fun getParams() {
//        mUrl = intent.getStringExtra(WEB_URL_KEY)
//        if (!TextUtils.isEmpty(mUrl)) {
//            mUrl = getUrl(mUrl)
//        }
//        mTitleStr = intent.getStringExtra(TITLE_KEY)
//        title_bar.setTitle(mTitleStr ?: "")
//    }
//
////    protected fun initBackClick(): View.OnClickListener? {
////        return View.OnClickListener {
////            if (!TextUtils.isEmpty(backMsg)) {
////                Builder(getActivity())
////                    .title(getString(R.string.mu_prompt))
////                    .content(backMsg)
////                    .rightText(getString(R.string.mu_continue_str))
////                    .leftText(getString(R.string.mu_give_up))
////                    .leftClickCall(object : MUDialogClickCallBack() {
////                        fun btnClick(dialog: Dialog, view: View?) {
////                            if (!mAgentWeb!!.back()) {
////                                navController.popBackStack()
////                            }
////                            dialog.dismiss()
////                        }
////                    }).rightClickCall(object : MUDialogClickCallBack() {
////                        fun btnClick(dialog: Dialog, view: View?) {
////                            dialog.dismiss()
////                        }
////                    }).build().show()
////            } else {
////                if (!mAgentWeb!!.back()) {
////                    navController.popBackStack()
////                }
////            }
////        }
////    }
//
//    override fun onPause() {
//        mAgentWeb!!.webLifeCycle.onPause()
//        super.onPause()
//    }
//
//    override fun onResume() {
//        mAgentWeb!!.webLifeCycle.onResume()
//        super.onResume()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        mAgentWeb!!.webLifeCycle.onDestroy()
//    }
//
//
//    private fun chooseFileFun() {
//
//        PermissionUtil.requestPermission(this@WebActivity, filePermissions, Action {
//            var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            if (takePictureIntent!!.resolveActivity(this@WebActivity.packageManager) != null) {
//                // Create the File where the photo should go
//                val imagePath: File = File(
//                    this@WebActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//                        .toString() + "/image/"
//                )
//                if (!imagePath.exists()) {
//                    imagePath.mkdirs()
//                }
//                val newFile = File(imagePath, System.currentTimeMillis().toString() + ".jpg")
//                imageUri = Uri.fromFile(newFile)
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    takePictureIntent.addFlags(
//                        Intent.FLAG_GRANT_READ_URI_PERMISSION
//                                or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
//                    ) //添加这一句表示对目标应用临时授权该Uri所代表的文件
//                }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    imageUri = FileProvider.getUriForFile(
//                        this@WebActivity,
//                        this@WebActivity.packageName.toString() + ".provider",
//                        newFile
//                    ) //通过FileProvider创建一个content类型的Uri
//                }
//                //将拍照结果保存至photo_file的Uri中，不保留在相册中
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
//            } else {
//                takePictureIntent = null
//            }
//
//            var intentArray: Array<Intent>
//
//            if (takePictureIntent != null) {
//                intentArray = arrayOf<Intent>(takePictureIntent)
//            } else {
//                intentArray = arrayOf<Intent>()
//            }
//
//            val mPhotoIntent = Intent(Intent.ACTION_PICK)
//            mPhotoIntent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//            val chooserIntent = Intent(Intent.ACTION_CHOOSER)
//            chooserIntent.putExtra(Intent.EXTRA_INTENT, mPhotoIntent)
//            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
//            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
//            startActivityForResult(Intent.createChooser(chooserIntent, "Select images"), GET_IMAGE_CODE)
//
//        })
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode ==GET_IMAGE_CODE) {
//            if (null == mUploadMessage && null == mUploadCallbackAboveL) return
//            val result = if (data == null || resultCode != RESULT_OK) null else data.data
//            if (mUploadCallbackAboveL != null) {
//                onActivityResultParseAboveL(requestCode, resultCode, data)
//            } else if (mUploadMessage != null) {
//                mUploadMessage.onReceiveValue(result)
//                mUploadMessage = null
//
//            }
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private fun onActivityResultParseAboveL(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode != GET_IMAGE_CODE || mUploadCallbackAboveL == null) {
//            return
//        }
//        var results: Array<Uri>? = null
//        if (resultCode == RESULT_OK) {
//            if (data == null) {
//                results = arrayOf(imageUri)
//            } else {
//                val dataString = data.dataString
//                val clipData = data.clipData
//                if (clipData != null) {
//                    results = arrayOfNulls(clipData.itemCount)
//                    for (i in 0 until clipData.itemCount) {
//                        val item = clipData.getItemAt(i)
//                        results[i] = item.uri
//                    }
//                }
//                if (dataString != null) results = arrayOf(Uri.parse(dataString))
//            }
//        }
//        mUploadCallbackAboveL?.onReceiveValue(results)
//        mUploadCallbackAboveL = null
//    }
//
//    fun setRegisterCallBack(registCallBack: RegisterCallBack?) {
//        this.registCallBack = registCallBack
//    }
//
//    interface RegisterCallBack {
//        fun onBackClick()
//    }
//
//
//
//    private fun callPhone(phone: String?) {
//        phone?.apply {
//            PermissionUtil.requestPermission(this@WebActivity, callPhonePermissions, Action {
//                if (TextUtils.isEmpty(phone)) return@Action
//                val intent = Intent(Intent.ACTION_DIAL)
//                intent.data = Uri.parse("tel:$phone")
//                startActivity(intent)
//            })
//        }
//    }
//
//    internal class AndroidFragmentInterface(context: MUBaseFragment, agentWeb: AgentWeb) {
//        private val weak: WeakReference<MUBaseFragment>
//        private val agentWebWeak: WeakReference<AgentWeb>
//
//        /**
//         * 跳转到google应用市场
//         */
//        @RequiresApi(api = Build.VERSION_CODES.DONUT)
//        @JavascriptInterface
//        fun openGooglePlay(appPkg: String) {
//            var params: String? = null
//            if (appPkg.startsWith("https://play.google.com/store/apps/details")) {
//                params = appPkg.replace("https://play.google.com/store/apps/details", "")
//            } else if (appPkg.startsWith("market://details")) {
//                params = appPkg.replace("market://details", "")
//            }
//            if (params == null) return
//            val GOOGLE_PLAY = "com.android.vending" //这里对应的是谷歌商店，跳转别的商店改成对应的即可
//            try {
//                val intent = Intent(Intent.ACTION_VIEW)
//                intent.data = Uri.parse("market://details$params")
//                intent.setPackage(GOOGLE_PLAY)
//                if (intent.resolveActivity(
//                        MUBaseApplication.getInstance().getPackageManager()
//                    ) != null
//                ) {
//                    weak.get().startActivity(intent)
//                } else { //没有应用市场，通过浏览器跳转到Google Play
//                    val intent2 = Intent(Intent.ACTION_VIEW)
//                    intent2.data = Uri.parse("https://play.google.com/store/apps/details$params")
//                    if (intent2.resolveActivity(
//                            MUBaseApplication.getInstance().getPackageManager()
//                        ) != null
//                    ) {
//                        weak.get().startActivity(intent2)
//                    } else {
//                        //没有Google Play 也没有浏览器
//                    }
//                }
//            } catch (activityNotFoundException1: ActivityNotFoundException) {
////                Log.e(AppRater.class.getSimpleName(), "GoogleMarket Intent not found");
//            }
//        }
//
//        /**
//         * 跳转scheme
//         */
//        @JavascriptInterface
//        fun openUrl(url: String?) {
//            MUNaviUtils.getInstance().navigate(getContext(), url)
//        }
//
//        /**
//         * 关闭当前H5
//         */
//        @JavascriptInterface
//        fun closeSyn() {
//            MUNaviUtils.getInstance().back()
//        }
//
//        /**
//         * 原生页面跳转
//         */
//        @JavascriptInterface
//        fun jump(linkUrl: String, params: String?) {
//            var linkUrl = linkUrl
//            if (!linkUrl.startsWith(MUNaviUtils.APP_SCHEME)) return
//            if (!TextUtils.isEmpty(params)) {
//                try {
//                    val paramsjson = JSONObject.parseObject(params)
//                    if (paramsjson != null) {
//                        val sb = StringBuilder(linkUrl)
//                        if (linkUrl.contains("?")) {
//                            sb.append("&")
//                        } else {
//                            sb.append("?")
//                        }
//                        for (key in paramsjson.keys) {
//                            sb.append(
//                                key + "=" + URLEncoder.encode(
//                                    paramsjson[key].toString(),
//                                    "UTF-8"
//                                ) + "&"
//                            )
//                        }
//                        linkUrl = sb.substring(0, sb.length - 1)
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//            MUNaviUtils.getInstance().navigate(getActivity(), linkUrl)
//        }
//
//        /**
//         * 首页
//         */
//        @JavascriptInterface
//        fun jumpToHome() {
//            MUNaviUtils.getInstance().jumpMainFrag(MUNaviUtils.MAIN_TAB.HOME)
//        }
//        //
//        //        /**
//        //         * 贷款
//        //         */
//        //        @JavascriptInterface
//        //        public void jumpToLoanMarket() {
//        //            startActivity(DKMainActivity.class);
//        //            DKRxBus.getInstance().post(new DKJumpToMainBus(DKJumpToMainBus.PageType.Loan));
//        //        }
//        //
//        /**
//         * 个人中心
//         */
//        @JavascriptInterface
//        fun jumpToUserCenter() {
//            if (!MUUserGetInfoTool.getInstance().isLogin()) {
//                MUNaviUtils.getInstance().navigate(MUNaviUtils.FragConstant.LOGIN)
//            } else {
//                MUNaviUtils.getInstance().jumpMainFrag(MUNaviUtils.MAIN_TAB.MINE)
//            }
//        }
//
//        /**
//         * 登录
//         */
//        @JavascriptInterface
//        fun jumpToLogin() {
//            if (MUUserGetInfoTool.getInstance().isLogin()) {
//                return
//            }
//            MUNaviUtils.getInstance().navigate(MUNaviUtils.FragConstant.LOGIN)
//        }
//
//        @JavascriptInterface
//        fun backDialog(msg: String) {
//            //初始化时候调用(点击返回键挽留用户)
//            backMsg = msg
//        }
//
//        /**
//         * 顶部右上角拨打电话
//         */
//        @JavascriptInterface
//        fun topCallPhone(phone: String) {
//            //页面初始化的时候调用
//            rightCallPhone = phone
//            adapterTitleView.setRightIv(R.drawable.mu_svg_web_right_call)
//        }
//
//        @JavascriptInterface
//        fun callPhoneMethod(phone: String?) {
//            //网页内部点击拨打电话
//            callPhone(phone)
//        }
//
//        @JavascriptInterface
//        fun uploadRiskLoan(productId: String?, orderId: String?) {
//            MUActionUtils.actionRecord(MUActionEnum.Loan, productId, pageCreateTime)
//        }
//
//        init {
//            weak = WeakReference<MUBaseFragment>(context)
//            agentWebWeak = WeakReference(agentWeb)
//        }
//    }
//
//    private val webViewClient: WebViewClient = object : WebViewClient() {
//        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//            if (isGooglePlayURL(url)) {
//                openGooglePlay(getContext(), url)
//                return true
//            }
//            if (url.startsWith("http://") || url.startsWith("https://") || "about:blank" == url) {
//                return super.shouldOverrideUrlLoading(view, url)
//            }
//            for (action in mActionArray) {
//                if (url.startsWith(action)) {
//                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                    startActivity(intent)
//                    return true
//                }
//            }
//            view.loadUrl(url)
//            return false
//        }
//
//        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
//            //dk_ssl_cert_invalid
//            Builder(getContext())
//                .canBackDismiss(true)
//                .title(getString(R.string.mu_prompt))
//                .content("Sertifikat situs web tidak normal. Terus?")
//                .leftText(getString(R.string.mu_cancel))
//                .leftClickCall(object : MUDialogClickCallBack() {
//                    fun btnClick(dialog: Dialog?, view: View?) {
//                        handler.cancel()
//                    }
//                })
//                .rightText(getString(R.string.mu_continue_terus))
//                .rightClickCall(object : MUDialogClickCallBack() {
//                    fun btnClick(dialog: Dialog?, view: View?) {
//                        handler.proceed()
//                    }
//                }).build()
//        }
//
//        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
//            super.onPageStarted(view, url, favicon)
//            //            binding.progressBar.setVisibility(View.VISIBLE);
//        }
//
//        override fun onPageFinished(view: WebView, url: String) {
//            super.onPageFinished(view, url)
//            if (mAgentWeb!!.webCreator.webView.settings.loadsImagesAutomatically) {
//                mAgentWeb!!.webCreator.webView.settings.loadsImagesAutomatically = true
//            }
//            mUrl = url
//            //            binding.progressBar.setVisibility(View.GONE);
//        }
//    }
//
//
//    private val mWebChromeClient: WebChromeClient = object : WebChromeClient() {
//        override fun onProgressChanged(view: WebView, newProgress: Int) {
////            binding.progressBar.setProgress(newProgress);
//        }
//
//        override fun onReceivedTitle(view: WebView, title: String) {
//            super.onReceivedTitle(view, title)
//            if (TextUtils.isEmpty(mTitleStr)) {
//                adapterTitleView.setTvTitleText(title)
//            }
//        }
//
//        // For Android 5.0+  相机
//        override fun onShowFileChooser(
//            view: WebView,
//            filePathCallback: ValueCallback<Array<Uri>>,
//            fileChooserParams: FileChooserParams
//        ): Boolean {
//            if (mUploadCallbackAboveL != null) {
//                mUploadCallbackAboveL.onReceiveValue(null)
//                mUploadCallbackAboveL = null
//            }
//            mUploadCallbackAboveL = filePathCallback
//            chooseFileFun()
//            return true
//        }
//
//        //下面是处理不同系统的文件选择
//        override fun openFileChooser(uploadMsg: ValueCallback<Uri>) {
//            openFileChooser(uploadMsg, "image/*", null)
//        }
//
//        override fun openFileChooser(uploadMsg: ValueCallback<*>, acceptType: String) {
//            openFileChooser(uploadMsg, acceptType, null)
//        }
//
//        override fun openFileChooser(
//            uploadMsg: ValueCallback<Uri>,
//            acceptType: String,
//            capture: String
//        ) {
//            if (mUploadMessage != null) {
//                mUploadMessage.onReceiveValue(null)
//            }
//            mUploadMessage = uploadMsg
//            chooseFileFun()
//        }
//    }
//
//    private fun isGooglePlayURL(url: String?): Boolean {
//        return url != null && (url.startsWith("market://") || url.startsWith("https://play.google.com/store/apps/details"))
//    }
//
//    private fun openGooglePlay(context: Context, url: String) {
//        var params: String? = null
//        if (url.startsWith("https://play.google.com/store/apps/details")) {
//            params = url.replace("https://play.google.com/store/apps/details", "")
//        } else if (url.startsWith("market://details")) {
//            params = url.replace("market://details", "")
//        }
//        if (params == null) return
//        val GOOGLE_PLAY = "com.android.vending" //这里对应的是谷歌商店，跳转别的商店改成对应的即可
//        try {
//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.data = Uri.parse("market://details$params")
//            intent.setPackage(GOOGLE_PLAY)
//            if (intent.resolveActivity(context.packageManager) != null) {
//                context.startActivity(intent)
//            } else { //没有应用市场，通过浏览器跳转到Google Play
//                val intent2 = Intent(Intent.ACTION_VIEW)
//                intent2.data = Uri.parse("https://play.google.com/store/apps/details$params")
//                if (intent2.resolveActivity(context.packageManager) != null) {
//                    context.startActivity(intent2)
//                }
//            }
//        } catch (activityNotFoundException1: ActivityNotFoundException) {
//        }
//    }
//
//
//    fun initSetting(mWebView: WebView, context: Context) {
//        mWebView.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
//            val uri = Uri.parse(url)
//            val intent = Intent(Intent.ACTION_VIEW, uri)
//            context.applicationContext.startActivity(intent)
//        }
//        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
//        val settings = mWebView.settings
//        settings.javaScriptEnabled = true
//        settings.allowFileAccess = true
//        settings.cacheMode = WebSettings.LOAD_DEFAULT
//        settings.domStorageEnabled = true
//        settings.useWideViewPort = true
//        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
//        settings.allowContentAccess = true // 是否可访问Content Provider的资源，默认值 true
//        settings.allowFileAccessFromFileURLs =
//            false // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
//        settings.allowUniversalAccessFromFileURLs = false
//        settings.loadWithOverviewMode = true
//        settings.javaScriptCanOpenWindowsAutomatically = true
//        settings.databaseEnabled = true
//        settings.loadsImagesAutomatically = true
//        //webview在安卓5.0之前默认允许其加载混合网络协议内容
//        // 在安卓5.0之后，默认不允许加载http与https混合内容，需要设置webview允许其加载混合网络协议内容
//        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
//    }
//
//    fun getUrl(url: String?): String? {
//        if (url==null||TextUtils.isEmpty(url)) {
//            return ""
//        }
//        var ret_url = ""
//        /**** 第三方的webview请求不带参数，自己的请求需要带参数  */
//        return if (url.startsWith(H5_SERVICE_URL)) {  //不是第三方
//            if (url.contains("clientType=android&appVersion=")) {
//                url
//            } else {
//                ret_url = if (url.contains("?")) {
//                    "$url&"
//                } else {
//                    "$url?"
//                }
//                ret_url += "clientType=android&appVersion=" + BuildConfig.VERSION_NAME
//                    .toString() + "&deviceId=" + MyDevTool.getDeviceId(ContextUtils.getSharedContext())
//                    .toString() + "&gps_adid=" + MUMMKVTool.decodeString(MUConstant.SpKey.GPS_ADID)
//                    .toString() + "&mobilePhone=" + MUUserGetInfoTool.getInstance().getUsername()
//                    .toString() + "&deviceName=" + MyDevTool.getDeviceName()
//                    .toString() + "&sessionId=" + MUUserGetInfoTool.getInstance().getSessionid()
//                    .toString() + "&userId=" + MUUserGetInfoTool.getInstance().getUid()
//                    .toString() + "&merchantNumber=" + MUBaseApplication.getInstance()
//                    .getString(com.mari.network.R.string.mu_channel_merchant_number)
//                    .toString() + "&appName=" + MUBaseApplication.getInstance()
//                    .getString(com.mari.network.R.string.mu_channel_name_short)
//                    .toString() + "&appMarket=" + MUBaseApplication.getInstance()
//                    .getString(com.mari.network.R.string.mu_app_mark)
//                    .toString() + "&channel=" + MUBaseApplication.getInstance()
//                    .getString(com.mari.network.R.string.mu_channel_name_short)
//                    .toString() + "&osVersion=" + MyDevTool.getOsVersion()
//                    .toString() + "&merchant=" + MUBaseApplication.getInstance()
//                    .getString(com.mari.network.R.string.mu_channel_merchant_number)
//                    .toString() + "&platform=" + "1" + "&packageId=" + MUBaseApplication.getInstance()
//                    .getString(com.mari.network.R.string.mu_application_id)
//                ret_url.replace(" ", "")
//            }
//        } else {
//            url
//        }
//    }
//
//    fun getHeader(): Map<String, String>? {
//        val headerMap: MutableMap<String, String> = HashMap()
//        headerMap["sessionid"] = sessionid
//        headerMap["userId"] = uid
//        headerMap["channel"] =strings(R.string.channel_name_short)
//        return headerMap
//    }
//}