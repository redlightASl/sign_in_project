package cn.edu.dlut.mail.wuchen2020.signinserver.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.Student;

public interface StudentDAO extends JpaRepository<Student, Long> {

}
