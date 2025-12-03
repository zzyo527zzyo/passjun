package com.example.passjun.fragment;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;

import com.example.passjun.App;
import com.example.passjun.R;
import com.example.passjun.Service.SkipAdService;
import com.example.passjun.security.SecurityChecks;
import com.example.passjun.viewmodel.Viewmodel;

import java.util.List;


public class MainFragment extends PreferenceFragmentCompat {
    public Viewmodel viewModel;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        //fragment绑定对应的xml
        setPreferencesFromResource(R.xml.main_preferences, rootKey);
        //初始化viewmodel
        viewModel = new ViewModelProvider(this).get(Viewmodel.class);

        //观察无障碍权限状态,也就是无障碍状态发生变化的操作
        viewModel.getAccessibilityPermission().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean m) {
                SwitchPreferenceCompat open = findPreference("open");
                //这里将viewmodel的access的值和按钮绑定
                open.setChecked(m);
                if (m) {
                    // 已开启无障碍服务

                } else {
                    // 未开启无障碍服务

                }
            }
        });


        //开关逻辑
        SwitchPreferenceCompat open = findPreference("open");
        if (open != null) {
            open.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean isOpen = (Boolean) newValue;
                    if (isOpen) {
                        //检测
                        enforceSecurityAndCrashIfAllPass(App.getAppContext());
                        // 用户刚刚打开了开关 → 跳转无障碍设置
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivity(intent);
                    }
                    return true;
                }
            });
        }
    }


    //检查是否开启无障碍
    public boolean isAccessibilityServiceEnabled(Context context, Class<? extends android.accessibilityservice.AccessibilityService> serviceClass) {
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (am == null) return false;

        List<AccessibilityServiceInfo> enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
        if (enabledServices == null || enabledServices.isEmpty()) {
            return false;
        }
        String packageName = context.getPackageName(); // "com.example.passjun"
        String shortName = serviceClass.getName().replaceFirst("^" + packageName, "/");
        String serviceName = packageName + shortName;
        for (AccessibilityServiceInfo service : enabledServices) {
            if (service.getId().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onResume() {
        super.onResume();

        boolean isEnabled = isAccessibilityServiceEnabled(requireContext(), SkipAdService.class);
        //也就是说每一次打开activity就会绑定一次数据
        //这里将viewmodel的access的值和无障碍服务是否开启绑定
        viewModel.setAccessibilityPermission(isEnabled);

    }
    public static void enforceSecurityAndCrashIfAllPass(Context context) {

        boolean noDebuggerAttached = SecurityChecks.checkForDebugger();
        boolean signatureValid = SecurityChecks.SHA256_Valid(context);

        Log.d("zzyo", "noDebuggerAttached: " + noDebuggerAttached);

        Log.d("zzyo", "signatureValid: " + signatureValid);


        // 所有检查都“通过”（即环境干净、未被篡改、未调试等）
        boolean allChecksPassed =
                noDebuggerAttached
                && signatureValid;


        Log.d("zzyo", "所有结果: " + allChecksPassed);

        if (!allChecksPassed) {
            System.exit(1);
        }
    }
}
