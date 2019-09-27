package com.hs.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.File;
import java.io.IOException;

/**
 * 机器磁盘文件的操作
 */
public class FsUtil {

    private Log LOG = LogFactory.getLog(FsUtil.class);

    /**
     * 判断文件是否存在
     * @param file
     */
    public void checkFileExists(File file) {
        if (file.exists()) {
            LOG.info("待写入文件存在");
        } else {
            LOG.info("待写入文件不存在, 创建文件成功");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 判断文件夹是否存在
     * @param file
     */
    public static void checkDirExists(File file) {
         Log LOG = LogFactory.getLog(FsUtil.class);
        if (file.exists()) {
            if (file.isDirectory()) {
                LOG.info("目录存在");
            } else {
                LOG.info("同名文件存在, 不能创建");
            }
        } else {
            LOG.info("目录不存在，创建目录");
            file.mkdir();
        }
    }
}

