package cn.edu.dlut.mail.wuchen2020.signinserver.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.SigninRecord;

public interface SigninRecordDAO extends JpaRepository<SigninRecord, Long> {

}
