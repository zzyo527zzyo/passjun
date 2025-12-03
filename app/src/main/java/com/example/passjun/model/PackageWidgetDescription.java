package com.example.passjun.model;
import android.graphics.Rect;
import java.io.Serializable;
import java.util.Objects;


public class PackageWidgetDescription implements Serializable {

    private static final long serialVersionUID = 1L;

    private String packageName;     // 包名
    private String activityName;    // activity包名
    private String className;       // 类名
    private String idName;          // ID名称
    private String description;     // 描述
    private String text;            // 文本内容
    private Rect position;          // 坐标位置
    private boolean clickable;      // 是否可点击
    private boolean onlyClick;      // 是否只可以点击

    // 默认构造函数（无参）
    public PackageWidgetDescription() {
        this.position = new Rect();
    }

    // 全参构造函数
    public PackageWidgetDescription(
            String packageName,
            String activityName,
            String className,
            String idName,
            String description,
            String text,
            Rect position,
            boolean clickable,
            boolean onlyClick) {
        this.packageName = packageName;
        this.activityName = activityName;
        this.className = className;
        this.idName = idName;
        this.description = description;
        this.text = text;
        this.position = (position != null) ? new Rect(position) : new Rect(); // 深拷贝 Rect
        this.clickable = clickable;
        this.onlyClick = onlyClick;
    }

    // 复制构造函数（从另一个对象复制）
    public PackageWidgetDescription(PackageWidgetDescription other) {
        if (other == null) {
            this.position = new Rect();
            return;
        }
        this.packageName = other.packageName;
        this.activityName = other.activityName;
        this.className = other.className;
        this.idName = other.idName;
        this.description = other.description;
        this.text = other.text;
        this.position = (other.position != null) ? new Rect(other.position) : new Rect();
        this.clickable = other.clickable;
        this.onlyClick = other.onlyClick;
    }

    // ===== Getter 和 Setter =====

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Rect getPosition() {
        return position;
    }

    public void setPosition(Rect position) {
        this.position = (position != null) ? new Rect(position) : new Rect();
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public boolean isOnlyClick() {
        return onlyClick;
    }

    public void setOnlyClick(boolean onlyClick) {
        this.onlyClick = onlyClick;
    }

    // ===== 重写 equals / hashCode / toString （模拟 Kotlin data class）=====

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PackageWidgetDescription)) return false;
        PackageWidgetDescription that = (PackageWidgetDescription) o;
        return clickable == that.clickable &&
                onlyClick == that.onlyClick &&
                Objects.equals(packageName, that.packageName) &&
                Objects.equals(activityName, that.activityName) &&
                Objects.equals(className, that.className) &&
                Objects.equals(idName, that.idName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(text, that.text) &&
                Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                packageName, activityName, className, idName,
                description, text, position, clickable, onlyClick
        );
    }

    @Override
    public String toString() {
        return "PackageWidgetDescription{" +
                "packageName='" + packageName + '\'' +
                ", activityName='" + activityName + '\'' +
                ", className='" + className + '\'' +
                ", idName='" + idName + '\'' +
                ", description='" + description + '\'' +
                ", text='" + text + '\'' +
                ", position=" + position +
                ", clickable=" + clickable +
                ", onlyClick=" + onlyClick +
                '}';
    }
}