package net.dks.onlineshoppingfs.handler;

 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import net.dks.onlineshoppingfs.model.RegisterModel;
import net.dks.shoppingbackendfs.dao.UserDAO;
import net.dks.shoppingbackendfs.dto.Address;
import net.dks.shoppingbackendfs.dto.Cart;
import net.dks.shoppingbackendfs.dto.User;

@Component
public class RegisterHandler {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	public RegisterModel init()
	{
		return new RegisterModel();
	}

	public void addUser(RegisterModel registerModel, User user)
	{
		registerModel.setUser(user);
	}
	public void addBilling(RegisterModel registerModel, Address billing)
	{
		registerModel.setBilling(billing);
	}
	
	public String validateUser(User user,MessageContext error)
	{
		String transitionValue ="success";
		
		if(!(user.getPassword().equals(user.getConfirmPassword())))
		
		{
			error.addMessage(new MessageBuilder().error().source("confirmPassword").defaultText("Password does not match the confirm Password!").build());
			transitionValue ="failure";
		}
		
		if(userDAO.getByEmail(user.getEmail())!=null)
		{
			error.addMessage(new MessageBuilder().error().source("email").defaultText("Email address is already used!").build());
			
			transitionValue ="failure";
		}
		return transitionValue;
		
	}
	
	public String saveAll(RegisterModel model)
	{
		String transitionValue="success";
		
		User user=model.getUser();
		if(user.getRole().equals("USER"))
		
		{
			Cart cart = new Cart();
			cart.setUser(user);
			user.setCart(cart);
			 
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userDAO.addUser(user);
		Address billing = model.getBilling();
		billing.setUserId(user.getId());
		billing.setBilling(true);
		userDAO.addAddress(billing);
		return transitionValue;
	}
	
	
}
