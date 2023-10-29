package com.xuecheng.media;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * 测试大文件上传方法
 *
 * @author xuqizheng
 * @date 2023/10/22
 */
public class BigFileTest {
   //分块测试
    @Test
    public void testChunk() throws IOException {
     //源文件
     File sourcefile = new File("E:\\素材\\日语.mp4");
     //分块文件存储路径
     String chunkFilePath="E:\\素材\\chunk\\";
     //分块文件大小
     int chunkSize=1024*1024*5;
     //分块文件个数
     int chunkNum=(int) Math.ceil(sourcefile.length()*1.0/chunkSize);
     //使用流从源文件读数据，向分块文件写数据
     RandomAccessFile raf_r = new RandomAccessFile(sourcefile, "r");
     //缓冲区
     byte[] bytes=new byte[1024];
     for (int i = 0; i <chunkNum ; i++) {
      File chunkFile = new File(chunkFilePath + i);
      //分块文件写入流
      RandomAccessFile raf_rw = new RandomAccessFile(chunkFile, "rw");
      int len=-1;
      while ((len=raf_r.read(bytes))!=-1){
         raf_rw.write(bytes,0,len);
         if (chunkFile.length()>=chunkSize){
          break;
         }
      }
      raf_rw.close();
     }
     raf_r.close();
    }
    //分块后的文件合并
    @Test
    public void testMerge() throws IOException {
     //块文件目录
     File chunkFolder = new File("E:\\素材\\chunk\\");
     //源文件
     File sourcefile = new File("E:\\素材\\日语.mp4");
     //合并后的文件
     File mergefile = new File("E:\\素材\\日语2.mp4");

     //取出所有的分块文件
     File[] files = chunkFolder.listFiles();
     //数组转为list
     List<File> filesList = Arrays.asList(files);
     //分块文件排序
     Collections.sort(filesList, new Comparator<File>() {
      @Override  //自定义排序规则
      public int compare(File o1, File o2) {
       return Integer.parseInt(o1.getName())-Integer.parseInt(o2.getName());
      }
     });
     //向合并文件写的流
     RandomAccessFile raf_rw=new RandomAccessFile(mergefile,"rw");
     //缓冲区
     byte[] bytes=new byte[1024];
     //遍历分块文件，向合并的文件去写
     for (File file : filesList) {
      //读分块的流
      RandomAccessFile raf_r=new RandomAccessFile(file,"r");
      int len=-1;
      while ((len=raf_r.read(bytes))!=-1){
       raf_rw.write(bytes,0,len);
      }
      raf_r.close();
     }
     raf_rw.close();
     //合并文件完成，对合并文件进行校验
     //合并后的文件流
     FileInputStream mergefile_inputStream = new FileInputStream(mergefile);
     //源文件的文件流
     FileInputStream sourcefile_inputStream = new FileInputStream(sourcefile);
     String md5Hex_mergefile = DigestUtils.md5Hex(mergefile_inputStream);
     String md5Hex_sourcefile = DigestUtils.md5Hex(sourcefile_inputStream);
     if (md5Hex_mergefile.equals(md5Hex_sourcefile)){
      System.out.println("文件合并成功");
     }
    }
}
