/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package executorpts;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author miklo_000
 */
public class TaskQueue {
    private final Queue<Runnable> tasks;
    private boolean end = false; // tento end by bolo fajn presunut kedze tu nema co robit
                                 // prip premenovat toto na synch. storage a end tu moze ostat - nie velmi pekne
    public TaskQueue() {
        tasks = new LinkedList<>(); // java nesuportuje default argument DI robit nebudem
        tasks.clear();
    }
    
    public synchronized void setEnd(boolean val){
            end = val;
    }
    
    public synchronized boolean getEnd(){
            return end;
    }
    
    public synchronized boolean addTask(Runnable r){
            if(end == false){
                notifyAll();
                return tasks.add(r);
            };
            return false;
    }
    
    public synchronized boolean isEmpty(){
            return tasks.isEmpty();
    }
    
    public synchronized Runnable getTask(){
            return tasks.poll();
    }
    
    public synchronized int waitngTasks(){
        return tasks.size();
    }           
}
