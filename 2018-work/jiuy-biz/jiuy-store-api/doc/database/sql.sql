DROP TABLE IF EXISTS `yjj_Address`;
CREATE TABLE `yjj_Address` (
  `AddrId` int(11) NOT NULL AUTO_INCREMENT COMMENT '地址id',
  `UserId` int(20) NOT NULL COMMENT '关联的用户id',
  `ReceiverName` varchar(50) NOT NULL COMMENT '收件人名称',
  `ProvinceName` varchar(24) NOT NULL COMMENT '省编码',
  `CityName` varchar(24) NOT NULL COMMENT '市编码',
  `DistrictName` varchar(24) NOT NULL COMMENT '区/县编码',
  `AddrDetail` varchar(512) DEFAULT NULL COMMENT '详细地址',
  `MailCode` varchar(24) DEFAULT NULL COMMENT '邮政编码',
  `Telephone` varchar(22) DEFAULT NULL COMMENT '手机号码',
  `FixPhone` varchar(64) DEFAULT NULL COMMENT '固话，以-横杠作为分隔符',
  `AddrFull` varchar(800) DEFAULT NULL COMMENT '完整地址',
  `Status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态0-可用',
  `IsDefault` tinyint(4) NOT NULL DEFAULT '0' COMMENT '默认标识0-当前为默认，非0标识不是默认，并按照数字先后作为排序依据',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`AddrId`,`UserId`),
  KEY `idx_userid` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='地址表';

DROP TABLE IF EXISTS `yjj_supplier`;
create table `yjj_supplier`(
	`supplier_code` varchar(60) NOT NULL COMMENT '厂商id，企业信用编码',
	`inner_code` int NOT NULL AUTO_INCREMENT COMMENT '厂商内部id',
	`user_id` varchar(60) NOT NULL COMMENT '关联的用户id',
	`supplier_name` varchar(100) NOT NULL COMMENT '厂商名',
	`supplier_logo` varchar(500) NULL COMMENT '厂商头像',
	`supplier_desc` varchar(500) NULL COMMENT '厂商描述',
	`province_id` tinyint(4) NOT NULL default '0' COMMENT '省编码',
	`city_id` tinyint(4) NOT NULL default '0' COMMENT '市编码',
	`addr_detail` varchar(500) NULL COMMENT '详细地址',
	`contract_list` text NULL COMMENT '合同号列表',
	`contact_name` varchar(60) NULL COMMENT '联系人名',
	`contact_phone` varchar(60) NULL COMMENT '联系人电话',
	`supplier_status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态1-可用',
	`create_time` bigint(20) NOT NULL COMMENT '创建时间',
	`update_time` bigint(20) NOT NULL COMMENT '更新时间',
	UNIQUE KEY `uk_inner_supplier_code` (`inner_code`),
	PRIMARY KEY (`supplier_code`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='厂商表';

DROP TABLE IF EXISTS `yjj_batch_no`;
create table `yjj_batch_no`(
	`batch_no` varchar(60) NOT NULL COMMENT '批次号',
	`inner_code` int NOT NULL AUTO_INCREMENT COMMENT '内部批次id',
	`title` varchar(100) NULL COMMENT '批次标题',
	`purchase_site` varchar(60) NOT NULL default '' COMMENT '采购地',
	`warehouse_site` varchar(60) NOT NULL default '' COMMENT '配送地，仓库',
	`supplier_code` varchar(60) NOT NULL COMMENT '厂商id，企业信用编码',
	`gender` tinyint(4) NOT NULL DEFAULT '0' COMMENT '适用性别，0-ALL，1-男，2-女',
	`size_list` int NOT NULL DEFAULT '0' COMMENT '尺码列表',
	`color_list` int NOT NULL DEFAULT '0' COMMENT '颜色列表',
	`photo_list` text NULL COMMENT '图片列表',
	`attr_pair_list` text NULL COMMENT '其他属性键值对，json格式',
	`catagory_list1` bigint UNSIGNED NOT NULL DEFAULT '0' COMMENT '分类，可供查询用，二进制，2^64',
	`catagory_list2` bigint UNSIGNED NOT NULL DEFAULT '0' COMMENT '分类，可供查询用，二进制，2^64',
	`catagory_list3` bigint UNSIGNED NOT NULL DEFAULT '0' COMMENT '分类，可供查询用，二进制，2^64',
	`catagory_list4` bigint UNSIGNED NOT NULL DEFAULT '0' COMMENT '分类，可供查询用，二进制，2^64',
	`catagory_list5` bigint UNSIGNED NOT NULL DEFAULT '0' COMMENT '分类，可供查询用，二进制，2^64',
	`status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态1-未上架',
	`load_time` bigint(20) NOT NULL COMMENT '入货时间',
	`create_time` bigint(20) NOT NULL COMMENT '创建时间',
	`update_time` bigint(20) NOT NULL COMMENT '更新时间',
	`token_ratio` int NOT NULL default '0' COMMENT '对应代币',
	`total_count` int NOT NULL default '0' COMMENT '总数量',
	UNIQUE KEY `uk_inner_batch_no` (`inner_code`),
	PRIMARY KEY (`batch_no`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='批次号表';

alter table yjj_batch_no add batchid int(20) not null auto_increment ,add primary key (batchid);

DROP TABLE IF EXISTS `yjj_qrcode`;
create table `yjj_qrcode`(
	`qrcode_id` varchar(100) NOT NULL COMMENT '二维码id',
	`qrcode_url` varchar(100) NOT NULL COMMENT '二维码url',
	`batch_no` varchar(60) NOT NULL COMMENT '批次号',
	`size` smallint(6) NOT NULL DEFAULT '0' COMMENT '尺码代码',
	`size_txt` varchar(60) NOT NULL DEFAULT '0' COMMENT '尺码描述',
	`color` smallint(6) NOT NULL DEFAULT '0' COMMENT '颜色',
	`color_txt` varchar(60) NOT NULL DEFAULT '0' COMMENT '颜色描述',
	`title` varchar(100) NULL COMMENT '服装标题',
	`photo_list` text NULL COMMENT '图片列表',
	`attr_pair_list` text NULL COMMENT '其他属性键值对，json格式',
	`consumer_user_id` varchar(60) NULL COMMENT '消费者id',
	`exchange_user_id` varchar(60) NULL COMMENT '兑换者id',
	`goods_id` varchar(60) NULL COMMENT '商品id',
	`status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态1-可用',
	`create_time` bigint(20) NOT NULL COMMENT '创建时间',
	`update_time` bigint(20) NOT NULL COMMENT '更新时间',
	PRIMARY KEY (`qrcode_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='二维码表，库存表';


DROP TABLE IF EXISTS `tb_task`;
create table `tb_task`(
	`task_id` int NOT NULL AUTO_INCREMENT COMMENT '任务id',
	`status` tinyint NOT NULL DEFAULT '0' COMMENT '状态 0-创建, 1-正在执行, 3-完成, 4-失败',
	`retry` tinyint NOT NULL DEFAULT '0' COMMENT '已重试次数',
	`parent_id` int NOT NULL  COMMENT '父任务id',
	`create_time` bigint(20) NOT NULL COMMENT '创建时间',
	`update_time` bigint(20) NOT NULL COMMENT '更新时间',
	`data` text NULL COMMENT '数据，json格式',
	`query_key` varchar(60) NOT NULL DEFAULT '' COMMENT '查询键',
	PRIMARY KEY (`task_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务表';


DROP TABLE IF EXISTS `yjj_MailRegister`;
CREATE TABLE `yjj_MailRegister` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `RegisterUuid` varchar(50) NOT NULL COMMENT '注册的uuid',
  `UserName` varchar(100) NOT NULL COMMENT '用户名',
  `UserEmail` varchar(100) NOT NULL COMMENT '用户邮箱',
  `UserPassword` varchar(60) NOT NULL COMMENT '密码',
  `ExpireTime` bigint(20) NOT NULL COMMENT '失效时间',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`Id`),
  KEY `idx_username` (`UserName`),
  KEY `idx_uuid` (`RegisterUuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='邮箱用户注册信息表';

DROP TABLE IF EXISTS `yjj_User`;
CREATE TABLE `yjj_User` (
  `UserId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `UserName` varchar(100) NOT NULL COMMENT '用户名',
  `UserRelatedName` varchar(100) NOT NULL COMMENT '用户关联账号：邮箱、手机号..',
  `UserType` tinyint(4) unsigned DEFAULT '0' COMMENT '用户账号类型：0邮箱账号，1手机号账号',
  `UserNickname` varchar(100) DEFAULT NULL COMMENT '用户昵称',
  `UserIcon` varchar(500) DEFAULT NULL COMMENT '用户头像',
  `UserPassword` varchar(50) NOT NULL COMMENT '密码',
  `UserRole` tinyint(4) DEFAULT '0' COMMENT '角色，默认0-消费者，二进制',
  `UserPoints` bigint(20) DEFAULT '0' COMMENT '用户积分',
  `Status` tinyint(4) DEFAULT '0' COMMENT '状态0正常，其他..',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
  `UserDefinedLocations` varchar(2048) DEFAULT NULL COMMENT 'json格式，用户定义的收获地址，定义格式为{recipients:"",location:"||||",detail:"",code:"",phone:"",fixnumber:"",default:""}',
  `AccessToken` varchar(64) DEFAULT NULL COMMENT 'accesstoken',
  `AccessUpdateTime` bigint(20) DEFAULT '0' COMMENT '访问token创建时间',
  `AccessValidTime` bigint(20) DEFAULT '0' COMMENT '访问token有效时间',
  PRIMARY KEY (`UserId`),
  UNIQUE KEY `uk_user_name` (`UserName`),
  UNIQUE KEY `uk_user_related_name` (`UserRelatedName`),
  KEY `idx_user_accesstoken` (`AccessToken`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='用户表';

DROP TABLE IF EXISTS `Tb_Product`;
CREATE TABLE `Tb_Product` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `SaleState` TINYINT NOT NULL COMMENT '销售状态 0-未上架 1-已上架 2-已下架',
  `SaleStartTime` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '上架时间',
  `SaleEndTime` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '下架时间',
  `SaleCurrencyType` TINYINT NOT NULL COMMENT '销售类型：0-人民币 1-玖币',
  `SaleTotalCount` INT NOT NULL DEFAULT 0 COMMENT '总销量',
  `SaleMonthlyMaxCount` INT NOT NULL DEFAULT 0 COMMENT '月销量最大值，每月初重新计算',
  `Title` VARCHAR(128) NOT NULL DEFAULT '0' COMMENT '商品名称',
  `Classification` VARCHAR(32) COMMENT '商品分类：长裤，九分裤等',
  `Description` VARCHAR(4096) COMMENT '商品描述',
  `Price` FLOAT COMMENT '商品一口价，默认显示价格',
  `DetailImages` VARCHAR(1024) NOT NULL COMMENT '商品展示图片:JSON 格式数组',
  `Favorite` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '收藏数',
  `Style` VARCHAR(512) COMMENT '风格',
  `Material` VARCHAR(512) COMMENT '材质',
  `Brand` VARCHAR(64)  COMMENT '品牌',
  `SerialNum` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '货号',
  `Season` VARCHAR(16)  COMMENT '年度季节',
  `LocationProvince` VARCHAR(32) NOT NULL COMMENT '所在地：省',
  `LocationCity` VARCHAR(32) NOT NULL COMMENT '所在地：市',
  `LocationArea` VARCHAR(32) NOT NULL COMMENT '所在地：区',
  `ExpressFree` TINYINT(1) NOT NULL COMMENT '是否免邮 0-免邮 1-不免邮',
  `ExpressDetails` VARCHAR(1024) DEFAULT  NULL COMMENT 'JSON 格式的邮费说明',
  `Specifications` VARCHAR(8192) NOT NULL COMMENT '规格及库存',
  `Element` VARCHAR(512) COMMENT '时尚元素',
  `MarketPrice` FLOAT COMMENT '市场价，不是玖币价格',
  `AttributeComment` VARCHAR(512) COMMENT '商品参数描述',
  `NewModelID` bigint(20) COMMENT '对应新商品的商品ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品表（原始产品模型，后续废弃）';

DROP TABLE IF EXISTS `Tb_Product_SaleCount`;
CREATE TABLE `Tb_Product_SaleCount` (
  `ID` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '销量ID',
  `ProductID` BIGINT(20) NOT NULL COMMENT '商品 ID',
  `Size` TINYINT NOT NULL COMMENT '商品尺寸',
  `Color` VARCHAR(128) NOT NULL COMMENT '商品颜色图片',
  `SaleCount` INT NOT NULL DEFAULT 0 COMMENT '具体规格的某商品的销量',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `TPS_PSC` (`ProductID`, `Size`, `Color`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品销量表(某种具体规格的销量)，订单成交时更新数据';

DROP TABLE IF EXISTS `Tb_Product_MonthlySaleCount`;
CREATE TABLE `Tb_Product_MonthlySaleCount` (
  `ID` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '存档 ID',
  `ProductID` BIGINT(20) NOT NULL COMMENT '商品 ID',
  `Year` SMALLINT NOT NULL COMMENT '年份，如2015，2016',
  `Month` TINYINT NOT NULL COMMENT '月份,1-12',
  `SaleCount` INT NOT NULL DEFAULT 0 COMMENT '月销量',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PMSC_PIDYM` (`ProductID`, `Year`, `Month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品月销量表，订单成交时更新数据';

DROP TABLE IF EXISTS `Tb_Assessment`;
CREATE TABLE `Tb_Assessment` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `UserId` bigint(20) NOT NULL COMMENT '用户ID',
  `ProductId` bigint(20) NOT NULL COMMENT '商品ID',
  `AssessTime` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '评价时间',
  `AssessLevel` TINYINT NOT NULL DEFAULT 0 COMMENT '总体评价级别：1-5五个级别，同淘宝',
  `AssessContent` varchar(512) NOT NULL COMMENT '评价内容',
  `ItemAssessContent` varchar(512) NOT NULL COMMENT 'json格式的单项评价内容，如服务评价等',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='货品评价表';

create index assessment_user_id on `Tb_Assessment`(`UserId`);
create index assessment_prodid_id on `Tb_Assessment`(`ProductId`);


DROP TABLE IF EXISTS `Tb_PurchaseDetail`;
CREATE TABLE `Tb_PurchaseDetail` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '购买详情序号ID',
  `UserId` bigint(20) NOT NULL COMMENT '用户ID',
  `ProductId` bigint(20) NOT NULL COMMENT '商品ID',
  `PurchaseTime` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '购买时间',
  `PurchaseDetailContent` varchar(512) NOT NULL COMMENT 'json格式的购买详情，如产品的颜色和数量',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='购买详情表';

create index purchase_user_id on `Tb_PurchaseDetail`(`UserId`);
create index purchase_prodid_id on `Tb_PurchaseDetail`(`ProductId`);

DROP TABLE IF EXISTS `Tb_Classification_Definition`;
CREATE TABLE `Tb_Classification_Definition` (
  `Id` int(4) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `ClassificationBelong` varchar(32) NOT NULL COMMENT '总体归属',
  `ClassificationName` varchar(32) NOT NULL COMMENT '大类名称',
  `SubClassification` varchar(512) NOT NULL COMMENT '子类列表',
  `Outline`  varchar(512) NOT NULL COMMENT '廓型列表',
  `Material` varchar(512) NOT NULL COMMENT '面料材质列表',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='原始衣服分类定义表，后续废弃';

DROP TABLE IF EXISTS `Tb_Dictionary`;
CREATE TABLE `Tb_Dictionary` (
  `GroupId` varchar(32) NOT NULL COMMENT '字典组ID',
  `GroupName` varchar(32) NOT NULL comment '分组名称',
  `DictId` varchar(32) NOT NULL COMMENT '字典ID',
  `DictName` varchar(512) NOT NULL COMMENT '字典内容',
  `Status`  varchar(512) NOT NULL COMMENT '有效状态',
  PRIMARY KEY (`GroupId`,`DictId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典定义表';

create index dict_group_id on `Tb_Dictionary`(`GroupId`);
create index dict_dict_id on `Tb_Dictionary`(`DictId`);


DROP TABLE IF EXISTS `Tb_Tree_Dictionary`;
CREATE TABLE `Tb_Tree_Dictionary` (
  `GroupId` varchar(32) NOT NULL COMMENT '字典组ID',
  `GroupName` varchar(32) NOT NULL comment '分组名称',
  `DictId` varchar(32) NOT NULL COMMENT '字典ID',
  `DictName` varchar(512) NOT NULL COMMENT '字典内容',
  `ParentId` varchar(32) COMMENT '父节点字典ID，自关联到DictId',
  `DictLevel` INT NOT NULL DEFAULT 0 COMMENT '当前节点所在树级别',
  `Status`  varchar(512) NOT NULL COMMENT '有效状态',
  PRIMARY KEY (`GroupId`,`DictId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='树形字典定义表';

create index tree_dict_parentid on Tb_Tree_Dictionary(ParentId);
create index tree_dict_dictlevel on Tb_Tree_Dictionary(DictLevel);

-----add by BaiZhichao on 2015/07/17

DROP TABLE IF EXISTS `yjj_Category`;
CREATE TABLE `yjj_Category` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `CategoryName` varchar(60) NOT NULL COMMENT '分类名称',
  `ParentId` bigint(20) DEFAULT '0' COMMENT '分类父id，0表示顶级分类',
  `Status` tinyint(4) DEFAULT '0' COMMENT '状态:-1删除，0正常',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品分类表';

DROP TABLE IF EXISTS `yjj_PropertyName`;
CREATE TABLE `yjj_PropertyName` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '属性名id',
  `PropertyName` varchar(100) NOT NULL COMMENT '属性名称',
  `Status` tinyint(4) DEFAULT '0' COMMENT '状态:-1删除，0正常',
  `OrderIndex` tinyint(4) DEFAULT '0' COMMENT '排序索引',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品属性名表';

DROP TABLE IF EXISTS `yjj_PropertyValue`;
CREATE TABLE `yjj_PropertyValue` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '属性值id',
  `PropertyValue` varchar(100) NOT NULL COMMENT '属性值',
  `PropertyNameId` bigint(20) NOT NULL COMMENT '对应属性名id',
  `Status` tinyint(4) DEFAULT '0' COMMENT '状态:-1删除，0正常',
  `OrderIndex` tinyint(4) DEFAULT '0' COMMENT '排序索引',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `uk_nid_value` (`PropertyNameId`,`PropertyValue`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品属性值表';

DROP TABLE IF EXISTS `yjj_Product`;
CREATE TABLE `yjj_Product` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `Name` varchar(200) NOT NULL COMMENT '商品名',
  `CategoryId` bigint(20) NOT NULL COMMENT '所属分类id',
  `DetailImages` varchar(1024) NOT NULL COMMENT '商品展示图片:JSON 格式数组',
  `SummaryImages` varchar(1024) NOT NULL COMMENT '概要图 json数组',
  `Status` tinyint(4) DEFAULT '0' COMMENT '状态:-1删除，0正常',
  `SaleStartTime` bigint(20) NOT NULL COMMENT '上架时间',
  `SaleEndTime` bigint(20) NOT NULL COMMENT '下架时间',
  `SaleCurrencyType` tinyint(4) NOT NULL COMMENT '销售类型：0-人民币 1-玖币',
  `SaleTotalCount` int(11) DEFAULT '0' COMMENT '总销量',
  `SaleMonthlyMaxCount` int(11) DEFAULT '0' COMMENT '月销量最大值，每月初重新计算',
  `Price` int(11) NOT NULL COMMENT '价格冗余字段，用于列表显示，添加SKU的时候更新一个最小的价格到这个字段',
  `Favorite` bigint(20) DEFAULT '0' COMMENT '收藏数',
  `AssessmentCount` bigint(20) DEFAULT '0' COMMENT '评论数',
  `ExpressFree` tinyint(4) NOT NULL COMMENT '是否免邮 0-免邮 1-不免邮',
  `ExpressDetails` varchar(1024) DEFAULT NULL COMMENT 'JSON 格式的邮费说明',
  `MarketPrice` int COMMENT '市场价，不是玖币价格',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
  `ProductSeq` varchar(50) DEFAULT NULL COMMENT '款号',
  PRIMARY KEY (`Id`),
  KEY `idx_categoryid` (`CategoryId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品表';

DROP TABLE IF EXISTS `yjj_ProductProperty`;
CREATE TABLE `yjj_ProductProperty` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '属性id',
  `ProductId` bigint(20) NOT NULL COMMENT '商品id',
  `PropertyNameId` bigint(20) NOT NULL COMMENT '属性名id',
  `PropertyValueId` bigint(20) NOT NULL COMMENT '属性值id',
  `IsSKU` tinyint(4) DEFAULT '0' COMMENT '是否SKU属性',
  `OrderIndex` tinyint(4) DEFAULT '0' COMMENT '排序索引',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `uk_pid_nid_vid` (`ProductId`,`PropertyNameId`,`PropertyValueId`),
  KEY `idx_productid` (`ProductId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品属性表';

DROP TABLE IF EXISTS `yjj_ProductSKU`;
CREATE TABLE `yjj_ProductSKU` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `ProductId` bigint(20) NOT NULL COMMENT '商品id',
  `PropertyIds` varchar(500) NOT NULL COMMENT '商品SKU属性值聚合，ProductProperty的id以,隔开',
  `Price` int(11) NOT NULL COMMENT '价格，人民币以分为单位，玖币以1为单位',
  `RemainCount` int(11) NOT NULL COMMENT '库存',
  `SpecificImage` varchar(256) COMMENT '对应SKU的图片信息',
  `Status` tinyint(4) DEFAULT '0' COMMENT '状态:-1删除，0正常',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`Id`),
  KEY `idx_productid` (`ProductId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品SKU表';

DROP TABLE IF EXISTS `yjj_ShoppingCart`;
CREATE TABLE `yjj_ShoppingCart` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `UserId` bigint(20) NOT NULL COMMENT '用户id',
  `ProductId` bigint(20) NOT NULL COMMENT '商品id',
  `SkuId` bigint(20) NOT NULL COMMENT '对应ProductSKU的id',
  `BuyCount` int(11) NOT NULL COMMENT '购买数量',
  `Status` tinyint(4) DEFAULT '0' COMMENT '状态:-1删除，0正常',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `uk_userid_skuid` (`UserId`,`SkuId`),
  KEY `idx_userid` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='购物车表';

DROP TABLE IF EXISTS `yjj_Order`;
CREATE TABLE `yjj_Order` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `OrderNo` varchar(30) NOT NULL COMMENT '订单编号，前台展示，格式为当前时间yyyyMMddHHmmssSSS后面加6位字母数字随机串',
  `UserId` bigint(20) NOT NULL COMMENT '用户id',
  `OrderType` tinyint(4) NOT NULL COMMENT '订单类型：0-结算支付 1-回寄旧衣',
  `OrderStatus` tinyint(4) NOT NULL COMMENT '订单付款状态：参照参照OrderStatus',
  `TotalMoney` int(11) NOT NULL COMMENT '订单金额总价，不包含邮费',
  `TotalExpressMoney` int(11) NOT NULL COMMENT '邮费总价',
  `ExpressInfo` varchar(1024) NOT NULL COMMENT '邮寄信息',
  `AvalCoinUsed` int(11) NOT NULL COMMENT '使用的激活玖币',
  `UnavalCoinUsed` int(11) NOT NULL COMMENT '使用的未激活玖币',
  `PayAmountInCents` int(11) NOT NULL COMMENT '用户为该笔订单支付的钱总数',
  `Remark` varchar(1024) DEFAULT NULL COMMENT '用户订单备注',
  `Status` tinyint(4) DEFAULT '0' COMMENT '状态:-1删除，0正常',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
  `ExpressSupplier` varchar(32) DEFAULT NULL COMMENT '快递提供商',
  `ExpressOrderNo` varchar(64) DEFAULT NULL COMMENT '快递订单号',
  `ExpressUpdateTime` bigint(20) DEFAULT NULL COMMENT '快递信息更新时间',
  `Sended` tinyint(4) DEFAULT '0' COMMENT '回寄订单是否已填写资料标记',
  `Platform` varchar(30) DEFAULT NULL COMMENT '生成订单平台',
  `PlatformVersion` varchar(45) DEFAULT NULL COMMENT '生成订单平台版本号',
  `Ip` varchar(50) DEFAULT NULL COMMENT '客户端ip',
  `PaymentNo` varchar(50) DEFAULT NULL COMMENT '第三方的支付订单号',
  `PaymentType` tinyint(4) DEFAULT '0' COMMENT '使用的第三方支付方式，参考常量PaymentType',
  PRIMARY KEY (`Id`),
  KEY `idx_orderno` (`OrderNo`),
  KEY `idx_uid_orderno` (`UserId`,`OrderNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单表';

DROP TABLE IF EXISTS `yjj_OrderItem`;
CREATE TABLE `yjj_OrderItem` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `OrderId` bigint(20) NOT NULL COMMENT '对应Order表的id',
  `UserId` bigint(20) NOT NULL COMMENT '用户id',
  `ProductId` bigint(20) NOT NULL COMMENT '对应Product表的id',
  `SkuId` bigint(20) NOT NULL COMMENT '对应ProductSKU的id',
  `TotalMoney` int(11) NOT NULL COMMENT '订单金额总价，不包含邮费',
  `TotalExpressMoney` int(11) NOT NULL COMMENT '邮费总价',
  `Money` int(11) NOT NULL COMMENT '订单细目单价，不包含邮费',
  `ExpressMoney` int(11) NOT NULL COMMENT '邮费单价',
  `BuyCount` int(11) NOT NULL COMMENT '订购数量',
  `SkuSnapshot` varchar(1024) NOT NULL COMMENT 'sku快照，json',
  `Status` tinyint(4) DEFAULT '0' COMMENT '状态:-1删除，0正常',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`Id`),
  KEY `idx_userid` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户订单细目表';

DROP TABLE IF EXISTS `yjj_OrderLog`;
CREATE TABLE `yjj_OrderLog` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `UserId` bigint(20) NOT NULL COMMENT '关联User表的userId',
  `OrderId` bigint(20) NOT NULL COMMENT '关联Order表的id',
  `OldStatus` tinyint(4) NOT NULL COMMENT '老的订单状态',
  `NewStatus` tinyint(4) NOT NULL COMMENT '更新的订单状态',
  `CreateTime` bigint(20) NOT NULL COMMENT '记录创建时间',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单日志记录';

DROP TABLE IF EXISTS `yjj_SendBack`;
CREATE TABLE `yjj_SendBack` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `UserId` bigint(20) NOT NULL COMMENT '用户id',
  `OrderId` bigint(20) NOT NULL COMMENT '对应的Order表的id',
  `CreateTime` bigint(20) DEFAULT '0' COMMENT '创建时间',
  `UpdateTime` bigint(20) DEFAULT '0' COMMENT '更新时间',
  `Status` tinyint(4) DEFAULT '0',
  `ExpressSupplier` varchar(32) NOT NULL COMMENT '物流公司',
  `ExpressOrderNo` varchar(64) NOT NULL COMMENT '快递单号',
  `Phone` varchar(20) NOT NULL COMMENT '联系电话',
  PRIMARY KEY (`Id`),
  KEY `idx_uid_orderid` (`UserId`,`OrderId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='旧衣回寄记录表';

DROP TABLE IF EXISTS `yjj_UserCoin`;
CREATE TABLE `yjj_UserCoin` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `UserId` bigint(20) NOT NULL COMMENT '用户id',
  `AvalCoins` int(11) DEFAULT '0' COMMENT '用户激活玖币',
  `UnavalCoins` int(11) DEFAULT '0' COMMENT '用户未激活玖币',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `uk_userid` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户玖币表';

DROP TABLE IF EXISTS `yjj_UserCoinLog`;
CREATE TABLE `yjj_UserCoinLog` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `UserId` bigint(20) NOT NULL COMMENT '用户Id',
  `Operation` tinyint(4) NOT NULL COMMENT '操作类型',
  `OldAvalCoins` int(11) NOT NULL COMMENT '原有的激活玖币值',
  `NewAvalCoins` int(11) NOT NULL COMMENT '更新后的激活玖币值',
  `OldUnavalCoins` int(11) NOT NULL COMMENT '原有的未激活玖币值',
  `NewUnavalCoins` int(11) NOT NULL COMMENT '更新后的未激活玖币值',
  `RelatedId` varchar(50) DEFAULT NULL COMMENT '对应的关联操作的id',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`Id`),
  KEY `idx_userid` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户玖币操作日志表';

DROP TABLE IF EXISTS `yjj_ProductCategory`;
CREATE TABLE `yjj_ProductCategory` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `ProductId` bigint(20) NOT NULL COMMENT '产品ID',
  `CategoryId` bigint(20) NOT NULL COMMENT '分类ID',
  `Status` tinyint(4) DEFAULT '0' COMMENT '状态',
  `CreateTime` bigint(20) DEFAULT '0' COMMENT '创建时间',
  `UpdateTime` bigint(20) DEFAULT '0' COMMENT '修改时间',
  PRIMARY KEY (`Id`),
  KEY `idx_porductid` (`ProductId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品分类关联表';

DROP TABLE IF EXISTS `yjj_CategoryProperty`;
CREATE TABLE `yjj_CategoryProperty` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类与属性对应关系id',
  `CategoryId` bigint(20) NOT NULL COMMENT '分类ID',
  `PropertyNameId` bigint(20) NOT NULL COMMENT '属性名id',
  `PropertyValueId` bigint(20) NOT NULL COMMENT '属性值id',
  `Status` tinyint(4) DEFAULT '0' COMMENT '状态:-1删除，0正常',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `uk_cid_nid_vid` (`CategoryId`,`PropertyNameId`,`PropertyValueId`),
  KEY `idx_cid` (`CategoryId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分类属性关系表，定义某一分类下，属性的存在性以及属性值的取值范围';

DROP TABLE IF EXISTS `yjj_PaymentLog`;
CREATE TABLE `yjj_PaymentLog` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '全局唯一序列号',
  `UserId` bigint(20) NOT NULL COMMENT '用户号',
  `OrderId` bigint(20) NOT NULL COMMENT '订单ID',
  `OrderNo` varchar(30) NOT NULL COMMENT '订单号',
  `PaymentCount` bigint(20) NOT NULL COMMENT '支付金额',
  `BuyerEmail` varchar(32) DEFAULT NULL COMMENT '支付者支付宝账号',
  `BuyerId` varchar(32) DEFAULT NULL COMMENT '支付者支付宝ID',
  `SellerEmail` varchar(32) DEFAULT NULL COMMENT '收款人支付宝账号',
  `SellerId` varchar(32) DEFAULT NULL COMMENT '收款人支付宝ID',
  `TradeNo` varchar(64) DEFAULT NULL COMMENT '支付宝交易号',
  `TradeStatus` varchar(32) DEFAULT NULL COMMENT '状态:-1删除，0正常',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
  `OperationPoint` varchar(32) DEFAULT NULL COMMENT '执行操作的点：支付前、支付后',
  PRIMARY KEY (`Id`),
  KEY `idx_paymentlog_orderid` (`OrderId`),
  KEY `idx_paymentlog_userid` (`UserId`),
  UNIQUE KEY `idx_paymentlog_tradeno` (`TradeNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='支付日志信息表';

DROP TABLE IF EXISTS `yjj_UserSign`;
CREATE TABLE `yjj_UserSign` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `UserId` bigint(20) NOT NULL COMMENT '用户id',
  `DayTime` int(11) NOT NULL COMMENT '当天0时时间，格式YYYYMMdd',
  `MondayTime` int(11) NOT NULL COMMENT '每周的周一时间，格式YYYYMMdd',
  `WeekDay` tinyint(4) NOT NULL COMMENT '周几星期一用1表示',
  `GrantCoins` int(11) NOT NULL COMMENT '获得玖币值',
  `CreateTime` bigint(20) NOT NULL COMMENT '记录创建时间',
  `UpdateTime` bigint(20) NOT NULL COMMENT '记录更新时间',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `uk_uid_daytime` (`UserId`,`DayTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户签到表';

CREATE TABLE `yjj_SmsRecord` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  `Phone` varchar(20) NOT NULL COMMENT '手机号',
  `Content` varchar(200) NOT NULL COMMENT '短信内容',
  PRIMARY KEY (`Id`),
  KEY `idx_phone` (`Phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='短信发送记录';

DROP TABLE IF EXISTS `yjj_AdminUser`;
CREATE TABLE `yjj_AdminUser` (
  `UserId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `UserName` varchar(100) NOT NULL COMMENT '用户名',
  `UserIcon` varchar(500) DEFAULT NULL COMMENT '用户头像',
  `UserPassword` varchar(60) NOT NULL COMMENT '密码',
  `UserRole` bigint(20) DEFAULT '0' COMMENT '用户角色',
  `UserDeviceMAC` varchar(100) NOT NULL COMMENT '用户设备MAC地址',
  `Status` tinyint(4) DEFAULT '0' COMMENT '状态0正常，其他..',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`UserId`),
  UNIQUE KEY `uk_user_name` (`UserName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='后台管理用户表';

DROP TABLE IF EXISTS `yjj_RoleDefine`;
CREATE TABLE `yjj_RoleDefine` (
  `RoleId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `RoleDesc` varchar(100) NOT NULL COMMENT '角色描述',
  `Status` tinyint(4) DEFAULT '0' COMMENT '状态0正常，其他..',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`RoleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='后台管理角色定义表';


DROP TABLE IF EXISTS `yjj_RoleAuth`;
CREATE TABLE `yjj_RoleAuth` (
  `RoleAuthorizationId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色权限id',
  `RoleId` bigint(20) NOT NULL COMMENT '角色ID',
  `AuthId` bigint(20) DEFAULT NULL COMMENT '用户头像',
  `Status` tinyint(4) DEFAULT '0' COMMENT '状态0正常，其他..',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`RoleAuthorizationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='后台管理用户角色权限对应表';

DROP TABLE IF EXISTS `yjj_AuthDefine`;
CREATE TABLE `yjj_AuthDefine` (
  `AuthId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '权限id',
  `AuthType` tinyint(4) DEFAULT '0'  COMMENT '权限类型：控制菜单：0、控制按钮：1',
  `AuthPath` varchar(100) NOT NULL COMMENT '权限可访问路径',
  `AuthPathDeny` varchar(100) NOT NULL COMMENT '权限不可访问路径',
  `Status` tinyint(4) DEFAULT '0' COMMENT '状态0正常，其他..',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`AuthId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='后台管理权限定义表';

