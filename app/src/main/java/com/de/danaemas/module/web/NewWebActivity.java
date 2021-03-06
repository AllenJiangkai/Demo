package com.de.danaemas.module.web;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.alibaba.fastjson.JSONObject;
import com.coupang.common.base.BaseSimpleActivity;
import com.coupang.common.user.UserManager;
import com.coupang.common.utils.ContextUtils;
import com.de.danaemas.R;
import com.de.danaemas.config.ConstantConfig;
import com.de.danaemas.event.ActionEnum;
import com.de.danaemas.event.ActionUtil;
import com.de.danaemas.module.login.LoginActivity;
import com.de.danaemas.module.main.MainActivity;
import com.de.danaemas.util.WebViewUtil;
import com.de.danaemas.util.PermissionUtil;
import com.de.danaemas.util.RouterUtil;
import com.de.danaemas.widget.TipsDialog;
import com.de.danaemas.widget.TitleBarView;
import com.yanzhenjie.permission.Action;

import java.io.File;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class NewWebActivity extends BaseSimpleActivity {

    private static int GET_IMAGE_CODE = 10001;

    private WebView mAgentWeb;

    String[] mActionArray = {"mailto:", "geo:", "tel:", "sms:"};

    private String backMsg;
    private String rightCallPhone;


    private String mTitleStr;
    private String mUrl;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private Uri imageUri;
    private RegisterCallBack registCallBack;

    private String[] filePermissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String[] callPhonePermissions = {Manifest.permission.CALL_PHONE};




    private void getParams() {
        mUrl = getIntent().getStringExtra(ConstantConfig.WEB_URL_KEY);
        if (!TextUtils.isEmpty(mUrl)) {
            mUrl = WebViewUtil.getUrl(mUrl);
        }
        mTitleStr = getIntent().getStringExtra(ConstantConfig.TITLE_KEY);
        title_var.setTitle(mTitleStr);
    }


    private void onClickBack() {
        if (!TextUtils.isEmpty(backMsg)) {
            TipsDialog dialog = new TipsDialog(this);
            dialog.setTitle(getString(R.string.dialog_per_prompt))
                    .setMessage(backMsg)
                    .setPositiveButton(getString(R.string.web_dialog_continue), new Function0<Boolean>() {
                        @Override
                        public Boolean invoke() {
                            dialog.dismiss();
                            return true;
                        }
                    })
                    .setNegativeButton(getString(R.string.web_dialog_left), new Function0<Boolean>() {
                        @Override
                        public Boolean invoke() {
                            if (!mAgentWeb.canGoBack()) {
                                finish();
                            }
                            dialog.dismiss();
                            return true;
                        }
                    });


            dialog.show();

        } else {
            if (!mAgentWeb.canGoBack()) {
                finish();
            }
        }

    }


    @Override
    public void onPause() {
        mAgentWeb.onPause();
        super.onPause();

    }

    @Override
    public void onResume() {
        mAgentWeb.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mAgentWeb.destroy();
        super.onDestroy();
    }

    private void chooseFileFun() {

        PermissionUtil.INSTANCE.requestPermission(this,filePermissions,  new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(NewWebActivity.this.getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File imagePath = new File(NewWebActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/image/");
                    if (!imagePath.exists()) {
                        imagePath.mkdirs();
                    }
                    File newFile = new File(imagePath, System.currentTimeMillis() + ".jpg");
                    imageUri = Uri.fromFile(newFile);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION); //???????????????????????????????????????????????????Uri??????????????????
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        imageUri = FileProvider.getUriForFile(NewWebActivity.this, NewWebActivity.this.getPackageName() + ".provider", newFile);//??????FileProvider????????????content?????????Uri
                    }
                    //????????????????????????photo_file???Uri???????????????????????????
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                } else {
                    takePictureIntent = null;
                }

                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent mPhotoIntent = new Intent(Intent.ACTION_PICK);
                mPhotoIntent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, mPhotoIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(Intent.createChooser(chooserIntent, "Select images"), GET_IMAGE_CODE);
            }
        },false,false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_IMAGE_CODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != Activity.RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultParseAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultParseAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != GET_IMAGE_CODE || mUploadCallbackAboveL == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                results = new Uri[]{imageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        mUploadCallbackAboveL.onReceiveValue(results);
        mUploadCallbackAboveL = null;
    }

    public void setRegisterCallBack(RegisterCallBack registCallBack) {
        this.registCallBack = registCallBack;
    }

    public interface RegisterCallBack {
        void onBackClick();
    }

    private void callPhone(String phone) {
        PermissionUtil.INSTANCE.requestPermission(this,callPhonePermissions,new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                if (TextUtils.isEmpty(phone))
                    return;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        },false,false);
    }

    class AndroidFragmentInterface {
        private WeakReference<NewWebActivity> weak;
        private WeakReference<WebView> agentWebWeak;

        public AndroidFragmentInterface(NewWebActivity context, WebView agentWeb) {
            this.weak = new WeakReference<>(context);
            this.agentWebWeak = new WeakReference<>(agentWeb);
        }

        /**
         * ?????????google????????????
         */
        @RequiresApi(api = Build.VERSION_CODES.DONUT)
        @JavascriptInterface
        public void openGooglePlay(String appPkg) {
            String params = null;
            if (appPkg.startsWith("https://play.google.com/store/apps/details")) {
                params = appPkg.replace("https://play.google.com/store/apps/details", "");
            } else if (appPkg.startsWith("market://details")) {
                params = appPkg.replace("market://details", "");
            }
            if (params == null) return;
            String GOOGLE_PLAY = "com.android.vending";//????????????????????????????????????????????????????????????????????????
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details" + params));
                intent.setPackage(GOOGLE_PLAY);
                if (intent.resolveActivity(ContextUtils.getApplication().getPackageManager()) != null) {
                    weak.get().startActivity(intent);
                } else {//?????????????????????????????????????????????Google Play
                    Intent intent2 = new Intent(Intent.ACTION_VIEW);
                    intent2.setData(Uri.parse("https://play.google.com/store/apps/details" + params));
                    if (intent2.resolveActivity(ContextUtils.getApplication().getPackageManager()) != null) {
                        weak.get().startActivity(intent2);
                    } else {
                        //??????Google Play ??????????????????
                    }
                }
            } catch (ActivityNotFoundException activityNotFoundException1) {
//                Log.e(AppRater.class.getSimpleName(), "GoogleMarket Intent not found");
            }
        }

        /**
         * ??????scheme
         */
        @JavascriptInterface
        public void openUrl(String url) {
            RouterUtil.INSTANCE.router(NewWebActivity.this,url,false);
        }

        /**
         * ????????????H5
         */
        @JavascriptInterface
        public void closeSyn() {
           finish();
        }

        /**
         * ??????????????????
         */
        @JavascriptInterface
        public void jump(String linkUrl, String params) {
            if (!linkUrl.startsWith(RouterUtil.INSTANCE.getAPP_SCHEME())) return;
            if (!TextUtils.isEmpty(params)) {
                try {
                    JSONObject paramsjson = JSONObject.parseObject(params);
                    if (paramsjson != null) {
                        StringBuilder sb = new StringBuilder(linkUrl);
                        if (linkUrl.contains("?")) {
                            sb.append("&");
                        } else {
                            sb.append("?");
                        }
                        for (String key : paramsjson.keySet()) {
                            sb.append(key + "=" + URLEncoder.encode(String.valueOf(paramsjson.get(key)), "UTF-8") + "&");
                        }
                        linkUrl = sb.substring(0, sb.length() - 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            RouterUtil.INSTANCE.router(NewWebActivity.this,linkUrl,false);
        }

        /**
         * ??????
         */
        @JavascriptInterface
        public void jumpToHome() {
           Intent intent=new Intent(NewWebActivity.this, MainActivity.class);
           startActivity(intent);
        }
//
//        /**
//         * ??????
//         */
//        @JavascriptInterface
//        public void jumpToLoanMarket() {
//            startActivity(DKMainActivity.class);
//            DKRxBus.getInstance().post(new DKJumpToMainBus(DKJumpToMainBus.PageType.Loan));
//        }
//

        /**
         * ????????????
         */
        @JavascriptInterface
        public void jumpToUserCenter() {
            if (!UserManager.INSTANCE.isLogin()) {
                Intent intent=new Intent(NewWebActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Intent intent=new Intent(NewWebActivity.this, MainActivity.class);
                intent.putExtra("index",1);
                startActivity(intent);
            }
        }

        /**
         * ??????
         */
        @JavascriptInterface
        public void jumpToLogin() {
            if (UserManager.INSTANCE.isLogin()) {
                return;
            }
            Intent intent=new Intent(NewWebActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        @JavascriptInterface
        public void backDialog(String msg) {
            //?????????????????????(???????????????????????????)
            backMsg = msg;
        }


        /**
         * ???????????????????????????
         */
        @JavascriptInterface
        public void topCallPhone(String phone) {
            //??????????????????????????????
            rightCallPhone = phone;
            title_var.setRightImage(R.drawable.bg_svg_web_right_call);
        }

        @JavascriptInterface
        public void callPhoneMethod(String phone) {
            //??????????????????????????????
            callPhone(phone);
        }

        @JavascriptInterface
        public void uploadRiskLoan(String productId, String orderId) {
            ActionUtil.INSTANCE.actionRecord(ActionEnum.Loan, productId,getPageCreateTime());
        }
    }

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (isGooglePlayURL(url)) {
                openGooglePlay(NewWebActivity.this, url);
                return true;
            }
            if (url.startsWith("http://") || url.startsWith("https://") || "about:blank".equals(url)) {
                return super.shouldOverrideUrlLoading(view, url);
            }
            for (String action : mActionArray) {
                if (url.startsWith(action)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            }
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            //dk_ssl_cert_invalid

            TipsDialog dialog = new TipsDialog(NewWebActivity.this);
            dialog.setTitle(getString(R.string.dialog_per_prompt))
                    .setMessage("Sertifikat situs web tidak normal. Terus?")
                    .setPositiveButton(getString(R.string.dialog_continue), new Function0<Boolean>() {
                        @Override
                        public Boolean invoke() {
                            handler.proceed();
                            return true;
                        }
                    })
                    .setNegativeButton(getString(R.string.dialog_cancel), new Function0<Boolean>() {
                        @Override
                        public Boolean invoke() {
                            handler.cancel();
                            return true;
                        }
                    });


            dialog.show();



        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

             progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(final WebView view, String url) {
            super.onPageFinished(view, url);
            if (mAgentWeb.getSettings().getLoadsImagesAutomatically()) {
                mAgentWeb.getSettings().setLoadsImagesAutomatically(true);
            }
            mUrl = url;
//            binding.progressBar.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    };


    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
//            binding.progressBar.setProgress(newProgress);
            progressBar.setProgress(newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (TextUtils.isEmpty(mTitleStr)) {
                title_var.setTitle(title);
            }
        }

        // For Android 5.0+  ??????
        public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (mUploadCallbackAboveL != null) {
                mUploadCallbackAboveL.onReceiveValue(null);
                mUploadCallbackAboveL = null;
            }
            mUploadCallbackAboveL = filePathCallback;
            chooseFileFun();
            return true;
        }

        //??????????????????????????????????????????

        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "image/*", null);
        }

        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            openFileChooser(uploadMsg, acceptType, null);
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
            }
            mUploadMessage = uploadMsg;
            chooseFileFun();
        }
    };

    private static boolean isGooglePlayURL(String url) {
        if (url != null && (url.startsWith("market://") || url.startsWith("https://play.google.com/store/apps/details"))) {
            return true;
        }
        return false;
    }

    private static void openGooglePlay(Context context, String url) {
        String params = null;
        if (url.startsWith("https://play.google.com/store/apps/details")) {
            params = url.replace("https://play.google.com/store/apps/details", "");
        } else if (url.startsWith("market://details")) {
            params = url.replace("market://details", "");
        }
        if (params == null) return;
        String GOOGLE_PLAY = "com.android.vending";//????????????????????????????????????????????????????????????????????????
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details" + params));
            intent.setPackage(GOOGLE_PLAY);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {//?????????????????????????????????????????????Google Play
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse("https://play.google.com/store/apps/details" + params));
                if (intent2.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent2);
                }
            }
        } catch (ActivityNotFoundException activityNotFoundException1) {

        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    TitleBarView title_var;
    ProgressBar progressBar;
    @Override
    public void initView() {
        title_var = findViewById(R.id.title_bar);
        mAgentWeb=findViewById(R.id.web_view);
        progressBar  =findViewById(R.id.progressBar);
        getParams();
//        mAgentWeb = AgentWeb.with(this)
//                .setAgentWebParent(findViewById(R.id.parentView), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
//                .useDefaultIndicator(ContextCompat.getColor(this, R.color.transparent))
//                .setWebViewClient(webViewClient)
//                .additionalHttpHeader(mUrl, MUWebViewUtil.getHeader())
//                .setWebChromeClient(mWebChromeClient)
//                .createAgentWeb()
//                .ready()
//                .go(mUrl);
//        mAgentWeb.getWebCreator().getWebView().loadUrl(mUrl,WebUtil.getHeader());
//        mAgentWeb.getJsInterfaceHolder().addJavaObject("nativeMethod", new AndroidFragmentInterface(this, mAgentWeb));
        mAgentWeb.addJavascriptInterface(new AndroidFragmentInterface(this, mAgentWeb),"nativeMethod");
        WebViewUtil.initSetting(mAgentWeb , this);
        mAgentWeb.setWebChromeClient(mWebChromeClient);
        mAgentWeb.setWebViewClient(webViewClient);
        mAgentWeb.loadUrl(mUrl, WebViewUtil.getHeader());


        title_var.onClickRightListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                callPhone(rightCallPhone);
                return null;
            }
        });
        title_var.onClickBackListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                onClickBack();
                return null;
            }
        });


    }

    @Override
    public void registerObserver() {

    }

    @Override
    public void initData() {

    }
}
