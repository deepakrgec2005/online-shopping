package net.dks.shoppingbackendfs.dao;

import java.util.List;

import net.dks.shoppingbackendfs.dto.Cart;
import net.dks.shoppingbackendfs.dto.CartLine;
import net.dks.shoppingbackendfs.dto.OrderDetail;

public interface CartLineDAO {
	
	public CartLine get(int id);
	public boolean add(CartLine cartLine);
	public boolean update(CartLine cartLine);
	public boolean delete(CartLine cartLine);
	public List<CartLine> list(int cartId);
	
	public List<CartLine> listAvailable(int cartId);
	public CartLine getByCartAndProduct(int cartId, int productID);
	
	boolean updateCart(Cart cart);
 
	
	// adding order details
	boolean addOrderDetail(OrderDetail orderDetail);

}
