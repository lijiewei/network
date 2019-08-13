package URLConnection;

import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/8/7 23:15
 */
public class URLConnectionTest {

    @Test
    public void URLConnectionTest() throws Exception {
        DownUtil downUtil = new DownUtil("https://tvax1.sinaimg.cn/large/005BYqpggy1g47ltvmq2ej30sg0lcqqi.jpg", "ios.jpg", 5);
        downUtil.download();

        while (downUtil.getCompleteRate() <1){
            System.out.println("已完成："+downUtil.getCompleteRate());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
