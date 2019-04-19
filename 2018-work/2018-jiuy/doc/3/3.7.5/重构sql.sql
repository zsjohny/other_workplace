-- 添加一个code字段..查询的时候可以使用右like来查询
alter TABLE yjj_Category add COLUMN code varchar(64) COMMENT '类目编码'