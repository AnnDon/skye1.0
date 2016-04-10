package com.skye.test.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.skye.core.base.vo.Pagination;
import com.skye.test.demo.dao.IStudentDao;
import com.skye.test.demo.service.IStudentService;
import com.skye.test.demo.vo.StudentVO;
@Service("iStudentService")
public class StudentServiceImpl implements IStudentService {
	@Autowired
	@Qualifier("iStudentDao")
	private IStudentDao iStudentDao;
	@Override
	public List<StudentVO> queryStudents(StudentVO vo, Pagination page) {
		
		return iStudentDao.queryStudents(vo, page);
	}
	@Override
	public void getStudent() {
		iStudentDao.getStudent();
	}

}
