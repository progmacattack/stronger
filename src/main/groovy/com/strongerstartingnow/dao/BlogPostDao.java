package com.strongerstartingnow.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

@Component
public class BlogPostDao {
	@Autowired
	DataSource dataSource;
	
	@Autowired
	BlogPost blogPost;
	
	private NamedParameterJdbcTemplate jdbc;
	
	private void setupJdbc() {
		if(this.jdbc == null) {
			this.jdbc = new NamedParameterJdbcTemplate(dataSource);
		}
	}
	
	/** Create a blog post with the provided content
	 * @param content
	 * @return the blog post id assigned to the newly-created post. returns null if nothing
	 * was created
	 */
	public Integer create(String content) {
		Integer blogPostId = null;
		setupJdbc();
		String sql = "insert into blog_post(datetime, content) values(:datetime, :content)";
		Map<String, String> paramMap = new HashMap<>();
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatForDb = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		String dateTime = now.format(formatForDb);
		paramMap.put("datetime", dateTime);
		paramMap.put("content", content);
		try {
			jdbc.update(sql, paramMap);
			String selectFromDb = "select blog_post_id from blog_post where datetime = :datetime";
			SqlParameterSource paramSource = new MapSqlParameterSource("datetime", dateTime);
			blogPostId = jdbc.queryForObject(selectFromDb, paramSource, Integer.class);
		} catch(DataAccessException dae) {
			dae.printStackTrace();
		}
		return blogPostId;
	}
	
	/** Delete blog post with the provided id
	 * @param blogPostId
	 * @return the number of rows affected by the delete
	 */
	public int delete(Integer blogPostId) {
		int rowsAffected = 0;
		setupJdbc();
		String sql = "delete from blog_post where blog_post_id = :blogPostId limit 1";
		SqlParameterSource paramSource = new MapSqlParameterSource("blogPostId", blogPostId);
		try {
			rowsAffected = jdbc.update(sql, paramSource);
		} catch(DataAccessException dae) {
			dae.printStackTrace();
		}
		return rowsAffected;
	}
	
	/** Update a blog post. Date is not updated.
	 * @param blogPostId the id of the blog post to update
	 * @param content the new content
	 * @return int representing the number of rows updated
	 */
	public int update(Integer blogPostId, String content) {
		int rowsUpdated = 0;
		setupJdbc();
		String sql = "select blog_post_id from blog_post where blog_post_id = :blogPostId";
		SqlParameterSource paramSource = new MapSqlParameterSource("blogPostId", blogPostId);
		try {
			Integer postId = jdbc.queryForObject(sql, paramSource, Integer.class);
			if(postId != null) {
				String updateSql = "update blog_post set content = :content where blog_post_id = :blogPostId";
				Map<String, String> paramMap = new HashMap<>();
				paramMap.put("content", content);
				paramMap.put("blogPostId", postId.toString());
				rowsUpdated = jdbc.update(updateSql, paramMap);
			}
		} catch(DataAccessException dae) {
			dae.printStackTrace();
		}
		return rowsUpdated;
	}
	
	/** Return all blog posts
	 * @return List of all blog posts, sorted by date
	 */
	public List<BlogPost> getAll() {
		List<BlogPost> blogPosts = new ArrayList<>();
		setupJdbc();
		String sql = "select * from blog_post";
		try {
			blogPosts = jdbc.query(sql, new RowMapper<BlogPost>() {
				@Override
				public BlogPost mapRow(ResultSet rs, int rowNum) throws SQLException {
					BlogPost post = new BlogPost();
					post.setBlogPostId(rs.getInt("blog_post_id"));
					post.setDateTime(rs.getString("datetime"));
					post.setContent(rs.getString("content"));
					return post;
				}
			});
		} catch (DataAccessException dae) {
			dae.printStackTrace();
		}
		return blogPosts;
	}
}
