package com.project.schoolmanagment.repository.user;


import com.project.schoolmanagment.entity.concretes.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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


    @Override
    @Modifying
    @Query("DELETE FROM Student s WHERE s.id = :id")
    void deleteById(@Param("id") Long id);

    @Query("SELECT s FROM Student  s WHERE s.id IN :id")
    List<Student>findByIdsEquals(Long [] id);










}
