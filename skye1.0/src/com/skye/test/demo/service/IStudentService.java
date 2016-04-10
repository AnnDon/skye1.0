package com.skye.test.demo.service;

import java.util.List;

import com.skye.core.base.vo.Pagination;
import com.skye.test.demo.vo.StudentVO;

public interface IStudentService {
	public List<StudentVO> queryStudents(StudentVO vo, Pagination page);
	public void getStudent();
}
