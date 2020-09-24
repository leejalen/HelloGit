package demo;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.MultipleFileDownload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;

import java.io.*;

/**
 * Created on 2020/9/15
 *
 * @author: leejalen
 */
public class DownloadManager {

    private static AmazonS3 S3Client;

    public DownloadManager() {
        Client client=new Client();
        S3Client=client.initClient();
        System.setProperty("com.amazonaws.services.s3.disableGetObjectMD5Validation","true");
    }
    /**
     * 下载对象文件为字符串
     * */
    public String downloadObjAsString(String bucket_name, String key_name){
        String content = null;
        if (S3Client.doesObjectExist(bucket_name, key_name)){
            content = S3Client.getObjectAsString (bucket_name, key_name);
            System.out.println("下载对象文件作为字符串成功");
        } else {
            System.out.format("object %s is not exist.\n", key_name);
        }
        return content;
    }
    /**
     * 下载对象文件为本地文件
     * */
    public void downObjAsLocalFile(String bucket_name, String key_name, String file_path){
        if (S3Client.doesObjectExist(bucket_name, key_name)){
            S3Client.getObject(new GetObjectRequest(bucket_name, key_name), new File(file_path));
            System.out.format("object %s is downloaded in %s.\n", key_name, file_path);
        } else {
            System.out.format("object %s is not exist.\n", key_name);
        }
    }
    /**
     * 下载对象文件为流
     * */
    public void downObjAsStream(String bucket_name, String key_name, String file_path, int byteSize){
        try {
            S3Object object = S3Client.getObject(bucket_name, key_name);
            //获取元数据，根据实际需要决定要不要
            ObjectMetadata meta = new ObjectMetadata();
            meta= S3Client.getObjectMetadata(bucket_name, key_name);
            System.out.format("the file md5 is %s.\n", meta.getContentMD5());
            S3ObjectInputStream inputStream = object.getObjectContent();
            //key_name 文件保存的地址
            FileOutputStream outputStream = new FileOutputStream(new File(file_path));
            byte[] read_buf=new byte[byteSize];
            int read_len=0;
            while ((read_len = inputStream.read(read_buf)) > 0){
                outputStream.write(read_buf,0,read_len);
            }
            if (read_buf!=null){
                System.out.println(read_buf);
                System.out.println("文件流下载成功");
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
     * 范围下载文件
     * */
    public void downObjByRange(String bucket_name, String key_name, int start, int end) throws IOException{
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucket_name, key_name);
        getObjectRequest.setRange(start, end);
        S3Object s3Object = S3Client.getObject(getObjectRequest);
        byte[] buf = new byte[1024];
        InputStream in = s3Object.getObjectContent();
        for (int n = 0; n != -1; ) {
            n = in.read(buf, 0, buf.length);
        }
        if (buf!=null){
            System.out.println("范围下载成功");
        }
        in.close();
    }
    /**
     * 异步下载单个文件
     * */
    public void downSingleByAsync(String bucket_name, String key_name, String file_path){
        File file = new File(file_path);
        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(S3Client).build();
        Download xfer = transferManager.download(bucket_name, key_name, file);
        try {
            xfer.waitForCompletion();
            System.out.println("异步下载完成");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        transferManager.shutdownNow();
    }
    /**
     * 异步下载所有文件
     * */
    public void downAllByAsync(String bucket_name, String key_prefix, String dir_path){
        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(S3Client).build();
        MultipleFileDownload xfer = transferManager.downloadDirectory(bucket_name, key_prefix, new File(dir_path));
        try {
            xfer.waitForCompletion();
            System.out.println("异步下载完成");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        transferManager.shutdownNow();
    }
}
