package com.skye.test.demo.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.skye.core.base.dao.BaseDao;
import com.skye.core.base.vo.Pagination;
import com.skye.test.demo.dao.IStudentDao;
import com.skye.test.demo.vo.StudentVO;
@Repository(value="iStudentDao")
public class StudentDaoImpl extends BaseDao implements IStudentDao {

	@Override
	public List<StudentVO> queryStudents(StudentVO vo, Pagination page) {
		List<StudentVO> list = findAll_myBatis("com.skye.test.demo.vo.StudentVO.queryStudents", vo, page.getPageIndex(), page.getPageSize());
		return list;
	}

	@Override
	public String getStudent() {
		List<Map> list = findAll_JDBC("select * from students");
		for(Map t:list){
	    	System.out.println(t.containsKey("id"));
	    }
		return null;
	}

}
