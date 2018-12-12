package com.onway.web.controller.result;

public class JsonPagePostResult<E> extends JsonPageResult<E> {

    /**  */
    private static final long serialVersionUID = 1L;

    public JsonPagePostResult(boolean bizSucc) {
        super(bizSucc);
    }

    private String[] imgList;

    public String[] getImgList() {
        return imgList;
    }

    public void setImgList(String[] imgList) {
        this.imgList = imgList;
    }

}
