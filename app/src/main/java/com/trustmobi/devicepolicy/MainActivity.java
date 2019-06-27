package com.trustmobi.devicepolicy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.trustmobi.devicem.DeviceManger;

public class MainActivity extends AppCompatActivity {

    private DeviceManger deviceManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceManger = new DeviceManger(this);
    }

    // 激活设备管理器
    public void enableDeviceManager(View view) {
        deviceManger.enableDeviceManager();
    }

    // 取消激活设备管理器
    public void disableDeviceManager(View view) {
        deviceManger.disableDeviceManager();
    }

    // 设置解锁方式 不需要激活就可以运行
    public void setPwdType(View view) {
        deviceManger.setNewPwd();
    }

    // 设置解锁方式 需要激活
    public void setPwdType1(View view) {
        deviceManger.setLockMethod();
    }

    // 立即锁屏
    public void lockNow(View view) {
        deviceManger.lockNow();
    }

    // 设置5秒后锁屏
    public void lockOnTime(View view) {
        deviceManger.lockByTime(5000);
    }

    // 清除数据  恢复出厂设置
    public void onWipeData(View view) {
        deviceManger.wipeData();
    }


    // 修改锁密码
    public void onResetPwd(View view) {
        deviceManger.resetPassword("123456");
    }


    // 禁用相机
    public void onDisableCamera(View view) {
        deviceManger.disableCamera(true);
    }

    // 解除禁用相机
    public void enableCamera(View view) {
        deviceManger.disableCamera(false);
    }
}
