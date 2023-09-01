package com.project.schoolmanagment.service.user;

import com.project.schoolmanagment.entity.concretes.businnes.LessonProgram;
import com.project.schoolmanagment.entity.concretes.user.Teacher;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.mappers.user.TeacherMapper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.payload.request.user.TeacherRequest;
import com.project.schoolmanagment.payload.response.message.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.TeacherResponse;
import com.project.schoolmanagment.repository.user.TeacherRepository;
import com.project.schoolmanagment.service.business.LessonProgramService;
import com.project.schoolmanagment.service.business.LessonService;
import com.project.schoolmanagment.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final LessonProgramService lessonProgramService;
    private final LessonService lessonService;
    private final TeacherMapper teacherMapper;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final AdvisorTeacherService advisorTeacherService;

    private Teacher isTeacherExistById(Long id){
        return teacherRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("dfsf"));
    }
    public ResponseMessage<TeacherResponse> saveTeacher(TeacherRequest teacherRequest) {

        Set<LessonProgram> lessonProgramSet = lessonProgramService.getLessonProgramById(teacherRequest.getLessonsIdList());

        uniquePropertyValidator.checkDuplicate(
                teacherRequest.getUsername(),
                teacherRequest.getSsn(),
                teacherRequest.getPhoneNumber(),
                teacherRequest.getEmail());
        Teacher teacher= teacherMapper.mapTeacherRequestToTeacher(teacherRequest);
        teacher.setUserRole(userRoleService.getUserRole(RoleType.TEACHER));
        teacher.setLessonsProgramList(lessonProgramSet);
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        Teacher savedTeacher = teacherRepository.save(teacher);

        if(teacherRequest.getIsAdvisorTeacher()){
            advisorTeacherService.saveAdvisoryTeacher(teacher);
        }

        return ResponseMessage.<TeacherResponse>builder()
                .message(SuccessMessages.TEACHER_SAVE)
                .httpStatus(HttpStatus.CREATED)
                .object(teacherMapper.mapTeacherToTeacherResponse(savedTeacher))
                .build();

    }
}
