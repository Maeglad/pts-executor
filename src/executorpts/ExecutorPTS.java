/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package executorpts;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miklo_000
 */
public class ExecutorPTS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TaskQueue tq = new TaskQueue();
        ThreadPool executor = new ThreadPool(4, tq);
            for(int i = 0; i < 10; ++i){
            executor.add(new Runnable() {

                @Override
                public void run() {
                    
                    try {
                        
                        Thread.sleep(1000);
                        
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ExecutorPTS.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            }
        executor.end();
        
    }
}
