package test_package;

import demo.*;

import java.util.List;

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
        UploadManager uploadManager = new UploadManager();
       /* String content="this is a test content";
        uploadManager.uploadString("bucket01", "key05", content);*/
        /*String file_path="D:\\testPackage\\testFile01.pdf";
        uploadManager.uploadLocalFile("bucket01", "key07", file_path);*/
        String content="wwwwwwwww";
        uploadManager.uploadString("bucket01", "key08", content);
        /*DownloadManager downloadManager = new DownloadManager();*/
        /*String content = downloadManager.downloadObjAsString("bucket01", "key04");
        System.out.println(content);*/
        /*downloadManager.downloadObjAsStream("bucket01", "key02",15*1024);*/
        /*downloadManager.downloadObjAsLocalFile("bucket01", "key02","D:\\download\\file01.docx");*/

    }
}
