package com.onway.web.controller.result;

import java.util.List;

import com.onway.cif.common.service.model.UserInfo;


public class RelationListResult extends JsonResult{
   private static final long serialVersionUID = 741231858441822688L;
   
   /**
    * @param bizSucc
    * @param errCode
    * @param message
    */
   public RelationListResult(boolean bizSucc, String errCode, String message) {
      super(bizSucc, message, message);
   }
   
   public List<UserInfo> getUserInfoList() {
    return userInfoList;
}

   public void setUserInfoList(List<UserInfo> userInfoList) {
           this.userInfoList = userInfoList;
   }

   private List<UserInfo> userInfoList;

}
