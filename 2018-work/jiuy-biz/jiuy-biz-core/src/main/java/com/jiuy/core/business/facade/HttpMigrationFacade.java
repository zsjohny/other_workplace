package com.jiuy.core.business.facade;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.jiuy.core.service.admin.AdminUserService;
import com.jiuyuan.constant.DB;
import com.jiuyuan.constant.ThirdPartService;
import com.qianmi.open.api.tool.util.LogUtil;

@Service
public class HttpMigrationFacade {
	
	@Resource
	private AdminUserService adminUserService;
	
	public void migrationProductImg() throws Exception {
		Set<String> tables = new HashSet<String>();
		Set<String> columnNames = new HashSet<String>();
        
        PreparedStatement stmt = null;
        Statement stmt2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;

        Connection conn = getConnection();

        DatabaseMetaData databaseMetaData = conn.getMetaData();
        rs3 = databaseMetaData.getTables(null, null, "%", null);

        // 获取所有数据表
        while (rs3.next()) {
            tables.add(rs3.getString("TABLE_NAME"));
        }

        for (String tableName : tables) {
            String sql = "select * from " + tableName;
//        String sql = "select * from yjj_Brand"; 
            try {
                stmt = conn.prepareStatement(sql);
                stmt2 = conn.createStatement();
                rs = stmt.executeQuery(sql);
                ResultSetMetaData data = rs.getMetaData();
                rs2 = stmt2.executeQuery(sql);

                // 获取某一张数据表的所有列名字
                for (int i = 1; i <= data.getColumnCount(); i++) {
                    // 获得指定列的列名
                    String columnName = data.getColumnName(i);
                    columnNames.add(columnName);
                }
                
                // 获取数据中的内容
                while (rs2.next()) {
                	for (String columnName : columnNames) {
                        // 循环取得结果集(遍历)
//                        System.out.println("columnName:" + columnName + ", columnValue:" + rs2.getString(columnName));
//                		System.out.println(columnName + " :" + rs2.getString(columnName));        
                    	//激活图片
                		String url = rs2.getString(columnName);
                        if(!("".equals(url)) && url != null && activeUrl(url)) {
//                        	updateOSSUrl(data.getColumnName(1), Long.parseLong(rs2.getString(data.getColumnName(1))), columnName, url, "yjj_Brand");
                        	System.out.println("tablename:"+tableName);
                        	System.out.println("columnName:"+columnName);
//                        	System.out.println("column:"+data.getColumnName(1));
//                        	System.out.println("columdata:"+rs2.getString(data.getColumnName(1)));
                        } 
                    	
                    	//更新OSS图片路径
////                    	System.out.println(tableName);
//                    	System.out.println(data.getColumnName(1));
//                    	System.out.println(rs2.getString(data.getColumnName(1)));
                    }
                }
                columnNames.clear();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (stmt2 != null) {
                    stmt2.close();
                    stmt2 = null;
                }
                if (rs2 != null) {
                    rs2.close();
                    rs2 = null;
                }
                if (rs3 != null) {
                    rs3.close();
                    rs3 = null;
                }
            }
        }
        if (conn != null) {
            conn.close();
            conn = null;
        }
        System.out.println("mission complete!");
	}
	
	private void updateOSSUrl(String primaryKey, long id, String columnName, String oldUrl, String tableName) {
    	String newUrl = StringUtils.replace(oldUrl, "http", "https");
    	System.out.println("primary: " + primaryKey + ", id: " + id + " , columnName: " + columnName + ", newUrl: " + newUrl + ", tableName: " + tableName);
    	adminUserService.updateHttpUrl(primaryKey, id, columnName, newUrl, tableName);
    	System.out.println("after update");
    }

	public boolean activeUrl(String newUrl) throws Exception {
    	List<String> urls = new ArrayList<String>();
    	Set<String> encodeUrls = new HashSet<String>();

    	Pattern pattern = Pattern.compile("http://(?!(\\.jpg|\\.png|\\.gif)).+?(\\.jpg|\\.png|\\.gif)");
        Matcher m = pattern.matcher(newUrl);
        
        while (m.find()) {
        	urls.add(m.group());
        }

//        encodeUrl(encodeUrls, urls);
//        executeGet(encodeUrls);
        if(urls.size()>0){
        	return true;
        }else{
        	return false;
        }
	}

	private void encodeUrl(Set<String> encodeUrls, List<String> urls) throws UnsupportedEncodingException {
		for(String url : urls) {
			String link = "";
			String rootUrl = url.substring(0, url.lastIndexOf("/"));
			String endUrl = url.substring(url.lastIndexOf("/")+1,url.length());
			if(StringUtils.contains(endUrl, "%")){
				link =  rootUrl + "/" + url;
			} else {
				link =  rootUrl + "/" + URLEncoder.encode(endUrl, "UTF-8");
			}
			link = link.replace("http", "https");
			System.out.println("link:"+link);
			encodeUrls.add(link);
		}
	}

	public void executeGet(Set<String> encodeUrls) throws Exception {
        HttpClient client = new HttpClient();  
        
        // Create a method instance.  
        for(String url : encodeUrls) {
        	GetMethod method = new GetMethod(url);
        	
        	// Provide custom retry handler is necessary  
        	method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,  
        			new DefaultHttpMethodRetryHandler(3, false));  
        	try {
				int statusCode = client.executeMethod(method);
				if(statusCode != 200) {
					System.out.println("code: " + statusCode + ", url: " + url);
				}
        	} catch (HttpException e) {
        		System.err.println("Fatal protocol violation: " + e.getMessage());
        	} catch(Exception exception) {
    			System.err.println("you have error! " + url);
    			continue;
    		} finally {
        		method.releaseConnection();
        	}
        }
    }
	
    /**
     * 获取连接
     * 
     * @return
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(DB.JDBC_DRIVERCLASSNAME);
            String url = DB.JDBC_URL;
            String username = DB.JDBC_USERNAME;
            String password = DB.JDBC_PASSWORD;
            
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}