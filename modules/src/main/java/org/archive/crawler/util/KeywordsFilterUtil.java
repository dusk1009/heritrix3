package org.archive.crawler.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class KeywordsFilterUtil {
	
	private static Set<String> urlSet = Collections.synchronizedSet(new HashSet<String>());
	
	public static Boolean contains(String url){
		return urlSet.contains(url);
	}
	
	public static void add(String url){
		urlSet.add(url);
	}

}
