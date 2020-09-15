package demo;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2020/9/15
 *
 * @author: leejalen
 */
public class UploadManager {

    private static AmazonS3 S3Client;

    public UploadManager() {
        Client client=new Client();
        S3Client=client.initClient();
    }

    public PutObjectResult uploadString(String bucket_name, String key_name, String content){
        PutObjectResult putResult=null;
        if (S3Client.doesObjectExist(bucket_name, key_name)){
            System.out.format("object %s is already exists, upload fail.\n", key_name);
        } else {
            putResult=S3Client.putObject(bucket_name, key_name, content);
            System.out.format("object %s is uploaded success.\n", key_name);
        }
        return putResult;
    }

    public PutObjectResult uploadLocalFile(String bucket_name, String key_name, String file_path){
        PutObjectResult putResult=null;
        File file = new File(file_path);
        if (!S3Client.doesObjectExist(bucket_name, key_name)){
            putResult=S3Client.putObject(bucket_name,key_name,file);
            if (putResult != null){
                System.out.format("object %s is uploaded successfully.\n", key_name);
            } else {
                System.out.format("object %s is uploaded fail.\n", key_name);
            }
        } else {
            System.out.format("object %s is already exists, upload fail.\n", key_name);
        }
        return putResult;
    }

    public PutObjectResult uploadLocalFileInStream(String bucket_name, String key_name, String file_path){
        File file=new File(file_path);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(file.length());
        PutObjectResult putResult=S3Client.putObject(bucket_name, key_name, inputStream, meta);
        if (putResult != null){
            System.out.format("object %s is uploaded successfully.\n", key_name);
        }else {
            System.out.format("object %s is uploaded fail.\n", key_name);
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return putResult;
    }

    public String uploadByMultipart(String bucket_name, String key_name, String file_path){
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucket_name, key_name);
        InitiateMultipartUploadResult init_result = S3Client.initiateMultipartUpload(request);

        File file = new File(file_path);
        long contentLength = file.length();
        /*分片大小设置为5M*/
        long partSize = 5*1024*1024;
        long filePosition = 0;
        List<PartETag> partETags = new ArrayList<PartETag>();
        String uploadId = init_result.getUploadId();

        /*创建请求去完成一个分片*/
        for (int i = 1; filePosition < contentLength; i++){
            partSize = Math.min(partSize, (contentLength - filePosition));
            UploadPartRequest uploadRequest = new UploadPartRequest()
                    .withBucketName(bucket_name).withKey(key_name)
                    .withUploadId(uploadId).withPartNumber(i)
                    .withFileOffset(filePosition)
                    .withFile(file)
                    .withPartSize(partSize);
            UploadPartResult uret = S3Client.uploadPart(uploadRequest);
            partETags.add(uret.getPartETag());
            filePosition += partSize;
        }

        CompleteMultipartUploadRequest compRequest = new
                CompleteMultipartUploadRequest(
                bucket_name,
                key_name,
                uploadId,
                partETags);

        CompleteMultipartUploadResult ret = S3Client.completeMultipartUpload(compRequest);
        System.out.println("分片上传文件成功");
        return uploadId;
    }

    public void abortUploadBytMultipart(String bucket_name, String key_name, String uploadId){
        AbortMultipartUploadRequest abortMultipartUploadRequest = new AbortMultipartUploadRequest(bucket_name, key_name, uploadId);
        try {
            S3Client.abortMultipartUpload(abortMultipartUploadRequest);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        }
    }
}
