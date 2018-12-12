package com.onway.baib.core.model.jsonresult;

public class PostUserInfo {

    private static final long serialVersionUID = 1L;

    private String            userName;

    private String[]          imgList;

    private String            baibName;

    private int               postId;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getbaibName() {
        return baibName;
    }

    public void setbaibName(String baibName) {
        this.baibName = baibName;
    }

    /**
     * This property corresponds to db column <tt>OBJECT_ID</tt>.
     */
    private String objectId;

    /**
     * This property corresponds to db column <tt>IMAGE_URL</tt>.
     */
    private String imageUrl;

    /**
     * This property corresponds to db column <tt>ROLE</tt>.
     */
    private String role;

    /**
     * This property corresponds to db column <tt>TITILE</tt>.
     */
    private String titile;

    /**
     * This property corresponds to db column <tt>CONTENT</tt>.
     */
    private String content;

    /**
     * This property corresponds to db column <tt>SHOW_FLAG</tt>.
     */
    private String showFlag;

    /**
     * This property corresponds to db column <tt>VERIFY_FLAG</tt>.
     */
    private String verifyFlag;

    /**
     * This property corresponds to db column <tt>PROCESS_STATUS</tt>.
     */
    private String processStatus;

    /**
     * This property corresponds to db column <tt>COMMENT_NUM</tt>.
     */
    private int    commentNum;

    /**
     * This property corresponds to db column <tt>LIKE_NUM</tt>.
     */
    private int    likeNum;

    /**
     * This property corresponds to db column <tt>TYPE</tt>.
     */
    private String type;

    /**
     * This property corresponds to db column <tt>GMT_CREATE</tt>.
     */
    private String gmtCreate;

    /**
     * This property corresponds to db column <tt>GMT_MODIFIED</tt>.
     */
    private String gmtModified;

    /**
     * This property corresponds to db column <tt>MEMO</tt>.
     */
    private String memo;

    /**
     * This property corresponds to db column <tt>IMAGES</tt>.
     */
    private String images;

    /**
     * This property corresponds to db column <tt>C_DELETE</tt>.
     */
    private String CDelete;

    /**
     * This property corresponds to db column <tt>SUGGESTION</tt>.
     */
    private String suggestion;

    /**
     * This property corresponds to db column <tt>CELL</tt>.
     */
    private String cell;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTitile() {
        return titile;
    }

    public void setTitile(String titile) {
        this.titile = titile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(String showFlag) {
        this.showFlag = showFlag;
    }

    public String getVerifyFlag() {
        return verifyFlag;
    }

    public void setVerifyFlag(String verifyFlag) {
        this.verifyFlag = verifyFlag;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getCDelete() {
        return CDelete;
    }

    public void setCDelete(String cDelete) {
        CDelete = cDelete;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String[] getImgList() {
        return imgList;
    }

    public void setImgList(String[] imgList) {
        this.imgList = imgList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
