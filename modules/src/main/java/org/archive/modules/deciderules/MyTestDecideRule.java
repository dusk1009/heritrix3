package org.archive.modules.deciderules;

import org.apache.commons.httpclient.URIException;
import org.archive.crawler.util.KeywordsFilterUtil;
import org.archive.modules.CrawlURI;

public class MyTestDecideRule extends DecideRule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;

	@Override
	protected DecideResult innerDecide(CrawlURI uri) {
		String u = uri.getURI();
        //只抓取http://news.163.com/13/0922/10/网易新闻
        if (u.startsWith("dns") || u.startsWith("DNS")) {
            return DecideResult.ACCEPT;
        }
        
        if(u.endsWith(".html")){
        	if(u.contains("autohome.com") || u.contains("news.bitauto.com") || u.contains("auto.sina.com.cn") || u.contains("auto.sohu.com")){
        		 return DecideResult.ACCEPT;
        	}
        	return DecideResult.REJECT;
        }
        
        if( u.endsWith(".gif") || u.endsWith(".jpg") || u.endsWith(".jpeg") || u.endsWith(".png")){
			try {
				if(KeywordsFilterUtil.contains(uri.getVia().getURI()))
					return DecideResult.ACCEPT;
			} catch (URIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        return DecideResult.REJECT;
	}

}
