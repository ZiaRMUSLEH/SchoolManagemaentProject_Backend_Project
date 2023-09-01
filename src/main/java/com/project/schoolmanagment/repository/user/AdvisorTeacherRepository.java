package com.project.schoolmanagment.repository.user;

import com.project.schoolmanagment.entity.concretes.user.AdvisoryTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvisorTeacherRepository extends JpaRepository<AdvisoryTeacher,Long> {
}
