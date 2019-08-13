package URLConnection.nio;

import org.junit.Test;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/8/13 22:07
 */
public class NioTest {

    @Test
    public void NioTest() throws Exception {
        DownUtil downUtil =  new DownUtil("F:\\aa.txt", "F:\\b.txt", 5);
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

    @Test
    public void NioTest1() throws Exception {
        RandomAccessFile file = new RandomAccessFile("F:\\a.txt", "rw");
        try(FileChannel readChannel = file.getChannel();
            FileChannel writeChannel = new RandomAccessFile("F:\\b.txt", "rw").getChannel() ){

            MappedByteBuffer buffer = readChannel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            writeChannel.position(0);
            writeChannel.write(buffer);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
