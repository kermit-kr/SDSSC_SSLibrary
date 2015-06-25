package com.board;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.frame.Dao;
import com.frame.SearchDao;
import com.mybatis.mapper.BoardMapper;

@Repository("boarddao")
public class BoardDao implements Dao ,SearchDao {

	@Autowired
	BoardMapper mapper;
	@Override
	public Object insert(Object obj) throws Exception {
		return mapper.insertboard(obj);
	}

	@Override
	public Object delete(Object obj) throws Exception {
		return mapper.delectboard(obj);
	}

	@Override
	public Object update(Object obj) throws Exception {
		return mapper.updateboard(obj);
	}

	@Override
	public Object select(Object obj) throws Exception {
		return mapper.selectboard(obj);
	}

	@Override
	public ArrayList<Object> select() throws Exception {
		return mapper.selectboards();
	}

	@Override
	public ArrayList<Object> searchname(Object obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> searchtitle(Object obj) throws Exception {
		return mapper.selecttitleboards(obj);
	}

	@Override
	public ArrayList<Object> searchcontent(Object obj) throws Exception {
		return mapper.selectcontentboards(obj);
	}

	@Override
	public ArrayList<Object> searchwriter(Object obj) throws Exception {
		return null;
	}

}