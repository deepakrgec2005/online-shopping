package net.dks.shoppingbackendfs.daoimpl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.dks.shoppingbackendfs.dao.UserDAO;
import net.dks.shoppingbackendfs.dto.Address;
import net.dks.shoppingbackendfs.dto.Cart;
import net.dks.shoppingbackendfs.dto.User;

@Repository("userDAO")
@Transactional
public class UserDAOImpl implements UserDAO {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public boolean addUser(User user) {
		try {

			sessionFactory.getCurrentSession().persist(user);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean addAddress(Address address) {
		try {

			sessionFactory.getCurrentSession().persist(address);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	

	@Override
	public User getByEmail(String email) {
		 String selectQuery ="From User where email= :email";
		 try {
			 return sessionFactory.getCurrentSession().createQuery(selectQuery, User.class).setParameter("email", email).getSingleResult();
			 
		 }catch (Exception e) {
			//e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public Address getBillingAddress(User user) {
		 String selectQuery= "From Address where userId=:userId AND billing =:billing";
		 try {
			 return sessionFactory.getCurrentSession().createQuery(selectQuery, Address.class).setParameter("userId", user.getId()).setParameter("billing", true).getSingleResult();
			 
		 }catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	
	
	@Override
	public List<Address> listShippingAddress(User user) {
		
		String selectQuery= "From Address where userId=:userId AND shipping =:shipping";
		 try {
			 System.out.println("Inside listShippingAddress function of USerDAOIMPL");
			 return sessionFactory.getCurrentSession().createQuery(selectQuery, Address.class).setParameter("userId", user.getId()).setParameter("shipping", true).getResultList();
			 
		 }catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public User get(int id) {
		try {			
			return sessionFactory.getCurrentSession().get(User.class, id);			
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}

	@Override
	public Address getAddress(int addressId) {
		try {			
			return sessionFactory.getCurrentSession().get(Address.class, addressId);			
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}

	@Override
	public boolean updateAddress(Address address) {
		try {			
			sessionFactory.getCurrentSession().update(address);			
			return true;
		}
		catch(Exception ex) {
			return false;
		}
	}

}
