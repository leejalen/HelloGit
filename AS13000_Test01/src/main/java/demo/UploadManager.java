package demo;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
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
    /**
     * 上传字符串
     * */
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
    /**
     * 上传本地文件
     * */
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
    /**
     *上传本地文件为流
     * */
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
    /**
     * 分片上传
     * */
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
    /**
     * 停止分片上传
     * */
    public void abortUploadBytMultipart(String bucket_name, String key_name, String uploadId){
        AbortMultipartUploadRequest abortMultipartUploadRequest = new AbortMultipartUploadRequest(bucket_name, key_name, uploadId);
        try {
            S3Client.abortMultipartUpload(abortMultipartUploadRequest);
            System.out.println("停止分片上传成功");
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        }
    }
    /**
     * 放弃某个时间之前的多片上传
     * */
    public void abortMultiUploadBefore(String bucket_name, Date date){
        TransferManager xfer_mgr = TransferManagerBuilder.standard().withS3Client(S3Client).build();
        xfer_mgr. abortMultipartUploads (bucket_name, date);
        xfer_mgr.shutdownNow();
        System.out.println("已停止某时间之前的分片上传");
    }
    /**
     * 获取已上传的分片
     * */
    public void listParts(String bucket_name, String key_name, String uploadId){
        PartListing list = S3Client.listParts(new ListPartsRequest(bucket_name, key_name, uploadId));
        List<PartSummary> result = list.getParts();
        for (PartSummary m : result) {
            System.out.println("modified time " + m.getLastModified() + " partNumber: " + m.getPartNumber() + " ETag: " + m.getETag() + "size " + m.getSize() + "\n ");
        }
    }
    /**
     * 列举分片上传事件
     * */
    public void listMultipartUploads(String bucket_name){
        MultipartUploadListing list = S3Client.listMultipartUploads(new ListMultipartUploadsRequest(bucket_name));
        List<MultipartUpload> result = list.getMultipartUploads();
        for (MultipartUpload m : result) {
            System.out.println("obj_name " + m.getKey() + " UploadId: " + m.getUploadId() + "\n");
        }
    }
    /**
     * 异步上传单个文件
     * */
    public void uploadSingleByAsync(String bucket_name, String key_name, String file_path){
        File file = new File(file_path);
        TransferManager xfer_mgr = TransferManagerBuilder.standard().withS3Client(S3Client).build();
        Upload xfer = xfer_mgr.upload(bucket_name, key_name, file);
        try {
            xfer.waitForCompletion();
            System.out.println("异步上传成功");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        xfer_mgr.shutdownNow();
    }
    /**
     * 异步上传文件列表
     * */
    public void uploadListByAsync(String bucket_name, String key_prefix, String dir_path, String[] file_paths){
        ArrayList<File> files = new ArrayList();
        for (String path : file_paths) {
            files.add(new File(path));
        }
        TransferManager xfer_mgr = TransferManagerBuilder.standard().withS3Client(S3Client).build();
        MultipleFileUpload xfer = xfer_mgr.uploadFileList(bucket_name, key_prefix, new File(dir_path), files);
        try {
            xfer.waitForCompletion();
            System.out.println("异步上传成功");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        xfer_mgr.shutdownNow();
    }
    /**
     * 异步上传目录
     * @param recursive 是否包含子文件
     * */
    public void uploadDirByAsync(String bucket_name, String key_prefix, String dir_path, boolean recursive){
        TransferManager xfer_mgr = TransferManagerBuilder.standard().withS3Client(S3Client).build();
        MultipleFileUpload xfer = xfer_mgr.uploadDirectory(bucket_name, key_prefix, new File(dir_path), recursive);
        try {
            xfer.waitForCompletion();
            System.out.println("异步上传成功");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        xfer_mgr.shutdownNow();
    }
}
