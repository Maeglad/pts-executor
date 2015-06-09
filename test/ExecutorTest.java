/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import executorpts.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author miklo_000
 */
public class ExecutorTest {
    
    ThreadPool executor;
    TaskQueue tq;
    public ExecutorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        // ked som to mal tu z nejakeho dovodu to nefungovalo presunutie do 
        // jednotlivych testov to opravilo
      //  tq = new TaskQueue();
      //  executor = new ThreadPool(4, tq);
        
    }
    
    @After
    public void tearDown() {
        // to iste co pre setup class
    //    executor.end();
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    /**
     * kazdy task, ktory sa do executora nascheduluje sa aj naozaj spusti
     */
    @Test
    public void goodShedulingTest() {
        tq = new TaskQueue();
        executor = new ThreadPool(4, tq);
        final ArrayList<Integer> counter = new ArrayList<>();
        counter.clear();
        for(int i = 0; i < 10; ++i){
            executor.add(new Runnable() {

                @Override
                public void run() {
                    synchronized (counter) {
                        counter.add(1);
                        //System.out.println(counter.size());
                    }
                }
            });
        }
        executor.end();
        //System.out.println(counter.size());
        
        assertTrue(counter.size() == 10);    
    }
    
    /**
     * executor s k threadmi zacne prvych k taskov naozaj vykonavat naraz, k+1 a dalsie musia cakat
    */
    @Test
    public void countingTasksTest() {
        tq = new TaskQueue();
        executor = new ThreadPool(4, tq);
        for(int i = 0; i < 10; ++i){
            executor.add(new Runnable() {

                @Override
                public void run() {
                    try {
                        
                        Thread.sleep(500);
                        
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ExecutorTest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            });
        }
        
        try {
            //System.out.println(counter.size());
            //System.out.println(tq.waitngTasks());
            synchronized (this){
                this.wait(100); // delay nech si stihnu thready zobrat tasky z queue
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ExecutorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertTrue(tq.waitngTasks() == 6);
        executor.end();
        
    }
    
    /**
     * N taskov, z ktorych kazdy trva M milisekund a nijako nevytazuje procesor, by mali dokopy trvat zhruba N*M/k milisekund
     */
    @Test
    public void runtimeTest() {
        tq = new TaskQueue();
        executor = new ThreadPool(4, tq);
        // toto by malo bezat na 4 threadovom executore 3s + create + destroy threadov
        long startTime = System.currentTimeMillis();
        for(int i = 0; i < 1000; ++i){
            executor.add(new Runnable() {

                @Override
                public void run() {
                    try {
                        
                        Thread.sleep(5);
                        
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ExecutorTest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            });
        }
        executor.end();
        
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
        assertTrue((endTime - startTime) < 1500); // 1250 by to malo bezat 250 je rezerva obvikle to skonci to rozdielu 100ms pre 10 taskov to je len 4ms
    }
    
    /**
     * executor vytvara k threadov a nie viac
     */
    @Test
    public void countingThreadsTest(){
        tq = new TaskQueue();
        executor = new ThreadPool(4, tq);
        for(int i = 0; i < 10; ++i){
            executor.add(new Runnable() {

                @Override
                public void run() {
                    try {
                        
                        Thread.sleep(500);
                        
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ExecutorTest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            });
        }
        synchronized (this){
            try {
                wait(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(ExecutorTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        assertTrue(executor.getNumberOfThreads() == 4);
        executor.end();
        
        
    
    }
}
