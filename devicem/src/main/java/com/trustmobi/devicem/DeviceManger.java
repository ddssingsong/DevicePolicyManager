package com.trustmobi.devicem;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by dds on 2019/6/27.
 * android_shuai@163.com
 */
public class DeviceManger {

    private DevicePolicyManager devicePolicyManager;
    private Context mContext;
    private ComponentName componentName;

    public DeviceManger(Context context) {
        mContext = context;
        //获取设备管理服务
        devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        //DeviceReceiver 继承自 DeviceAdminReceiver
        componentName = new ComponentName(context, DeviceReceiver.class);
    }


    // 测试
    public void test(Context context) {
        DevicePolicyManager dm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);

    }

    // 激活设备管理器
    public void enableDeviceManager() {
        //判断是否激活  如果没有就启动激活设备
        if (!devicePolicyManager.isAdminActive(componentName)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    mContext.getString(R.string.dm_extra_add_explanation));
            mContext.startActivity(intent);
        } else {
            Toast.makeText(mContext, "设备已经激活,请勿重复激活", Toast.LENGTH_SHORT).show();
        }
    }

    // 取消激活设备管理器
    public void disableDeviceManager() {
        devicePolicyManager.removeActiveAdmin(componentName);

    }

    // 设置解锁方式 不需要激活就可以运行
    public void setNewPwd() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
        mContext.startActivity(intent);
    }

    // 设置解锁方式 需要激活设备管理器（配置策略）
    public void setLockMethod() {
        // PASSWORD_QUALITY_ALPHABETIC
        // 用户输入的密码必须要有字母（或者其他字符）。
        // PASSWORD_QUALITY_ALPHANUMERIC
        // 用户输入的密码必须要有字母和数字。
        // PASSWORD_QUALITY_NUMERIC
        // 用户输入的密码必须要有数字
        // PASSWORD_QUALITY_SOMETHING
        // 由设计人员决定的。
        // PASSWORD_QUALITY_UNSPECIFIED
        // 对密码没有要求。
        if (devicePolicyManager.isAdminActive(componentName)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
            devicePolicyManager.setPasswordQuality(componentName,
                    DevicePolicyManager.PASSWORD_QUALITY_NUMERIC);
            mContext.startActivity(intent);
        } else {
            Toast.makeText(mContext, "请先激活设备", Toast.LENGTH_SHORT).show();
        }
    }

    //立刻锁屏
    public void lockNow() {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.lockNow();
        } else {
            Toast.makeText(mContext, "请先激活设备", Toast.LENGTH_SHORT).show();
        }
    }

    // 设置多长时间后锁屏
    public void lockByTime(long time) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.setMaximumTimeToLock(componentName, time);
        } else {
            Toast.makeText(mContext, "请先激活设备", Toast.LENGTH_SHORT).show();
        }
    }

    // 恢复出厂设置
    public void wipeData() {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
        } else {
            Toast.makeText(mContext, "请先激活设备", Toast.LENGTH_SHORT).show();
        }
    }

    // 设置密码锁
    public void resetPassword(String password) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.resetPassword(password,
                    DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
        } else {
            Toast.makeText(mContext, "请先激活设备", Toast.LENGTH_SHORT).show();
        }
    }

    // 禁用相机
    public void disableCamera(boolean disabled) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.setCameraDisabled(componentName, disabled);
        } else {
            Toast.makeText(mContext, "请先激活设备", Toast.LENGTH_SHORT)
                    .show();
        }
    }


}
