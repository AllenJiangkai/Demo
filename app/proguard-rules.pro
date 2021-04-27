# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-optimizationpasses 5

# 去除编译时警告
-ignorewarnings

#不压缩输入的类文件
-dontshrink

#不优化输入的类文件
-dontoptimize

# 不混淆输入的类文件
#-dontobfuscate
# 混合时不使用大小写混合，混合后的类名为小写
-dontusemixedcaseclassnames

# 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses

# 这句话能够使我们的项目混淆后产生映射文件
# 包含有类名->混淆后类名的映射关系
-verbose

# 指定不去忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers

# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
#-optimizations!code/simplification/cast,!field/*,!class/merging/*

# 指定混淆是采用的算法
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*

# 指定外部模糊字典 proguard-chinese.txt 改为混淆文件名，下同
#-obfuscationdictionary proguard.txt
# 指定class模糊字典
#-classobfuscationdictionary proguard.txt
# 指定package模糊字典
#-packageobfuscationdictionary proguard.txt

# 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
-dontpreverify

# 保留Annotation不混淆
-keepattributes *Annotation*,InnerClasses

# 避免混淆泛型
-keepattributes Signature

# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

#############################################
#
# Android开发中一些需要保留的公共部分 start
#
#############################################

# 保留我们使用的四大组件，自定义的Application等等这些类不被混淆
# 因为这些子类都有可能被外部调用
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService

# 保留support下的所有类及其内部类
-keep class android.support.** {*;}

# 保留继承的
-keep class com.google.android.material.** {*;}
-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**

-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**

# 保留R下面的资源
-keep class **.R$* {*;}

# 保留本地native方法不被混淆
-keepclasseswithmembernames class * {
native <methods>;
}

# 保留在Activity中的方法参数是view的方法，
# 这样以来我们在layout中写的onClick就不会被影响
-keepclassmembers class * extends android.app.Activity{
public void *(android.view.View);
}

# 保留枚举类不被混淆
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}

# 保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
*** get*();
void set*(***);
public <init>(android.content.Context);
public <init>(android.content.Context, android.util.AttributeSet);
public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator *;
}

# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
static final long serialVersionUID;
private static final java.io.ObjectStreamField[] serialPersistentFields;
!static !transient <fields>;
!private <fields>;
!private <methods>;
private void writeObject(java.io.ObjectOutputStream);
private void readObject(java.io.ObjectInputStream);
java.lang.Object writeReplace();
java.lang.Object readResolve();
}

# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
void *(**On*Event);
void *(**On*Listener);
}

# webView处理，项目中没有使用到webView忽略即可
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
public void *(android.webkit.webView, jav.lang.String);
}

-keepattributes *JavascriptInterface*

-keepclassmembers class * {
public <init> (org.json.JSONObject);
}

-keep public class com.xxx.xxx.R$*{
public static final int *;
}


# Tablayout反射修改下划线宽度导致的tabtrip空指针问题 - 需要可打开
#-keep class android.support.design.widget.TabLayout{*;}

#（可选）避免Log打印输出
-assumenosideeffects class android.util.Log {
public static *** v(...);
public static *** d(...);
public static *** i(...);
public static *** w(...);
}

# gson
-dontwarn com.google.**
-keep class com.google.gson.** {*;}

# fastjson
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }

# glide混淆
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
**[] $VALUES;
public *;
}
#glide如果你的API级别<=Android API 27 则需要添加 4.6.1
-dontwarn com.bumptech.glide.load.resource.bitmap.VideoDecoder
-dontwarn me.iwf.photopicker.adapter.**
# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

# rxjava+okhttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# rxjava retrofit2.**
-dontwarn rx.internal.util.unsafe.*
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Exceptions
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
long producerIndex;
long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
long producerNode;
long consumerNode;
}
-keep class rx.schedulers.Schedulers {
public static <methods>;
}
-keep class rx.schedulers.Schedulers {
public static ** test();
}
-keep class rx.schedulers.ImmediateScheduler {
public <methods>;
}
-keep class rx.schedulers.TestScheduler {
public <methods>;
}


# af
-keep class com.appsflyer.** { *; }

# google
-keep class com.google.android.gms.common.ConnectionResult {
int SUCCESS;
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {
com.google.android.gms.ads.identifier.AdvertisingIdClient$Info getAdvertisingIdInfo(android.content.Context);
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {
java.lang.String getId();
boolean isLimitAdTrackingEnabled();
}
-keep class dalvik.system.VMRuntime {
java.lang.String getRuntime();
}
-keep class android.os.Build {
java.lang.String[] SUPPORTED_ABIS;
java.lang.String CPU_ABI;
}
-keep class android.content.res.Configuration {
android.os.LocaleList getLocales();
java.util.Locale locale;
}
-keep class android.os.LocaledList {
java.util.Locale get(int);
}
-keep public class com.android.installreferrer.** { *; }
-dontwarn com.android.installreferrer












#***************EventBus
-keepattributes *Annotation*
-keepclassmembers class * {
@org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
<init>(java.lang.Throwable);
}

#****************** agentweb
-keep class com.just.agentweb.** {
*;
}
-dontwarn com.just.agentweb.**

-keepclassmembers class com.mari.dk.utils.web.AndroidInterface{ *; }


#**********************头条屏幕适配
-keep class me.jessyan.autosize.** { *; }
-keep interface me.jessyan.autosize.** { *; }


#****************PictureSelector
-keep class com.luck.picture.lib.** { *; }

#-------------BaseRecyclerViewAdapterHelper--------
-keep class com.chad.library.adapter.** {
*;
}
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
-keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
<init>(...);
}
#-------------------------
-keep class  com.yanzhenjie.permission.** { *; }

-keepclasseswithmembernames class *{
    native <methods>;
 }

-keep interface com.coupang.common.network.DTO
-keep class * implements com.coupang.common.network.DTO { *; }

-keep class  com.coupang.common.**.** { *; }



