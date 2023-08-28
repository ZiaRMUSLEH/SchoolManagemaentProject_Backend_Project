package com.project.schoolmanagment.repository.bussiness;

import com.project.schoolmanagment.entity.concretes.businnes.EducationTerm;
import com.project.schoolmanagment.entity.enums.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationTermRepository extends JpaRepository<EducationTerm , Long> {



    @Query("SELECT (count (e)>0 ) FROM EducationTerm e WHERE e.term = ?1 AND EXTRACT(YEAR FROM e.startDate) = ?2")
    boolean existsByTermAndYear(Term term, int year);


}
