# 设备管理器测试功能

**DevicePolicyManager**，设备策略管理器。顾名思义，**DevicePolicyManager**为Android系统的管理提供了一套策略，有三种方案

- Device Administration, 设备管理员
- Profile Owner, 配置文件所有者
- Device Owner, 设备所有者

### Device Admin

过用户授权自己的应用设备管理权限后，可以在代码中修改一些系统设置，需要在应用中配置一个xml，xml中声明相应的权限，这些权限基本代表了**DeviceAdmin**可以使用的能力

```xml
<?xml version="1.0" encoding="utf-8"?>
<device-admin xmlns:android="http://schemas.android.com/apk/res/android">
  <uses-policies>
    <!-- 密码长度限制 -->
    <limit-password />
    <!-- 监控屏幕解锁尝试次数 -->
    <watch-login />
    <!-- 更改锁屏密码 -->
    <reset-password />
    <!-- 锁屏(可实现一键锁屏功能) -->
    <force-lock />
    <!-- 清除所有数据 -->>
    <wipe-data />
    <!-- 设置锁屏密码的有效期 -->
    <expire-password />
    <!-- 设置存储设备加密 -->
    <encrypted-storage />
    <!-- 禁用相机 -->
    <disable-camera />
  </uses-policies>
</device-admin>
```



### ProfileOwner

**ProfileOwner** 译为配置文件所有者，在Android5.0系统推出。**ProfileOwner**涵盖了所有**DeviceAdmin**用户的管理能力。Google为了细化行业领域的管理而推出了这一组API，也被称为**Android for work**,旨在让用户在体验上可以轻松的兼顾生活和工作，可以将你的个人信息和工作信息等进行分类，随时查看

具体功能如下

- 隐藏应用，可停用制定应用并且不再界面显示，除非调用相应API恢复可用，否则该应用永远无法运行。可以用来开发应用黑白名单功能。

- 禁止卸载应用，被设置为禁止卸载的应用将成为受保护应用，无法被用户卸载，除非取消保护。

- 复用系统APP

- 修改系统设置

- 调节静音

- 修改用户图标

- 修改权限申请的策略

- 限制指定应用的某些功能

- 允许辅助服务

- 允许输入法服务

- 禁止截图

- 禁止蓝牙访问联系人

  

### DeviceOwner

**DeviceOwner**, 设备所有者，Android5.0引入。同样的，DeviceOwner涵盖了所有**DeviceAdmin**用户的管理能力，是一类特殊的设备管理员，具有在设备上创建和移除辅助用户以及配置全局设置的额外能力。**DeviceOwner**完善了行业用户的**MDM(Mobile Device Manager)**行业管理能力，主要能力如下：

- 设置网络时间同步, 设置后无法从Settings取消
- 用户管理, 创建用户、删除用户等
- 管理账号系统
- 清除锁屏
- 设置Http代理
- 禁止状态栏
- 通知等待更新
- 禁用相机
- 隐藏应用
- 禁止卸载应用
- 复用系统APP
- 获取wifi地址
- 重启系统







## Demo演示Device Admin

- 激活设备管理器
- 取消激活设备管理器
- 设置解锁方式 不需要激活就可以运行
- 设置解锁方式 需要激活设备管理器
- 立即锁屏
- 设置5秒后锁屏 充电状态下不管用
- 清除数据  恢复出厂设置
- 修改锁密码(PIN)
- 禁用相机

## 开启设备管理器流程

1. 编写广播接收器DeviceReceiver继承DeviceAdminReceiver

   ```java
   public class DeviceReceiver extends DeviceAdminReceiver {
       @Override
       public void onEnabled(Context context, Intent intent) {
           // 设备管理：可用
           Toast.makeText(context, "设备管理：可用", Toast.LENGTH_SHORT).show();
       }
   @Override
   public void onDisabled(final Context context, Intent intent) {
       // 设备管理：不可用
       Toast.makeText(context, "设备管理：不可用", Toast.LENGTH_SHORT).show();
   
    }
   }
   ```
   

2. AndroidManifest注册广播接收器

   ```xml
        <!--设备管理 begin -->
           <receiver
               android:name=".DeviceReceiver"
               android:permission="android.permission.BIND_DEVICE_ADMIN">
               <meta-data
                   android:name="android.app.device_admin"
                   android:resource="@xml/device_admin" />
               <intent-filter>
                   <action android:name="android.app.action.DEVICE_ADMIN_ENABLED" />
                   <action                      android:name="android.app.action.DEVICE_ADMIN_DISABLE_REQUESTED" />
                   <action android:name="android.app.action.DEVICE_ADMIN_DISABLED" />
   
                   <category android:name="android.intent.category.HOME" />
               </intent-filter>
           </receiver>
           <!--设备管理 end -->
   ```

   

3. xml下添加文件device_admin.xml

   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <device-admin xmlns:android="http://schemas.android.com/apk/res/android">
       <uses-policies>
           <!-- 限制密码类型 -->
           <limit-password />
           <!-- 监控登录尝试 -->
           <watch-login />
           <!-- 重置密码 -->
           <reset-password />
           <!--锁屏 -->
           <force-lock />
           <!-- 恢复出厂设置 -->
           <wipe-data />
           <!--禁用相机-->
           <disable-camera />
           
           <disable-keyguard-features />
   
           <set-global-proxy />
   
           <!-- 设置锁屏密码的有效期 -->
           <expire-password />
   
       </uses-policies>
   </device-admin>
   ```

   

4. 激活设备管理器

   ```java
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
   ```

   

5. 移除设备管理器

   ```java
      // 取消激活设备管理器
       public void disableDeviceManager() {
           devicePolicyManager.removeActiveAdmin(componentName);
       }
   
   ```

   
## 详细代码

https://github.com/ddssingsong/DevicePolicyManager




