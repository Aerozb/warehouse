package com.yhy.sys.common;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Properties;

public class MyFileUtil {

    /**
     * 文件上传的保存路径,给个默认值
     */
    public static String UPLOAD_PATH=System.getProperty("user.dir") + File.separator + "upload" + File.separator;

    /**
     * 图片保存的公共路径
     * D:/JAVA_Project/IDEA/warehouse/upload/
     */
    public static String IMAGE_DIRECTORY = "file:" + UPLOAD_PATH ;

    /**
     * 读取yml里的属性值
     */
    static {
        YamlPropertiesFactoryBean yamlMapFactoryBean = new YamlPropertiesFactoryBean();
        //可以加载多个yml文件
        yamlMapFactoryBean.setResources(new ClassPathResource("application.yml"));
        Properties properties = yamlMapFactoryBean.getObject();
        //获取yml里的参数
        UPLOAD_PATH = properties.getProperty("filepath");
    }

    /**
     * 根据文件老名字得到新名字
     *
     * @param oldName
     * @return
     */
    public static String createNewFileName(String oldName) {
        String suffix = FileUtil.extName(oldName);
        return IdUtil.fastSimpleUUID() + "." + suffix;
    }

    /**
     * 保存文件,返回图片所在目录和图片自己
     * @return 如2020-02-12/54255afd988f47648cd8a6675ae8c55c.jpg
     */
    public static String save(FileInputStream fis, String fileName) {
        FileOutputStream fos = null;
        //创建通道
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        //要返回的路径
        String selfPath = null;
        //图片公用的存储位置
        String publicPath = null;
        try {
            selfPath = DateUtil.today() + "/" + fileName;
            publicPath = MyFileUtil.UPLOAD_PATH+selfPath;
            //如不存在则创建目录及文件
            FileUtil.touch(publicPath);
            fos = new FileOutputStream(publicPath);

            inChannel = fis.getChannel();
            outChannel = fos.getChannel();
            //通道间传输
            inChannel.transferTo(0, inChannel.size(), outChannel);

            inChannel.close();
            outChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return selfPath;
    }

    /**
     * 根据路径删除图片
     * @param oldPath
     */
    public static void removeFileByPath(String oldPath) {
           FileUtil.del(UPLOAD_PATH+oldPath);
    }
}
