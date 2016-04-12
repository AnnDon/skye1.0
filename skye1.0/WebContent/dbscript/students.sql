drop table students;
CREATE TABLE students (
	id int(10) NOT NULL auto_increment,
	username varchar(25) NOT NULL,
	sex varchar(25),
	age int(10),
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;