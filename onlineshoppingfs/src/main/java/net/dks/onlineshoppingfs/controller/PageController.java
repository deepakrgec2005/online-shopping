package net.dks.onlineshoppingfs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import net.dks.shoppingbackendfs.dao.CategoryDAO;
import net.dks.shoppingbackendfs.dto.Category;

@Controller
public class PageController {
	
	@Autowired
	private CategoryDAO categoryDAO;
	
	
	@RequestMapping(value= {"/","/home","/index"})
	public ModelAndView index()
	{
		ModelAndView mv = new ModelAndView("page");
		
		
		mv.addObject("userClickHome",true);
		mv.addObject("categories",categoryDAO.list());
	 
		mv.addObject("title","Home");
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
		 
		mv.addObject("userClickCategoryProducts",true);
		mv.addObject("categories",categoryDAO.list()); 
	 
		mv.addObject("title",category.getName());
		mv.addObject("category",category);
		return mv;
	}
	
	
}
