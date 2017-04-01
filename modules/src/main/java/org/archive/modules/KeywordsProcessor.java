package org.archive.modules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.archive.crawler.util.KeywordsFilterUtil;
import org.archive.io.RecordingInputStream;
import org.archive.io.ReplayInputStream;
import org.archive.net.UURI;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class KeywordsProcessor extends Processor {

	public List<String[]> keyWordsList = new ArrayList<String[]>();
	
	public ProcessResult pr = ProcessResult.PROCEED;
	
    {
        setKeywords("");
    }
    public String getKeywords() {
        return (String) kp.get("keywords");
    }
    public void setKeywords(String keywords) {
    	keyWordsList.clear();
        kp.put("keywords",keywords);
        String[] kwl = keywords.split(",");
        for(String s : kwl){
        	keyWordsList.add(s.split(" "));
        }
    }
	
	@Override
	protected boolean shouldProcess(CrawlURI curi) {
		return isSuccess(curi);
	}

    @Override
    protected ProcessResult innerProcessResult(CrawlURI uri) 
    throws InterruptedException {
        innerProcess(uri);
        return pr;
    }
	
	@Override
	protected void innerProcess(CrawlURI curi) throws InterruptedException {
		pr = ProcessResult.PROCEED;
        UURI uuri = curi.getUURI(); // Current URI.

        // Only http and https schemes are supported.
        String scheme = uuri.getScheme();
        if (!"http".equalsIgnoreCase(scheme)
                && !"https".equalsIgnoreCase(scheme)) {
            return;
        }
        
        String uri = curi.getURI();
        if(uri.endsWith(".txt")){
        	pr = ProcessResult.jump("candidates");
        	return;
        }
        if(!uri.endsWith("html"))
        	return;
        
        	
        
        
        RecordingInputStream recis = curi.getRecorder().getRecordedInput();
        if (0L == recis.getResponseContentLength()) {
            return;
        }

		Document doc = null;
		ReplayInputStream r = null;
		try {
			r = recis.getMessageBodyReplayInputStream();
			if(r.getContentSize() == 0)
				return;
			if(uri.contains("http://club.autohome.com.cn/bbs/thread-c-163-61744029-1.html"))
				doc = null;
			doc = Jsoup.parse(r, "gb2312", uri);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		if(doc!=null){
			String data = doc.data();
			Boolean hit = false;
			for(String[] keywords : keyWordsList){
				hit = true;
				for(String key : keywords){
					if(!data.contains(key)){
						hit = false;
						break;
					}
				}
				if(hit){
					pr = ProcessResult.PROCEED;
					KeywordsFilterUtil.add(uri);
					return;
				}
			}
		}
		pr = ProcessResult.jump("candidates");

	}

}
