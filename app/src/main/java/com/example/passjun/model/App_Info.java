package com.example.passjun.model;

import android.graphics.drawable.Drawable;

import java.util.Objects;

public class App_Info implements Comparable<App_Info>{
    private String packageName; //包名
    private String applicationName; //应用名
    private Drawable Icon;//icon
    private Boolean isChecked;//是否是白名单应用

    public App_Info(String packageName, String applicationName, Drawable icon, Boolean isChecked) {
        this.packageName = packageName;
        this.applicationName = applicationName;
        Icon = icon;
        this.isChecked = isChecked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        App_Info appInfo = (App_Info) o;
        return Objects.equals(packageName, appInfo.packageName) && Objects.equals(applicationName, appInfo.applicationName) && Objects.equals(Icon, appInfo.Icon) && Objects.equals(isChecked, appInfo.isChecked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(packageName, applicationName, Icon, isChecked);
    }

    @Override
    public int compareTo(App_Info other) {
        if (isChecked && !other.isChecked) {
            return -1;
        } else if (!isChecked && other.isChecked) {
            return 1;
        }
        return 0;
    }
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Drawable getIcon() {
        return Icon;
    }

    public void setIcon(Drawable icon) {
        Icon = icon;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }


}
