package com.example.passjun.activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.example.passjun.R;
import com.example.passjun.databinding.ActivityMainBinding;
import com.example.passjun.fragment.MainFragment;
import com.example.passjun.security.SecurityChecks;
import es.dmoral.toasty.Toasty;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inflate() 创建 binding 实例
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //绑定对应的fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(binding.frameLayout.getId(),new MainFragment());
        fragmentTransaction.commit();
        // 设置标题
        getSupportActionBar().setTitle("pass君");



        //设置关于的点击事件
        binding.about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.success(MainActivity.this, "开发者:卑微王二狗\nhttps://github.com/zzyo527zzyo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //重写方法，处理actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); // 加载菜单
        MenuItem item = menu.findItem(R.id.setting);//获取控件
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // 跳转到设置页面
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                return true;
            }
        });
        return true;
    }



}