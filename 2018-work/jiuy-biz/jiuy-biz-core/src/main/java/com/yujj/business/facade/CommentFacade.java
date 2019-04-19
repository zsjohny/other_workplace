package com.yujj.business.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.entity.comment.CommentGroupVO;
import com.jiuyuan.entity.comment.CommentVO;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.BeanUtil;
import com.yujj.business.assembler.BrandAssembler;
import com.yujj.business.assembler.ProductAssembler;
import com.yujj.business.assembler.ProductPropAssembler;
import com.yujj.business.service.ProductSKUService;
import com.yujj.dao.mapper.CommentMapper;
import com.yujj.entity.comment.Comment;
import com.yujj.entity.order.OrderItem;
import com.yujj.entity.product.ProductPropVO;
import com.yujj.entity.product.ProductSKU;

@Service
public class CommentFacade {
	@Autowired
	private CommentMapper commentMap;

    @Autowired
    private ProductSKUService productSKUService;

	@Autowired
	private ProductAssembler productAssembler;
	
	@Autowired
	private ProductPropAssembler productPropAssembler;	
	
	@Autowired
	private BrandAssembler brandAssembler;
	
	@Autowired
	private MemcachedService memcachedService;
	
	@SuppressWarnings("unchecked")
	public List<CommentVO> getCommentList(long productId, PageQuery pageQuery) {
		List<Comment> comments = new ArrayList<Comment>();
		String groupKey = MemcachedKey.GROUP_KEY_COMMENT;
        
        String key = productId + "_" + pageQuery.getPageSize() + "";
        Object obj = null; //memcachedService.get(groupKey, key);
        
        if (obj != null) {
        	comments = (List<Comment>) obj;
        } else {
        	comments = commentMap.getComments(null, productId, pageQuery);	
        	
        	memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, comments);
        		
        	
        }

		List<CommentVO> commentVOComposites = new ArrayList<CommentVO>();

        Set<Long> skuIds = new HashSet<Long>();
		for (Comment comment : comments) {
			CommentVO vo = new CommentVO();
			BeanUtil.copyProperties(vo,  comment);
			
			commentVOComposites.add(vo);
			skuIds.add(comment.getSkuId());
		}
		
		productAssembler.assemble(commentVOComposites);


        List<ProductSKU> productSkus = skuIds.isEmpty() ? new ArrayList<ProductSKU>() : productSKUService.getProductSKUs(skuIds);

        List<ProductPropVO> skuComposites = new ArrayList<ProductPropVO>();
        Map<Long, List<ProductPropVO>> skuPropMap = new HashMap<Long, List<ProductPropVO>>();
        Map<Long, ProductSKU> skuMap = new HashMap<Long, ProductSKU>();
        for (ProductSKU sku : productSkus) {
            skuMap.put(sku.getId(), sku);

            List<ProductPropVO> skuProps = sku.getProductProps();
            skuComposites.addAll(skuProps);
            skuPropMap.put(sku.getId(), skuProps);
        }
        productPropAssembler.assemble(skuComposites);
        
        for (CommentVO commentVO : commentVOComposites) {
       	 	commentVO.setSku(skuMap.get(commentVO.getSkuId()));
       	 	commentVO.setSkuProps(skuPropMap.get(commentVO.getSkuId()));
        }
        
        return commentVOComposites;
	}

	public int addComment(Comment comment) {
		
		long now = System.currentTimeMillis();
		
		comment.setStatus(0);
		comment.setCreateTime(now);
		comment.setUpdateTime(now);

		return commentMap.addComment(comment);
			
	}

	@Transactional(rollbackFor = Exception.class)
	public int addComment(List<OrderItem> orderItems) {
		long now = System.currentTimeMillis();
		
		return commentMap.addComments(now, orderItems);
	}
	
	public int updateComment(Comment comment) {
		
		long now = System.currentTimeMillis();
		
		comment.setUpdateTime(now);

		return commentMap.updateComment(comment);
	}
	
	public int deleteComment(Long id) {
		
		return commentMap.deleteComment(id);
			
	}
	
	public List<CommentGroupVO> getCommentProduct(long userId, PageQuery pageQuery) {
		
		List<Comment> comments = commentMap.getComments(userId, null, pageQuery);
		
		Map<Long, List<CommentVO>> result = new HashMap<Long, List<CommentVO>>();
		List<CommentVO> commentVOComposites = new ArrayList<CommentVO>();

		List<CommentGroupVO> commentGroupVOs = new ArrayList<CommentGroupVO>();
        Set<Long> skuIds = new HashSet<Long>();
		for (Comment comment : comments) {
			CommentVO vo = new CommentVO();
			BeanUtil.copyProperties(vo,  comment);
			
			long brandId = comment.getBrandId();
			List<CommentVO> list = result.get(brandId);
			if (list == null) {
				CommentGroupVO commentGroupVO = new CommentGroupVO();
				list = new ArrayList<CommentVO>();
				commentGroupVO.setCommentVOGroup(list);
				commentGroupVO.setBrandId(brandId);
				commentGroupVOs.add(commentGroupVO);
				result.put(brandId, list);
			}
			list.add(vo);
			commentVOComposites.add(vo);
			skuIds.add(comment.getSkuId());
		}
		
		productAssembler.assemble(commentVOComposites);
		brandAssembler.assemble(commentGroupVOs);

        List<ProductSKU> productSkus = skuIds.isEmpty() ? new ArrayList<ProductSKU>() : productSKUService.getProductSKUs(skuIds);
        List<ProductPropVO> skuComposites = new ArrayList<ProductPropVO>();
        Map<Long, List<ProductPropVO>> skuPropMap = new HashMap<Long, List<ProductPropVO>>();
        Map<Long, ProductSKU> skuMap = new HashMap<Long, ProductSKU>();
        for (ProductSKU sku : productSkus) {
            skuMap.put(sku.getId(), sku);

            List<ProductPropVO> skuProps = sku.getProductProps();
            skuComposites.addAll(skuProps);
            skuPropMap.put(sku.getId(), skuProps);
        }
        productPropAssembler.assemble(skuComposites);
        
        for (CommentVO commentVO : commentVOComposites) {
       	 	commentVO.setSku(skuMap.get(commentVO.getSkuId()));
       	 	commentVO.setSkuProps(skuPropMap.get(commentVO.getSkuId()));
        }
        
		return commentGroupVOs;
	}

	//用户待评价商品
	public int getUserCommentCount(long userId) {
		return commentMap.countComment(userId, null);
	}
	//商品的所有评价
	public int getProductCommentCount(long productId) {
		return commentMap.countComment(null, productId);
	}
}
