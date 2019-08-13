package URLConnection.nio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/8/13 21:31
 */
public class DownThread implements Runnable {

    /** 定义下载资源的路径. */
    private String originPath;
    /** 当前线程的开始下载位置. */
    private long startIndex;
    /** 当前线程负责下载的部分文件大小. */
    private long currentPartSize;
    /** 当前线程需要下载的文件块. */
    private RandomAccessFile currentPartFile;
    /** 该线程已经下载的字节数. */
    public int length;

    public DownThread(String originPath, long startIndex, long currentPartSize, RandomAccessFile currentPartFile) {
        this.originPath = originPath;
        this.startIndex = startIndex;
        this.currentPartSize = currentPartSize;
        this.currentPartFile = currentPartFile;
    }


    @Override
    public void run()  {
        try(FileChannel writeChannel = currentPartFile.getChannel();
                FileChannel readChannel = new RandomAccessFile(originPath, "rw").getChannel()){
            //定义该线程的下载位置
            writeChannel.position(startIndex);

            //把channel的记录指针移动到开始下载位置
            readChannel.position(startIndex);
            //定义一个byteBuffer,用于重复取水
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int hasRead = 0;
            while(length < currentPartSize && (hasRead = readChannel.read(buffer)) != -1 ){
                buffer.flip();
                writeChannel.write(buffer);
                length += hasRead;
                buffer.clear();
            }
        }catch (Exception e){
         e.printStackTrace();
        }
    }
}
