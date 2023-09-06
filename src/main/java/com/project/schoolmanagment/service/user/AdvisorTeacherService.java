package com.project.schoolmanagment.service.user;

import com.project.schoolmanagment.entity.concretes.user.AdvisoryTeacher;
import com.project.schoolmanagment.entity.concretes.user.Teacher;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.mappers.user.AdvisorTeacherMapper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.repository.user.AdvisorTeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdvisorTeacherService {

    private final AdvisorTeacherRepository advisorTeacherRepository;
    private final UserRoleService userRoleService;
    private final AdvisorTeacherMapper advisorTeacherMapper;


    public void saveAdvisoryTeacher(Teacher teacher){
        AdvisoryTeacher advisoryTeacher = advisorTeacherMapper.mapTeacherToAdvisorTeacher(teacher);
        advisoryTeacher.setUserRole(userRoleService.getUserRole(RoleType.ADVISORY_TEACHER));
        advisorTeacherRepository.save(advisoryTeacher);
    }

    public void updateAdvisoryTeacher(boolean status, Teacher teacher){
        Optional<AdvisoryTeacher> advisoryTeacher = advisorTeacherRepository.getAdvisoryTeacherByTeacher_Id(teacher.getId());
        AdvisoryTeacher.AdvisoryTeacherBuilder advisoryTeacherBuilder = AdvisoryTeacher.builder()
                .teacher(teacher)
                .userRole(userRoleService.getUserRole(RoleType.ADVISORY_TEACHER));

        if(advisoryTeacher.isPresent()){
            // while advisoryTeacher is present it means its previous isAdvisory value is true;
            // then we will check, if it is changed to false then delete it.
            if(!status){
                advisorTeacherRepository.deleteById(advisoryTeacher.get().getId());}

            // while advisoryTeacher is not present it means its previous isAdvisory value is false;
            // then we will check, if it is changed to true then save it.
        }else {
            if(status){advisoryTeacherBuilder.id(teacher.getId());
                advisorTeacherRepository.save(advisoryTeacherBuilder.build());
            }
        }



//        if(advisoryTeacher.isPresent()){
//            if(status){
//                advisoryTeacherBuilder.id(advisoryTeacher.get().getId());
//                advisorTeacherRepository.save(advisoryTeacherBuilder.build());
//            } else {
//                advisorTeacherRepository.deleteById(advisoryTeacher.get().getId());
//            }
//        } else {
//            advisorTeacherRepository.save(advisoryTeacherBuilder.build());
//        }
    }

    public AdvisoryTeacher getAdvisorTeacherById(Long id){
        return advisorTeacherRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE,id)));
    }

    public AdvisoryTeacher getAdvisorTeacherByUsername(String username){
        return advisorTeacherRepository.findByTeacher_UsernameEquals(username)
                .orElseThrow(()->new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE_USERNAME,username)));

    }

}
