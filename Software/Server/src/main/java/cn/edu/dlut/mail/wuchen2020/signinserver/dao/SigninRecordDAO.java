package cn.edu.dlut.mail.wuchen2020.signinserver.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.SigninRecord;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.Student;

public interface SigninRecordDAO extends JpaRepository<SigninRecord, Long> {
    List<SigninRecord> findByStudentAndTimeBetween(Student student, Date max, Date min);
}
