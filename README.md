<div align="center">


<h1>QAvatar</h1>
<h2>自动更换QQ头像</h2>

<div align="center">


  [![Stars](https://img.shields.io/github/stars/nooniebao/qavatar?label=stars)](https://github.com/nooniebao/qavatar)
  [![LSP%20Repo](https://img.shields.io/github/downloads/Xposed-Modules-Repo/icu.insomnia.qavatar/total?label=LSP%20Repo&labelColor=F48FB1)](https://github.com/Xposed-Modules-Repo/icu.insomnia.qavatar/releases)
</div>

[![Release](https://img.shields.io/github/v/release/Xposed-Modules-Repo/icu.insomnia.qavatar)](https://github.com/Xposed-Modules-Repo/icu.insomnia.qavatar/releases/latest)

</div>



## 重要信息

**`https://avatar.insomnia.icu/`即将弃用, 请使用新的头像源**

**若提示上传头像失败，请清除`/storage/emulated/0/Android/data/com.tencent.mobileqq/files/qavatar/`下的所有文件**

## 头像源
- `https://avatar.corosy.com/`
- `https://uglyavatar.corosy.com`

> https://avatar.corosy.com/ 可以返回一张**远古的潮流头像**
> 
> https://uglyavatar.corosy.com/ 可以返回一张**丑陋头像**. 必须指出: 丑陋头像的生成依赖于[txstc55](https://github.com/txstc55)的项目[ugly-avatar](https://github.com/txstc55/ugly-avatar)

## 功能：

- QAvatar**始终**从指定文件夹自动上传`nextTick.jpg`作为QQ头像，你可以手动移入**1:1 jpg**到此目录

  `/storage/emulated/0/Android/data/com.tencent.mobileqq/files/qavatar/`

- QAvatar always try to rename a img in the Dir to `nextTick.jpg` randomly，And rename the used as `timespam-Used.jpg`

- 支持从指定URL下载图片到上述目录（URL如 ~~https://avatar.insomnia.icu/~~ (已变更为https://avatar.corosy.com/ )）

- **如果你选择手动移入1:1 jpg，请确保移动后的文件具有正确的权限。如660**



## 初始化：

- 见`QQ设置首页`或`QQ账号管理`
- <img src="https://github.com/Xposed-Modules-Repo/icu.insomnia.qavatar/blob/main/img/161ff7eff5c18faaee576a6ac07f5bed.jpg?raw=true" alt="img" style="zoom:67%;" />


## 触发条件
1. QAvatar并未设置定时器，HOOK入口点为`android.app.Application#attach`，因此，只有在重启QQ或QQ加载其内部某些组件的时，才会触发上传行为
2. 你可以通过调整冷却时间来调节上传频率


## 测试列表：

| phone        | API        | os              | Q_version          |
| ------------ | ---------- | --------------- | ------------------ |
| Mi 10 Ultra  | Android 11  | MIUI12.5 12.5.10 | QQ9.1.0           |
| Mi 10 Ultra  | Android 11  | MIUI12.5 12.5.10 | QQ8.9.68           |
| K40 pro      | Android 11  | MIUI12.5 12.5.8 | QQ8.9.68           |
| Xiaomi Note3 | Android 11 | crDroid 7.21    | QQ9.0.17 \ QQ8.6.0 |
| MI 6         | Android 9  | MIUI10 9.9.3    | QQ8.9.71           |

在上述设备上测试通过
