package URLConnection;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/8/7 21:37
 */
public class DownThread extends Thread{


    /** 定义下载资源的路径. */
    private String path;
    /** 当前线程的开始下载位置. */
    private int startPos;
    /** 当前线程负责下载的部分文件大小. */
    private int currentPartSize;
    /** 当前线程需要下载的文件块. */
    private RandomAccessFile currentPartFile;
    /** 该线程已经下载的字节数. */
    public int length;

    public DownThread(String path, int startPos, int currentPartSize, RandomAccessFile currentPartFile){
        this.path = path;
        this.startPos =  startPos;
        this.currentPartSize = currentPartSize;
        this.currentPartFile = currentPartFile;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accrpt", "image/pjpeg, */*");
            conn.setRequestProperty("Accept-Language", "zh-CN");
            conn.setRequestProperty("Charset", "UTF-8");
            InputStream inStream = conn.getInputStream();
            //跳过startPos个字节，表明该线程只下载自己负责的那部分文件
            inStream.skip(startPos);
            byte[] buffer = new byte[1024];
            int hasRead = 0;
            while (length < currentPartSize && (hasRead = inStream.read(buffer)) != -1){
                currentPartFile.write(buffer, 0 , hasRead);
                //累计该线程下载的总大小
                length += hasRead;
            }
            currentPartFile.close();
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
