package net.dks.onlineshoppingfs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import net.dks.onlineshoppingfs.service.CartService;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;
	
	@RequestMapping("/show")
	public ModelAndView showCart(@RequestParam(name="result",required=false)String result)
	{
		ModelAndView mv =  new ModelAndView("page");
		if(result !=null) {
			switch(result) {
			case "updated":
				mv.addObject("message","Cart Line has been updated successfully !");
				break;
			case "added":
				mv.addObject("message","Cart Line has been added successfully !");
				break;
			case "maximum":
				mv.addObject("message","Cart Line has reached to maximum count !");
				break;
			case "deleted":
				mv.addObject("message","Cart Line has been removed successfully !");
				break;
			case "unavailable":
				mv.addObject("message","Product quantity is not available !");
				break;
			case "error":
				mv.addObject("message","Something went wrong !");
				break;
			}
		} 
		else {
			String response = cartService.validateCartLine();
			if(response.equals("result=modified")) {
				mv.addObject("message", "One or more items inside cart has been modified!");
			}
		}
	 	
		mv.addObject("title", "User Cart");
		mv.addObject("userClickShowCart", true);
		mv.addObject("cartLine", cartService.getCartLines());
 		 
		return mv;
	}
	
	@RequestMapping("/{cartLineId}/update")
	public String updateCart(@PathVariable int cartLineId, @RequestParam int count)
	{
		String response = cartService.manageCartLine(cartLineId, count);
		 
		return "redirect:/cart/show?"+response;
	}
	@RequestMapping("/{cartLineId}/delete")
	public String updateCart(@PathVariable int cartLineId)
	{
		String response = cartService.deleteCartLine(cartLineId);
		 
		return "redirect:/cart/show?"+response;
	}
	
	@RequestMapping("/add/{productId}/product")
	public String addCart(@PathVariable int productId)
	{
		String response = cartService.addCartLine(productId);
		 
		return "redirect:/cart/show?"+response;
	}
	@RequestMapping("/validate")
	public String validateCart() {	
		System.out.println("I am inside validate in CartController");
		String response = cartService.validateCartLine();
		System.out.println("Result of response in cart Controller inside validate");
		if(!response.equals("result=success")) {
			System.out.println("Result of response in cart Controller inside validate fail");
			return "redirect:/cart/show?"+response;
		}
		else {
			System.out.println("i M INSIDE cARTCONTROLLER redirect:/cart/checkout");
			return "redirect:/cart/checkout";
			
		}
	}	
}
