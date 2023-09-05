package com.project.schoolmanagment.service.user;

import com.project.schoolmanagment.entity.concretes.businnes.LessonProgram;
import com.project.schoolmanagment.entity.concretes.user.Teacher;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.mappers.user.TeacherMapper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.payload.request.user.ChooseLessonTeacherRequest;
import com.project.schoolmanagment.payload.request.user.TeacherRequest;
import com.project.schoolmanagment.payload.response.message.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.TeacherResponse;
import com.project.schoolmanagment.repository.user.TeacherRepository;
import com.project.schoolmanagment.service.business.LessonProgramService;
import com.project.schoolmanagment.service.business.LessonService;
import com.project.schoolmanagment.service.validator.DateTimeValidator;
import com.project.schoolmanagment.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final DateTimeValidator dateTimeValidator;

    private Teacher isTeacherExistById(Long id){
        return teacherRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE,id)));
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

    private Teacher isTeacherExist(Long id){
        return teacherRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE,id)));
    }

    public ResponseMessage<TeacherResponse> updateTeacher(TeacherRequest teacherRequest, Long userId) {
        Teacher teacher = isTeacherExist(userId);
        Set<LessonProgram> lessonPrograms = lessonProgramService.getLessonProgramById(teacherRequest.getLessonsIdList());
        uniquePropertyValidator.checkUniqueProperties(teacher, teacherRequest);
        Teacher updatedTeacher = teacherMapper.mapTeacherRequestToUpdatedTeacher(teacherRequest, userId);
        updatedTeacher.setPassword(passwordEncoder.encode(teacherRequest.getPassword()));
        updatedTeacher.setLessonsProgramList(lessonPrograms);
        updatedTeacher.setUserRole(userRoleService.getUserRole(RoleType.TEACHER));
        Teacher savedTeacher = teacherRepository.save(updatedTeacher);

        advisorTeacherService.updateAdvisoryTeacher(teacherRequest.getIsAdvisorTeacher(), savedTeacher);

        return ResponseMessage.<TeacherResponse>builder()
                .object(teacherMapper.mapTeacherToTeacherResponse(savedTeacher))
                .message(SuccessMessages.TEACHER_UPDATE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public List<TeacherResponse> getTeacherByName(String teacherName) {
        return teacherRepository.getTeachersByNameContaining(teacherName)
                .stream()
                .map(teacherMapper::mapTeacherToTeacherResponse)
                .collect(Collectors.toList());
    }

    public List<TeacherResponse> getAllTeacher() {
        return teacherRepository.findAll()
                .stream()
                .map(teacherMapper::mapTeacherToTeacherResponse)
                .collect(Collectors.toList());
    }


    public ResponseMessage<TeacherResponse> chooseLesson(ChooseLessonTeacherRequest chooseLessonTeacherRequest) {

        Teacher teacher = isTeacherExist(chooseLessonTeacherRequest.getTeacherId());

        Set<LessonProgram> lessonPrograms = lessonProgramService.getLessonProgramById(chooseLessonTeacherRequest.getLessonProgramId());
        Set<LessonProgram> teachersLessonProgram = teacher.getLessonsProgramList();

        dateTimeValidator.checkLessonPrograms(teachersLessonProgram, lessonPrograms);

        teachersLessonProgram.addAll(lessonPrograms);
        teacher.setLessonsProgramList(teachersLessonProgram);;

        Teacher updatedTeacher = teacherRepository.save(teacher);
        return ResponseMessage.<TeacherResponse>builder()
                .message(SuccessMessages.TEACHER_UPDATE)
                .object(teacherMapper.mapTeacherToTeacherResponse(updatedTeacher))
                .build();

    }

    public Teacher getTeacherByUserName (String username){
        if(!teacherRepository.existsByUsername(username)){
            throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE_USERNAME,username));
        }
        return teacherRepository.getTeachersByUsername(username);
    }
}
