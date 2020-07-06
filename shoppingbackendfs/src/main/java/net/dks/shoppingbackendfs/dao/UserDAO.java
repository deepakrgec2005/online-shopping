package net.dks.shoppingbackendfs.dao;

import java.util.List;

import net.dks.shoppingbackendfs.dto.Address;
import net.dks.shoppingbackendfs.dto.Cart;
import net.dks.shoppingbackendfs.dto.User;

public interface UserDAO {
	User getByEmail(String email);
	User get(int id);
	
	boolean addUser(User user);
	Address getAddress(int addressId);
	boolean addAddress(Address address);
	boolean updateAddress(Address address);
	Address getBillingAddress(User user);
	List<Address> listShippingAddress(User user);
	
	
}
