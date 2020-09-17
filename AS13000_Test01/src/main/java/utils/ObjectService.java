package utils;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;

import java.io.File;

/**
 * Created on 2020/9/16
 *
 * @author: leejalen
 */
public class ObjectService {

    private AmazonS3 S3Client;
    /**
     * 初始化客户端
     * */
    public AmazonS3 initClient(String access_key, String secret_key, String endPoint){
        AmazonS3 S3Client=null;
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSignerOverride("S3SignerType");
        clientConfiguration.setProtocol(Protocol.HTTP);
        AWSCredentials awsCredentials = new BasicAWSCredentials(access_key, secret_key);
        S3Client=new AmazonS3Client(awsCredentials, clientConfiguration);
        S3Client.setEndpoint(endPoint);
        S3Client.setS3ClientOptions(S3ClientOptions.builder().setPathStyleAccess(true).build());
        this.S3Client=S3Client;
        return S3Client;
    }
    /**
     * 关闭客户端
     * */
    public void closeClient(){
        if (S3Client != null){
            S3Client.shutdown();
        }
    }
    /**
     * 判断对象是否存在
     * */
    public boolean doesObjectExist(String platform, String bucket_name, String key_name){
        bucket_name=platform+"-"+bucket_name;
        bucket_name=bucket_name.trim();
        if (S3Client.doesObjectExist(bucket_name, key_name)) {
            return true;
        }else {
            return false;
        }
    }
    /**
     * 上传本地文件
     * */
    public PutObjectResult uploadLocalFile(String platform, String bucket_name, String key_name, String file_path){
        bucket_name=platform+"-"+bucket_name;
        bucket_name=bucket_name.trim();
        PutObjectResult putResult=null;
        File file = new File(file_path);
        putResult=S3Client.putObject(bucket_name,key_name,file);
        return putResult;
    }
    /**
     * 下载文件到本地
     * */
    public ObjectMetadata downloadObjAsLocalFile(String platform, String bucket_name, String key_name, String file_path){
        bucket_name=platform+"-"+bucket_name;
        bucket_name=bucket_name.trim();
        ObjectMetadata objectMetadata=S3Client.getObject(new GetObjectRequest(bucket_name, key_name), new File(file_path));
        return objectMetadata;
    }
}
