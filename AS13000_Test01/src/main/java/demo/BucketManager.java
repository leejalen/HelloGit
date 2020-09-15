package demo;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2020/9/14
 *
 * @author: leejalen
 */
public class BucketManager {

    private static AmazonS3 S3Client;

    public BucketManager() {
        Client client=new Client();
        S3Client=client.initClient();
    }
    /**
     * 创建存储桶
     * @param bucket_name
     * @return 返回桶类型
     * */
    public Bucket createBucket(String bucket_name){
        Bucket bucket = null;
        if (S3Client.doesBucketExist(bucket_name)){
            System.out.format("Bucket %s already exists.\n", bucket_name);
        } else {
            try {
                bucket = S3Client.createBucket(bucket_name);
                System.out.format("Bucket %s has been created.\n", bucket_name);
            } catch (AmazonS3Exception e) {
                System.err.println(e.getErrorMessage());
            }
        }
        return bucket;
    }
    /**
     * 删除存储桶
     * @param bucket_name
     * */
    public void deleteBucket(String bucket_name){
        if (S3Client.doesBucketExistV2(bucket_name)){
            S3Client.deleteBucket(bucket_name);
        } else {
            System.out.format("Bucket %s does not exists.\n", bucket_name);
        }
    }
    /**
     * 列举出所有桶的名称
     * */
    public List<String> listBuckets(){
        List<Bucket> buckets = S3Client.listBuckets();
        List<String> buckets_name=null;
        if (buckets.size() == 0){
            System.out.println("there are no buckets");
        } else {
            buckets_name = new ArrayList<String>();
            for (Bucket bucket : buckets){
                buckets_name.add(bucket.getName());
            }
        }
        return buckets_name;
    }
    /**
     * 判断桶是否存在
     * */
    public boolean doesBucketExist(String bucket_name){
        if (S3Client.doesBucketExistV2(bucket_name)){
            return true;
        }else {
            return false;
        }
    }
    /**
     * 关闭客户端
     * */
    public void closeClient(){
        if (S3Client != null){
            S3Client.shutdown();
        }
    }

}
