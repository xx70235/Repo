package org.cnstar.webfetcher.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class GroupModel {

    private String groupName;
    private Map<Integer, PageModel> adList;
    
    public GroupModel()
    {
	adList = new LinkedHashMap<Integer, PageModel>();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Map<Integer, PageModel> getAdList() {
        return adList;
    }

    public void setAdList(Map<Integer, PageModel> adList) {
        this.adList = adList;
    }
    
    
}
