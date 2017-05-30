package com.strongerstartingnow.controllers

import groovy.util.logging.Slf4j

import java.security.Principal

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView

import com.strongerstartingnow.dao.BlogPost
import com.strongerstartingnow.dao.BlogPostDao
import com.strongerstartingnow.dao.UserAccount
import com.strongerstartingnow.dao.UserAccountDao
import com.strongerstartingnow.dao.UserAccountRoleDao
import com.strongerstartingnow.dao.UserAccountRole.RoleEnum

@Controller
@Slf4j
class BlogController {
	@Autowired
	BlogPostDao blogPostDao;
	
	@Autowired
	UserAccountDao userAccountDao
	
	@Autowired
	UserAccountRoleDao userAccountRoleDao
	
	@Autowired
	UserDetailsService userDetailsServiceImpl
	
	@ModelAttribute("blogPost")
	public BlogPost blogPost() {
		return new BlogPost();
	}
	
	@ModelAttribute("allBlogPosts")
	public List<BlogPost> allBlogPosts() {
		return blogPostDao.getAll();
		 
	}
	
	@GetMapping("/blogadmin")
	public ModelAndView blogAdmin(Principal principal, @ModelAttribute BlogPost blogPost) {
		if(principal == null) {
			return new ModelAndView("redirect:/")
		}
		
		if(isAdmin(principal)) {
			log.info("This user is an admin");
			return new ModelAndView("blog/layout_admin");
		}
		
		return new ModelAndView("redirect:/")
	}
	
	@GetMapping("/blog")
	public ModelAndView blog(Principal principal) {
		Boolean blogEditor = isAdmin(principal);
		ModelAndView m = new ModelAndView("blog/layout_blog_main");
		m.addObject("blogEditor", blogEditor);
		return m;
	}
	
	@PostMapping("/blogsavepost")
	public String blogSavePost(Principal principal, @ModelAttribute BlogPost blogPost){
		log.info("Blog post submitted by ***" + principal.getName()
				+ "*** content follows:\n"
				+ blogPost.getContent());

		if(isAdmin(principal)) {
			log.info("Your blog post will be entered into the blogpost db");
			if(blogPost.getBlogPostId() != null && blogPost.getBlogPostId() > 0
				&& blogPost.getContent() != null && blogPost.content.length() > 0) {
				blogPostDao.update(blogPost.getBlogPostId(), blogPost.getContent());
			} else if ((blogPost.getBlogPostId() == null || blogPost.getBlogPostId() == 0)
					&& blogPost.getContent() != null && blogPost.getContent().length() > 0) {				
					blogPostDao.create(blogPost.getContent());
			} else {
				log.info("Could not process this blog post. Doing nothing.");
			}
		} else {
			log.info("You need to be an admin to add a blog post. Your post content: " + blogPost.getContent() + " will be discarded.");
		}
		return "blog/layout_blog_main";
	}
	
	@PostMapping("/blogeditpost")
	public ModelAndView blogAdmin(Principal principal, BlogPost blogPost, BindingResult br) {
		if(principal == null) {
			return new ModelAndView("redirect:/")
		}
		
		if(isAdmin(principal) && blogPost.blogPostId != null) {
			log.info("Editing post with id: " + blogPost.getBlogPostId());
			ModelAndView m = new ModelAndView("blog/layout_admin");
			m.addObject("blogPost", blogPost);
			return new ModelAndView("blog/layout_admin");
		}
		
		return new ModelAndView("redirect:/")
	}
	
	@PostMapping("/blogdeletepost")
	public String blogDeletePost(Principal principal, Integer blogPostId){
		log.info("Blog post with id: " + blogPostId + " deleted by ***" + principal.getName() + "***");

		if(isAdmin(principal)) {
			log.info("Your blog post will be deleted from the blogpost db");
			boolean success = blogPostDao.delete(blogPostId) > 0;
			if(success) {
				log.info("Post with id: " + blogPostId + " was successfully deleted")
			} else {
				log.info("Post with id: " + blogPostId + " could not be deleted")
			}
		} else {
			log.info("You need to be an admin to delete a blog post.");
		}
		return "blog/layout_blog_main";
	}
	
	private boolean isAdmin(Principal principal) {
		if(principal == null) return false;
		log.info("User is: " + principal.getName());
		UserAccount u = userAccountDao.getUserAccount(principal.getName());
		RoleEnum role = userAccountRoleDao.getUserRole(u);
		log.info("Role is: " + role.toString());
		return (role.equals(RoleEnum.ROLE_ADMIN));
	}
}
