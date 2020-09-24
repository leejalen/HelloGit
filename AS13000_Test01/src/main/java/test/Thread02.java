package test;

import demo.UploadManager;

/**
 * Created on 2020/9/18
 *
 * @author: leejalen
 */
public class Thread02 implements Runnable{
    private Thread thread;

    @Override
    public void run() {
        UploadManager uploadManager = new UploadManager();
        while (true){
            uploadManager.listMultipartUploads("bucket01");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /*for (int i=0 ; i<100 ;i++){
            System.out.println("执行02");
        }*/
    }

    public void start(){
        if (thread == null){
            thread = new Thread(this);
            thread.start();
        }
    }
}
