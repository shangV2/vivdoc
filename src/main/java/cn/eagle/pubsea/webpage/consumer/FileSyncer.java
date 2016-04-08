package cn.eagle.pubsea.webpage.consumer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.eagle.pubsea.webpage.proto.WebPageProto.WebPageMessage;

public class FileSyncer {
	private static final Logger logger = LoggerFactory.getLogger(FileSyncer.class);
	private static String folderName = "/tmp/webpages/";

	public static String getFolderName() {
		return folderName;
	}

	public static void setFolderName(String folderName) {
		FileSyncer.folderName = folderName;
	}

	public void storeInFile(WebPageMessage msg, int dbId) {
		String webId = msg.getWebid();
		String source = msg.getSource().toStringUtf8();
		long dateId = msg.getDateid();
		String url = msg.getUrl();
		String title = msg.getTitle().toStringUtf8();
		String timeStamp = msg.getTimestamp();
		String content = msg.getContent().toStringUtf8();
		String titleHtml = msg.getTitleHtml().toStringUtf8();
		String timeStampHtml = msg.getTimestampHtml().toStringUtf8();
		String contentHtml = msg.getContentHtml().toStringUtf8();
		String pageHtml = msg.getPageHtml().toStringUtf8();

		FileWriter fw = null;
		try {
			String filePath = getFilePath(webId, dateId, url, dbId);
			fw = new FileWriter(filePath);
			fw.write(content);
			logger.info("stored file, path:" + filePath);
		} catch (IOException e) {
			logger.error("write to file error, msg:" + e.getMessage());
		}finally{
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					logger.error("close error file error, msg:" + e.getMessage());
				}
			}
		}
	}

	private String getFilePath(String webId, long dateId, String url, int dbId) {
		int pos = url.lastIndexOf('/');
		if (pos != -1) {
			url = url.substring(pos+1);
		}

		String path = folderName  + webId + "/" + dateId;
		File fp = new File(path);
		if (!fp.exists()) {
			fp.mkdirs();
		}

		return path + "/" + url +"_" + dbId;
	}

	public static void main(String[] args) {
		FileSyncer fileSyncer = new FileSyncer();
	}
}
