#-------------------------5.基本不用动区域--------------------------
#指定代码的压缩级别
-optimizationpasses 5

#包明不混合大小写
-dontusemixedcaseclassnames

#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers

#混淆时是否记录日志
-verbose

#优化  不优化输入的类文件
-dontoptimize

#预校验
-dontpreverify

# 保留sdk系统自带的一些内容 【例如：-keepattributes *Annotation* 会保留Activity的被@override注释的onCreate、onDestroy方法等】
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

# 记录生成的日志数据,gradle build时在本项根目录输出
# apk 包内所有 class 的内部结构
-dump proguard/class_files.txt
# 未混淆的类和成员
-printseeds proguard/seeds.txt
# 列出从 apk 中删除的代码
-printusage proguard/unused.txt
# 混淆前后的映射
-printmapping proguard/mapping.txt


# 避免混淆泛型
-keepattributes Signature
# 抛出异常时保留代码行号,保持源文件以及行号
-keepattributes SourceFile,LineNumberTable

#-----------------------------6.默认保留区-----------------------
# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers public class * extends android.view.View {
 public <init>(android.content.Context);
 public <init>(android.content.Context, android.util.AttributeSet);
 public <init>(android.content.Context, android.util.AttributeSet, int);
 public void set*(***);
}

#保持 Serializable 不被混淆
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

# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}
# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}
# 保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

# 保持枚举 enum 类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# 不混淆R文件中的所有静态字段，我们都知道R文件是通过字段来记录每个资源的id的，字段名要是被混淆了，id也就找不着了。
-keepclassmembers class **.R$* {
    public static <fields>;
}

#如果引用了v4或者v7包
-dontwarn android.support.**

# 保持哪些类不被混淆
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference
-keep class android.support.**{*;}

-keep class international.utils.** {*;}
-keep class wedemo.utils.** {*;}

-assumenosideeffects class android.util.Log {
   public static *** v(...);
   public static *** d(...);
   public static *** i(...);
   public static *** w(...);
 }

#--------------------------1.实体类---------------------------------
# 如果使用了Gson之类的工具要使被它解析的JavaBean类即实体类不被混淆。（这里填写自己项目中存放bean对象的具体路径）
-keep class com.sven.huinews.international.entity.**{*;}
-keep class wedemo.shot.bean.**{*;}
-keep class wedemo.utils.dataInfo.**{*;}

#--------------------------2.第三方包-------------------------------

#------eventbus------
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#--------jsbridge----------
-keep class com.github.lzyzsd.**{*;}

#-----------http框架----------
#retrofit2
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**

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

#Gson
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.gson.* { *;}
-dontwarn com.google.gson.**

-keep class com.tbruyelle.rxpermissions.**{*;}
-keep class okhttp.**{*;}
-keep class rx.android.**{*;}

#----------- 三方统计 ----------
-keep class com.umeng.analytics.**{ *;}
-keep class com.umeng.commonsdk.**{ *;}

#--------推送服务 -----------
-keep class com.google.firebase.**{*;}

#------- google相关 ---------
-keep class com.google.android.gms.**{*;}
#google服务
-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

#------- 广告------------
-keep class com.duapps.ad.**{*;}
-keep class com.flurry.android.**{*;}
-keep class com.ta.utdid2.**{*;}
-keep class com.ut.device.**{*;}
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
        @com.google.android.gms.common.annotation.KeepName *;}
-keep class com.google.android.gms.common.GooglePlayServicesUtil {
        public <methods>;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {
        public <methods>;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {
        public <methods>;}

#----- UI系列 ---------
-keep class com.flyco.tablayout.**{*;}
-keep class com.chad.library.**{*;}
-keep class com.zhy.view.flowlayout.**{*;}
-keep class com.scwang.smartrefresh.**{*;}
-keep class com.gcssloop.widget.**{*;}
-keep class com.app.hubert.guide.**{*;}

#--------图片加载 ----------
-keep class com.facebook.**{ *;}
-keep class pl.droidsonroids.gif.**{ *;}
-keep class com.nostra13.universalimageloader.**{ *;}
-keep class jp.wasabeef.glide.transformations.**{*;}
-keep class com.bumptech.glide.**{ *;}

#---------三方登录---------
-keep class com.twitter.**{ *;}
-keep class com.linkedin.**{*;}
-keep class com.android.volley.**{*;}

#----------三方分享----------
-keep class cn.sharesdk.facebook.**{*;}
-keep class cn.sharesdk.twitter.**{*;}

#-----------视频播放-----------
-keep class com.dueeeke.videoplayer.**{*;}
#ijkplayer
-keep class tv.danmaku.ijk.media.player.** {*;}
-keep class tv.danmaku.ijk.media.player.IjkMediaPlayer{*;}
-keep class tv.danmaku.ijk.media.player.ffmpeg.FFmpegApi{*;}


#----------美摄sdk-----------
-keep class com.cdv.**{*;}
-keep class com.meicam.sdk.**{*;}

#---------- 网宿系列 --------
-keep class com.chinanetcenter.wcs.android.**{*;}


#-------------------------3.与js互相调用的类------------------------
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

#-------------------------4.反射相关的类和方法----------------------
-keep class com.sven.huinews.international.main.advert.model.**{*;}
-keep class com.sven.huinews.international.main.bindemail.model.**{*;}
-keep class com.sven.huinews.international.main.earn.model.**{*;}
-keep class com.sven.huinews.international.main.fansandfollow.model.**{*;}
-keep class com.sven.huinews.international.main.follow.model.**{*;}
-keep class com.sven.huinews.international.main.forgetpass.model.**{*;}
-keep class com.sven.huinews.international.main.home.model.**{*;}
-keep class com.sven.huinews.international.main.login.model.**{*;}
-keep class com.sven.huinews.international.main.me.Model.**{*;}
-keep class com.sven.huinews.international.main.news.model.**{*;}
-keep class com.sven.huinews.international.main.permsg.model.**{*;}
-keep class com.sven.huinews.international.main.userdetail.module.**{*;}
-keep class com.sven.huinews.international.main.video.model.**{*;}
-keep class com.sven.huinews.international.main.web.model.**{*;}
-keep class com.sven.huinews.international.main.task.model.**{*;}
-keep class wedemo.model.**{*;}
-keep class wedemo.music.**{*;}
-keep class wedemo.activity.down.**{*;}

-keep class wedemo.activity.request.**{*;}

# ============忽略警告，否则打包可能会不成功=============
-ignorewarnings

#-------------------------Vungle混淆----------------------------
-dontwarn com.vungle.**
-keep class com.vungle.** { *; }
-keep class javax.inject.*
