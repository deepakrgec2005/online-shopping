package net.dks.onlineshoppingfs.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.dks.onlineshoppingfs.model.UserModel;
import net.dks.shoppingbackendfs.dao.CartLineDAO;
import net.dks.shoppingbackendfs.dao.ProductDAO;
import net.dks.shoppingbackendfs.dto.Cart;
import net.dks.shoppingbackendfs.dto.CartLine;
import net.dks.shoppingbackendfs.dto.Product;

@Service("cartService")
public class CartService {
	@Autowired
	private ProductDAO productDAO;

	@Autowired
	private CartLineDAO cartLineDAO;
	@Autowired
	private HttpSession session;

	private Cart getCart() {

		return ((UserModel) session.getAttribute("userModel")).getCart();
	}

	public List<CartLine> getCartLines() {

		return cartLineDAO.list(getCart().getId());
	}

	public String manageCartLine(int cartLineId, int count) {

		System.out.println("value of cartLineID and count" + cartLineId+"and value of count"+ count);
		CartLine cartLine = cartLineDAO.get(cartLineId);
		System.out.println("value of cartLine" + cartLine);

		if (cartLine == null) {
			return "result=error";
		} else {
			Product product = cartLine.getProduct();
			double oldTotal = cartLine.getTotal();
			if (product.getQuantity() < count) {

				return "result=unavailable";
			}

			cartLine.setProductCount(count);
			cartLine.setBuyingPrice(product.getUnitPrice());
			cartLine.setTotal(product.getUnitPrice() * count);
			cartLineDAO.update(cartLine);
			Cart cart = this.getCart();
			cart.setGrandTotal(cart.getGrandTotal() - oldTotal + cartLine.getTotal());
			cartLineDAO.updateCart(cart);
			return "result=updated";
		}

	}

	public String deleteCartLine(int cartLineId) {
		CartLine cartLine = cartLineDAO.get(cartLineId);
		if (cartLine == null) {
			return "result=error";
		} else {
			Cart cart = this.getCart();
			cart.setGrandTotal(cart.getGrandTotal() - cartLine.getTotal());
			cart.setCartLines(cart.getCartLines() - 1);
			cartLineDAO.updateCart(cart);
			cartLineDAO.delete(cartLine);
			return "result=deleted";
		}

	}

	public String addCartLine(int productId) {
		// TODO Auto-generated method stub
		System.out.println("---------------------------");
		System.out.println("Inside add CartLine");
		System.out.println("--------------------");
		String response = null;
		Cart cart = this.getCart();
		System.out.println("---------------------------");
		System.out.println("value of cart :-" + cart);
		System.out.println("value of productId :-" + productId);
		System.out.println("--------------------");

		CartLine cartLine = cartLineDAO.getByCartAndProduct(cart.getId(), productId);
		System.out.println("value of cartLine in addCartLine function" + cartLine);
		if (cartLine == null) {
			cartLine = new CartLine();
			System.out.println("Inside cartLine=null");
			Product product = productDAO.get(productId);
			cartLine.setCartId(cart.getId());
			cartLine.setProduct(product);
			cartLine.setBuyingPrice(product.getUnitPrice());
			cartLine.setProductCount(1);
			cartLine.setTotal(product.getUnitPrice());
			cartLine.setAvailable(true);
			cartLineDAO.add(cartLine);
			cart.setCartLines(cart.getCartLines() + 1);
			cart.setGrandTotal(cart.getGrandTotal() + cartLine.getTotal());
			cartLineDAO.updateCart(cart);
			response = "result=added";
		} else {
					System.out.println("I am inside else function of CartService");
					if (cartLine.getProductCount() < 3) 
					{
						System.out.println("I am inside else function of CartService and value of cartLine.getProductCount()"+cartLine.getProductCount());
						response = this.manageCartLine(cartLine.getId(), cartLine.getProductCount() + 1);
						System.out.println("value of response id "+ response);
					} 
					else {
						response = "result=maximum";
						}

			}

		return response;
	}

	public String validateCartLine() {

		Cart cart = this.getCart();
		List<CartLine> cartLines = cartLineDAO.list(cart.getId());
		double grandTotal = 0.0;
		int lineCount = 0;
		String response = "result=success";
		boolean changed = false;
		Product product = null;
		System.out.println("I am inside 0f CartService in CartService ");
		for(CartLine cartLine : cartLines) {					
			product = cartLine.getProduct();
			changed = false;
			// check if the product is active or not
			// if it is not active make the availability of cartLine as false
			if((!product.isActive() && product.getQuantity() == 0) && cartLine.isAvailable()) {
				cartLine.setAvailable(false);
				changed = true;
			}			
			// check if the cartLine is not available
			// check whether the product is active and has at least one quantity available
			if((product.isActive() && product.getQuantity() > 0) && !(cartLine.isAvailable())) {
					cartLine.setAvailable(true);
					changed = true;
			}
			
			// check if the buying price of product has been changed
			if(cartLine.getBuyingPrice() != product.getUnitPrice()) {
				// set the buying price to the new price
				cartLine.setBuyingPrice(product.getUnitPrice());
				// calculate and set the new total
				cartLine.setTotal(cartLine.getProductCount() * product.getUnitPrice());
				changed = true;				
			}
			
			// check if that much quantity of product is available or not
			if(cartLine.getProductCount() > product.getQuantity()) {
				cartLine.setProductCount(product.getQuantity());										
				cartLine.setTotal(cartLine.getProductCount() * product.getUnitPrice());
				changed = true;
				
			}
			
			// changes has happened
			if(changed) {				
				//update the cartLine
				cartLineDAO.update(cartLine);
				// set the result as modified
				response = "result=modified";
			}
			
			grandTotal += cartLine.getTotal();
			lineCount++;
		}
		
		System.out.println("value of response in CartService :-"+response);
		
		
		cart.setCartLines(lineCount++);
		System.out.println("value of lineCount in CartService :-"+lineCount);
		cart.setGrandTotal(grandTotal);
		
		System.out.println("value of grandTotal in CartService :-"+grandTotal);
		cartLineDAO.updateCart(cart);

		return response;
	}

}
