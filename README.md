##  1 导入文件：
###  1.1 导入SDK
#### 下载[AdhiSdk](http://admo5-static.2bx.com/adhi/android/aar/adhi-sdk-v2.1.zip "AdSdk")，复制到应用Module/libs文件夹（没有的话须手动创建）并将以下代码添加到您app的build.gradle中:
```xml
 repositories {
        flatDir { dirs 'libs' }
    }
android {
	defaultConfig {
	ndk {
		abiFilters "armeabi-v7a"
}
}
    api(name: 'adapt-1.3.3', ext: 'aar')
    api(name: 'commonbase-1.0.2', ext: 'aar')
    api(name: 'discovery-2.0.17', ext: 'aar')
    api(name: 'GDTSDK.unionNormal.4.211.1081', ext: 'aar')
    api(name: 'shanhuAD-1.3.1', ext: 'aar')
    api(name: "ks_adsdk-2.6.8", ext: 'aar')
```
##### 将40805.dat文件放至assets文件夹下，注意禁止修改文件名否则可能导致，初始化失败.
![示例](https://www.showdoc.cc/server/api/common/visitfile/sign/f584887861d4dcd0f50593904acf8e80?showdoc=.jpg "示例")
#### 提示：adhi Sdk需要接入android.support.v4：
```xml
implementation 'com.android.support:support-v4:28.0.0'
implementation 'com.android.support:appcompat-v7:28.0.0'
implementation 'com.android.support:recyclerview-v7:28.0.0'
```
### 1.2添加对应的权限
```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission  android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES"/>
```
## 2适配不同版本
### 2.1 适配7.0及以上
##### 如果您的应用需要在Anroid7.0及以上环境运行，请在AndroidManifest中添加如下代码：
```xml
<provider
        android:name="android.support.v4.content.FileProvider"
        android:authorities="${applicationId}.fileprovider"
        android:exported="false"
        android:grantUriPermissions="true">
        <meta-data
         android:name="android.support.FILE_PROVIDER_PATHS"
         android:resource="@xml/gdt_file_path" />
    </provider>
```
##### 在res/xml目录下，新建一个xml文件gdt_file_path，在该文件中添加如下代码：
####提示下载地址不要更改
```xml
<?xml version="1.0" encoding="utf-8"?>
    <paths   xmlns:android="http://schemas.android.com/apk/res/android">
       <external-cache-path
        		name="gdt_sdk_download_path1"
       		 	path="com_qq_e_download" />
    <cache-path
        		name="gdt_sdk_download_path2"
        		path="com_qq_e_download" />
          </paths>
		  <external-path name="download" path="."/>
	
```
### 2.2为了适配andriod 9.0,请在入口的Appcation 添加
### android:usesCleartextTraffic=”true”

## 3 SDK集成部署介绍
### 3.1初始化，在application添加如下代码：
````xml
mBresult = TMSDKContext.init(this, new AbsTMSConfig() {
@Override
public String getServerAddress() {
return ''mazutest.3g.qq.com";
}
});
//初始化广告、H5Browser 外部下载器 详情可以查看demo
ShanHuAD.init(TMSDKContext.getApplicationContext(),
new H5Browser(),TMSDKContext.getCoinProductId());
````
##### 说明H5Browser 为H5BrowserListener实现类，实现下载、安装、激活、以及上报操作。下载[H5Browser](http://admo5-static.2bx.com/adhi/android/aar/H5Browser.java "H5Browser")
##### H5BrowserListener接口说明
````xml
public interface H5BrowserListener {
    //var1：跳转H5路径
    void openH5(String var1);
	// 封装下载url和广告数据
    void openAppDetailPage(AdMetaInfo var1, AdDisplayModel var2);
}
````
## 4广告拉取
#### 流程拉取任务->拉取对应广告->展示广告
#### 具体操作请查看示例
|   广告类型| 类名  |示例与文档|
| ------------ | ------------ |------------|
|   激励视频广告 | RewardVideo  |[示例](https://www.showdoc.cc/h5sdk?page_id=4730957262897097 "示例")
|   banner广告 | ADBanner  |[示例](https://www.showdoc.cc/h5sdk?page_id=4730564936861894 "示例")
|  开屏广告 | ADSplashImage  |[示例](https://www.showdoc.cc/h5sdk?page_id=4730339527768742 "示例")
|   下载广告 |  ADDownLoad |[示例](https://www.showdoc.cc/h5sdk?page_id=4730163365561201 "示例")
|   卡券广告 |  ADCard |[示例](https://www.showdoc.cc/h5sdk?page_id=4731147540076884 "示例")
|短视频广告|ADContentAlliance|[示例](https://www.showdoc.cc/h5sdk?page_id=4899395230450472 "示例")
|信息流广告|ADFeed |[示例](https://www.showdoc.cc/h5sdk?page_id=4898645419157139 "示例")
|全屏视频广告|ADFullScreenVideo |[示例](https://www.showdoc.cc/h5sdk?page_id=4899578977418138 "示例")
## 5广告统计测试及广告切换正式环境
### 1.1广告上报数据查询：
##### 接入方集中测试广告，并提供如下信息
测试时间：X日-X点X分~X点X分
测试imei：
测试guid：
|  |102卡券广告   |103下载广告   |104视频广告   |131视频广告   |134下载广告   | 125开屏广告  |130banner广告  |
| ------------ | ------------ | ------------ | ------------ | ------------ | ------------ | ------------ | ------------ |
|曝光   |   |   |   |   |   |   |   |
|   点击|   |   |   |   |   |   |   |
|   开始下载|   |   |   |   |   |   |   |
|   安装|   |   |   |   |   |   |   |
|   激活|   |   |   |   |   |   |   |
|   视频播放|   |   |   |   |   |   |   |
##### 注意事项：guid可以通过日志进行过滤GUID获取，每次安装guid值会变化。一次测试请误多次安装，导致数据不准确。[word模版下载](http://admo5-static.2bx.com/adhi/android/aar/ad-report-v2.1.xlsx "word模版下载")
##### 提示：131、134属于测试环境才拥有广告类型
### 1.2切换环境
#### 当测试没问题之后，替换成正式参数的40805.dat文件，同时初始化将地址改为"mazu.3g.qq.com"如下：
```java
  mBresult = TMSDKContext.init(this, new AbsTMSConfig() {
            @Override
            public String getServerAddress() {
                return "mazu.3g.qq.com";
            }
        });
```
### 1.3重新正式版广告上报数据查询，测试方式与1.1一致。






