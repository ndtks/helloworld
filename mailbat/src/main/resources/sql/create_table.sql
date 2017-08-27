drop table if exists m_user;
drop table if exists m_group;
drop table if exists m_user_grp;
drop table if exists m_account;
drop table if exists m_cond_type;
drop table if exists m_condition;
drop table if exists m_filter;
drop table if exists m_forward;
drop table if exists m_forward_grp;
drop table if exists m_forward_user;
drop table if exists d_mail_header;
drop table if exists d_mail_body;
drop table if exists d_mail_to;
drop table if exists d_mail_cc;
drop table if exists d_mail_fwd_log;

create table m_user (
	user_id int NOT NULL,
	user_name varchar(80) NOT NULL,
	login_id varchar(40),
	password varchar(40),
	mail_addr varchar(100),
	admin_flg int NOT NULL,
	created timestamp default current_timestamp,
	created_by int,
	updated timestamp default current_timestamp,
	updated_by int,
	primary key (user_id)
);

create table m_group (
	group_id int NOT NULL,
	group_name varchar(80) NOT NULL,
	created timestamp default current_timestamp,
	created_by int,
	updated timestamp default current_timestamp,
	updated_by int,
	primary key (group_id)
);

create table m_user_grp (
	user_id int NOT NULL,
	group_id int NOT NULL,
	created timestamp default current_timestamp,
	created_by int,
	updated timestamp default current_timestamp,
	updated_by int,
	primary key (user_id, group_id)
);

create table m_account (
	acc_id int NOT NULL,
	acc_name varchar(80) NOT NULL,
	mail_addr varchar(100),
	pop3_host varchar(40),
	pos3_user varchar(100),
	pop3_pass varchar(40),
	smtp_host varchar(40),
	smtp_user varchar(100),
	smtp_pass varchar(40),
	created timestamp default current_timestamp,
	created_by int,
	updated timestamp default current_timestamp,
	updated_by int,
	primary key (acc_id)
);

create table m_cond_type (
	cond_type_id int NOT NULL,
	cond_type_name varchar(40) NOT NULL,
	description varchar(100),
	created timestamp default current_timestamp,
	created_by int,
	updated timestamp default current_timestamp,
	updated_by int,
	primary key (cond_type_id)
);

create table m_condition (
	cond_id int NOT NULL,
	cond_type_id int NOT NULL,
	cond_name varchar(40),
	str1 varchar(40),
	str2 varchar(40),
	str3 varchar(40),
	num1 int,
	num2 int,
	num3 int,
	created timestamp default current_timestamp,
	created_by int,
	updated timestamp default current_timestamp,
	updated_by int,
	primary key (cond_id)
);

create table m_filter (
	filter_id int NOT NULL,
	cond_id int NOT NULL,
	or_flg int NOT NULL,
	created timestamp default current_timestamp,
	created_by int,
	updated timestamp default current_timestamp,
	updated_by int,
	primary key (filter_id, cond_id)
);

create table m_forward (
	fwd_id int NOT NULL,
	acc_id int NOT NULL,
	filter_id int NOT NULL,
	created timestamp default current_timestamp,
	created_by int,
	updated timestamp default current_timestamp,
	updated_by int,
	primary key (fwd_id)
);

create table m_forward_grp (
	fwd_id int NOT NULL,
	group_id int NOT NULL,
	created timestamp default current_timestamp,
	created_by int,
	updated timestamp default current_timestamp,
	updated_by int,
	primary key (fwd_id, group_id)
);

create table m_forward_user (
	fwd_id int NOT NULL,
	user_id int NOT NULL,
	created timestamp default current_timestamp,
	created_by int,
	updated timestamp default current_timestamp,
	updated_by int,
	primary key (fwd_id, user_id)
);

create table d_mail_header (
	mail_id int NOT NULL,
	acc_id int NOT NULL,
	mail_from varchar(100),
	subject varchar(100),
	mail_raw mediumtext,
	mail_contenttype varchar(40),
	contenttype varchar(40),
	mail_raw_md5 varchar(40),
	created timestamp default current_timestamp,
	created_by int,
	updated timestamp default current_timestamp,
	updated_by int,
	primary key (mail_id)
);

create table d_mail_body (
	mail_id int NOT NULL,
	mail_body mediumtext,
	created timestamp default current_timestamp,
	created_by int,
	updated timestamp default current_timestamp,
	updated_by int,
	primary key (mail_id)
);

create table d_mail_to (
	mail_id int NOT NULL,
	mail_to varchar(100),
	created timestamp default current_timestamp,
	created_by int,
	updated timestamp default current_timestamp,
	updated_by int,
	primary key (mail_id)
);

create table d_mail_cc (
	mail_id int NOT NULL,
	mail_cc varchar(100),
	created timestamp default current_timestamp,
	created_by int,
	updated timestamp default current_timestamp,
	updated_by int,
	primary key (mail_id)
);

create table d_mail_fwd_log (
	mail_id int NOT NULL,
	fwd_id int NOT NULL,
	forwarded timestamp default current_timestamp NOT NULL,
	created timestamp default current_timestamp,
	created_by int,
	updated timestamp default current_timestamp,
	updated_by int,
	primary key (mail_id)
);
