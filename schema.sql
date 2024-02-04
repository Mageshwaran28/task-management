show databases;
create database task_management;
use task_management;
show tables;
drop database task_management;

create table roles (
	id bigint auto_increment unique not null primary key,
    role varchar(50) unique not null
);

create table users (
	id bigint auto_increment unique not null primary key,
    email varchar(320) unique not null,
    role_id bigint not null,
    password varchar(16) not null check(char_length(password)>=6 and char_length(password)<=16) ,
    ip_address varchar(15) not null,
    constraint users_fk_role_id
    foreign key(role_id) references roles(id)
     on update cascade
    on delete cascade
);

create table projects (
	id bigint auto_increment unique not null primary key,
    name varchar(255) not null,
    description text not null,
    creator_id bigint not null ,
    constraint projects_fk_creator_id 
    foreign key(creator_id) references users(id)
);

create table project_assignments(
	id bigint auto_increment unique not null primary key,
    project_id bigint not null,
    assignee_id bigint not null,
    constraint project_assign_fk_project_id
    foreign key(project_id) references projects(id)
    on update cascade
    on delete cascade,
    constraint project_assignments_fk_assignee_id
    foreign key(assignee_id) references users(id)
    on update cascade
    on delete cascade
);

create table tasks (
	id bigint auto_increment unique not null primary key,
    name varchar(255) not null,
    description text not null,
    priority enum("HIGH" , "MEDIUM" , "LOW") not null,
	status enum("PENDING" , "PROCESSING", "COMPLETED") not null,
    creator_id bigint not null,
	created_at datetime default now() not null,
	start_date datetime not null ,
	due_date date not null,
    parent_task_id bigint null,
    depth int default 1 check(depth<=3),
    constraint tasks_fk_task_creator_id
    foreign key(creator_id) references users(id)
    on update cascade
    on delete cascade,
    constraint tasks_fk_parent_task_id
	foreign key(parent_task_id) references tasks(id)
    on update cascade
    on delete cascade
);


create table task_assignments(
	id bigint auto_increment unique not null primary key,
    task_id bigint not null,
    assignee_id bigint not null,
    constraint task_assignments_fk_task_id
    foreign key(task_id) references tasks(id)
    on update cascade
    on delete cascade,
    constraint task_assignments_fk_assignee_id
    foreign key(assignee_id) references users(id)
    on update cascade
    on delete cascade
);

create table files(
	id bigint auto_increment unique not null primary key,
    task_id bigint not null,
    name varchar(255) not null,
    document mediumblob not null,
    uploaded_date datetime default now() not null,
    constraint files_fk_task_id
    foreign key(task_id) references tasks(id)
    on update cascade
    on delete cascade
);


create table project_task_assignments(
	id bigint auto_increment unique not null primary key,
    task_id bigint not null,
    project_id bigint not null,
    constraint project_task_assignments_fk_task_id
    foreign key(task_id) references tasks(id)
    on update cascade
    on delete cascade,
    constraint project_task_assignments_fk_project_id
    foreign key(project_id) references projects(id)
    on update cascade 
    on delete cascade
);

-- Insert roles
INSERT INTO roles (role) VALUES
  ('Admin'),
  ('Manager'),
  ('Developer'),
  ('Tester');

-- Insert users
INSERT INTO users (email, role_id, password) VALUES
  ('admin@example.com', 1, 'adminpass'),
  ('manager@example.com', 2, 'managerpass'),
  ('developer1@example.com', 3, 'devpass1'),
  ('developer2@example.com', 3, 'devpass2'),
  ('tester1@example.com', 4, 'testpass1'),
  ('tester2@example.com', 4, 'testpass2');

-- Insert projects
INSERT INTO projects (name, description, creator_id) VALUES
  ('Project 1', 'Description for Project 1', 2),
  ('Project 2', 'Description for Project 2', 3),
  ('Project 3', 'Description for Project 3', 4);

-- Insert project assignments
INSERT INTO project_assignments (project_id, assignee_id) VALUES
  (1, 3),
  (1, 4),
  (2, 3),
  (3, 4);

-- Insert tasks
INSERT INTO tasks (name, description, priority, status, creator_id, start_date, due_date) VALUES
  ('Task 1', 'Description for Task 1', 'HIGH', 'PENDING', 3, '2024-01-01', '2024-01-15'),
  ('Task 2', 'Description for Task 2', 'MEDIUM', 'PROCESSING', 4, '2024-01-05', '2024-01-20'),
  ('Task 3', 'Description for Task 3', 'LOW', 'COMPLETED', 3, '2024-01-10', '2024-01-25');

-- Insert task assignments
INSERT INTO task_assignments (task_id, assignee_id) VALUES
  (1, 4),
  (2, 3),
  (3, 4);

-- Insert project task assignments
INSERT INTO project_task_assignments (task_id, project_id) VALUES
  (1, 1),
  (2, 2),
  (3, 3);
  
  desc roles;
select * from roles;
desc users;
select * from users;
delete from users;
desc projects;
select * from projects;

desc project_assignments;
select * from project_assignments;

desc tasks;
select * from tasks;
delete from tasks where id = 2;

desc task_assignments;
select * from task_assignments;

desc files;
select * from files;
delete from files;

desc project_task_assignments;
select * from project_task_assignments;