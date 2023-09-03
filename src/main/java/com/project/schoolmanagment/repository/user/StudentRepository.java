package com.project.schoolmanagment.repository.user;

import com.project.schoolmanagment.entity.concretes.user.Admin;
import com.project.schoolmanagment.entity.concretes.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {



    boolean existsByUsername(String username);

    boolean existsBySsn(String ssn);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    Student findByUsernameEquals(String username);

    @Query(value = "select (count(s)>0) from Student s")
    boolean findStudent();

    @Query(value = "select max(s.studentNumber) from Student s")
    int getMaxStudentNumber();

    List<Student> getStudentsByNameContaining(String studentName);

    @Query(value = "select s from Student s where s.advisoryTeacher.teacher.username =:username")
    List<Student> getStudentByAdvisoryTeacher_UserName(String username);




}
