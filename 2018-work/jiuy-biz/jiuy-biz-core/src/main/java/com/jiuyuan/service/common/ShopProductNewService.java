package com.jiuyuan.service.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiuyuan.constant.AdminConstants;
import com.jiuyuan.constant.ThirdPartService;
import com.jiuyuan.dao.mapper.supplier.ProductDetailMapper;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.dao.mapper.supplier.ShopProductMapper;
import com.jiuyuan.entity.newentity.*;
import com.jiuyuan.service.WxaqrCodeImageUtil;
import com.jiuyuan.util.BizImgUtils;
import com.jiuyuan.util.JiuyMultipartFile;
import com.yujj.util.file.OSSFileUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ShopProductNewService {
    private static final Logger logger = LoggerFactory.getLogger(ShopProductNewService.class);

    /**
     * 商品所属类型,自有
     */
    private static final Object PRODUCT_OWN_TYPE_SELF = 1;
    /**
     * 商品状态,正常
     */
    private static final int NORMAL = 0;


    private final String DEFAULT_BASEPATH_NAME = ThirdPartService.OSS_DEFAULT_BASEPATH_NAME;

    public static String weixinServiceUrl = AdminConstants.WEIXIN_SERVER_URL;

    public static String getProductQrcodeUrl = "/code/getProductQrcodeUrl";

    @Autowired
    private ShopProductMapper shopProductMapper;

    @Autowired
    private ProductNewMapper productNewMapper;

    @Autowired
    private OSSFileUtil ossFileUtil;
    @Autowired
    private ProductDetailMapper productDetailMapper;
    @Autowired
    private IMyStoreActivityService myStoreActivityService;

    /**
     * 获得商家商品列表（不填充平台商品库存）
     */
    public ShopProduct getShopProductInfoNoStock(long shopProductId) {
        ShopProduct shopProduct = shopProductMapper.selectById(shopProductId);
//		logger.info("shopProductId:"+shopProductId+",shopProduct:"+JSON.toJSONString(shopProduct));
        if (shopProduct != null) {
            if (shopProduct.getOwn() == 0) {
                long productId = shopProduct.getProductId();
                ProductNew product = productNewMapper.selectById(productId);
                shopProduct.setName(product.getName());
                shopProduct.setXprice(product.getWholeSaleCash());
//					shopProduct.setMarketPrice((double) product.getMarketPrice());
                shopProduct.setClothesNumber(product.getClothesNumber());
                shopProduct.setVideoUrl(product.getVideoUrl());

                //获取库存需要查询sku信息非常慢，所有不进行填充，如果发小其他问题请单独获取
//					int stock = getStockByProductIdCache(productId);
//					shopProduct.setStock((long) stock);
                shopProduct.setSummaryImages(product.getDetailImages());

                String sizeTableImage = product.getSizeTableImage();
                String detailImages = product.getSummaryImages();
                if (!StringUtils.isEmpty(detailImages) && JSON.parseArray(detailImages).size() <= 0) {
                    detailImages = product.getDetailImages();
                }
                String remark = "{\"SizeTableImage\":" + sizeTableImage + ",\"DetailImages\":" + detailImages + "}";
                shopProduct.setRemark(remark);

//					if(shopProduct.getStock()>0){
//						shopProduct.setStockTime(System.currentTimeMillis());
//					}

//					shopProduct.setPlatformProductState(productSKUService.getPlatformProductState(productId));
                String platformProductState = "1";// 商品状态:0已上架、1已下架、2已删除
                int state = product.getState();//商品状态： 0（编辑中，未完成编辑）、 1（新建，编辑完成、待提审）、2（待提审）、3（待审核，审核中）、4（审核不通过）、 5（待上架，审核通过、待上架）、 6（上架，审核通过、已上架）、 7（下架，审核通过、已下架）
                if (state == ProductNewStateEnum.up_sold.getIntValue()) {
                    platformProductState = "0";
                }
                shopProduct.setPlatformProductState(platformProductState);
            }
        }
        return shopProduct;
    }

    /**
     * 根据ID获取对应的小程序商品
     *
     * @param shopProductId
     */
    public ShopProduct getShopProduct(long shopProductId) {
        //shopProductMapper.selectById(shopProductId);
        return shopProductMapper.selectShopProduct(shopProductId);
    }

    /**
     * 获取小程序分享图片
     *
     * @param shopProductOld
     * @param request
     */
    public String getShareImage(ShopProduct shopProductOld, HttpServletRequest request) throws Exception {
        long shopProductId = shopProductOld.getId();
        long storeId = shopProductOld.getStoreId();
        int own = shopProductOld.getOwn();
        String showcaseImgs = "";
        if (own == 1) {//自有商品
            showcaseImgs = shopProductOld.getSummaryImages();
        } else {//平台商品
//			Wrapper<ProductDetail> wrapper = new EntityWrapper<ProductDetail>().eq("productId", shopProductOld.getProductId());
//			ProductDetail productDetail = productDetailMapper.selectList(wrapper).get(0);
            ProductDetail productDetail = productDetailMapper.selectProductDetailList(shopProductOld.getProductId()).get(0);
            showcaseImgs = productDetail.getShowcaseImgs();
        }
        if (StringUtils.isEmpty(showcaseImgs) || showcaseImgs.length() <= 0) {
            logger.error("小程序分享拼接图片无法更新:没有小程序商品橱窗图shopProductId:" + shopProductId + ",showcaseImgs:" + showcaseImgs);
            throw new RuntimeException("小程序分享拼接图片无法更新:没有小程序商品橱窗图shopProductId:" + shopProductId);
        }
        JSONArray json = JSONArray.parseArray(showcaseImgs);
        Object[] imageArray = json.toArray();
        //获取对应的小程序商品二维码
        String productQrcodeUrl = shopProductOld.getWxaqrcodeUrl();
        if (StringUtils.isEmpty(productQrcodeUrl)) {
            logger.error("小程序分享拼接图片无法更新:没有小程序商品二维码shopProductId:" + shopProductId + ",productQrcodeUrl:" + productQrcodeUrl);
            throw new RuntimeException("小程序分享拼接图片无法更新:没有小程序商品二维码shopProductId:" + shopProductId);
        }
        String[] imageArrayNew = new String[imageArray.length + 1];
        for (int i = 0; i < imageArrayNew.length - 1; i++) {
            imageArrayNew[i] = (String) imageArray[i];
        }
        imageArrayNew[imageArrayNew.length - 1] = productQrcodeUrl;
        String filePath = "";
        String path = "";
        try {
            //拼接图片
            logger.info("imageArrayNew:" + JSON.toJSONString(imageArrayNew));
            Map<String, Object> result = WxaqrCodeImageUtil.getWxaqrCodeImage(imageArrayNew);
//			logger.info("result:"+JSON.toJSONString(result));
            MultipartFile multipartFile = (MultipartFile) result.get("multipartFile");
            logger.info("multipartFile为空:" + multipartFile.isEmpty());
            filePath = (String) result.get("filePath");
            logger.info("filePath:" + JSON.toJSONString(filePath));
            //上传小程序分享图片
            path = this.uploadWxaqrcode(multipartFile, request);
            logger.info("上传小程序分享图片path:" + JSON.toJSONString(path));

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("小程序分享拼接图片无法更新:拼接图片shopProductId:" + shopProductId + ",productQrcodeUrl:" + productQrcodeUrl);
            throw new RuntimeException("小程序分享拼接图片无法更新:拼接图片shopProductId:" + shopProductId);
        } finally {
            if (!StringUtils.isEmpty(filePath)) {
                //删掉临时文件夹
                WxaqrCodeImageUtil.deleteFile(new File(filePath));
            }
        }
        //小程序分享拼接图片更新
        if (!StringUtils.isEmpty(path)) {
            int record = this.updateShopProductWxaqrcodeImage(shopProductId, path, json.toJSONString());
            if (record != 1) {
                logger.error("小程序分享拼接图片无法更新:storeId:" + storeId + ",shopProductId:" + shopProductId);
            }
        }
        return path;
    }

    /**
     * 将商品分享图片上传到阿里云服务器
     *
     * @param file
     * @param request
     */
    private String uploadWxaqrcode(MultipartFile file, HttpServletRequest request) {
        try {
            String path = null;
            String oldPath = request.getParameter("oldPath");
            Map<String, String> result = new HashMap<String, String>();
            logger.debug("yes you are!");
            if (file == null) {
                logger.error("请求中没有file对象，请排查问题");
                logger.error("request file null oldPath:" + oldPath);
                return "";
            }
            logger.debug("request file name :" + file.getName());
            path = ossFileUtil.uploadFile(DEFAULT_BASEPATH_NAME, file);

            //覆盖旧路径则删除
            if (StringUtils.isNotEmpty(StringUtils.trim(oldPath))) {
                String key = oldPath.split("/")[oldPath.split("/").length - 1];
                ossFileUtil.removeFile(DEFAULT_BASEPATH_NAME, key);
            }
            logger.info("上传文件接口返回数据，result:" + result.toString());
            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 小程序分享图片更新
     *
     * @param shopProductId
     */
    public int updateShopProductWxaqrcodeImage(long shopProductId, String wxaProductShareImage, String wxaProductShareOldImages) {
        ShopProduct shopProduct = new ShopProduct();
        shopProduct.setId(shopProductId);
        shopProduct.setWxaProductShareImage(wxaProductShareImage);
        shopProduct.setWxaProductShareOldImages(wxaProductShareOldImages);
        return shopProductMapper.updateById(shopProduct);
    }

    /**
     * 判断商品详情图片发生改变
     */
    public boolean checkShopProducImages(ShopProduct shopProductOld) {
        String wxaProductShareOldImages = shopProductOld.getWxaProductShareOldImages();
        //小程序分享拼接图片上传更新
        JSONArray jsonOld = JSONArray.parseArray(wxaProductShareOldImages);
        //获取小程序商品当前的橱窗图
        String showcaseImgs = "";
        int own = shopProductOld.getOwn();
        if (own == 1) {//自有商品
            showcaseImgs = shopProductOld.getSummaryImages();
        } else {//平台商品
//			Wrapper<ProductDetail> wrapper = new EntityWrapper<ProductDetail>().eq("productId", shopProductOld.getProductId());
//			ProductDetail productDetail = productDetailMapper.selectList(wrapper).get(0);
            ProductDetail productDetail = productDetailMapper.selectProductDetailList(shopProductOld.getProductId()).get(0);
            showcaseImgs = productDetail.getShowcaseImgs();
        }
        JSONArray json = JSONArray.parseArray(showcaseImgs);
        //如果图片个数不同
        if (json.size() != jsonOld.size()) {
            return true;
        }
        //每个位置的每张图片都要一样
        for (int i = 0; i < json.size(); i++) {
            if (!json.get(i).equals(jsonOld.get(i))) {
                return true;
            }
        }
        return false;
    }

    public List<ShopProduct> selectList(long storeId, long productId) {
        Wrapper<ShopProduct> wrapper = new EntityWrapper<ShopProduct>().eq("status", 0).eq("store_id", storeId).eq("product_id", productId);
        return shopProductMapper.selectList(wrapper);
    }


    /**
     * 获取分享合成图图
     *
     * @param shopProductId 商品id
     * @return java.lang.String 图片阿里云地址
     * @author Charlie
     * @date 2018/7/16 19:37
     */
    public String getShareCompositeImgV377(Long shopProductId, long storeId) {
        if (shopProductId == null) {
            throw new RuntimeException("请求参数不合法,商品id不可为空");
        }

        ShopProduct query = new ShopProduct();
        query.setId(shopProductId);
        query.setStatus(NORMAL);
        ShopProduct product = shopProductMapper.selectOne(query);

//        ShopProduct product = shopProductMapper.selectShopProductNew(shopProductId, 0);
        if (ObjectUtils.isEmpty(product)) {
            throw new RuntimeException("请求参数不合法,未找到商品信息");
        }

        Integer type = shopProductMapper.getByStoreId(storeId);
        //展示的图片
        String showImg = acquireShowImg(product, shopProductId);
        //商品二维码
        String wxRQcode = product.getWxaqrcodeUrl();
        //合成图
        String finalImg = product.getWxaProductShareImage();
        //s是店中店
        boolean isShareStore = type == 1;
        logger.info("isShareStore={}", isShareStore);
        if (isShareStore) {
            //店中店商品分享二维码
            wxRQcode = product.getProductNewImg();
            finalImg = product.getInShopShareImg();
        }


        if (StringUtils.isEmpty(wxRQcode)) {
            logger.error("小程序分享拼接图片无法更新:没有小程序商品二维码shopProductId:" + shopProductId + ",wxRQcode:" + wxRQcode);
            throw new RuntimeException("小程序分享拼接图片无法更新:没有小程序商品二维码shopProductId:" + shopProductId);
        }
        //合成图片元素封装
        ShopProductShareImgVo imgDetailVo = buildShareProductImgVo(showImg, wxRQcode, product);
        logger.info("imgDetailVo: " + imgDetailVo.toString());

        //文件没有修改,则直接返回
        int code = imgDetailVo.createCode();
        boolean noChange = ObjectUtils.nullSafeEquals(code, product.getShareImgCode());
        if (noChange) {
            logger.info("小程序分享拼接图片,商品信息并没有修改");
            return finalImg;
        }

        //临时目录
        File directory = createTempDirectory();
        //生成合成图到目录中
        String filePath = createCompositeImg(imgDetailVo, directory);
        //上传图片到阿里云
        String imgUrl = saveOrUpdImgOnAliyun(finalImg, new JiuyMultipartFile(new File(filePath)));
        if (StringUtils.isEmpty(imgUrl)) {
            logger.error("小程序分享拼接图片, 上传到阿里云失败 imgUrl:'', shopProductId:" + shopProductId);
            throw new RuntimeException("小程序分享拼接图片, 上传到阿里云失败 imgUrl:'', shopProductId:" + shopProductId);
        }
        //将图片信息保存到商品中
        ShopProduct entity = new ShopProduct();
        entity.setId(shopProductId);
        if (isShareStore) {
            entity.setInShopShareImg(imgUrl);
        } else {
            entity.setWxaProductShareImage(imgUrl);
        }
        entity.setShareImgCode(code);
        int updRecord = shopProductMapper.updateById(entity);
        if (updRecord != 1) {
            logger.error("小程序分享拼接图片无法更新:storeId:" + product.getStoreId() + ",shopProductId:" + shopProductId);
            throw new RuntimeException("小程序分享拼接图片无法更新:storeId:" + product.getStoreId() + ",shopProductId:" + shopProductId + shopProductId);
        }
        deleteTempFile(directory);
        return imgUrl;
    }


    /**
     * 分享商品图片所需要的信息的封装
     *
     * @param showImg  showImg
     * @param wxRQcode wxRQcode
     * @param product  product
     * @return void
     * @author Charlie
     * @date 2018/7/17 14:01
     */
    private ShopProductShareImgVo buildShareProductImgVo(String showImg, String wxRQcode, ShopProduct product) {
        ShopProductShareImgVo vo = ShopProductShareImgVo.createInstance(product, showImg, wxRQcode);

        Long storeId = product.getStoreId();
        SecondBuyActivity secondBuyActivity = myStoreActivityService.getCurrentSecondBuyActivity(product.getId(), storeId);
        if (secondBuyActivity != null) {
            return vo.setActivity(secondBuyActivity);
        }

        TeamBuyActivity teamBuyActivity = myStoreActivityService.getCurrentTeamBuyActivity(product.getId(), storeId);
        return vo.setActivity(teamBuyActivity);
    }


    /**
     * 生成保存合成图的临时目录,用完之后记得删除
     *
     * @return java.io.File
     * @author Charlie
     * @date 2018/7/17 12:57
     */
    private File createTempDirectory() {
        String directoryName = UUID.randomUUID().toString();
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext servletContext = webApplicationContext.getServletContext();
        String filePath = servletContext.getRealPath("/" + directoryName + "/");
        logger.info("获取小程序商品分享图片filePath:" + filePath);
        WxaqrCodeImageUtil.createfile(filePath);
        return new File(filePath);
    }


    /**
     * 删除合成图
     *
     * @param directory 合成图的临时目录
     * @return void
     * @author Charlie
     * @date 2018/7/17 11:40
     */
    private void deleteTempFile(File directory) {
        if (!ObjectUtils.isEmpty(directory) && directory.exists()) {
            WxaqrCodeImageUtil.deleteFile(directory);
        }
    }


    /**
     * 上传图片到阿里云
     *
     * @param newImgFile 新合成的图片
     * @author Charlie
     * @date 2018/7/17 11:25
     */
    private String saveOrUpdImgOnAliyun(String historyPath, MultipartFile newImgFile) {
        String path = "";
        try {
            //上传新的
            if (newImgFile.isEmpty()) {
                logger.error("上传分享商品合成图, 文件对象为空，Filename:" + newImgFile.getOriginalFilename());
                return path;
            }
            path = ossFileUtil.uploadFile(DEFAULT_BASEPATH_NAME, newImgFile);

            //删除原来的
            if (StringUtils.isNotBlank(historyPath)) {
                historyPath = historyPath.trim();
                String[] pathElems = historyPath.split("/");
                String key = pathElems[pathElems.length - 1];
                ossFileUtil.removeFile(DEFAULT_BASEPATH_NAME, key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("上传分享商品合成图到阿里云 失败，Filename:" + newImgFile.getOriginalFilename());
        }

        logger.info("上传分享商品合成图到阿里云 success");
        return path;
    }


    /**
     * 创建保存合成图片到本地
     *
     * @param vo            合成图片所需要的业务数据的封装
     * @param destDirectory 文件保存的目录
     * @return java.lang.String 合成后的图片的url
     * @author Charlie
     * @date 2018/7/16 20:05
     */
    private String createCompositeImg(ShopProductShareImgVo vo, File destDirectory) {
        Long productId = vo.getShopProduct().getId();
        //图片资源
        BufferedImage showImgBuf;
        BufferedImage wxRQcodeBuf;
        InputStream showImgIn = null, wxRQcode = null;
        try {
//            showImgIn = WxaqrCodeImageUtil.getInputStreamByGet("https://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/15419903924241541990392424.jpg");
            showImgIn = WxaqrCodeImageUtil.getInputStreamByGet(vo.getShowImg());
//            wxRQcode = WxaqrCodeImageUtil.getInputStreamByGet("https://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/15419903924241541990392424.jpg");
            wxRQcode = WxaqrCodeImageUtil.getInputStreamByGet(vo.getWxRQcode());
            //橱窗图

            try {
                showImgBuf = BizImgUtils.imgFile2BufferedImg(showImgIn);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("生成分享图片将橱窗图读取为缓存失败 img:" + vo.getShowImg() + ", productId:" + productId);
                throw new RuntimeException("将橱窗图读取为缓存失败");
            }
            //二维码图
            try {
                wxRQcodeBuf = BizImgUtils.imgFile2BufferedImg(wxRQcode);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("生成分享图片将二维码读取为缓存失败 img:" + vo.getWxRQcode() + ", productId:" + productId);
                throw new RuntimeException("将二维码读取为缓存失败");
            }
        } finally {
            BizImgUtils.closeStream(showImgIn, wxRQcode);
        }

        //统一大小
        int w = 750;
        int h = new BigDecimal(w + "").divide(new BigDecimal(String.valueOf(showImgBuf.getWidth())), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(String.valueOf(showImgBuf.getHeight()))).intValue();
        showImgBuf = BizImgUtils.zoomImage(showImgBuf, w, h);

        //缩放二维码
        int rqMaxWith = showImgBuf.getWidth() / 10 * 3;
        wxRQcodeBuf = BizImgUtils.zoomImage(wxRQcodeBuf, rqMaxWith, rqMaxWith);
        //生成商品信息图
        //商品信息图片左边贴上二维码的空隙
        int xLeftSpace = 50;
        //二维码占用的左边的空间
        int unableWith = wxRQcodeBuf.getWidth() + xLeftSpace * 2;
        BufferedImage imgTemp = generateProductShareDescriptionImg(vo, showImgBuf.getWidth(), w / 2, unableWith);
        //合并二维码
        int x = imgTemp.getWidth() - unableWith + xLeftSpace;
        int y = 50;
        imgTemp = BizImgUtils.overlapImage(imgTemp, wxRQcodeBuf, x, y);
        //合并橱窗图
        BufferedImage finalImage = BizImgUtils.mergeImage(false, showImgBuf, imgTemp);

        //输入图片内容,并保存到本地
        String localServerImgFile = destDirectory.getAbsolutePath() + "/" + System.currentTimeMillis() + this.hashCode() + ".jpg";
        logger.info("开始将合成图片暂时保存到本地... localServerImgFile = " + localServerImgFile);
        try {
            if (!BizImgUtils.bufferedImg2ImgFile(finalImage, localServerImgFile)) {
                logger.error("保存合成图片到本地失败");
                throw new RuntimeException("保存合成图片失败");
            }
            logger.info("成功将合成图片暂时保存到本地, localServerImgFile = " + localServerImgFile);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("保存合成图片到本地失败");
            throw new RuntimeException("保存合成图片失败");

        }
        return localServerImgFile;
    }


    /**
     * 将商品详情生成图片
     *
     * @param vo    vo
     * @param width 生成图片的宽度
     * @return java.awt.image.BufferedImage
     * @author Charlie
     * @date 2018/7/16 20:02
     */
    private BufferedImage generateProductShareDescriptionImg(ShopProductShareImgVo vo, Integer width, Integer height, Integer unableWith) {
        logger.info("商品信息生成图片 ShopProductShareImgVo:" + vo.toString());
        //============================= 配置 =============================
        //初始高度
        int initH = 90;
        //实际宽度
        int actualWith = width - unableWith;
        //左边留白宽度
        int leftSpaceW = 35;
        //商品名一行最长15个
        int maxWord = 15;
        //商品名最长多少行
        int maxRow = 2;
        //红色价格:除去左边留白,应该只占1/2,按最大长度9来算,每两个数字占一个字符,9/2~4 (做微调,*0.8)
        int redSize = (actualWith - leftSpaceW) / 2 / 4 / 10 * 8;
        //黑字大小,以商品名为准:按一行最长15个,除去左边留白,右边留白
//        int blackSize = (actualWith-leftSpaceW-leftSpaceW)/maxWord;
        int blackSize = redSize / 10 * 9;
        //===============================================================
        String fontStyle = "SimSun";
//        String fontStyle = Font.A;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics2D g2d = image.createGraphics();
        //消除锯齿
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
        g2d.setClip(0, 0, width, height);
        //背景色
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, width, height);

        if (vo.isHasActivity()) {
            logger.info("拼接活动商品信息");
            /* 活动商品 */
            //活动价
            String actPrice = "¥" + String.valueOf(vo.getActivityPriceStr());
            g2d.setColor(Color.red);
            g2d.setFont(new Font(fontStyle, Font.BOLD, redSize));
            g2d.drawString(actPrice, leftSpaceW, initH);
            //原价
            String orgPrice = "¥" + vo.getOriginalPriceStr();
            g2d.setColor(Color.black);
            AttributedString as = new AttributedString(orgPrice);
            Font font = new Font(fontStyle, Font.PLAIN, blackSize);
            logger.info("字体为:" + font.getName());
            as.addAttribute(TextAttribute.FONT, (font));
            as.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
            boolean temp = actPrice.length() % 2 == 0;
            g2d.drawString(as.getIterator(), redSize * (actPrice.length() / 2) + (temp ? leftSpaceW * 2 : leftSpaceW * 3) + 20, initH);
            //活动标签底色
            g2d.setColor(Color.red);
            g2d.fillRect(leftSpaceW - 10, 127, redSize * 3, redSize);
            //活动标签
            g2d.setFont(new Font(fontStyle, Font.PLAIN, blackSize));
            String tag = vo.getActivityTag();
            g2d.setColor(Color.black);
            g2d.drawString(tag, leftSpaceW, 155);
            //活动说明(限长8)
            int dLimitLen = 8;
            String activityDescription = vo.getActivityDescription();
            activityDescription = activityDescription == null ? "" : activityDescription;
            if (ShopProductShareImgVo.TEAM_ACTIVITY.equals(vo.getActivityTag())) {
                activityDescription = (activityDescription.length() <= dLimitLen ? activityDescription : activityDescription.substring(0, dLimitLen) + "...");
            }
            g2d.drawString(activityDescription, leftSpaceW + blackSize * 4, 155);
            //商品名(商品名的长度,每行限长15,最多两行)
            String name = vo.getProductName();
            drawProductName(220, leftSpaceW, maxWord, maxRow, blackSize, g2d, name);
        } else {
            logger.info("拼接普通商品信息");
            /* 普通商品 */
            //原价
            String orgPrice = "¥" + String.valueOf(vo.getOriginalPriceStr());
            g2d.setColor(Color.red);
            g2d.setFont(new Font(fontStyle, Font.BOLD, redSize));
            g2d.drawString(orgPrice, leftSpaceW, initH);
            //商品名
            g2d.setColor(Color.black);
            g2d.setFont(new Font(fontStyle, Font.PLAIN, blackSize));
            String name = vo.getProductName();
            drawProductName(190, leftSpaceW, maxWord, maxRow, blackSize, g2d, name);
        }
        g2d.dispose();
        return image;
    }


    /**
     * 商品名
     *
     * @param firstRowY  首行默认高度
     * @param leftSpaceW leftSpaceW
     * @param maxWord    maxWord
     * @param maxRow     maxRow
     * @param blackSize  blackSize
     * @param g          g
     * @param name       name
     * @return void
     * @author Charlie
     * @date 2018/7/18 17:54
     */
    private void drawProductName(int firstRowY, int leftSpaceW, int maxWord, int maxRow, int blackSize, Graphics g, String name) {
        int secondRowY = firstRowY + blackSize * 3 / 2;
        if (StringUtils.isNotBlank(name)) {
            if (name.length() > maxWord * maxRow) {
                g.drawString(name.substring(0, maxWord), leftSpaceW, firstRowY);
                g.drawString(name.substring(maxWord, maxWord * maxRow) + "...", leftSpaceW - 1, secondRowY);
            } else if (name.length() > maxWord) {
                g.drawString(name.substring(0, maxWord), leftSpaceW, firstRowY);
                g.drawString(name.substring(maxWord, name.length()), leftSpaceW, secondRowY);
            } else {
                g.drawString(name, leftSpaceW, firstRowY);
            }
        }
    }


    /**
     * 获取商品img
     *
     * @param history       history
     * @param shopProductId shopProductId
     * @return java.lang.String
     * @author Charlie
     * @date 2018/7/16 19:41
     */
    private String acquireShowImg(ShopProduct history, Long shopProductId) {
        String showcaseImgs;
        if (ObjectUtils.nullSafeEquals(history.getOwn(), PRODUCT_OWN_TYPE_SELF)
                || ObjectUtils.nullSafeEquals(history.getOwn(), PRODUCT_OWN_TYPE_SELF)
        ) {
            //商家自有
            showcaseImgs = history.getSummaryImages();
        } else {
            //平台商品
//            ProductDetail query = new ProductDetail();
//            query.setProductId(history.getProductId());
//            ProductDetail productDetail = productDetailMapper.selectOne(query);
//            if (ObjectUtils.isEmpty(productDetail)) {
//                throw new RuntimeException("未找到商品详情信息");
//            }

            //查询图片,这里做个重复数据的兼容,线上没有看到重复数据,仅在本地线有脏数据 2019-1-4 update by Charlie
            Wrapper<ProductDetail> detailQuery = new EntityWrapper<>();
            detailQuery.eq("productId", history.getProductId());
            List<ProductDetail> productDetails = productDetailMapper.selectList(detailQuery);
            if (productDetails == null || productDetails.isEmpty()) {
                throw new RuntimeException("未找到商品详情信息");
            }
            ProductDetail productDetail = null;
            if (productDetails.size() > 1) {
                logger.error("查到多个商品详情");
                for (ProductDetail detail : productDetails) {
                    if (StringUtils.isNotBlank(detail.getDetailImgs())) {
                        productDetail = detail;
                        logger.info("找到兼容数据");
                        break;
                    }
                }
            }
            if (productDetail == null) {
                productDetail = productDetails.get(0);
            }
            showcaseImgs = productDetail.getShowcaseImgs();
        }
        if (StringUtils.isBlank(showcaseImgs)) {
            logger.error("小程序分享拼接图片无法更新:没有小程序商品橱窗图shopProductId:" + shopProductId + ",showcaseImgs:" + showcaseImgs);
            throw new RuntimeException("小程序分享拼接图片无法更新:没有小程序商品橱窗图shopProductId:" + shopProductId);
        }

        //获取首张
        return acquireFirstImg(showcaseImgs);
    }


    /**
     * 获取首张图片
     *
     * @param showcaseImgs showcaseImgs
     * @return java.lang.String
     * @author Charlie
     * @date 2018/7/16 19:44
     */
    private String acquireFirstImg(String showcaseImgs) {
        List<String> imgList = new Gson().fromJson(showcaseImgs, new TypeToken<List<String>>() {
        }.getType());
        if (ObjectUtils.isEmpty(imgList)) {
            throw new RuntimeException("未找到橱窗图");
        }

        return imgList.get(0);
    }
}