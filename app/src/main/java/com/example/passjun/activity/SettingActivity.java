package com.example.passjun.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.net.Uri;
import java.io.File;
import com.example.passjun.R;
import com.example.passjun.fragment.SettingsFragment;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //绑定对应的fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,new SettingsFragment());
        fragmentTransaction.commit();
        // 设置标题
        getSupportActionBar().setTitle("设置");
        //创建一个空的构建器
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        //在构建器中启用了 URI 暴露检测
        builder.detectFileUriExposure();
        //应用该策略
        StrictMode.setVmPolicy(builder.build());
    }

    //重写方法，处理actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu); // 加载菜单
        MenuItem item = menu.findItem(R.id.share);//获取控件
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // 获取当前应用的 APK 文件路径
                File apkFile = new File(SettingActivity.this.getApplicationContext().getPackageResourcePath());
                // 创建分享 Intent
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(apkFile));
                // 启动分享
                SettingActivity.this.startActivity(intent);
                return true;
            }
        });
        return true;
    }
}