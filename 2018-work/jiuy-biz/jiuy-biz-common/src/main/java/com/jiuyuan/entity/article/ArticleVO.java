package com.jiuyuan.entity.article;

public class ArticleVO extends Article {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5894418887881787665L;

	private int likeCount;
	
	private boolean isLiked;
	
	private boolean isFavorite;

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public boolean isLiked() {
		return isLiked;
	}

	public void setLiked(boolean isLiked) {
		this.isLiked = isLiked;
	}

	public boolean isFavorite() {
		return isFavorite;
	}

	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}
	
	
}
