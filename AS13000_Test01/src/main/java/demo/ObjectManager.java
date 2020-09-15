package demo;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2020/9/15
 *
 * @author: leejalen
 */
public class ObjectManager {

    private static AmazonS3 S3Client;

    public ObjectManager() {
        Client client=new Client();
        S3Client=client.initClient();
    }

    public PutObjectResult uploadObject(String bucket_name, String key_name, String file_path){
        PutObjectResult putResult=null;
        File file = new File(file_path);
        //上传文件前需要判断，桶中是否存在同名文件
        if (!doesObjectExist(bucket_name, key_name)){
            putResult=S3Client.putObject(bucket_name,key_name,file);
            if (putResult != null){
                System.out.format("object %s is uploaded successfully.\n", key_name);
            } else {
                System.out.format("object %s is uploaded fail.\n", key_name);
            }
        }
        return putResult;
    }

    public void downloadObject(String bucket_name, String key_name, int byteSize){
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

    public void deleteObject(String bucket_name, String key_name){
        if (doesObjectExist(bucket_name, key_name)){
            S3Client.deleteObject(bucket_name, key_name);
            System.out.format("object %s is deleted successfully.\n", key_name);
        } else {
            System.out.format("object %s is deleted fail.\n", key_name);
        }
    }

    public List<String> listObject(String bucket_name){
        ObjectListing objectListing = S3Client.listObjects(bucket_name);
        List<S3ObjectSummary> objects = objectListing.getObjectSummaries();
        List<String> objects_name = new ArrayList<String>();
        for (S3ObjectSummary object : objects){
            objects_name.add(object.getKey());
        }
        return objects_name;
    }

    public boolean doesObjectExist(String bucket_name, String key_name){
        if (S3Client.doesObjectExist(bucket_name, key_name)) {
            System.out.format("object %s is exist.\n", key_name);
            return true;
        }else {
            System.out.format("object %s is not exist.\n", key_name);
            return false;
        }
    }
}
