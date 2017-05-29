package com.strongerstartingnow.dao;

import org.springframework.stereotype.Component;

@Component
public class BlogPost {
	private Integer blogPostId;
	private String dateTime;
	private String content;
	
	public Integer getBlogPostId() {
		return blogPostId;
	}
	public void setBlogPostId(Integer blogPostId) {
		this.blogPostId = blogPostId;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
