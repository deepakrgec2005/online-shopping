package net.dks.shoppingbackendfs.daoimpl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.dks.shoppingbackendfs.dao.CartLineDAO;
import net.dks.shoppingbackendfs.dto.Cart;
import net.dks.shoppingbackendfs.dto.CartLine;
import net.dks.shoppingbackendfs.dto.OrderDetail;
@Repository("cartLineDAO")
@Transactional
public class CartLineDAOImpl implements CartLineDAO {

	@Autowired
	private SessionFactory	sessionFactory;
	
	@Override
	public CartLine get(int id) {
		// TODO Auto-generated method stub
		System.out.println("Inside CartLineDAOIMpl and value of id"+id);
		System.out.println("value of return :-"+sessionFactory.getCurrentSession());
		return sessionFactory.getCurrentSession().get(CartLine.class, id);
	}

	@Override
	public boolean add(CartLine cartLine) {
		try {
			sessionFactory.getCurrentSession().persist(cartLine);
			return true;
		}catch (Exception e) {
			return false;
		}
  
	}

	@Override
	public boolean update(CartLine cartLine) {
		try {
			sessionFactory.getCurrentSession().update(cartLine);
			return true;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean delete(CartLine cartLine) {
		try {			
			sessionFactory.getCurrentSession().delete(cartLine);
			return true;
		}catch(Exception ex) {
			return false;
		}
	}

	@Override
	public List<CartLine> list(int cartId) {
	String query="From CartLine where cartId = :cartId";
		return sessionFactory.getCurrentSession().createQuery(query,CartLine.class).setParameter("cartId", cartId).getResultList();
	}

	@Override
	public List<CartLine> listAvailable(int cartId) {
		String query="From CartLine where cartId = :cartId AND available =:available";
		return sessionFactory.getCurrentSession().createQuery(query,CartLine.class).setParameter("cartId", cartId).setParameter("available", true).getResultList();
		
	}

	@Override
	public CartLine getByCartAndProduct(int cartId, int productId) {
		// TODO Auto-generated method stub
		String query="From CartLine where cartId = :cartId AND product.id  =:productId";
		try {
			System.out.println("value of cartID and product ID  inside cartDAOIMPL "+cartId+" , "+productId);
			System.out.println("output"+sessionFactory.getCurrentSession() .createQuery(query,CartLine.class).setParameter("cartId", cartId).setParameter("productId", productId).getSingleResult());
			System.out.println("before return");
			return sessionFactory.getCurrentSession().createQuery(query,CartLine.class).setParameter("cartId", cartId).setParameter("productId", productId).getSingleResult();
			} catch ( Exception e) {
			// TODO Auto-generated catch block
				System.out.println("inside error");
			return null;
		}
		
	}
	@Override
	public boolean updateCart(Cart cart) {
		try {

			sessionFactory.getCurrentSession().update(cart); 
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean addOrderDetail(OrderDetail orderDetail) {
		try {			
			sessionFactory.getCurrentSession().persist(orderDetail);			
			return true;
		}
		catch(Exception ex) {
			return false;
		}
	}

//	------------------//
	
	
	
}
