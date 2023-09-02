package com.project.schoolmanagment.repository.business;

import com.project.schoolmanagment.entity.concretes.businnes.LessonProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface LessonProgramRepository extends JpaRepository<LessonProgram, Long> {



    List<LessonProgram> findByTeachers_IdNull();
    List<LessonProgram>findByTeachers_IdNotNull();


    @Query("select l from LessonProgram  l inner join l.teachers teacher where teacher.username = ?1")
    Set<LessonProgram> getLessonProgramByTeachersUsername(String username);


    @Query("select l from LessonProgram  l inner join l.students student where student.username = ?1")
    Set<LessonProgram> getLessonProgramByStudentsUsername(String username);






}
