package com.markoproject.service;


import com.markoproject.objects.SearchPropertiesWrapper;
import com.markoproject.runnebleImpl.Scanner;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Marko on 22.11.2016.
 */
//Class for meneging threads, main work its getting from BlackingQueue,creating scanners and puting into them values,  skanedLinks set for chacking unique of links
//implementation of runnable
public class ThreadManager implements Runnable {

    private static final Logger logger = LogManager.getLogger(ThreadManager.class);
    //@params : word(searching word) ,url( url for scanning), results( for mapping pages :url-key , error link or count of words on page-value),
    private BlockingQueue<String> linksForScanning;
    private Map<String, String> results;
    private ExecutorService fixedPool;
    private String word;
    private int countOfLinks;
    private int countOfThreads;
    Set<String> skanedLinks;//=new HashSet<String>();

    public ThreadManager() {
        results = new LinkedHashMap<String, String>();
    }


    public BlockingQueue<String> getLinksForScanning() {
        return linksForScanning;
    }

    public String getLoaded(){//for returning loaded factor which used in progress-bar
        Pattern p = Pattern.compile("[_]*\\d");
        int complite=0;
        List<String> list = new ArrayList<String>(results.values());
        for(String str:list){
            if (((ThreadPoolExecutor) fixedPool).getActiveCount() == 0 && linksForScanning.isEmpty()) {//chack for shutdouning fixedPool
                fixedPool.shutdown();
                break;
            }
            Matcher m = p.matcher(str);//chack if value of url in map is numeric add one for complite
            if (m.find()){
                complite++;
            }
        }

       double loaded=(double) complite/countOfLinks;//loaded factor its complite links/count of links
        int result= (int) (loaded*100);
        return result+"%";
    }

    public Map<String, String> getResults() {
        return results;
    }

    public ExecutorService getFixedPool() {
        return fixedPool;
    }

    public void continueScanning() {//this pethod call if user press "start" after "pause"
        fixedPool = Executors.newFixedThreadPool( countOfThreads);
        Thread thread = new Thread(this);
        thread.start();
    }
    //If user press "start".Param SearchPropertiesWrapper, class wrapper for values from user
    //exstracting values from searchProperties and starting of ThreadManager
    public void startScanning(SearchPropertiesWrapper searchProperties) {
        word=searchProperties.getWord();
        skanedLinks=new HashSet<String>();
        countOfLinks=searchProperties.getCountOfLinks();
        countOfThreads=searchProperties.getCountOfThreads();
        fixedPool = Executors.newFixedThreadPool( countOfThreads);
        linksForScanning = new ArrayBlockingQueue<String>(10);
        try {
            linksForScanning.put(searchProperties.getUrl());
        } catch (InterruptedException e) {
            logger.error("puting url in the start"+e.getMessage() );
        }
        Thread thread = new Thread(this);//Start ThreadManager
        thread.start();
    }


    public void run() {
        while (countOfLinks > skanedLinks.size()) {
            String url = null;
            if (((ThreadPoolExecutor) fixedPool).getActiveCount() == 0 && linksForScanning.isEmpty()) {//if there is no one link in queue and any one thread work
                System.out.println("BREAK");
                break;
            }
            try {
                url = linksForScanning.take();//take url from queue.If queue empty , waiting
            } catch (InterruptedException e) {
                logger.error("Thread can not take url from queue"+e.getMessage() );
            }
           if(skanedLinks.add(url)) {//If thread manager can add url to set with urls for scanning
               Scanner scanner = new Scanner(word, url, results, linksForScanning);//create scanner
               fixedPool.submit(scanner);//run scanner
           }
        }
        fixedPool.shutdown();//if Scanners have scaned countOfLinks from user - shutdown

    }
}
