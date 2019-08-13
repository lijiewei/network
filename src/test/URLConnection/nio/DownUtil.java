package URLConnection.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/8/13 20:39
 */
public class DownUtil {
    //定义下载资源的路径
    private String originPath;
    //指定下载文件的保存位置
    private String targetPath;
    //共有多少个线程
    private long num;
    //指定使用多少个线程下载资源
    private int threadNum;
    //定义下载的线程对象
    private DownThread[] threads;
    //定义下载的文件总大小
    private long fileSize;

    public DownUtil(String originPath, String targetPath, int threadNum) {
        this.originPath = originPath;
        this.targetPath = targetPath;
        this.threadNum = threadNum;
        this.threads = new DownThread[threadNum];
    }

    public void download() throws IOException {
        fileSize = Files.size(Paths.get(originPath));
        long currentPartSize = 0;
        if(fileSize <= threadNum){
            currentPartSize = fileSize;
            num = 1;
        }else{
            currentPartSize = fileSize/threadNum + 1;
            num  = fileSize%currentPartSize ==0 ? fileSize/currentPartSize : fileSize/currentPartSize + 1;
        }

        //指定下载文件
        RandomAccessFile targetFile = new RandomAccessFile(targetPath, "rw");
        //设置下载文件大小
        targetFile.setLength(fileSize);

        for (int i = 0; i < threadNum; i++) {
            //计算每个线程下载的开始位置
            long startIndex = i * currentPartSize;
            //每个线程使用一个RandomAccessFile对象进行下载
            RandomAccessFile currentPartFile = new RandomAccessFile(targetPath, "rw");

            threads[i] = new DownThread(originPath, startIndex, currentPartSize, currentPartFile);
            //启动线程
            new Thread(threads[i]).start();
        }
    }

    /**
     * 获取下载百分比
     * @return
     */
    public double getCompleteRate(){
        //统计多个线程已经下载的总大小
        int sumSize = 0;
        for (int i = 0; i < num; i++) {
            sumSize += threads[i].length;
        }
        //返回完成的百分比
        return  sumSize * 1.0 / fileSize;
    }
}
