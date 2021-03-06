package net.dks.onlineshoppingfs.controller;

 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import net.dks.onlineshoppingfs.exception.ProductNotFoundException;
import net.dks.shoppingbackendfs.dao.CategoryDAO;
import net.dks.shoppingbackendfs.dao.ProductDAO;
import net.dks.shoppingbackendfs.dto.Category;
import net.dks.shoppingbackendfs.dto.Product;

@Controller
public class PageController {
	private static final Logger logger=LoggerFactory.getLogger(PageController.class);
	@Autowired
	private CategoryDAO categoryDAO;
	
	@Autowired
	private ProductDAO productDAO;
	
	@RequestMapping(value= {"/","/home","/index"})
	public ModelAndView index()
	{
		ModelAndView mv = new ModelAndView("page");
		
		
		mv.addObject("userClickHome",true);
		mv.addObject("categories",categoryDAO.list());
	 
		mv.addObject("title","Home");
		logger.info("INSIDE PageController index method-INFO");
		logger.debug("INSIDE PageController index method-DEBUG");
		return mv;
	}
	
	
	@RequestMapping(value= {"/about"})
	public ModelAndView about()
	{
		ModelAndView mv = new ModelAndView("page");
		
		mv.addObject("userClickAbout",true);
	 
		mv.addObject("title","About Us");
		return mv;
	}
	
	
	@RequestMapping(value= {"/contact"})
	public ModelAndView contact()
	{
		ModelAndView mv = new ModelAndView("page");
		
		mv.addObject("userClickContact",true);
	 
		mv.addObject("title","Contact Us");
		return mv;
	}
	
	@RequestMapping(value= "/show/all/products")
	public ModelAndView showAllProducts()
	{
		ModelAndView mv = new ModelAndView("page");
		
		
		mv.addObject("userClickAllProducts",true);
		mv.addObject("categories",categoryDAO.list());
	 
		mv.addObject("title","AllProducts");
		return mv;
	}
	
	@RequestMapping(value= "/show/category/{id}/products")
	public ModelAndView showCategoryProducts(@PathVariable("id") int id)
	{
		ModelAndView mv = new ModelAndView("page");
		Category category=null;
		category=categoryDAO.get(id);
		mv.addObject("title",category.getName());
		mv.addObject("categories",categoryDAO.list()); 
		mv.addObject("category",category);
		mv.addObject("userClickCategoryProducts",true);
		 
		return mv;
	}
	
	@RequestMapping(value="/show/{id}/product")
	public ModelAndView showSingleProduct(@PathVariable int id) throws ProductNotFoundException
	{
		ModelAndView mv= new ModelAndView("page");
		
		
		Product product=productDAO.get(id);
		if(product==null) throw new ProductNotFoundException();	
		
		product.setViews(product.getViews()+1);
		productDAO.update(product);
		mv.addObject("title",product.getName());
		mv.addObject("product",product);
		mv.addObject("userClickShowProduct",true);
		return mv;
		
	}
	
	@RequestMapping(value= {"/register"})
	public ModelAndView register()
	{
		ModelAndView mv = new ModelAndView("page");
		
		mv.addObject("title","About Us");
	 
		 
		return mv;
	}
	
	@RequestMapping(value= {"/login"})
	public ModelAndView login(@RequestParam(name="error",required =false)String error, @RequestParam(name="logout", required=false)String logout)
	{
		ModelAndView mv = new ModelAndView("login");
		
		if(error!=null)
		{
			mv.addObject("message","Invalid Username and Password !");
		}
		
		if(logout!=null)
		{
			mv.addObject("logout","User has Succesfully logged out!");
		}
			
		mv.addObject("title","Login");
	 
		 
		return mv;
	}
	
	
	@RequestMapping(value= {"/access-denied"})
	public ModelAndView accessDenied()
	{
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("title","403-Access Denied");
		mv.addObject("errorTitle","Aha! Caught You.");
		mv.addObject("errorDescription","You are not authorized to view this page!");
	 
		 
		return mv;
	}
	
	@RequestMapping(value = "/perform-logout")
	public String logout(HttpServletRequest request, HttpServletResponse response)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth!=null)
		{
			new SecurityContextLogoutHandler().logout(request,response,auth);
		}
		
		
		return "redirect:/login?logout";
	}
	
}
