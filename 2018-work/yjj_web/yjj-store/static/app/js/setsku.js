
//1、为颜色数组和尺寸数组赋值
//2、解析获得规格列表

var colorArr = new Array();//颜色数组，格式例：[{id:xx,state:true}]
var sizeArr = new Array();//尺寸数组，格式例：[{id:xx,state:true}]
var skuArr = new Array();//{colorId:xx,sizeId:xx,count:xx}
var haveStockArr = new Array();//有库存规格信息数组
var noHaveStockArr = new Array();//没有库存规格信息数组

//1、初始化颜色和尺寸数组，colorIdArr（颜色ID数组），sizeIdArr（尺寸ID数组）
function initColorAndSize(g_skuMap,g_skuProps){
	for(var key in g_skuMap)  {
		var value = g_skuMap[key];
		//console.log("initColorAndSize skuArr::"+JSON.stringify(value));
		  
		var skuKeyArr = key.split(";"); //字符分割 
	
		var color = skuKeyArr[0].split(":");
		var colorId = color[1];
		var size = skuKeyArr[1].split(":");
		var sizeId = size[1];
		var colorName = value.color;
		var sizeName = value.size;
		var skuObj = value.sku; 
		
		//1、初始化库存
		var sku = {};
		sku.key = key;
		sku.colorId = colorId;
		sku.colorName = colorName;
		sku.sizeId = sizeId;
		sku.sizeName = sizeName;
		sku.count = skuObj.remainCount;
		sku.skuNo = skuObj.skuNo;
		sku.skuId = skuObj.id;
		sku.buyCount = value.buyCount;
		skuArr.push(sku);

	}  
	
	$.each(g_skuProps,function(index){
		var skuProps = this;
		var propName = skuProps.propName;
		var propValues = skuProps.propValues;
		var propNameId = propName.id;
		if(propNameId == 7){
			$.each(propValues,function(index){
				var value = this;
				//2、初始化颜色
				var color = {};
				color.id = value.id;
				color.name = value.propertyValue;
				color.state = 0;//默认都未选择
				colorArr.push(color);
			});
		}else if(propNameId == 8){
			$.each(propValues,function(index){
				var value = this;
				//3、初始化尺寸
				var size = {};
				size.id = value.id;
				size.name = value.propertyValue;
				size.state = 0;//默认都未选择
				sizeArr.push(size);
			});
		}
	});
	//console.log("initColorAndSize colorArr::"+JSON.stringify(colorArr));
	//console.log("initColorAndSize sizeArr::"+JSON.stringify(sizeArr));
	//console.log("initColorAndSize skuArr::"+JSON.stringify(skuArr));	
}

//清空选中
function closeByClean(g_skuMap){
	//清空颜色的状态
	$.each(colorArr,function(index){
		var color =  this ;
		color.state = 0;
	});
	//清空尺码的状态
	$.each(sizeArr,function(index){
		var size =  this ;
		size.state = 0;
	});
	//清空购买数量
	for(var key in g_skuMap)  {
		var value = g_skuMap[key];
		value.buyCount = 1;
	}  
}

//2、更改颜色的选择状态，type（颜色为0，尺寸为1）、id（颜色ID或尺寸ID）、state（选中为1，取消选中为0）
function changeColorAndSizeState(type,id,state){
	if(type == 0){
		$.each(colorArr,function(index){
			var color =  this ;
			var colorId = color.id;
			if(colorId == id){
				color.state = state;
			}
		});
	
	}else if(type == 1){
		$.each(sizeArr,function(index){
			var size =  this ;
			var sizeId = size.id;
			if(sizeId == id){
				size.state = state;
			}
		});
	}	
}
//3、编译库存信息
function buildStockInfo(g_skuMap,closeflag){
	haveStockArr = new Array();
	noHaveStockArr = new Array();
	
	//console.log("initColorAndSize colorArr::"+JSON.stringify(colorArr));
	//console.log("initColorAndSize sizeArr::"+JSON.stringify(sizeArr));
	$.each(colorArr,function(index){
		var color = this;
		var colorId = color.id;//ID
		var colorState = color.state;//状态
		if(colorState == 1){
			$.each(sizeArr,function(index){
				var size = this;
				var sizeId = size.id;//ID
				var sizeState = size.state;//状态
				if(sizeState == 1){
					var skuInfo = getSkuInfo(colorId,sizeId);
					//alert(JSON.stringify(skuInfo))
					//组装库存对象
					var stockInfo = {};
					stockInfo.colorId = colorId;
					stockInfo.sizeId = sizeId;
					stockInfo.count = skuInfo.count;
					stockInfo.skuNo = skuInfo.skuNo;
					stockInfo.colorName = skuInfo.colorName;
					stockInfo.sizeName = skuInfo.sizeName;
					stockInfo.skuId = skuInfo.skuId;
					stockInfo.key = skuInfo.key;
					//console.log("skuInfo.key："+ skuInfo.key);
					//console.log("g_skuMap："+ g_skuMap);
					if(g_skuMap != undefined)
							//console.log("g_skuMap[skuInfo.key]："+ g_skuMap[skuInfo.key]);
					if(g_skuMap != undefined && g_skuMap[skuInfo.key] != undefined){
						//console.log("g_skuMap[skuInfo.key].buyCount："+ g_skuMap[skuInfo.key].buyCount);
					
					}
					//有库存
					if(skuInfo.count > 0){
					//有库存 默认为1
						stockInfo.buyCount = 1;
						if(g_skuMap && g_skuMap[skuInfo.key] && g_skuMap[skuInfo.key].buyCount >= 0){
							stockInfo.buyCount = g_skuMap[skuInfo.key].buyCount;
							
						}
						haveStockArr.push(stockInfo);
					}else{//无库存
						stockInfo.buyCount = 0;
						noHaveStockArr.push(stockInfo);
					}
				}
			});
		}
	});
	
	$.each(noHaveStockArr,function(index){
		var stockInfo = this;
		haveStockArr.push(stockInfo);
	});

	//console.log("buildStockInfo haveStockArr::"+JSON.stringify(haveStockArr));
	//console.log("buildStockInfo noHaveStockArr::"+JSON.stringify(noHaveStockArr));
	return haveStockArr;
	
}

//判断是否有库存
function getSkuInfo(colorId,sizeId){
	var skuInfo = {};
	var selectKey = "7:"+colorId+";8:"+sizeId;
	$.each(skuArr,function(index){
		var sku = this;
		var key = sku.key;
		if(key == selectKey){
			skuInfo = sku;
		}
	});
	
	return skuInfo;
}


