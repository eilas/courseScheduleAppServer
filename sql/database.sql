create database course_schedule;
use course_schedule;

create table student
(
    id         int unsigned auto_increment,
    student_id varchar(9) primary key not null,
    name       varchar(16)            not null,
    sex        enum ('MALE','FEMALE') not null,
    password   varchar(40)            not null,
    unique (id)
) engine = InnoDB;

create table teacher
(
    id         int unsigned auto_increment,
    teacher_id varchar(9) primary key not null,
    name       varchar(16)            not null,
    sex        enum ('MALE','FEMALE') not null,
    unique (id)
) engine = InnoDB;

create table course
(
    id                  int unsigned auto_increment,
    course_id           varchar(9) primary key not null,
    name                varchar(16)            not null,
    info                text,
#     odd_even_week       boolean default false  not null,
    last_week           tinyint unsigned       not null,

    odd_week_str_time1  datetime               not null,
    odd_week_end_time1  datetime               not null,
    odd_week_str_time2  datetime,
    odd_week_end_time2  datetime,
    even_week_str_time1 datetime               not null,
    even_week_end_time1 datetime               not null,
    even_week_str_time2 datetime,
    even_week_end_time2 datetime,
    unique (id)
) engine = InnoDB;

create table course_record
(
    id         int unsigned primary key auto_increment,
    course_id  varchar(9) not null,
    student_id varchar(9) not null,
    teacher_id varchar(9) not null,
    foreign key course_id_key (course_id) references course (course_id),
    foreign key student_id_key (student_id) references student (student_id),
    foreign key teacher_id_key (teacher_id) references teacher (teacher_id)
) engine = InnoDB;

