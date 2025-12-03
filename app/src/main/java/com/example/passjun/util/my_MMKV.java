package com.example.passjun.util;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import com.tencent.mmkv.MMKV;
import com.example.passjun.App;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

//mmkv数据存储类
public final class my_MMKV {

    // 私有构造函数，防止实例化
    private my_MMKV() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }


    //获取关键词
    public static String getKeyWords() {
        return MMKV.defaultMMKV().decodeString("keyword","跳过 暂不更新 退出 立即体验 关闭 领取");
    }

    //设置关键词
    public static void setKeyWords(String keyWords) {
        MMKV.defaultMMKV().encode("keyword", keyWords);
    }

    //获取倍速
    public static String getSpeed() {
        return MMKV.defaultMMKV().decodeString("speed");
    }

    //设置倍速
    public static void setSpeed(String speed) {
        MMKV.defaultMMKV().encode("speed", speed);
    }

    //设置白名单
    public static void setWhiteList( Set<String> list) {
        MMKV.defaultMMKV().encode("whitelist", list);
    }


    //获取白名单的包名集合
    public static Set<String> getWhiteList() {
        Set<String> stored = MMKV.defaultMMKV().decodeStringSet("whitelist");
        //如果有值就直接返回
        if (stored != null && !stored.isEmpty()) {
            return stored;
        }

        // 第一次获取：生成默认白名单（系统应用 + 自身）
        PackageManager packageManager = App.getAppContext().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL);

        Set<String> pkgSystems = new HashSet<>();
        for (ResolveInfo info : resolveInfoList) {
            ApplicationInfo appInfo = info.activityInfo.applicationInfo;
            //获取系统的应用
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                pkgSystems.add(appInfo.packageName);
            }
        }
        // 添加当前 App 自身
        pkgSystems.add(App.getAppContext().getPackageName());
        //设置白名单
        MMKV.defaultMMKV().encode("whitelist", pkgSystems);

        return pkgSystems;
    }


}