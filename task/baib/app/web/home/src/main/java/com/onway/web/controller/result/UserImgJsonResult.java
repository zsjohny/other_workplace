package com.onway.web.controller.result;

/**
 * 用户图像信息结果集
 * 
 * @author qiang.wq
 * @version $Id: UserImgJsonResult.java, v 0.1 2015年9月4日 下午4:13:23 qiang.wq Exp $
 */
public class UserImgJsonResult extends JsonResult {


	public UserImgJsonResult(boolean bizSucc, String errCode, String message) {
		super(bizSucc, errCode, message);
	}

	/** serialVersionUID */
    private static final long serialVersionUID = -6929937906521074512L;

    /** 图片地址 */
    private String            imgUrl           = "";

    /** 是否设置过图片 */
    private boolean           isSetImg;
    
    /** 文件路径 */
    private String            filePath;
    
    /** 用户Id */
    private String            userId;
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isSetImg() {
        return isSetImg;
    }

    public void setSetImg(boolean isSetImg) {
        this.isSetImg = isSetImg;
    }
}
