package com.strongerstartingnow.dao.tests;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.strongerstartingnow.dao.BlogPost;
import com.strongerstartingnow.dao.BlogPostDao;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class BlogPostDaoTest {

	@Autowired
	BlogPostDao blogPostDao;
	
	@Test
	public void testCreateAndDelete() {
		Integer id = blogPostDao.create("This is just some test content from a junit test case");
		System.out.println("Id should be non-null. It is " + id.toString());
		Assert.assertNotNull(id);
		int rowsAffected = blogPostDao.delete(id);
		System.out.println(rowsAffected + " rows were deleted. Expect 1.");
		Assert.assertTrue(rowsAffected == 1);
	}
	
	@Test
	public void testCreateAndUpdate() {
		Integer id = blogPostDao.create("This is just another junit test case");
		System.out.println("Id should be non-null. It is " + id.toString());
		Assert.assertNotNull(id);
		int rowsUpdated = blogPostDao.update(id, "UPDATE: This is just another junit test case");
		System.out.println(rowsUpdated + " rows were updated. Expect 1.");
		Assert.assertTrue(rowsUpdated == 1);
		blogPostDao.delete(id);
	}
	
	@Test
	public void testGetAll() throws InterruptedException {
		final int SET_SIZE = 15;
		final int MS_BETWEEN_INSERTS = 10;
		for(int i = 1; i <= SET_SIZE; i++) {
			blogPostDao.create("Number " + i + ": This is just another junit test case");
			Thread.sleep(MS_BETWEEN_INSERTS);
		}
		List<BlogPost> blogPosts = blogPostDao.getAll();
		int lastPostId = 0;
		for(BlogPost post: blogPosts) {
			System.out.println("\nPost id: " + post.getBlogPostId().toString() +
					"\nPost datetime: " + post.getDateTime() + "\nPost content: " + post.getContent());
			System.out.println("Checking post order. Ensuring that " + post.getBlogPostId() + " > " + lastPostId);
			Assert.assertTrue("Posts should be returned in order of post id", post.getBlogPostId() > lastPostId);
			lastPostId = post.getBlogPostId();
		}
		System.out.println(blogPosts.size() + " blog posts were retrieved. Expect " + SET_SIZE);
		Assert.assertTrue(blogPosts.size() == SET_SIZE);
		for(BlogPost post: blogPosts) {
			System.out.println("Deleting post with id " + post.getBlogPostId());
			blogPostDao.delete(post.getBlogPostId());
		}
	}
	
}
