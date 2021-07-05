## 概述
AndroidShareFile 是一个整合Android文件相关的Demo。
包含以下功能
1. 接收外部其他应用的分享文件；
2. 应用内文件管理，包括查看、分享、删除等操作；

应用了以下知识
1. 文件分享(包括接收外部分享、文件对外分享)
2. Android 7.0 FileProvider
3. Android 10.0 文件分区存储
4. Android动态权限申请

## Android文件路径

Api | 对应物理路径 | 应用卸载是否删除
---|---|---
Context.getFilesDir() | /data/user/0/{packageName}/files | 是
Context.getCacheDir() | /data/user/0/{packageName}/cache | 是
Environment.getExternalStorageDirectory() | /storage/emulated/0/Android/data/{packageName}/files | 是
Context.getExternalFilesDir(String) | /storage/emulated/0/Android/data/{packageName}/cache | 是
Context.getExternalCacheDir() | /storage/emulated/0/Download | 否
