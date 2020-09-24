package test;

/**
 * Created on 2020/9/18
 *
 * @author: leejalen
 */
public class ThreadTest {

    public static void main(String[] args) {
        Thread01 thread01 = new Thread01();
        thread01.start();

        Thread02 thread02 = new Thread02();
        thread02.start();
    }
}
