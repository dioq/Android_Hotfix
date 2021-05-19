# Hotfix_Android
 Android热修复

##
1、可以用jdk 里的javac xxx.java 直接将java文件编译成xxx.class文件
2、用Android Sdk里的工具 将xxx.class文件编译成xxx.dex文件
   D:\work_space\class1 里放的是xxx.class文件，而且文件目录结构要与xxx.class里的包名要一致
##
D:\Android\Sdk\build-tools\30.0.3\dx.bat --dex --output=D:\work_space\dex1\MyLogic.dex D:\work_space\class1
