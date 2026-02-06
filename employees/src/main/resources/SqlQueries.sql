create sequence employees_emp_id_seqs start 1;

create table employees(
emp_id varchar(20) primary key default 'TEK' || nextval('employees_emp_id_seqs'),
emp_name varchar(50) not null,
dept varchar(20) not null,
email varchar(50) unique not null,
phnNo varchar(10) unique not null
);


create table emp_auth(
 emp_id varchar(20) primary key,
 password varchar(256) not null,
  constraint f_key
  foreign key (emp_id)
  references employees(emp_id)
  on delete cascade
);

create type emp_role as enum('ADMIN','MANAGER','EMPLOYEE');

create table emp_roles(
	emp_id varchar(20),
	roles emp_role not null,
	primary key(emp_id,roles),
	constraint f_key
  foreign key (emp_id)
  references employees(emp_id)
  on delete cascade
);

ALTER TABLE employees
ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT true,
ADD COLUMN deleted_at TIMESTAMP NULL;

INSERT INTO employees (emp_name, dept, email, phnNo)
VALUES 
    ('admin', 'Administration', 'Admin@gmail.com', '9876543210');


insert into emp_auth(emp_id,password) values('TEK1','8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918');
insert into emp_roles(emp_id,roles) values('TEK1','ADMIN');
