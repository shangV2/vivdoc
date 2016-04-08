package cn.eagle.pubsea.webpage.consumer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.Statement;

import cn.eagle.pubsea.webpage.proto.WebPageProto.WebPageMessage;

public class DbSyncer {
	private static final Logger logger = LoggerFactory.getLogger(DbSyncer.class);
	private Connection conn = null;
	private static String driver = "com.mysql.jdbc.Driver";
	private static String dbIp = "127.0.0.1";
	private static int dbPort = 3306;
	private static String dbName = "weiyudoc";
	private static String dbUser = "root";
	private static String dbPass = "123456";

	public static String getDbIp() {
		return dbIp;
	}

	public static void setDbIp(String dbIp2) {
		dbIp = dbIp2;
	}

	public static int getDbPort() {
		return dbPort;
	}

	public static void setDbPort(int dbPort2) {
		dbPort = dbPort2;
	}

	public static String getDbName() {
		return dbName;
	}

	public static void setDbName(String dbName2) {
		dbName = dbName2;
	}

	public static String getDbUser() {
		return dbUser;
	}

	public static void setDbUser(String dbUser2) {
		dbUser = dbUser2;
	}

	public static String getDbPass() {
		return dbPass;
	}

	public static void setDbPass(String dbPass2) {
		dbPass = dbPass2;
	}

	public int storeInDB(WebPageMessage msg) {
		String id = msg.getWebid();
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
		
		try {
			Connection conn = getDBConnection();
			String insertSql = "insert into webpages (webid, source, dateid, url, title, timestamp,"
					+ "content, titlehtml, timestamphtml, contenthtml, pagehtml) values "
					+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement pStatement = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
			pStatement.setString(1, id);
			pStatement.setString(2, source);
			pStatement.setLong(3, dateId);
			pStatement.setString(4, url);
			pStatement.setString(5, title);
			pStatement.setString(6, timeStamp);
			pStatement.setString(7, content);
			pStatement.setString(8, titleHtml);
			pStatement.setString(9, timeStampHtml);
			pStatement.setString(10, contentHtml);
			pStatement.setString(11, pageHtml);
		
			pStatement.executeUpdate();
			
			ResultSet rs = pStatement.getGeneratedKeys();
			if (rs.next()) {
				logger.info("stroe to db, id:" + rs.getInt(1) + " url:" + url);
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			logger.error("get sql exception, msg:" + e.getMessage());
			try {
				conn.close();
			} catch (SQLException e1) {
				logger.error("db connection can't close, msg:" + e.getMessage());
			}
			conn = null;
		}
		return -1;
	}

	public void getPageFromDB() {		
		try {
			Connection conn = getDBConnection();
			String insertSql = "select * from webpages;";
			PreparedStatement pStatement = conn.prepareStatement(insertSql);
			ResultSet rs = pStatement.executeQuery();
			while(rs.next()){
				String pageHtml = rs.getString("timestamphtml");
				System.out.println(pageHtml);
			}
		} catch (SQLException e) {
			logger.error("get sql exception, msg:" + e.getMessage());
			try {
				conn.close();
			} catch (SQLException e1) {
				logger.error("db connection can't close, msg:" + e.getMessage());
			}
			conn = null;
		}
	}

	private Connection getDBConnection() throws SQLException {
		if (conn == null) {
			try {
				Class.forName(driver);
			} catch (ClassNotFoundException e) {
				logger.error("can't find db driver, msg:" + e.getMessage());
			}
			conn = DriverManager.getConnection("jdbc:mysql://" + dbIp + ":" + dbPort + "/" + dbName +"?useUnicode=true&characterEncoding=UTF-8", dbUser, dbPass);
		}
		return conn;
	}

	public static void main(String[] args) {
		DbSyncer dbSyncer = new DbSyncer();
		dbSyncer.getPageFromDB();
	}
}
