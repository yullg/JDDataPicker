package com.yullg.JDDataPicker;

public class Goods {

	private String url;
	private String name;
	private String price;
	private String commentCount;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}

	@Override
	public String toString() {
		return "Goods [url=" + url + ", name=" + name + ", price=" + price + ", commentCount=" + commentCount + "]";
	}

}