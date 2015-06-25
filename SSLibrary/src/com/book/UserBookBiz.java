package com.book;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.frame.Biz;
import com.frame.Dao;
import com.frame.SearchBiz;
import com.frame.SearchDao;
@Service("userbookbiz")
public class UserBookBiz implements Biz, SearchBiz {
	@Resource(name="userbookdao")
	Dao dao;
	@Resource(name="userbookdao")
	SearchDao dao2;
	
	@Override
	public Object register(Object obj) throws Exception {
		return dao.insert(obj);
	}

	@Override
	public Object modify(Object obj) throws Exception {
		return dao.update(obj);
	}

	@Override
	public Object remove(Object obj) throws Exception {
		return dao.delete(obj);
	}

	@Override
	public Object get(Object obj) throws Exception {
		return dao.select(obj);
	}


	@Override
	public ArrayList<Object> get() throws Exception {
		return dao.select();
	}

	@Override
	public ArrayList<Object> getname(Object obj) throws Exception {
		return dao2.searchname(obj);
	}

	@Override
	public ArrayList<Object> gettitle(Object obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> getcontent(Object obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> getwriter(Object obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}