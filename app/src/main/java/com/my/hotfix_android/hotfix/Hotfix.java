package com.my.hotfix_android.hotfix;

import android.content.Context;

import com.my.hotfix_android.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;

public class Hotfix {

    /*
     * 参考文章
     * https://blog.csdn.net/dsczxcc/article/details/83865395
     * */
    public static void dynamicUpdate(Context context) {
        File cacheFile = context.getDir("dex", 0);//此目录下,只有你自己的应用有权限访问
        String dex_store_path = cacheFile.getAbsolutePath() + File.separator + "target.dex";
        File des_store_file = new File(dex_store_path);//JudgeInputStr.dex 复制的目标目录

        String dex_parsed_path = cacheFile.getAbsolutePath() + File.separator + "parsed_dex_file";
        File dex_parsed_folder = new File(dex_parsed_path);//JudgeInputStr.dex解析成dex 后存放的文件夹
        try {
            if (!des_store_file.exists()) {
                if (!des_store_file.createNewFile()) return;
                //把assets 里的 xxx.dex文件 复制到 本地文件里
                FileUtils.copyFiles(context, "JudgeInputStr.dex", des_store_file);
            }
            if (!dex_parsed_folder.exists()) {
                if (!dex_parsed_folder.mkdir()) return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
         * 正常来说热修复所需的dex文件是从服务器端传过来,然后加载到内存。这里为了方便就把dex文件放在assets文件夹下(JudgeInputStr.dex),
         * 通过把JudgeInputStr.dex写到本地文件夹(des_store_file 就是这个文件)下,然后用DexClassLoader加载。
         * 在生产环境中完全可以把接收到的dex文件 以二进制形式存到本地文件夹(des_store_file)下
         * */
        //根据本地文件路径获取要加载的dex文件
        DexClassLoader dexClassLoader = new DexClassLoader(dex_store_path, dex_parsed_path, null, context.getClassLoader());

        try {
            //原来的dexElements
            ClassLoader baseDexClassLoader = context.getClassLoader();
            Object dexPathListObj = getPathList(baseDexClassLoader);
            Object oldDexElements = getDexElements(dexPathListObj);
            //要添加的 add_dexElements
            Object add_dexElements = getDexElements(getPathList(dexClassLoader));

            //合并
            Object mergeDexElements = combineArray(add_dexElements, oldDexElements);

            // 获取到dexPathListObj和mergeDexElements,完成替换,也实现了热修复功能
            setField(dexPathListObj, mergeDexElements);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     * 需要分析这两个类
     * http://androidxref.com/6.0.0_r1/xref/libcore/dalvik/src/main/java/dalvik/system/BaseDexClassLoader.java
     * http://androidxref.com/6.0.0_r1/xref/libcore/dalvik/src/main/java/dalvik/system/DexPathList.java#dexElements
     * */

    //通过反射拿到 DexPathList 类里的 dexElements
    private static Object getDexElements(Object pathListObj) throws Exception {
        Class cls = Class.forName("dalvik.system.DexPathList");
        Field localField = cls.getDeclaredField("dexElements");
        localField.setAccessible(true);
        return localField.get(pathListObj);
    }

    //通过反射拿到 BaseDexClassLoader 类里的 pathList
    private static Object getPathList(Object baseDexClassLoader) throws Exception {
        Class cls = Class.forName("dalvik.system.BaseDexClassLoader");
        Field localField = cls.getDeclaredField("pathList");
        localField.setAccessible(true);
        return localField.get(baseDexClassLoader);
    }

    /**
     * 两个数组合并
     *
     * @param arrayHead 合并后在前
     * @param arrayTail 合并后在后
     * @return Object   合并后的
     */
    private static Object combineArray(Object arrayHead, Object arrayTail) {
        Class<?> localClass = arrayHead.getClass().getComponentType();
        int i = Array.getLength(arrayHead);
        int j = i + Array.getLength(arrayTail);
        assert localClass != null;
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayHead, k));
            } else {
                Array.set(result, k, Array.get(arrayTail, k - i));
            }
        }
        return result;
    }

    //替换dex文件
    private static void setField(Object obj, Object value) throws Exception {
        Field localField = obj.getClass().getDeclaredField("dexElements");
        localField.setAccessible(true);
        localField.set(obj, value);
    }
}
