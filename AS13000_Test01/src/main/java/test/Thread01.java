package test;

import demo.UploadManager;

/**
 * Created on 2020/9/18
 *
 * @author: leejalen
 */
public class Thread01 implements Runnable{

    private Thread thread;

    @Override
    public void run() {
        UploadManager uploadManager = new UploadManager();
        String file_path = "D:\\testPackage\\testFile06.pdf";
        uploadManager.uploadByMultipart("bucket01", "key17", file_path);
        /*for (int i=0 ; i<100 ;i++){
            System.out.println("执行01");
        }*/

    }

    public void start(){
        if (thread == null){
            thread = new Thread(this);
            thread.start();
        }
    }
}
