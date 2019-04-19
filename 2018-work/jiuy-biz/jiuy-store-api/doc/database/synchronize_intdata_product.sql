/***************************************************************************/
/*功能：从测试线同步产品数据到正式线                                                                                                                                                                                   */
/*注意事项：无                                                                                                                                                                                                                                    */ 
/***************************************************************************/

/*同步产品表*/
delete from yjj_online.yjj_Product;
insert into yjj_online.yjj_Product select * from yjj.yjj_Product;
/*
create table yjj_online.yjj_Product as select * from yjj.yjj_Product;
*/
/*同步产品分类表*/
delete from yjj_online.ProductCategory;
insert into yjj_online.ProductCategory select * from yjj.ProductCategory;
/*
create table yjj_online.yjj_ProductCategory as select * from yjj.yjj_ProductCategory;
*/
/*同步产品属性表*/
delete from yjj_online.ProductProperty;
insert into yjj_online.ProductProperty select * from yjj.ProductProperty;
/*
create table yjj_online.yjj_ProductProperty as select * from yjj.yjj_ProductProperty;
*/
/*同步产品SKU*/
delete from yjj_online.yjj_ProductSKU;
insert into yjj_online.yjj_ProductSKU select * from yjj.yjj_ProductSKU;
/*
create table yjj_online.yjj_ProductSKU as select * from yjj.yjj_ProductSKU;
*/