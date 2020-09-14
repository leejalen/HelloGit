package demo;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.*;
import constant.AccountInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2020/9/14
 *
 * @author: leejalen
 */
public class TestDB {

    private static AmazonS3 S3Client;
    /**
     * 初始化AmazonS3信息
     * @return 返回AmazonS3类型
     * */
    private static AmazonS3 initAmazonS3(){
        String access_key = AccountInfo.access_key;
        String secret_key = AccountInfo.secret_key;
        String endPoint = AccountInfo.endPoint;

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSignerOverride("S3SignerType");
        clientConfiguration.setProtocol(Protocol.HTTP);

        AWSCredentials awsCredentials = new BasicAWSCredentials(access_key, secret_key);
        S3Client=new AmazonS3Client(awsCredentials, clientConfiguration);
        S3Client.setEndpoint(endPoint);
        S3Client.setS3ClientOptions(S3ClientOptions.builder().setPathStyleAccess(true).build());
        return S3Client;
    }
    /**
     * 关闭AmazonS3
     * @return 返回空类型
     * */
    private static void closeAmazonS3(){
        if (S3Client != null){
            S3Client.shutdown();
        }
    }
    /**
     * 创建存储桶
     * @param bucket_name
     * @param S3Client
     * @return 返回桶类型
     * */
    public Bucket createBucket(String bucket_name, AmazonS3 S3Client){
        Bucket bucket = null;
        if (S3Client.doesBucketExistV2(bucket_name)){
            System.out.format("Bucket %s already exists.\n", bucket_name);
        } else {
            try {
                bucket = S3Client.createBucket(bucket_name);
            } catch (AmazonS3Exception e) {
                System.err.println(e.getErrorMessage());
            }
        }
        return bucket;
    }
    /**
     * 上传文件到指定桶中
     * @param file_path
     * @param bucket_name
     * @param key_name
     * @param S3Client
     * @return
     * */
    public PutObjectResult uploadObject(String file_path, String bucket_name, String key_name, AmazonS3 S3Client){
        File file = new File(file_path);
        //上传文件前需要判断，桶中是否存在同名文件
        PutObjectResult putResult=S3Client.putObject(bucket_name,key_name,file);
        return putResult;
    }
    /**
     * 获取对象文件
     * @param bucket_name
     * @param key_name
     * @param byteSize
     * @param S3Client
     * */
    public void getObject(String bucket_name, String key_name, int byteSize, AmazonS3 S3Client){
        try {
            S3Object object = S3Client.getObject(bucket_name, key_name);
            //获取元数据，根据实际需要决定要不要
            /*ObjectMetadata meta = new ObjectMetadata();
              meta= S3Client.getObjectMetadata(bucket_name, key_name);
              System.out.format("the file md5 is %s.\n", meta.getContentMD5());*/
            S3ObjectInputStream inputStream = object.getObjectContent();
            //key_name 文件保存的地址
            FileOutputStream outputStream = new FileOutputStream(new File(key_name));
            byte[] read_buf=new byte[byteSize];
            int read_len=0;
            while ((read_len = inputStream.read(read_buf)) > 0){
                outputStream.write(read_buf,0,read_len);
            }
            inputStream.close();
            outputStream.close();
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
    /**
     * 列举某个桶中所有的对象文件
     * @param bucket_name
     * @param S3Client
     * */
    public List<String> listObject(String bucket_name, AmazonS3 S3Client){
        ObjectListing objectListing = S3Client.listObjects(bucket_name);
        List<S3ObjectSummary> objects = objectListing.getObjectSummaries();
        List<String> objects_name = new ArrayList<String>();
        for (S3ObjectSummary object : objects){
            objects_name.add(object.getKey());
        }
        return objects_name;
    }
}
