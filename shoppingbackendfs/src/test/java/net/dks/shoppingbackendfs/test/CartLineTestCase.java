package net.dks.shoppingbackendfs.test;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import net.dks.shoppingbackendfs.dao.CartLineDAO;
import net.dks.shoppingbackendfs.dao.ProductDAO;
import net.dks.shoppingbackendfs.dao.UserDAO;
import net.dks.shoppingbackendfs.dto.Cart;
import net.dks.shoppingbackendfs.dto.CartLine;
import net.dks.shoppingbackendfs.dto.Product;
import net.dks.shoppingbackendfs.dto.User;

 

public class CartLineTestCase {

	

	private static AnnotationConfigApplicationContext context;
	
	
	private static CartLineDAO cartLineDAO=null ;
	private static ProductDAO productDAO =null;
	private static UserDAO userDAO=null;
	
	
	  private Product product=null; 
	  private User user=null; 
	  private Cart cart=null;
	  private CartLine cartLine = null;
	 
	
	@BeforeClass
	public static void init() {
		context = new AnnotationConfigApplicationContext();
		context.scan("net.dks.shoppingbackendfs");
		context.refresh();
		
		productDAO = (ProductDAO)context.getBean("productDAO");
		userDAO = (UserDAO)context.getBean("userDAO");
		cartLineDAO = (CartLineDAO)context.getBean("cartLineDAO");
	}
	
	
	
	  @Test public void testAddCartLine() {
	  
	  // fetch the user and then cart of that user 
		  User user = userDAO.getByEmail("deepak@gmail.com"); 
		  Cart cart = user.getCart();
	  System.out.println("value of user"+user); 
	  // fetch the product Product
	  product = productDAO.get(1);
	  
	  // Create a new 
	  CartLine cartLine = new CartLine();
	  cartLine.setBuyingPrice(product.getUnitPrice());
	  //cartLine.setCartId(cart.getId());
	  
	  cartLine.setProductCount(cartLine.getProductCount()+1);
	  
	  double oldTotal = cartLine.getTotal();
	  
	  cartLine.setTotal(cartLine.getProductCount()*product.getUnitPrice());
	  cartLine.setAvailable(true); cartLine.setCartId(cart.getId());
	  cartLine.setProduct(product); cartLine.setProduct(product);
	  assertEquals("Failed to add the cartLine", true, cartLineDAO.add(cartLine));
	  cart.setGrandTotal(cart.getGrandTotal()+cartLine.getTotal());
	  
	  
	  cart.setCartLines(cart.getCartLines() + 1); //
	  cart.setGrandTotal(cart.getGrandTotal() + (cartLine.getTotal()- oldTotal));
	  
	  //assertEquals("Failed to add the CartLine!",true,cartLineDAO.add(cartLine));
	  assertEquals("Failed to update the cart!",true, cartLineDAO.updateCart(cart));
	  
	  }
	 
	 
	
	
		
	/*
	 * @Test public void testUpdateCartLine() {
	 * 
	 * // fetch the user and then cart of that user User user =
	 * userDAO.getByEmail("deepak@gmail.com"); Cart cart = user.getCart();
	 * 
	 * cartLine = cartLineDAO.getByCartAndProduct(cart.getId(), 2);
	 * 
	 * cartLine.setProductCount(cartLine.getProductCount() + 1);
	 * 
	 * double oldTotal = cartLine.getTotal();
	 * 
	 * cartLine.setTotal(cartLine.getProduct().getUnitPrice() *
	 * cartLine.getProductCount());
	 * 
	 * cart.setGrandTotal(cart.getGrandTotal() + (cartLine.getTotal() - oldTotal));
	 * 
	 * assertEquals("Failed to update the CartLine!",true,cartLineDAO.update(
	 * cartLine));
	 * 
	 * 
	 * }
	 */
		  
		 
}
