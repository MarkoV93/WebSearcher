package com.markoproject.objects;

import java.io.Serializable;

/**
 * Created by Marko on 23.11.2016.
 */
//class to transfer values from user to threadManager
public class SearchPropertiesWrapper implements Serializable{
    String url;
    String word;
    int countOfThreads;
    int countOfLinks;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getCountOfThreads() {
        return countOfThreads;
    }

    public void setCountOfThreads(int countOfThreads) {
        this.countOfThreads = countOfThreads;
    }

    public int getCountOfLinks() {
        return countOfLinks;
    }

    public void setCountOfLinks(int countOfLinks) {
        this.countOfLinks = countOfLinks;
    }
}
