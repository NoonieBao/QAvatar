# RdAvadar

An Xposed module that would change QQ avatar automatically, Chosing new Avatars from directory Path or URL


RdAvatar is powered by xposed, and target `com.tencent.mobileqq`

RdAvatar always try to update `nextTick.jpg` in `/storage/emulated/0/Android/data/com.tencent.mobileqq/files/avatar/` as new QQ avatar

You can Coping avatar files to the directory manually, or 
Setting avatars url(Like https://avatar.insomnia.icu/) that RdAvatar will request automatically

