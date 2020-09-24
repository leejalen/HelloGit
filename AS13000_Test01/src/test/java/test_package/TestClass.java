package test_package;

import demo.*;
import test.Thread01;
import test.Thread02;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created on 2020/9/15
 *
 * @author: leejalen
 */
public class TestClass {

    public static void main(String[] args) {
        /*BucketManager bucketManager = new BucketManager();
        bucketManager.createBucket("bucket02");
        System.out.println(bucketManager.doesBucketExist("bucket05"));
        List<String> buckets = bucketManager.listBuckets();
        System.out.println(buckets);
        bucketManager.closeClient();*/
        /*ObjectManager objectManager = new ObjectManager();
        *//*String path=System.getProperty("user.dir");*//*
        String content="wwwwwwwww";
        objectManager.uploadObject("bucket01","key04",content);*/
        /**
         * 上传字符串
         * */
        /*UploadManager uploadManager = new UploadManager();
        String content="this is a test content";
        uploadManager.uploadString("bucket01", "key05", content);*/
        /**
         * 上传本地文件
         * */
        /*UploadManager uploadManager = new UploadManager();
        String file_path="D:\\testPackage\\testFile01.pdf";
        BucketManager bucketManager = new BucketManager();
        bucketManager.createBucket("bucket01");
        uploadManager.uploadLocalFile("bucket01", "key01", file_path);*/
        /**
         * 分片上传文件
         * */
        /*UploadManager uploadManager = new UploadManager();
        String file_path="D:\\testPackage\\testFile07.pdf";
        uploadManager.uploadByMultipart("bucket01", "key02", file_path);*/
        /**
         * 上传文件为流
         * */
        /*String file_path="D:\\testPackage\\testFile03.pdf";
        UploadManager uploadManager = new UploadManager();
        uploadManager.uploadLocalFileInStream("bucket01", "key10",file_path);*/
        /**
         * 分片上传未完成时停止
         * */
        /*final Date date = new Date();
        final UploadManager uploadManager = new UploadManager();
        String file_path="D:\\testPackage\\testFile03.pdf";
        final String uploadId=uploadManager.uploadByMultipart("bucket01", "key11", file_path);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                uploadManager.abortMultiUploadBefore("bucket01", date);
                System.out.println("定时成功");
            }
        },5000);*/
        /**
         * 获取已上传的分片
         * */
        /*final UploadManager uploadManager = new UploadManager();
        String file_path="D:\\testPackage\\testFile07.pdf";
        final String uploadId=uploadManager.uploadByMultipart("bucket01", "key13", file_path);
        uploadManager.listMultipartUploads("bucket01");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                uploadManager.listParts("bucket01", "key13", uploadId);
            }
        },5000);*/
        /**
         * 实时查看分片信息
         * */

        /**
         * 异步上传单个文件
         * */
        /*UploadManager uploadManager = new UploadManager();
        String file_path="D:\\testPackage\\testFile03.pdf";
        uploadManager.uploadSingleByAsync("bucket01", "key14", file_path);*/
        /**
         * 异步上传文件列表
         * */
        /*UploadManager uploadManager = new UploadManager();
        String[] files = new String[3];
        String file_path1="D:\\testPackage\\testFile01.pdf";
        String file_path2="D:\\testPackage\\testFile02.pdf";
        String file_path3="D:\\testPackage\\testFile03.pdf";
        files[0]=file_path1;
        files[1]=file_path2;
        files[2]=file_path3;
        for (String file : files){
            System.out.println(file);
        }
        String file_path="D:\\testPackage";
        uploadManager.uploadListByAsync("bucket01", "father01", file_path, files);*/
        /**
         * 异步上传目录
         * */
        /*UploadManager uploadManager = new UploadManager();
        String dir_path="D:\\testPackage";
        uploadManager.uploadDirByAsync("bucket01","father02", dir_path, true);*/
        /**
         * 下载字符串
         * */
        /*DownloadManager downloadManager = new DownloadManager();
        String content = downloadManager.downloadObjAsString("bucket01", "key04");
        System.out.println(content);*/
        /**
         * 下载为文件字符流
         * */
        /*DownloadManager downloadManager = new DownloadManager();
        String file_path = "D:\\download\\file01.docx";
        downloadManager.downObjAsStream("bucket01", "key07",file_path,15*1024);*/
        /**
         * 下载为本地文件
         * */
        /*DownloadManager downloadManager = new DownloadManager();
        String file_path = "D:\\download\\test01.pdf";
        downloadManager.downObjAsLocalFile("bucket01", "key01", file_path);*/
        /*String dir_path="D:\\download\\testFile04.pdf";
        DownloadManager downloadManager=new DownloadManager();
        downloadManager.downloadObjAsLocalFile("bucket01","father02/file/testFile04.pdf", dir_path);*/
        /**
         * 异步下载单个文件
         * */
        DownloadManager downloadManager = new DownloadManager();
        String file_path = "D:\\download\\test03.pdf";
        downloadManager.downSingleByAsync("bucket01", "key02", file_path);
        /**
         * 异步下载所有文件
         * */
        /*DownloadManager downloadManager = new DownloadManager();
        String file_path = "D:\\download";
        downloadManager.downAllByAsync("bucket01", "father01", file_path);*/
    }
}
