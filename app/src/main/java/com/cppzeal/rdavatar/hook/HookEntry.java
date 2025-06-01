package com.cppzeal.rdavatar.hook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookEntry implements IXposedHookLoadPackage {

    public static final String TAG = "RdAvatar ";

    final static String packageName = "com.tencent.mobileqq";


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        if (!loadPackageParam.packageName.equals(packageName)) {
            return;
        }

        Hookers.HookRes(loadPackageParam);
        Hookers.MulDexHookRes(loadPackageParam);
////        Hookers.HookQLog(loadPackageParam);
        Hookers.SettingHook(loadPackageParam);
        Hookers.SettingHook9170(loadPackageParam);


    }

}