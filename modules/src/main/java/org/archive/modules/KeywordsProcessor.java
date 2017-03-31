package org.archive.modules;

import java.io.IOException;

import org.archive.crawler.util.KeywordsFilterUtil;
import org.archive.io.RecordingInputStream;
import org.archive.net.UURI;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class KeywordsProcessor extends Processor {

	public ProcessResult pr = ProcessResult.PROCEED;
	
    {
        setKeywords("");
    }
    public String getKeywords() {
        return (String) kp.get("keywords");
    }
    public void setKeywords(String keywords) {
        kp.put("keywords",keywords);
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
        if(!uri.endsWith("html"))
        	return;
        
        RecordingInputStream recis = curi.getRecorder().getRecordedInput();
        if (0L == recis.getResponseContentLength()) {
            return;
        }

		Document doc = null;
		try {
			doc = Jsoup.parse(recis.getMessageBodyReplayInputStream(), "gb2312", uri);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(doc!=null && doc.data().contains(getKeywords())){
			pr = ProcessResult.PROCEED;
			KeywordsFilterUtil.add(uri);
		}
		else{
			pr = ProcessResult.jump("candidates");
		}

	}

}
