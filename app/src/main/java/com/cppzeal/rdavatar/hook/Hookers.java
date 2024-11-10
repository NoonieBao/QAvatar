package com.cppzeal.rdavatar.hook;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cppzeal.rdavatar.data.Storage;
import com.cppzeal.rdavatar.ui.SettingUi;
import com.cppzeal.rdavatar.utils.FileDownloader;
import com.cppzeal.rdavatar.utils.FileUtil;
import com.cppzeal.rdavatar.data.Mp;
import com.cppzeal.rdavatar.ui.NotificationHelper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hookers {

    final static String CnQLog = "com.tencent.qphone.base.util.QLog";

    final static String CnNearbyPeoplePhotoUploadProcessor = "com.tencent.mobileqq.transfile.NearbyPeoplePhotoUploadProcessor";

    final static String CnTransferRequest = "com.tencent.mobileqq.transfile.TransferRequest";
    final static String CnTransFileControllerImpl = "com.tencent.mobileqq.transfile.api.impl.TransFileControllerImpl";

    static final String CnAppInterfaceFactory0 = "com.tencent.common.app.AppInterfaceFactory";
    static final String CnAppInterfaceFactory1 = "com.tencent.common.app.a";

//https://bbs.kanxue.com/thread-225190.htm
//https://forum.butian.net/share/2248
//方法声明在那里就hook那里. 而不是一股脑地hook子类

    // 外部声明 TAG 变量
    final static String Tag = "MulDexHookRes " + HookEntry.TAG;


    public static void log(String... args) {
        XposedBridge.log(Arrays.toString(args));
    }

    public static void cacheHook(XC_LoadPackage.LoadPackageParam loadPackageParam) throws IllegalAccessException {
        String TAG = "cacheHook ";


        StringWriter stringWriter = new StringWriter();
        Exception e = new Exception("error");
        e.printStackTrace(new PrintWriter(stringWriter));

        log(TAG, stringWriter.toString());
    }

    public static void MulDexHookRes(XC_LoadPackage.LoadPackageParam loadPackageParam) {


        final String TAG = "MulDexHookRes " + Tag;
        final Class<?>[] transFileControllerImpls = new Class<?>[1];
        final Class<?>[] TransferRequests = new Class<?>[1];
        final Class<?>[] nearbyPeoplePhotoUploadProcessors = new Class<?>[1];
        final Class<?>[] appInterfaceFactoryClasss = new Class<?>[2];
        XposedBridge.log(TAG + "noti");
        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);


                        //classloader
                        Context context = (Context) param.args[0];
                        ClassLoader classLoader = context.getClassLoader();// java.lang.BootClassLoader
                        XposedBridge.log(TAG + classLoader);

                        try {
                            // 获取ClassLoader中的"classes"字段，该字段是一个Hashtable，其中键是类名，值是Class对象
                            Field[] declaredFields = classLoader.getClass().getDeclaredFields();
                            for (Field declaredField : declaredFields) {
                                XposedBridge.log(TAG + declaredField.getName());

                            }
                            XposedBridge.log(TAG + "declaredField.getName()");

                            java.lang.reflect.Field classesField = ClassLoader.class.getDeclaredField("classes");
                            classesField.setAccessible(true);

                            // 获取Hashtable实例
                            @SuppressWarnings("unchecked")
                            java.util.Vector<Class<?>> classes = (java.util.Vector<Class<?>>) classesField.get(classLoader);

                            // 打印每个加载的类
                            for (Class<?> clazz : classes) {
                                XposedBridge.log(TAG + clazz.getName());
                            }
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            XposedBridge.log(TAG + e.getMessage());
                        }


                        boolean b = Mp.shouldUpdateNow(context);
                        if (!b) {
                            try {
                                long l = Mp.getLastUploadTime(context);
                                Date date = new Date(l * 1000);
                                NotificationHelper.sendNotification(context, "头像更换", "冷却中\n上次更新" + date.toString());

                            } catch (Exception e) {
                                XposedBridge.log(TAG + "noti " + e.getMessage());
                            }
                            return;
                        } else {
                            XposedBridge.log(TAG + "尝试更新");
                            try {
                                NotificationHelper.sendNotification(context, "头像更换", "尝试更新头像");

                            } catch (Exception e) {
                                XposedBridge.log(TAG + "noti" + e.getMessage());
                            }

                        }

                        if (appInterfaceFactoryClasss[0] == null) {
                            appInterfaceFactoryClasss[0] = XposedHelpers.findClassIfExists(
                                    CnAppInterfaceFactory1, classLoader);
                        }
                        if (appInterfaceFactoryClasss[1] == null) {
                            appInterfaceFactoryClasss[1] = XposedHelpers.findClassIfExists(
                                    CnAppInterfaceFactory0, classLoader);
                        }
                        XposedBridge.log(TAG + "appInterfaceFactory?" + appInterfaceFactoryClasss[0]);
                        XposedBridge.log(TAG + "appInterfaceFactory?" + appInterfaceFactoryClasss[1]);

                        if (transFileControllerImpls[0] == null) {
                            transFileControllerImpls[0] = XposedHelpers.findClassIfExists(
                                    CnTransFileControllerImpl, classLoader);
                        }

                        if (TransferRequests[0] == null) {
                            TransferRequests[0] = XposedHelpers.findClassIfExists(
                                    CnTransferRequest, classLoader);
                        }

                        if (nearbyPeoplePhotoUploadProcessors[0] == null) {
                            nearbyPeoplePhotoUploadProcessors[0] = XposedHelpers.findClassIfExists(
                                    CnNearbyPeoplePhotoUploadProcessor, classLoader);
                        }

                        Class<?> fileControllerClass = transFileControllerImpls[0];

                        Class<?> transferRequestClass = TransferRequests[0];

                        Class<?> NearbyPeoplePhotoUploadProcessor = nearbyPeoplePhotoUploadProcessors[0];

                        XposedHelpers.findAndHookMethod(NearbyPeoplePhotoUploadProcessor, "onSuccess",
                                new XC_MethodHook() {
                                    @Override
                                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                        super.afterHookedMethod(param);
                                        //success
                                        XposedBridge.log(TAG + "success");
                                        Mp.saveData(context);
                                        FileUtil.avatarChanged(context);
                                    }
                                });

                        Class<?> appInterfaceFactoryClass = null;

                        for (Class<?> interfaceFactoryClasss : appInterfaceFactoryClasss) {
                            try {
                                Method[] declaredMethods = interfaceFactoryClasss.getDeclaredMethods();
                                if (declaredMethods.length != 1) {
                                    XposedBridge.log(TAG + "appInterfaceFactoryClass 已经重构");
                                    continue;
                                }
                                appInterfaceFactoryClass = interfaceFactoryClasss;
                            } catch (Exception e) {
                                //pass
                            }

                        }

                        if (appInterfaceFactoryClass == null) {
                            XposedBridge.log(TAG + "no applia api");
                            return;
                        }

                        String name = appInterfaceFactoryClass.getDeclaredMethods()[0].getName();

                        XposedBridge.hookAllMethods(appInterfaceFactoryClass, name, new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                super.afterHookedMethod(param);
                                XposedBridge.log(TAG + "+appInterfaceFactoryClass afterHookedMethod");

                                Object result = param.getResult();
                                Object[] args = param.args;
                                final String str = "com.tencent.mobileqq";

                                if (args[1].equals(str)) {
                                    Storage instance = Storage.getInstance();
                                    if (instance.getAppInterface() == null) {
                                        //判断账号
//                                        try{
//                                            Object out=null;
//                                            for (Method method : result.getClass().getDeclaredMethods()) {
//                                                XposedBridge.log(TAG + "Uin " +method.getName());
//
//                                                if(method.getName().contains("getCurrentAccountUin")){
//                                                    method.setAccessible(true);
//                                                    out= method.invoke(instance.getAppInterface());
//                                                    XposedBridge.log(TAG + "Uin d " +out);
//                                                    break;
//                                                }
//                                            }
//                                            String targetAcc = Mp.getTargetAcc(context);
//                                            XposedBridge.log(TAG + "Uin targetAcc" +targetAcc);
//
//                                            if(!out.toString().contains(targetAcc)){
//                                                return;
//                                            }
//                                        }catch (Exception e){
//                                            XposedBridge.log(TAG + "Uin targetAcc err" +e.getMessage());
//
//                                        }


                                        instance.setAppInterface(result);
                                        XposedBridge.log(TAG + "resultset 设置 " + result + " " + instance.getClass().getClassLoader().hashCode());
                                    }
                                }

                            }
                        });

                        new Thread(
                                () -> {

                                    try {
                                        Thread.sleep(5 * 1000);
                                        Storage instance = Storage.getInstance();
                                        if (instance.getAppInterface() == null) {
                                            XposedBridge.log(TAG + "getAppInterface from cache failed");
                                            return;
                                        }
                                        Object transferRequestIns = null, fileControllerIns = null;
                                        Object NearbyPeoplePhotoUploadProcessorInstance = null;

                                        Thread currentThread = Thread.currentThread();
                                        XposedBridge.log(TAG + "'当前县城'" + currentThread.getName());
                                        if (instance == null) {
                                            return;
                                        }

                                        if (fileControllerIns == null) {

                                            for (Constructor<?> constructor : fileControllerClass.getConstructors()) {

                                                Class<?>[] parameterTypes = constructor.getParameterTypes();
                                                XposedBridge.log(TAG + "fileControllerClass 期待个数" + parameterTypes.length);
                                                if (parameterTypes.length == 1) {

                                                    constructor.setAccessible(true);

                                                    fileControllerIns = constructor.newInstance(instance.getAppInterface());
                                                    XposedBridge.log(TAG + "TransFileControllerImpl okkkkkkkkkk " + instance.getAppInterface());

                                                }
                                            }
                                        }

                                        if (transferRequestIns == null) {

                                            for (Constructor<?> constructor : transferRequestClass.getConstructors()) {
                                                Class<?>[] parameterTypes = constructor.getParameterTypes();
                                                XposedBridge.log(TAG + "transferRequestClass 期待个数" + parameterTypes.length);

                                                constructor.setAccessible(true);
                                                transferRequestIns = constructor.newInstance();
                                                XposedBridge.log(TAG + "TransFileControllerImpl okkkkkkkkkk");

                                            }

                                        }

                                        Field mLocalPath = transferRequestClass.getField("mLocalPath");
                                        mLocalPath.setAccessible(true);
                                        XposedBridge.log(TAG + "path");
                                        try {
                                            String name1 = FileUtil.getTargetPath(context).getPath();
                                            XposedBridge.log(TAG + "path" + name1);

                                        } catch (Exception e) {

                                        }

                                        XposedBridge.log(TAG + "path2");
                                        mLocalPath.set(transferRequestIns,
                                                FileUtil.getTargetPath(context).getPath()
                                        );
//                                            "/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent/MobileQQ/photo/qq.jpg"

                                        Field mFileType = transferRequestClass.getField("mFileType");
                                        mFileType.setAccessible(true);
                                        mFileType.set(transferRequestIns, 22);

                                        Field mIsUp = transferRequestClass.getField("mIsUp");
                                        mIsUp.setAccessible(true);
                                        mIsUp.set(transferRequestIns, true);

                                        XposedBridge.log(TAG + "创建对象中0");
                                        for (Constructor<?> constructor : NearbyPeoplePhotoUploadProcessor.getConstructors()) {
                                            XposedBridge.log(TAG + "创建对象中...");
                                            XposedBridge.log(TAG + constructor);
                                            XposedBridge.log(TAG + "fileControllerIns" + fileControllerIns);
                                            XposedBridge.log(TAG + "transferRequestIns" + transferRequestIns);
                                            try {
                                                constructor.setAccessible(true);
                                                NearbyPeoplePhotoUploadProcessorInstance = constructor.newInstance(fileControllerIns, transferRequestIns);
                                                XposedBridge.log(TAG + "创建对象 done " + NearbyPeoplePhotoUploadProcessorInstance.toString() + NearbyPeoplePhotoUploadProcessorInstance.getClass());

                                            } catch (Exception e) {
                                                XposedBridge.log(TAG + "init error " + e.getMessage());
                                            }
                                        }

                                        XposedBridge.log(TAG + "NearbyPeoplePhotoUploadProcessorInstance " + NearbyPeoplePhotoUploadProcessorInstance.toString());

                                        Method start = NearbyPeoplePhotoUploadProcessor.getDeclaredMethod("start");

                                        XposedBridge.log(TAG + NearbyPeoplePhotoUploadProcessorInstance);

                                        start.invoke(NearbyPeoplePhotoUploadProcessorInstance);

//                                        {
//                                            //inspect
//                                            try {
//                                                HashMap<String, Object> res0 = RefUtil.inspect(transferRequestClass, transferRequestIns);
//                                                XposedBridge.log(TAG + "transferRequestIns" + res0.toString());
//                                            } catch (Exception e) {
//                                                XposedBridge.log(TAG + e.getMessage());
//                                            }
//
//                                            try {
//                                                HashMap<String, Object> res1 = RefUtil.inspect(fileControllerClass, fileControllerIns);
//                                                XposedBridge.log(TAG + "fileControllerIns" + res1.toString());
//                                            } catch (Exception e) {
//                                                XposedBridge.log(TAG + e.getMessage());
//                                            }
//                                            XposedBridge.log(TAG + "mLocalPath0");
//                                        }
                                        XposedBridge.log(TAG + "resultset-use" + instance.getAppInterface());
                                    } catch (Exception e) {
                                        XposedBridge.log(TAG + e.getMessage());
                                    }

                                }).start();

                    }

                });
    }

    public static void HookRes(XC_LoadPackage.LoadPackageParam loadPackageParam) {

        final String TAG = "HookqRes ";
        XposedBridge.log(TAG + "filesDir");
        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);

                        Context context = (Context) param.args[0];

                        try {
                            FileUtil.prepareAvatar(context);

                        } catch (Exception e) {
                            XposedBridge.log(TAG + "filesDir" + e.getMessage());

                        }

                        boolean b = Mp.shouldDownloadNow(context);
                        if (b) {
                            XposedBridge.log(TAG + "下载AVa");
                            new Thread(
                                    new Runnable() {
                                        @Override
                                        public void run() {

                                            try {
                                                FileDownloader.downloadFile(context);
                                                Mp.onTryDownloading(context);
                                                NotificationHelper.sendNotification(context, "Download", "已下载一张图片");
                                            } catch (Exception e) {
                                                XposedBridge.log(TAG + "noti" + e.getMessage());
                                            }
                                        }
                                    }).start();
                        } else {
                            XposedBridge.log(TAG + "下载冷却");

                        }

                        boolean q = Mp.shouldRefreshInfo(context);
                        if (q) {
                            XposedBridge.log(TAG + "尝试拉取开发者通知");

                            new Thread(
                                    new Runnable() {
                                        @Override
                                        public void run() {

                                            try {
                                                String infoFromDev = FileDownloader.getInfoFromDev(context);
                                                if (!infoFromDev.contains("error")) {
                                                    Mp.onRetrievedInfoFromDev(context, infoFromDev);
                                                }
//                                        NotificationHelper.sendNotification(context, "InfoFromDev", "已下载一张图片");
                                            } catch (Exception e) {
                                                XposedBridge.log(TAG + "getInfoFromDev error" + e.getMessage());
                                            }
                                        }
                                    }).start();
                        } else {
                            XposedBridge.log(TAG + "开发者信息冷却");

                        }

                    }
                });
    }


    public static void SettingHook(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        //寄生于QQ设置
        final String TAG = "SettingHook ";

        final String CnQQSetting0 = "com.tencent.mobileqq.activity.QQSettingSettingActivity";
        final String KeyQQSetting0 = "FormSimpleItem";

        final String CnQQSetting1 = "com.tencent.mobileqq.activity.AccountManageActivity";
        final String KeyQQSetting1 = "QUISingleLineListItem";


        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Context context = (Context) param.args[0];


                        ClassLoader classLoader = context.getClassLoader();

                        Class<?> aClass = XposedHelpers.findClassIfExists(CnQQSetting0, classLoader);
                        if (aClass == null) {
                            aClass = XposedHelpers.findClassIfExists(CnQQSetting1, classLoader);
                        }
                        Class<?> CnFormSimpleItem = XposedHelpers.findClassIfExists("com.tencent.mobileqq.widget.FormSimpleItem", classLoader);

                        if (CnFormSimpleItem == null) {
                            XposedBridge.log(TAG + "CnFormSimpleItem" + " class not found");
                            return;
                        }

                        if (aClass == null) {
                            XposedBridge.log(TAG + CnQQSetting1 + " class not found");
                            return;
                        }

                        final Class<?> clazz = aClass;


                        XposedHelpers.findAndHookMethod(aClass, "doOnResume", new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                super.afterHookedMethod(param);
                                XposedBridge.log(TAG + "doOnResume");

                                Activity activity = (Activity) param.thisObject;
                                XposedBridge.log(TAG + activity);
                                ArrayList<Field> fields = new ArrayList<>();

                                for (Field declaredField : clazz.getDeclaredFields()) {
                                    XposedBridge.log(TAG + declaredField.getType() + " " + declaredField.getName());
                                    String name = declaredField.getType().getName();
                                    if (name.contains(KeyQQSetting0) || name.contains(KeyQQSetting1)) {
                                        XposedBridge.log(TAG + "found CnFormItemRelativeLayout");
                                        fields.add(declaredField);
                                    }
                                }

                                XposedBridge.log(TAG + "view " + fields.size());

                                try {
                                    for (Field field : fields) {
                                        field.setAccessible(true);
                                        ViewGroup viewGroup = (ViewGroup) field.get(activity);
                                        viewGroup.setVisibility(View.VISIBLE);
                                        XposedBridge.log(TAG + "view " + viewGroup);

                                        ViewGroup linearLayout = null;
                                        try {
                                            while (linearLayout == null) {
                                                viewGroup = (ViewGroup) viewGroup.getParent();
                                                XposedBridge.log(TAG + "view " + viewGroup.getClass());
                                                XposedBridge.log(TAG + "view " + (viewGroup instanceof LinearLayout));

                                                if (viewGroup.getClass() == LinearLayout.class) {
                                                    linearLayout = (ViewGroup) viewGroup;
                                                }
                                            }
                                        } catch (Exception e) {
                                            XposedBridge.log(TAG + "error q " + e.getMessage());
                                        }

                                        if (linearLayout != null) {
                                            XposedBridge.log(TAG + "adding");
                                            SettingUi.addSettingEntry(activity, linearLayout, CnFormSimpleItem);

                                        } else {
                                            XposedBridge.log(TAG + "paremt noll");
                                        }
                                        break;
                                    }
                                } catch (Exception e) {
                                    XposedBridge.log(TAG + "error " + e.getMessage());

                                }

                            }
                        });

                    }
                });
    }

    public static void HookQLog(XC_LoadPackage.LoadPackageParam loadPackageParam) {

        String TAG = "HookQLog ";

        XposedBridge.log(TAG);

        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);

                        Object result = param.thisObject;
                        XposedBridge.log(TAG + result);

                        //classloader
                        Context context = (Context) param.args[0];
                        ClassLoader classLoader = context.getClassLoader();
                        Class<?> aClass = XposedHelpers.findClassIfExists(CnQLog, classLoader);
                        if (aClass == null) {
                            return;
                        }

                        String[] strings = {"i", "d", "w", "e"};

                        for (String string : strings) {
                            XposedBridge.hookAllMethods(aClass, string, new XC_MethodHook() {
                                @Override
                                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                    super.beforeHookedMethod(param);
                                    StringBuilder msg = new StringBuilder();
                                    for (Object arg : param.args) {
                                        msg.append(arg.toString());
                                        msg.append(", ");
                                    }
                                    XposedBridge.log("fklog " + msg);
                                }
                            });
                        }

                        XposedHelpers.findAndHookMethod(aClass, "isColorLevel", new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                param.setResult(true);
                            }
                        });
                    }
                });

        if (true) {
            return;
        }
        XposedHelpers.findAndHookMethod(ClassLoader.class,
                "loadClass",
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        // 打印当前已经加载的类
//                        XposedBridge.log("clazz => " + param.getResult());
                        Class<?> clazz = (Class<?>) param.getResult();
                        XposedBridge.log(TAG + "tfcghjb");

                        if (clazz != null && clazz.getName().equals(CnQLog)) {

                            String[] strings = {"i", "d", "w", "e"};
                            XposedBridge.log(TAG + "t9416fcghjb");

                            for (String string : strings) {
                                XposedBridge.hookAllMethods(clazz, string, new XC_MethodHook() {
                                    @Override
                                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                        super.beforeHookedMethod(param);
                                        StringBuilder msg = new StringBuilder();
                                        for (Object arg : param.args) {
                                            msg.append(arg.toString());
                                            msg.append(", ");
                                        }
                                        XposedBridge.log("fklog " + msg);
                                    }
                                });
                            }

                            XposedHelpers.findAndHookMethod(clazz, "isColorLevel", new XC_MethodHook() {
                                @Override
                                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                    param.setResult(true);
                                }
                            });
                        }
                    }
                });

    }

}
