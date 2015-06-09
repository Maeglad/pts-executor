/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package executorpts;

import java.util.ArrayList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miklo_000
 */
public class ThreadPool {
    
    private class MyThread extends Thread{
        private Runnable toRun;
        private final TaskQueue tq;
        private final int id;
        public MyThread(TaskQueue tq, int id) {
            this.tq = tq;
            this.id = id;
        }
        
        @Override
        public String toString(){
            return Integer.toString(id);
        }
        
        @Override
        public void run() {
            while((tq.isEmpty() == false)||(tq.getEnd() == false)){
                
                while(((toRun = tq.getTask()) == null)&&(tq.getEnd() == false)){
                    try {
                        synchronized (tq){
                            tq.wait();
                        }
                    } catch (InterruptedException ex) {
                        System.err.println("Thread " + this + " spadol");
                    }
                }
                if(toRun != null){
                    toRun.run();
                }
            
            }
        }
    }

    private final ArrayList<MyThread> threads;
    private final TaskQueue tq;
    private boolean end = false;
    
    /**
     * n specifies number of threads that will be created
     */
    public ThreadPool(int n, TaskQueue tq) {
        threads = new ArrayList<>();
        threads.clear();
        this.tq = tq;
        for(int i = 0; i < n; ++i){
            MyThread th = new MyThread(tq, i);
            threads.add(th);
            th.start();
        }
    }
    
    public void add(Runnable r){
        if(r == null){
            System.out.println("AAAAAAA");
        }
        tq.addTask(r);
    }
    
    public void end(){
        tq.setEnd(true);
        for(MyThread t : threads){
            try {
                t.join();
                
            } catch (InterruptedException ex) {
                System.err.println("Joining threads error.");
            }
        }
        //System.out.println("Joining finshed");
    }
    
    public int getNumberOfThreads(){
        return threads.size();
    }
}
