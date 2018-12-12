package com.onway.baib.core.model.jsonresult;

public class PostTechnicianInfo {

    private final static long serialVersionUID = 1L;

    //≤…”√ ˝
    private int               adoptNum;

    private String            TechnicianInfoId;

    private String            TechnicianInfoName;

    private String            imageUrl;

    private int               no;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getTechnicianInfoName() {
        return TechnicianInfoName;
    }

    public void setTechnicianInfoName(String technicianInfoName) {
        TechnicianInfoName = technicianInfoName;
    }

    public String getTechnicianInfoId() {
        return TechnicianInfoId;
    }

    public void setTechnicianInfoId(String technicianInfoId) {
        TechnicianInfoId = technicianInfoId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getAdoptNum() {
        return adoptNum;
    }

    public void setAdoptNum(int adoptNum) {
        this.adoptNum = adoptNum;
    }

}
