package com.example.passjun.Service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.example.passjun.App;
import com.example.passjun.util.my_MMKV;
import com.tencent.mmkv.MMKV;

import java.util.Set;

import es.dmoral.toasty.Toasty;


public class SkipAdService extends AccessibilityService {



    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

    }
    //当屏幕发生变化就会调用这个方法
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event == null || event.getSource() == null) return;

        // 1. 获取当前应用包名
        String packageName = event.getPackageName() != null ? event.getPackageName().toString() : "";
        if (packageName.isEmpty()) return;

        // 2. 检查是否在白名单中（白名单跳过）
        Set<String> whiteList = my_MMKV.getWhiteList();
        if (whiteList.contains(packageName)) {
            return;
        }

        // 3. 只处理窗口内容变化或窗口状态变化（广告常见触发时机）
        int eventType = event.getEventType();
        Log.d("zzyo", "监听页面: " + packageName + " | 事件类型: " + eventType);
        if (eventType != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED &&
                eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return;
        }

        // 4. 获取关键词列表
        String keywords = my_MMKV.getKeyWords();
        String[] keywordslist=keywords.trim().split("\\s+");
        if (keywordslist.length == 0) {
            return;
        }
        // 5. 递归查找可点击的“跳过”按钮
        findAndClickSkipButton(event.getSource(), keywordslist);
    }
    /**
     * 递归遍历节点树，查找包含关键词的可点击节点
     */
    private void findAndClickSkipButton(AccessibilityNodeInfo node, String[] keywords) {
        if (node == null) return;

        // 1. 检查当前节点的文本是否匹配关键词
        CharSequence text = node.getText();
        CharSequence desc = node.getContentDescription();

        boolean matched = false;
        for (String keyword : keywords) {
            if (keyword.isEmpty()) continue;
            if ((text != null && text.toString().contains(keyword)) ||
                    (desc != null && desc.toString().contains(keyword))) {
                matched = true;
                Log.i("zzyo", "✅ 找到关键词 [" + keyword + "]！\n文本内容: \"" + text + "\"\n描述内容: \"" + desc + "\"");
                break;
            }
        }

        // 2. 如果匹配，尝试找到可点击的祖先并点击
        if (matched) {
            AccessibilityNodeInfo clickableAncestor = findClickableAncestor(node);
            if (clickableAncestor != null) {
                Log.i("zzyo", "🖱️ 找到可点击区域，即将自动点击！");
                clickableAncestor.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                Log.i("zzyo", "✅ 已执行点击操作！");
                clickableAncestor.recycle(); // 注意：这里需要 recycle！
                return;
            }else{
                Log.w("zzyo", "⚠️ 找到了关键词，但未找到可点击的按钮或父容器！");
            }
        }

        // 3. 递归子节点
        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo child = null;
            try {
                child = node.getChild(i);
                if (child != null) {
                    findAndClickSkipButton(child, keywords);
                }
            } catch (Exception e) {
                Log.w("zzyo", "遍历子节点时出错，索引: " + i, e);
            } finally {
                if (child != null) {
                    try {
                        child.recycle();
                    } catch (Exception ignored) {}
                }
            }
        }

    }
    /**
     * 从当前节点向上查找最近的可点击祖先节点
     */
    private AccessibilityNodeInfo findClickableAncestor(AccessibilityNodeInfo node) {
        AccessibilityNodeInfo current = node;
        // 最多向上查找 3 层，防止无限循环（安全兜底）
        int depth = 0;
        while (current != null && depth < 3) {
            if (current.isClickable()) {
                return current; // 找到可点击祖先
            }
            current = current.getParent();
            depth++;
            // 注意：getParent() 返回的新对象，不需要 recycle 当前层（由调用方管理）
        }
        return null; // 未找到
    }
    @Override
    public void onInterrupt() {
        // 无障碍服务被中断时调用（通常不需要处理）
        Toasty.success(App.getAppContext(), "无障碍服务被终止", Toast.LENGTH_SHORT).show();
    }
    //无障碍服务（AccessibilityService）被系统“断开连接”时自动调用的方法
    @Override
    public boolean onUnbind(Intent intent) {
        Toasty.success(App.getAppContext(), "无障碍服务被断开", Toast.LENGTH_SHORT).show();
        return super.onUnbind(intent);
    }
}

