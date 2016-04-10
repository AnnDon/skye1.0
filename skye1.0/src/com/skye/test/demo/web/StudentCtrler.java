package com.skye.test.demo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.skye.core.base.vo.Pagination;
import com.skye.test.demo.service.IStudentService;
import com.skye.test.demo.vo.StudentVO;

@Controller
@RequestMapping(value="/student")
public class StudentCtrler {
	@Autowired
	@Qualifier("iStudentService")
	private IStudentService iStudentService;
    public void getStudents(){
    	StudentVO vo = new StudentVO();
    	Pagination page = new Pagination();
    	page.setPageIndex((4-1)*4);
    	page.setPageSize(4);
	    List<StudentVO> list = iStudentService.queryStudents(vo, page);
	    for(StudentVO t:list){
	    	System.out.println(t);
	    }
	    iStudentService.getStudent();
    }
}
