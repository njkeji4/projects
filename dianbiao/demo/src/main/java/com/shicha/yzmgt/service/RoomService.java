package com.shicha.yzmgt.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.shicha.yzmgt.bean.User;
import com.shicha.yzmgt.bean.jifang.Room;
import com.shicha.yzmgt.dao.IRoomDao;
import com.shicha.yzmgt.dao.IUserDao;


@Service
public class RoomService {

	private static final Logger log = LoggerFactory.getLogger(RoomService.class);
	
	@Autowired
	IRoomDao roomDao;
	
	@Autowired
	IUserDao	userDao;
	
	
	public Page<Room> searchDevice(final Room room) {
		
		String userName = null;
		try{
			userName = SecurityContextHolder.getContext().getAuthentication().getName();			
			if(userName == null)
				return null;
		}catch(Exception ex) {
			return null;
		}
			
		User user = userDao.findByName(userName);
		Direction orderBy  = (room.getOrder() == null || room.getOrder().equals("asc")) ? Direction.ASC : Direction.DESC;		
		String sort = room.getSort();		
		Pageable pageable = PageRequest.of(room.getPage(), room.getSize(), Sort.by(orderBy, sort));	
		
		return roomDao.findAll(new Specification<Room>() {

			@Override
			public Predicate toPredicate(Root<Room> root, CriteriaQuery<?> criteria, CriteriaBuilder builder) {
				List<Predicate> predicatesList = new ArrayList<>();
				
				if(user != null && !user.getRole().equals(User.ROLE_ADMIN)) {						
					predicatesList.add(builder.and(
							builder.equal(root.get("groupName"), user.getGroupName())
							));
				}
				
				if(room.getName() != null) {
					predicatesList.add(builder.and(
							builder.like(root.get("name"), "%" + room.getName() + "%")
							));
				}				
				
				return builder.and(
	                    predicatesList.toArray(new Predicate[predicatesList.size()]));
			}}, pageable);
	}
	
	public void add(Room room) {
		if(nameExisted(room)) {
			return;
		}
		
		User user= getCurrentUser();		
		if(user!=null && !user.getRole().equals(User.ROLE_ADMIN)) {			
			room.setGroupName(user.getGroupName());
		}
		
		room.setCreateTime(System.currentTimeMillis());
		roomDao.save(room);
	}
	
	public boolean nameExisted(Room	room) {
		
		return (roomDao.findByName(room.getName()) != null);
	}

	public void del(String[] ids) {
		for(String id : ids)
			roomDao.deleteById(id);
	}
	
	private User getCurrentUser() {
		String userName = null;
		try{
			userName = SecurityContextHolder.getContext().getAuthentication().getName();			
			if(userName == null)
				return null;
			
			User user = userDao.findByName(userName);
			return user;
		}catch(Exception ex) {
			return null;
		}
	}
	
}
