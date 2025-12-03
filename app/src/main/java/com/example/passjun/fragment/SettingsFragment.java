package com.example.passjun.fragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.EditTextPreference;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import androidx.preference.PreferenceFragmentCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passjun.Adpter.AppInfoAdapter;
import com.example.passjun.R;
import com.example.passjun.model.App_Info;
import com.example.passjun.util.my_MMKV;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SettingsFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState,  String rootKey) {
        //fragment绑定对应的xml
        setPreferencesFromResource(R.xml.settings_preferences, rootKey);


        //设置关键词的逻辑
        EditTextPreference KeyWord = findPreference("keyword");

        // 设置到 Preference 上（作为初始显示值）
        KeyWord.setText(my_MMKV.getKeyWords());
        KeyWord.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue instanceof String) {
                    String trimmedValue = ((String) newValue).trim();
                    //存入mmkv
                    my_MMKV.setKeyWords(trimmedValue);

                }
                return true;
            }
        });
        ListPreference speed = findPreference("speed");
        speed.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String selectedSpeed = (String) newValue;
                // 保存到 MMKV
                my_MMKV.setSpeed(selectedSpeed);
                return true;
            }
        });





        //点击事件展示白名单
        Preference whiteList = findPreference("whitelist");
        whiteList.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                // 显示加载对话框
                ProgressDialog progressDialog = new ProgressDialog(requireContext());
                progressDialog.setMessage(getString(R.string.loading_app_list));
                progressDialog.setCancelable(false);
                progressDialog.show();

                // 启动后台线程加载应用列表
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler mainHandler = new Handler(Looper.getMainLooper());

                executor.execute(() -> {
                    // 后台：获取应用列表
                    List<App_Info> appInfoList = getAppInfoList(requireContext());

                    // 切回主线程更新 UI
                    mainHandler.post(() -> {
                        progressDialog.dismiss();
                        showAppInfoListDialog(preference, appInfoList);
                    });
                });

                return true;
            }
        });
    }

    // 展示白名单列表对话框
    private void showAppInfoListDialog(Preference preference, List<App_Info> appInfoList) {
        // 1. 加载对话框布局
        View dialogLayout = getLayoutInflater().inflate(R.layout.whitelist_layout, null, false);

        // 2. 初始化 RecyclerView
        RecyclerView rvWhitelist = dialogLayout.findViewById(R.id.rv_whitelist);
        rvWhitelist.setLayoutManager(new LinearLayoutManager(requireContext()));

        // 3. 设置 Adapter
        AppInfoAdapter appInfoAdapter = new AppInfoAdapter();
        rvWhitelist.setAdapter(appInfoAdapter);
        appInfoAdapter.submitList(appInfoList);

        // 4. 创建并显示对话框
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogLayout)
                .create();
        dialog.show();

        // 5. 对话框关闭时重新启用 Preference
        dialog.setOnDismissListener(dialogInterface -> {
            preference.setEnabled(true);
        });

        // 6. 取消按钮
        Button btnCancel = dialogLayout.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // 7. 确认按钮,按了确认就有新的白名单，也就要重新进行mmkv赋值
        Button btnConfirm = dialogLayout.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(v -> {
            // 收集选中的包名
            Set<String> pkgWhitelistNew = new HashSet<>();
            for (App_Info app : appInfoList) {
                if (app.getChecked()) {
                    pkgWhitelistNew.add(app.getPackageName());
                }
            }

            // 保存到配置
            my_MMKV.setWhiteList(pkgWhitelistNew);



            // 关闭对话框
            dialog.dismiss();
        });
    }

    //先获取所有应用名，然后判断是否在白名单内，转化成自定义的类appinfo。
    public List<App_Info> getAppInfoList(Context context) {
        PackageManager packageManager = context.getPackageManager();

        //获取所有有启动器，在主页面有图标的应用infolist
        List<String> appNameList = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL);
        //获取所有应用包名添加进去appnamelist
        for (ResolveInfo info : resolveInfoList) {
            appNameList.add(info.activityInfo.packageName);
        }

        // 构建我自己定义的类AppInfo列表
        List<App_Info> appInfoList = new ArrayList<>();
        Set<String> pkgWhitelist = my_MMKV.getWhiteList();

        for (String pkgName : appNameList) {
            try {
                ApplicationInfo appInfo = packageManager.getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
                String appName = packageManager.getApplicationLabel(appInfo).toString();
                // 注意：getApplicationIcon 可能较重，但通常可接受
                android.graphics.drawable.Drawable icon = packageManager.getApplicationIcon(appInfo);

                boolean isChecked = pkgWhitelist.contains(pkgName);

                App_Info item = new App_Info(pkgName, appName, icon, isChecked);
                appInfoList.add(item);
            } catch (PackageManager.NameNotFoundException e) {

            }
        }

        // 3. 排序：白名单在前，然后按应用名升序（A-Z）
        Collections.sort(appInfoList);
        return appInfoList;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}