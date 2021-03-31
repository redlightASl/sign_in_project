package cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
    
    @Column(name = "number", columnDefinition = "CHAR", nullable = false)
	private String number;
    
    @Column(name = "password", columnDefinition = "CHAR", nullable = false)
	private String password;
    
    @Column(name = "fingerprint", columnDefinition = "CHAR", nullable = false)
	private String fingerprint;
    
    @Column(name = "name", nullable = false)
	private String name;
    
    @Column(name = "class", nullable = false)
	private String className;
    
    @Column(name = "major", nullable = true)
	private String major;
	
    @Column(name = "department", nullable = true)
	private String department;
	
    public Student() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
