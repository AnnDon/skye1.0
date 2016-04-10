package com.skye.test.demo.dao;

import java.util.List;

import com.skye.core.base.vo.Pagination;
import com.skye.test.demo.vo.StudentVO;

public interface IStudentDao {
  public List<StudentVO> queryStudents(StudentVO vo,Pagination page);
  public String getStudent();
}
