package org.cnstar.webfetcher.model;

import java.util.HashMap;
import java.util.Map;

public class USStateName {
    static Map<String, String> stateMap ;
    static
    {
	stateMap = new HashMap<String, String>();
	stateMap.put("al", "Alabama");
	stateMap.put("ak", "Alaska");
	stateMap.put("az", "Arizona");
	stateMap.put("ar", "Arkansas");
	stateMap.put("ca", "California");
	stateMap.put("co", "Colorado");
	stateMap.put("ct", "Connecticut");
	stateMap.put("de", "Delaware");
	stateMap.put("dc", "District of Columbia");
	stateMap.put("fl", "Florida");
	stateMap.put("ga", "Georgia");
	stateMap.put("hi", "Hawaii");
	stateMap.put("id", "Idaho");
	stateMap.put("il", "Illinois");
	stateMap.put("in", "Indiana");
	stateMap.put("ia", "Iowa");
	stateMap.put("ks", "Kansas");
	stateMap.put("ky", "Kentucky");
	stateMap.put("la", "Louisiana");
	stateMap.put("me", "Maine");
	stateMap.put("md", "Maryland");
	stateMap.put("ma", "Massachusetts");
	stateMap.put("mi", "Michigan");
	stateMap.put("mn", "Minnesota");
	stateMap.put("ms", "Mississippi");
	stateMap.put("mi", "Michigan");
	stateMap.put("mt", "Montana");
	stateMap.put("ne", "Nebraska");
	stateMap.put("nv", "Nevada");
	stateMap.put("nh", "New Hampshire");
	stateMap.put("nj", "New Jersey");
	stateMap.put("nm", "New Mexico");
	stateMap.put("ny", "New York");
	stateMap.put("nc", "North Carolina");
	stateMap.put("nd", "North Dakota");
	stateMap.put("oh", "Ohio");
	stateMap.put("ok", "Oklahoma");
	stateMap.put("or", "Oregon");
	stateMap.put("pa", "Pennsylvania");
	stateMap.put("ri", "Rhode Island");
	stateMap.put("sc", "South Carolina");
	stateMap.put("sd", "South Dakota");
	stateMap.put("tn", "Tennessee");
	stateMap.put("tx", "Texas");
	stateMap.put("ut", "Utah");
	stateMap.put("vt", "Vermont");
	stateMap.put("va", "Virginia");
	stateMap.put("wa", "Washington");
	stateMap.put("wv", "West Virginia");
	stateMap.put("wi", "Wisconsin");
	stateMap.put("wy", "Wyoming");

	}
    
     public static String get(String key)
     {
    	 try{
	 return stateMap.get(key.toLowerCase()).toLowerCase();
    	 }
    	 catch(NullPointerException e)
    	 {
    		 return null;
    	 }
     }

}
