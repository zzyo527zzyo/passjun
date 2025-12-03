package com.example.passjun.Adpter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.example.passjun.databinding.RecycleItemWhitelistBinding;
import com.example.passjun.model.App_Info;


public class AppInfoAdapter extends ListAdapter<App_Info, AppInfoAdapter.MyViewHolder> {

    public AppInfoAdapter() {
        super(new AppInfoDiffCallback());
    }

    // DiffUtil 回调类：判断两个 AppInfo 是否相同
    public static class AppInfoDiffCallback extends DiffUtil.ItemCallback<App_Info> {
        @Override
        public boolean areItemsTheSame(App_Info oldItem,App_Info newItem) {
            return oldItem.getPackageName().equals(newItem.getPackageName());
        }

        @Override
        public boolean areContentsTheSame(App_Info oldItem,App_Info newItem) {
            return oldItem.equals(newItem);
        }
    }

    // ViewHolder：持有 ViewBinding
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ViewBinding binding;

        public MyViewHolder(ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewBinding getBinding() {
            return binding;
        }
    }

    // 创建 ViewHolder（通过RecycleItemWhitelistBinding加载 item 布局）
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecycleItemWhitelistBinding binding = RecycleItemWhitelistBinding.inflate(inflater, parent, false);
        return new MyViewHolder(binding);
    }

    // 绑定数据到视图
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        App_Info appInfo = getItem(position);
        RecycleItemWhitelistBinding binding = (RecycleItemWhitelistBinding) holder.getBinding();

        binding.check.setChecked(appInfo.getChecked());
        binding.icon.setImageDrawable(appInfo.getIcon());
        binding.name.setText(appInfo.getApplicationName());

        // 点击事件：切换选中状态
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appInfo.setChecked(!appInfo.getChecked());
                binding.check.setChecked(appInfo.getChecked());
            }
        });
    }
}