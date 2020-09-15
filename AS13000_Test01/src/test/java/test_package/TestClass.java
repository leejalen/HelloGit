package test_package;

import demo.BucketManager;
import demo.Client;
import demo.ObjectManager;
import demo.UploadManager;

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
        String path=System.getProperty("user.dir");
        String file_path=path+"\\filePackage\\testFile00.docx";
        System.out.println(file_path);
        objectManager.uploadObject("bucket01","key01",file_path);*/
        UploadManager uploadManager = new UploadManager();
        /*String content="this is a test content";
        uploadManager.uploadString("bucket01", "key01", content);*/
        String file_path="D:\\testPackage\\testFile00.docx";
        uploadManager.uploadLocalFile("bucket01", "key03", file_path);
    }
}
