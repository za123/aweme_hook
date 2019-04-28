package com.example.asus.aweme_xp;
//Android
import android.content.Context;
import android.view.View;
import android.graphics.Color;
import android.widget.Button;
import android.widget.FrameLayout;
import android.app.Application;
import android.app.AndroidAppHelper;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.Switch;
import com.example.asus.aweme_xp.MainActivity;
//xposed
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam; //包名

public class Hooktest implements IXposedHookLoadPackage{
    private Button tv; //控制开关
    Boolean code = false;//true状态为main界面ui hook选中状态
    Application context = AndroidAppHelper.currentApplication();
    int i=0;
    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam ) throws Throwable{
        XposedBridge.log("当前包名："+lpparam.packageName);
        if (!lpparam.packageName.equals("com.ss.android.ugc.aweme")){
            return;
        };
        XposedBridge.log("抖音加载！开始hook开关！");
        isxposed(lpparam);
        uihook(lpparam);
        //uihook(lpparam);
        wifi(lpparam);
    }
    public void wifi(LoadPackageParam lpparam ){
        Class<?> onwifi = XposedHelpers.findClassIfExists("com.bytedance.common.utility.DeviceUtils",lpparam.classLoader);
        if (onwifi!= null){
            XposedHelpers.findAndHookMethod(onwifi,
                    "isWifiProxy",
                    Context.class,
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable{
                            super.afterHookedMethod(param);
                            XposedBridge.log("hook生效：反制wifi代理检测"+param.args[0].toString());
                            param.setResult(true);
                        }
                    });
            XposedBridge.log("加载检测类");
        }
    }//抓包专用
    public void isxposed(LoadPackageParam lpparam ){
        Class<?> aweme = XposedHelpers.findClassIfExists("com.bytedance.common.utility.DeviceUtils",lpparam.classLoader);
        if (aweme!= null){
            XposedHelpers.findAndHookMethod(aweme,
                    "isInstallXposed",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable{
                            super.afterHookedMethod(param);
                            param.args[0] = true;
                            XposedBridge.log("hook生效：反制抖音检测");
                        }
                    });
            XposedBridge.log("加载检测类");
        }
    }//反制xp检测
    public void uihook(LoadPackageParam lpparam ){
        Class<?> share = XposedHelpers.findClassIfExists("com.ss.android.ugc.aweme.feed.feedwidget.VideoMusicTitleWidget",lpparam.classLoader);//可疑方法b
        if (share != null){
            XposedBridge.log("尝试hook音乐标题按钮");
            XposedHelpers.findAndHookMethod(share,
                    "a",
                    View.class,
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable{
                            super.afterHookedMethod(param);
                            final FrameLayout ll = (FrameLayout) param.args[0];
                            XposedBridge.log("ui:"+param.args[0].toString());
                            if (ll!=null){
                                tv = new Button(ll.getContext());
                                i++;
                                tv.setText("启动");
                                tv.setBackgroundColor(Color.rgb(0,0,0));
                                tv.setTextColor(Color.rgb(0,255,255));
                                ll.addView(tv);
                                XposedBridge.log("替换音乐标题按钮完成！替换次数："+i);
                                tv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        XposedBridge.log("按钮音乐标题点击事件");
                                        new MainActivity();
                                    }
                                });
                            }
                        }
                    });
        }
    } //替换音乐标题
}