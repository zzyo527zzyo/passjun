package com.example.passjun.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Viewmodel extends ViewModel {
    // LiveData 在数据发生变化时，自动通知处于活跃状态的 UI 组件（如 Activity、Fragment）进行更新
    // 无障碍权限状态
    private final MutableLiveData<Boolean> mAccessibilityPermission = new MutableLiveData<>();


    public MutableLiveData<Boolean> getAccessibilityPermission() {
        return mAccessibilityPermission;
    }
    public void setAccessibilityPermission(boolean enabled) {
        mAccessibilityPermission.setValue(enabled);
    }

}

