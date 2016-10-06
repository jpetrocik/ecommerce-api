package org.psoft.ecommerce.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.psoft.ecommerce.data.model.Group;
import org.psoft.ecommerce.data.model.User;

public class AclService  extends AbstractService<User> {

	/**
	 * Looks up the account by username
	 * 
	 * @param username
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<User> findUser(String username) {
		Session session = currentSession();

		username = StringUtils.trimToEmpty(username);
		
		if (StringUtils.isBlank(username)){
			return (List<User>) session.createCriteria(User.class).list();
		} else {
			return (List<User>) session.createCriteria(User.class).add(Restrictions.like("username", username + "%")).list();
		}
	}
	
	public User retrieveById(Long userId) {
		Session session = currentSession();

		User user = (User) session.get(User.class, userId);

		return user;
	}
	
	public void addGroup(Long userId, Long groupId){
		Session session = currentSession();

		User user = (User) session.get(User.class, userId);
		Group group = (Group) session.get(Group.class, groupId);

		user.addGroup(group);
		
		session.flush();
	}
	

	public void removeGroup(Long userId, Long groupId){
		Session session = currentSession();

		User user = (User) session.get(User.class, userId);
		Group group = (Group) session.get(Group.class, groupId);

		user.removeGroup(group);
		
		session.saveOrUpdate(user);
		
		session.flush();
	}

	public User save(Long userId, String username, String fullName){
		Session session = currentSession();

		User user;
		if (userId == null){
			user = new User();
		} else {
			user = (User) session.get(User.class, userId);
		}
		
		user.setUsername(username);
		user.setFullName(fullName);
		
		session.saveOrUpdate(user);
		
		session.flush();
		
		return user;
	}
	
	public List<Group> allGroups(){
		Session session = currentSession();

		
		return (List<Group>) session.createCriteria(Group.class).list();
	}

	public void savePassword(Long userId, String password) {
		Session session = currentSession();

		User user = (User) session.get(User.class, userId);
		user.setPassword(password);
		
		session.saveOrUpdate(user);
		
		session.flush();
	}

}
