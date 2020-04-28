package net.dks.shoppingbackendfs.daoimpl;


import org.hibernate.SessionFactory;
import java.util.List;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import net.dks.shoppingbackendfs.dao.CategoryDAO;
import net.dks.shoppingbackendfs.dto.Category;

@Repository("categoryDAO")
@Transactional
public class CategoryDAOImpl implements CategoryDAO {

	@Autowired
	private SessionFactory seesionFactory;

	@Override
	public List<Category> list() {

String selectActiveCategory = "FROM Category WHERE active = :active";
		
		Query query = seesionFactory.getCurrentSession().createQuery(selectActiveCategory);
				
		query.setParameter("active", true);
						
		return query.getResultList();
	}

	@Override
	public Category get(int id) {

		return seesionFactory.getCurrentSession().get(Category.class, Integer.valueOf(id));
	}

	@Override

	public boolean add(Category category) {

		try {
			seesionFactory.getCurrentSession().persist(category);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public boolean update(Category category) {

		try {
			seesionFactory.getCurrentSession().update(category);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean delete(Category category) {
		category.setActive(false);
		try {
			seesionFactory.getCurrentSession().update(category);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
