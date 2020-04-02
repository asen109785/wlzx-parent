package net.wanho.manage_media;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class WlzxServiceManageMediaApplicationTests {
//      短点续传，先分割 在合并
//    分割
    @Test
    public void contextLoads() throws Exception {
//        读一写多
        File file = new File("F:\\javaAll\\wlzxproject\\prj\\wlzx\\static\\a.mp4");
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        byte[] bs = new byte[1024*1024]; //1兆
        int len  ;
        BufferedOutputStream bos;
        int i =1;
        while ((len=bis.read(bs))!=-1){
             bos = new BufferedOutputStream(new FileOutputStream("F:\\javaAll\\wlzxproject\\prj\\video\\chunk\\"+ (i++)));
            bos.write(bs,0,len);
            bos.close();
        }
        bis.close();
    }

//    合并
    @Test
    public void contextLoads2() throws Exception {
//        读多写一
        File file = new File("F:\\javaAll\\wlzxproject\\prj\\video\\chunk\\");
        String[] list = file.list();
//        获得的所有文件，但是顺序不是自然顺序
        BufferedInputStream bis ;
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("F:\\javaAll\\wlzxproject\\prj\\video\\b.mp4"));;
        int len;
        byte[] bs = new byte[1024*1024]; //1兆
        for (int i = 0; i <list.length ; i++) {
//            new File(父级目录，文件名)
            bis = new BufferedInputStream(new FileInputStream(new File(file,i+1+"")));
            len = bis.read(bs);
            bos.write(bs,0,len);
            bis.close();
        }
        bos.close();
    }

}
