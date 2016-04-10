package com.skye.test.demo;

import static org.junit.Assert.*;

import java.beans.PropertyVetoException;
import java.sql.SQLException;

import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class TestC3p0 {

	@Test
	public void test() throws PropertyVetoException, SQLException {
		//fail("Not yet implemented");
		ComboPooledDataSource p = new ComboPooledDataSource();
		p.setDriverClass("com.mysql.jdbc.Driver");
		p.setJdbcUrl("jdbc:mysql://127.0.0.1:3307/test?noDatetimeStringSync=true");
		p.setUser("root");
		p.setPassword("root123");
		System.out.println(p.getConnection());
	}

}
