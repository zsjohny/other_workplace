package com.jiuy.rb.model.order; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.util.Date;

/**
 * 文件表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月03日 下午 02:49:29
 */
@Data
@ModelName(name = "文件表", tableName = "t_file_list")
public class FileList extends Model {  
 
	/**
	 * 主键:
	 */  
	@FieldName(name = "主键")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 文件地址:
	 */  
	@FieldName(name = "文件地址")  
	private String url;  
 
	/**
	 * 文件位置:1 oss 2  fastdfs
	 */  
	@FieldName(name = "文件位置")  
	private Integer position;  
 
	/**
	 * 文件用途: 1商品橱窗文件，2商品
	 */  
	@FieldName(name = "文件用途")  
	private Integer imgType;  
 
	/**
	 * 文件id
	 */  
	@FieldName(name = "文件id")  
	private String fileId;  
 
	/**
	 * 文件名称
	 */  
	@FieldName(name = "文件名称")  
	private String fileName;  
 
	/**
	 * 上传时间:
	 */  
	@FieldName(name = "上传时间")  
	private Date createTime;  
 
	/**
	 * 上传人id:
	 */  
	@FieldName(name = "上传人id")  
	private String uploadUserId;  
 
	/**
	 * 上传人名字:
	 */  
	@FieldName(name = "上传人名字")  
	private String uploadUserName;  
 
	/**
	 * 最后修改时间:
	 */  
	@FieldName(name = "最后修改时间")  
	private Date lastUpdateTime;  
 
	/**
	 * 最后修改人id:
	 */  
	@FieldName(name = "最后修改人id")  
	private String lastUpdateUserId;  
 
	/**
	 * 最后修改人名:
	 */  
	@FieldName(name = "最后修改人名")  
	private String lastUpdateUserName;  
 
	/**
	 * 文件code
	 */  
	@FieldName(name = "文件code")  
	private String fileCode;  
 
	/**
	 * 文件大小
	 */  
	@FieldName(name = "文件大小")  
	private Long size;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }