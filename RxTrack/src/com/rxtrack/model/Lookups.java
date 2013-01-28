package com.rxtrack.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Lookups {
	private static Lookups instance;
	
	private Map<String, SortedSet<String>> lookupLists = new HashMap<String, SortedSet<String>>();
	
	public static synchronized Lookups getInstance() {
		if (instance != null) {
			return instance;
		}
		instance = new Lookups();
		return instance;
	}
	
	public Set<String> getLookup(String key){
		SortedSet<String> set = lookupLists.get(key);
		if (set==null){
			set = new TreeSet<String>();
			lookupLists.put(key, set);
		}
		return set;
	}
}
