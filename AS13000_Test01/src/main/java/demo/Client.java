package demo;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import constant.AccountInfo;

/**
 * Created on 2020/9/14
 *
 * @author: leejalen
 */
public class Client {

    private AmazonS3 S3Client=null;
    /**
     * 初始化AmazonS3信息
     * */
    public Client() {
        String access_key = AccountInfo.access_key;
        String secret_key = AccountInfo.secret_key;
        String endPoint = AccountInfo.endPoint;

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSignerOverride("S3SignerType");
        clientConfiguration.setProtocol(Protocol.HTTPS);

        AWSCredentials awsCredentials = new BasicAWSCredentials(access_key, secret_key);
        S3Client=new AmazonS3Client(awsCredentials, clientConfiguration);
        S3Client.setEndpoint(endPoint);
        S3Client.setS3ClientOptions(S3ClientOptions.builder().setPathStyleAccess(true).build());
    }
    /**
     * 返回AmazonS3信息
     * */
    public AmazonS3 initClient(){
        if (S3Client != null){
            System.out.println("AmazonS3 client init success");
            return S3Client;
        } else {
            System.out.println("AmazonS3 client init fail");
            return null;
        }
    }
    /**
     * 关闭AmazonS3
     * */
    public void closeClient(){
        if (S3Client != null){
            S3Client.shutdown();
            System.out.println("AmazonS3 client has been shutdown");
        }
    }
}
