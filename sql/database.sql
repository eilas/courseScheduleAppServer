drop database course_schedule;
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
    teacher_id int unsigned auto_increment primary key,
    name       varchar(16)            not null,
    sex        enum ('MALE','FEMALE') not null
) engine = InnoDB;

# 课程通过名、地点和时间识别上同一节课的同学？
create table course
(
    course_id           int unsigned auto_increment primary key,
    name                varchar(16)      not null,
    info                text,
    location            tinytext         not null,

    str_week            tinyint unsigned not null,
    end_week            tinyint unsigned not null,

    odd_week_str_time1  datetime         not null,
    odd_week_end_time1  datetime         not null,
    odd_week_str_time2  datetime,
    odd_week_end_time2  datetime,
    even_week_str_time1 datetime         not null,
    even_week_end_time1 datetime         not null,
    even_week_str_time2 datetime,
    even_week_end_time2 datetime
) engine = InnoDB;


create table course_record
(
    id         int unsigned primary key auto_increment,
    course_id  int unsigned not null,
    student_id varchar(9)   not null,
    teacher_id int unsigned ,
    foreign key course_id_key (course_id) references course (course_id),
    foreign key student_id_key (student_id) references student (student_id),
    foreign key teacher_id_key (teacher_id) references teacher (teacher_id)
) engine = InnoDB;

alter table course_record add column additional_info text;