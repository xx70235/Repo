package org.cnstar.webfetcher.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PageModel {

    private String pageName;
    private String url;

    public PageModel(){
	
    }
    
    public PageModel(String url) {
	this.url = url;
    }

    /**
     * @return the pageName
     */
    public String getPageName() {
	return pageName;
    }

    /**
     * @param pageName
     *            the pageName to set
     */
    public void setPageName(String pageName) {
	this.pageName = pageName;
    }

    /**
     * @return the url
     */
    public String getUrl() {
	return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
	this.url = url;
    }

}
