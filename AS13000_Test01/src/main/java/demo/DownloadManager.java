package demo;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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

    public void downloadObjAsLocalFile(String bucket_name, String key_name, String file_path){
        if (S3Client.doesObjectExist(bucket_name, key_name)){
            S3Client.getObject(new GetObjectRequest(bucket_name, key_name), new File(file_path));
            System.out.format("object %s is downloaded in %s.\n", key_name, file_path);
        } else {
            System.out.format("object %s is not exist.\n", key_name);
        }
    }

    public void downloadObjAsStream(String bucket_name, String key_name, int byteSize){
        try {
            S3Object object = S3Client.getObject(bucket_name, key_name);
            //获取元数据，根据实际需要决定要不要
            ObjectMetadata meta = new ObjectMetadata();
            meta= S3Client.getObjectMetadata(bucket_name, key_name);
            System.out.format("the file md5 is %s.\n", meta.getContentMD5());
            S3ObjectInputStream inputStream = object.getObjectContent();
            //key_name 文件保存的地址
            FileOutputStream outputStream = new FileOutputStream(new File(key_name));
            byte[] read_buf=new byte[byteSize];
            int read_len=0;
            while ((read_len = inputStream.read(read_buf)) > 0){
                outputStream.write(read_buf,0,read_len);
            }
            System.out.println("文件流下载成功");
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
}
