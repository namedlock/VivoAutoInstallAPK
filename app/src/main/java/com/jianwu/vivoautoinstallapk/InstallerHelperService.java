package com.jianwu.vivoautoinstallapk;

import android.accessibilityservice.AccessibilityService;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

public class InstallerHelperService extends AccessibilityService {

    private boolean found = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.i("__auto_install_apk__", "onAccessibilityEvent: "+event.getPackageName());

        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode == null) return;

        if (event.getPackageName().equals("com.vivo.secime.service")
                || event.getPackageName().equals("com.android.packageinstaller")
                || event.getPackageName().equals("com.bbk.account")
        ) {
            //vivo账号密码
            String password = (String) SharePreferencesUtils.getParam(getApplicationContext(), AppConstants.KEY_PASSWORD, "");
            Log.i("__auto_install_apk__", "get stored password: "+password);

            if (!TextUtils.isEmpty(password)) {
                fillPassword(rootNode, password);
            }
        }
        findAndClickView(rootNode);
    }

    @Override
    protected void onServiceConnected() {
        Log.i("__auto_install_apk__", "onServiceConnected: ");
    }

    /**
     * 自动填充密码
     */
    private void fillPassword(AccessibilityNodeInfo rootNode, String password) {
        AccessibilityNodeInfo editText = rootNode.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
        if (editText == null) return;

        Log.i("__auto_install_apk__", "fillPassword packageName: "+editText.getPackageName());

        if(editText.getPackageName().equals("com.bbk.account") ){
            if(editText.getClassName().equals("android.widget.EditText")){
                Bundle arguments = new Bundle();
                arguments.putCharSequence(AccessibilityNodeInfo
                        .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, password);
                editText.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
            }
        }
    }

    /**
     * 查找按钮并点击
     */
    private void findAndClickView(AccessibilityNodeInfo rootNode) {
        List<AccessibilityNodeInfo> nodeInfoList = new ArrayList<>();
        nodeInfoList.addAll(rootNode.findAccessibilityNodeInfosByText("确定"));
        List<AccessibilityNodeInfo> continueNodeInfoList = rootNode.findAccessibilityNodeInfosByText("继续安装");
        if (!continueNodeInfoList.isEmpty()) {
            found = true;
        }
        nodeInfoList.addAll(rootNode.findAccessibilityNodeInfosByText("安装"));
        for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }

        List<AccessibilityNodeInfo> openNodeInfoList = new ArrayList<>(rootNode.findAccessibilityNodeInfosByText("打开"));
        for (AccessibilityNodeInfo nodeInfo : openNodeInfoList) {
            if (found) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
            found = false;
        }
    }

    @Override
    public void onInterrupt() {
    }
}
