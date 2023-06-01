### 基于Android Studio Flamingo | 2022.2.1 Patch 2
1. update target sdk version to 33 and test againt vivo Y70s
2. settings - Build, Execution, Deployment - Build Tool - Gradle - Gradle JDK = embeded java 17

### 使用方法
1.  初次进入首页输入你的vivo账号密码，键盘确认保存
2. 手动进入系统辅助功能开关，找到“vivo 免输入密码自动安装apk”，打开它，服务就启动了
3. 设置app不被关闭，vivo 多任务上锁，搞定

### 注意事项
gradle.properties 需要增加配置
android.injected.testOnly=false
