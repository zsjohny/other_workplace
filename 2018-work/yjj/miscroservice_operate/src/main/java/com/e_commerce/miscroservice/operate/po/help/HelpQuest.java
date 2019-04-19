package com.e_commerce.miscroservice.operate.po.help;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import lombok.Data;

import java.util.Date;
@Data
public class HelpQuest {
    @Id
    private Integer id;
    //type问题的类型
    private String type;
    //问题的名字
    private String questName;
    //创建用户的名字
    private String userName;
    //创建的时间
    private Date createTime;
    //有帮助
    private String help;
    //无帮助
    private String noHelp;
    //排序
    private String order;
    //问题类型的状态(显示/隐藏)
    private String state;
    //问题的回答
    private String answer;
    //问题类型的标识
    private String typeState;
    //空余字段
    private String free1;
    private String free2;
    private String free3;





}
