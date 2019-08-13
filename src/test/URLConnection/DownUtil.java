package URLConnection;

import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/8/7 21:27
 */
public class DownUtil {
    
    /** 定义下载资源的路径. */
    private String path;
    /** 指定所下载的文件的保存位置. */
    private String targetFile;
    /** 共有多少个线程. */
    private int threadNum;
    /** 定义需要使用多少个线程下载资源. */
    private int num;
    /** 定义下载的线程对象. */
    private DownThread[] threads;
    /** 定义下载的文件的总大小. */
    private int fileSize;

    public DownUtil(String path, String targetFile, int threadNum) {
        this.path = path;
        this.targetFile = targetFile;
        this.threadNum = threadNum;
        this.threads = new DownThread[threadNum];
    }

    /**
     * 多线程下载文件
     */
    public void download() throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5*1000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accrpt", "image/pjpeg, */*");
        conn.setRequestProperty("Accept-Language", "zh-CN");
        conn.setRequestProperty("Charset", "UTF-8");
        //得到文件大小
        fileSize = conn.getContentLength();
        conn.disconnect();
        int currentPartSize = 0;
        if(fileSize <= threadNum){
            currentPartSize = fileSize;
            num = 1;
        }else{
            currentPartSize = fileSize/threadNum + 1;
            num  = fileSize%currentPartSize ==0 ? fileSize/currentPartSize : fileSize/currentPartSize + 1;
        }
        RandomAccessFile file = new RandomAccessFile(targetFile, "rw");
        //设置本地文件大小
        file.setLength(fileSize);
        file.close();
        for (int i = 0; i < num; i++) {
            //计算每个线程下载的开始位置
            int startPos = i * currentPartSize;
            //每个线程使用一个RandomAccessFile对象进行下载
            RandomAccessFile currentPartFile = new RandomAccessFile(targetFile, "rw");
            //定义该线程的下载位置
            currentPartFile.seek(startPos);
            threads[i] = new DownThread(path, startPos, currentPartSize, currentPartFile);
            //启动线程
            threads[i].start();
        }
    }

    /**
     * 获取下载的完成百分比
     * @return
     */
    public double getCompleteRate(){
        //统计多个线程已经下载的总大小
        int sumsize = 0;
        for (int i = 0; i < num; i++) {
            sumsize += threads[i].length;
        }
        //返回完成的百分比
        return  sumsize * 1.0 / fileSize;
    }
}
