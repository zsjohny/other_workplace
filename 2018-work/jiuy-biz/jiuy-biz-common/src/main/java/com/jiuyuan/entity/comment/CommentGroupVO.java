package com.jiuyuan.entity.comment;

import java.util.List;

import com.yujj.business.assembler.composite.BrandComposite;
import com.yujj.entity.Brand;

public class CommentGroupVO implements BrandComposite {
	
	private Brand brand;
	
	private long brandId;
	private List<CommentVO> commentVOGroup;
	
	@Override
	public void assemble(Brand brand) {
		this.setBrand(brand);
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public List<CommentVO> getCommentVOGroup() {
		return commentVOGroup;
	}

	public void setCommentVOGroup(List<CommentVO> commentVOGroup) {
		this.commentVOGroup = commentVOGroup;
	}

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}
}
