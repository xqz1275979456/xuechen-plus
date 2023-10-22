package com.xuecheng.media;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;


import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * minio SDK测试
 *
 * @author xuqizheng
 * @date 2023/10/19
 */

public class MinioTest {
    MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://192.168.101.65:9000")
                    .credentials("minioadmin", "minioadmin").build();


    //上传文件
    @Test
    public void test_upload() throws Exception{
        //通过扩展名得到媒体资源类型mimeType
        //根据扩展名取出mimeType
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch("png");
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE; //通用mimeType 字节流
        if (extensionMatch!=null){
            mimeType=extensionMatch.getMimeType();
        }

        //上传文件的参数信息
        UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                .bucket("testbucket")  //确定桶
                .filename("E:\\QQ文件\\APP图标\\qq1.png") //指定本地文件路径
                //.object("qq1.png") //对象名 在根目录
                .object("test/01/qq1.png") //对象名 子目录
                .contentType(mimeType) //设置媒体文件类型
                .build();
        //上传文件
        minioClient.uploadObject(uploadObjectArgs);

    }
    //删除文件
    @Test
    public void  test_delete()  throws Exception{
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                .bucket("testbucket")
                .object("qq1.png")
                .build();
        //删除文件
        minioClient.removeObject(removeObjectArgs);

    }

    //查询文件 从minio中下载
    @Test
    public void test_getFile() throws Exception{
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket("testbucket")
                .object("test/01/qq1.png")
                .build();
        //查询远程服务器获取到一个流对象
        FilterInputStream inputStream = minioClient.getObject(getObjectArgs);
        //指定输出流
        FileOutputStream outputStream = new FileOutputStream("E:\\QQ文件\\APP图标\\qq2.png");
        IOUtils.copy(inputStream,outputStream);
    }

}
