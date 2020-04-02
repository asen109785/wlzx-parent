package net.wanho;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FastDFSTest {
    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    /**
     * 测试⽂件上传
     */
    @Test
    public void upload() throws Exception {
        InputStream is = new
                FileInputStream("F:\\javaAll\\wlzxproject\\wlzx-parent\\demo-freemarker\\src\\main\\resources\\templates\\course.ftl");
        StorePath storePath = fastFileStorageClient.uploadFile(is, is.available(), "ftl", null);
        System.out.println(storePath);
    }
    /**
     * 测试上传缩略图
     */
    @Test
    public void uploadCrtThumbImage() throws Exception {
        try {
            InputStream is = new
                    FileInputStream("C:\\Users\\Public\\Pictures\\Sample Pictures\\timg.jpg");
            // 测试上传 缩略图
            StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(is, is.available(), "jpg", null);
            System.out.println(storePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * 测试⽂件下载
     */
    @Test
    public void download() {
        try {
            byte[] bytes = fastFileStorageClient.downloadFile("group1",
                    "M00/00/00/wKhkBV3JAkyAYHMBAAAWRHklvyI226.jpg", new DownloadByteArray());
            FileOutputStream stream = new FileOutputStream("a.jpg");
            stream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 测试⽂件删除
     */
    @Test
    public void deleteFile(){

        fastFileStorageClient.deleteFile("group1","M00/00/00/wKhkBV3JAkyAYHMBAAAW RHklvyI226.jpg");
    }

}
