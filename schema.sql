show databases;
drop database task_management;

show tables;

create database task_management;
use task_management;


create table roles (
	id bigint auto_increment unique not null primary key,
    role varchar(50) unique not null
);

create table users (
	id bigint auto_increment unique not null primary key,
    name varchar(50) not null,
    email varchar(320) unique not null,
    role_id bigint not null,
    password varchar(70),
    ip_address varchar(39),
    constraint users_fk_role_id
    foreign key(role_id) references roles(id)
);

create table projects (
	id bigint auto_increment unique not null primary key,
    name varchar(255) not null,
    description varchar(2000) not null,
    status enum("PENDING" , "PROCESSING","HOLD", "COMPLETED") not null,
    created_at datetime default now() not null,
	start_date datetime not null ,
	due_date datetime not null,
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
    description varchar(2000) not null,
    priority enum("HIGH" , "MEDIUM" , "LOW") not null,
	status enum("PENDING" , "PROCESSING","HOLD", "COMPLETED") not null,
    creator_id bigint not null,
	created_at datetime default now() not null,
	start_date datetime not null ,
	due_date datetime not null,
    parent_task_id bigint null,
	project_id bigint not null,
    depth int default 1 check(depth<=3),
    constraint tasks_fk_task_creator_id
    foreign key(creator_id) references users(id)
    on update cascade
    on delete cascade,
    constraint tasks_fk_parent_task_id
	foreign key(parent_task_id) references tasks(id)
    on update cascade
    on delete cascade,
	constraint task_fk_project_id
    foreign key(project_id) references projects(id)
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
    uploader_id bigint not null,
    constraint files_fk_task_id
    foreign key(task_id) references tasks(id)
    on update cascade
    on delete cascade,
    constraint files_fk_uploader_id
    foreign key(uploader_id) references users(id)
    on update cascade
    on delete cascade
);


desc roles;
select * from roles;
desc users;
select * from users;
delete from users where id =4;
delete from users;
desc projects;
select * from projects;
desc project_assignments;
select * from project_assignments;
desc tasks;
select * from tasks;
desc task_assignments;
select * from task_assignments;
desc files;
select * from files;