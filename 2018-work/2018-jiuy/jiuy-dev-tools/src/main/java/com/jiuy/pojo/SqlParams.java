package com.jiuy.pojo;



/**   
* @Title: SqlParams.java 
* @Package mybatisganertor.aison.pojo 
* @Description: 
* 			   列与java类的属性的对应关系
* @author Aison 
* @date 2017年12月28日 上午10:47:55 
* @version V1.0   
*/
public class SqlParams {

	/** 
	* @Fields sqlType : 
	* 			数据库字段的类型名称
	*/ 
	private String sqlType;
	/** 
	* @Fields javaType : 
	* 			 java pojo字段的类型
	* 			  如java.util.Data
	*/ 	
	private String fullJavaType;
	/** 
	* @Fields shortJavaType : 
	* 			简写的java pojo字段名称 如Date
	*/ 
	private String shortJavaType;
	
	/** 
	* @Fields field : 
	* 			java pojo的字段名称
	*/ 
	private String property;
	/** 
	* @Fields columnName : 
	* 			sql 列的名称
	*/ 
	private String columnName;
	
	/** 
	* @Fields comments : 
	* 			字段的注释
	*/ 
	private String comments;
	
	/** 
	* @Fields isPk : 是否是主键
	*/ 
	private Boolean isPk;
	
	/** 
	* @Fields needImport : 是否需要显示的导入
	*/ 
	private Boolean notNeedImport;
	

	public String getSqlType() {
		return sqlType;
	}

	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}

	public String getFullJavaType() {
		return fullJavaType;
	}

	public void setFullJavaType(String fullJavaType) {
		this.fullJavaType = fullJavaType;
	}

	public String getShortJavaType() {
		return shortJavaType;
	}

	public void setShortJavaType(String shortJavaType) {
		this.shortJavaType = shortJavaType;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getColumnName() {
		
		
		
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Boolean getIsPk() {
		return isPk;
	}

	public void setIsPk(Boolean isPk) {
		this.isPk = isPk;
	}

	public Boolean getNotNeedImport() {
		return notNeedImport;
	}

	public void setNotNeedImport(Boolean notNeedImport) {
		this.notNeedImport = notNeedImport;
	}

	
	
}

