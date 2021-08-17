package cn.edu.dlut.mail.wuchen2020.signinserver.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.SigninRecord;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.Student;

public interface SigninRecordDAO extends JpaRepository<SigninRecord, Long> {
    Page<SigninRecord> findByStudent(Student student, Pageable pageable);
    List<SigninRecord> findByStudentAndTimeAfter(Student student, Date date);
}
