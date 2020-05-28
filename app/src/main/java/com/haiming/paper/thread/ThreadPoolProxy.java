package com.haiming.paper.thread;

import android.util.Log;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ThreadPoolProxy {
    private ThreadPoolExecutor mExecutor;
    private int mCorePoolSize;
    private int mMaximumPoolSize;

    public ThreadPoolProxy(int mCorePoolSize, int mMaximumPoolSize, long mKeepAliveTime) {
        this.mCorePoolSize = mCorePoolSize;
        this.mMaximumPoolSize = mMaximumPoolSize;
        this.mKeepAliveTime = 10;
    }

    private long mKeepAliveTime;

    /**
     * 执行任务
     *
     * @param task
     */
    public void execute(Runnable task) {
        initThreadPoolExecutor();
        //执行线程
        mExecutor.execute(task);
    }

    private synchronized void initThreadPoolExecutor() {
        if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()) {

            TimeUnit unit = TimeUnit.MILLISECONDS;
            BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>();//阻塞队列
            ThreadFactory threadFactory = Executors.defaultThreadFactory();
            RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
            mExecutor = new ThreadPoolExecutor(mCorePoolSize,//核心线程数
                    mMaximumPoolSize,//最大线程数
                    mKeepAliveTime,//保持时间长度
                    unit,//单位
                    workQueue,//队列
                    threadFactory,//线程工厂
                    handler);//错误捕获器
        }
    }

    public Future<?> submit(Runnable task) {
        initThreadPoolExecutor();
        return mExecutor.submit(task);
    }

    public void remove(Runnable task) {
        if (mExecutor != null) {
            mExecutor.remove(task);
        }
    }

    public void getPoolSize() {
        if (mExecutor != null) {
            Log.w("ThreadCount", mExecutor.getActiveCount() + "");
        }

    }


    public static class QuickSort{

        int number=100000000;
        int k=100;
        int range=100000001;
        int [] array = new int[number];
        public QuickSort(){
            init();
        }

        public void init(){
            Random random = new Random();
            for (int i=0;i<number;i++){
                array[i]=random.nextInt(range);
                System.out.println(array[i]);
            }
            showMessage();
        }

        public void showMessage(){
            long startTime=System.currentTimeMillis();

            long endTime=System.currentTimeMillis();
            System.out.println("The total execution time " +
                    "of quicksort based method is " + (startTime - endTime) +" millisecond!");

            // print out the top k largest values in the top array
            System.out.println("The top "+ k + " largest values are:");

            for (int i = 0; i < k; i++) {
                System.out.println(array[i]);
            }

        }

        private  void topHundred(int[] array,int start,int end,int k){
            int index = start;
            int temp=array[end];
            for(int i=start;i<end;i++){
                if(array[i]>=temp){
                    change(array,index,i);
                    index++;
                }
            }
            change(array,index,end);
            if(index < k-1){
                topHundred(array,index+1,end,k-index-1);
            }else if(index ==k-1){
                return;
            }else {
                topHundred(array,0,index-1,k);
            }

        }

        public void change(int[] array,int index,int i){
            int tem = array[index];
            array[index]=array[i];
            array[i]=tem;
        }

    }
}
