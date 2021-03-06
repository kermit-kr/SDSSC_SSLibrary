package com.board;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.frame.Biz;
import com.frame.Dao;
import com.frame.SearchBiz;
import com.frame.SearchDao;
import com.frame.UpdateAndReturnBiz;
import com.frame.UpdateAndReturnDao;

@Service("boardbiz")
public class BoardBiz implements Biz, SearchBiz,UpdateAndReturnBiz {
	
	@Resource(name="boarddao")
	Dao dao;
	@Resource(name="boarddao")
	SearchDao dao2;
	@Resource(name="boarddao")
	UpdateAndReturnDao dao3;
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
	public ArrayList<Object> gettitle(Object obj) throws Exception {
		return dao2.searchtitle(obj);
	}

	@Override
	public ArrayList<Object> getcontent(Object obj) throws Exception {
		return dao2.searchcontent(obj);
	}

	@Override
	public ArrayList<Object> getwriter(Object obj) throws Exception {
		return dao2.searchwriter(obj);
	}
	//리플 받아오는 용으로 사용
	@Override
	public ArrayList<Object> getname(Object obj) throws Exception {
		return dao2.searchname(obj);
	}

	@Override
	public ArrayList<Object> getid(Object obj) throws Exception {
		return dao2.searchid(obj);
	}

	@Override
	public Object logupdate(Object obj) throws Exception {
		return dao3.logupdate(obj);
	}

	@Override
	public Object logreturn(Object obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> getexpired() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getnum_reply(Object obj) throws Exception {
		int result = dao2.getnum_reply(obj);
		return result;
	}



}
