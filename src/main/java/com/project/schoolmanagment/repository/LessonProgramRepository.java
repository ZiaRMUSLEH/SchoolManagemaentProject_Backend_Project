package com.project.schoolmanagment.repository;

import com.project.schoolmanagment.entity.concretes.LessonProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface LessonProgramRepository extends JpaRepository<LessonProgram,Long> {


	List<LessonProgram> findByTeachers_IdNull();

	List<LessonProgram> findByTeachers_IdNotNull();


	@Query("SELECT l FROM LessonProgram l INNER JOIN l.teachers teachers WHERE teachers.username = ?1")
	Set<LessonProgram> getLessonProgramByTeachersUsername(String username);



}
