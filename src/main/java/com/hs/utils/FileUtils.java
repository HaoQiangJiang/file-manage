package com.hs.utils;

import java.io.File;

public class FileUtils {
    public static Boolean findFile(File file,String filename) {
        if (file.isDirectory()) { // 确保给定的对象是一个目录
            File[] fileList = file.listFiles(); // 获取到该目录下的子目录数组
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isFile()) { // 如果子目录是一个文件
                    if (fileList[i].getName().equals(filename)) {
                        System.out.println(fileList[i].getAbsolutePath());
                        return true;
                    }
                } else {
                   return false;
                }
            }
        }
        return false;
    }
}
