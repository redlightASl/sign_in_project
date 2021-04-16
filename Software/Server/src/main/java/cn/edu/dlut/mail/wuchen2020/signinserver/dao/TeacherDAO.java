package cn.edu.dlut.mail.wuchen2020.signinserver.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.Teacher;

public interface TeacherDAO extends JpaRepository<Teacher, Long> {
    Teacher findByNumber(String number);
}
