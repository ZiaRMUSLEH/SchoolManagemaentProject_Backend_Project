package com.project.schoolmanagment.service.user;

import com.project.schoolmanagment.entity.concretes.businnes.LessonProgram;
import com.project.schoolmanagment.entity.concretes.user.AdvisoryTeacher;
import com.project.schoolmanagment.entity.concretes.user.Student;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.mappers.user.StudentMapper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.payload.request.business.ChooseLessonProgramWithIdRequest;
import com.project.schoolmanagment.payload.request.user.StudentRequest;
import com.project.schoolmanagment.payload.response.message.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.StudentResponse;
import com.project.schoolmanagment.repository.user.StudentRepository;
import com.project.schoolmanagment.service.business.LessonProgramService;
import com.project.schoolmanagment.service.helper.PageableHelper;
import com.project.schoolmanagment.service.validator.DateTimeValidator;
import com.project.schoolmanagment.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final AdvisorTeacherService advisorTeacherService;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final PageableHelper pageableHelper;
    private final PasswordEncoder passwordEncoder;
    private final StudentMapper studentMapper;
    private final UserRoleService userRoleService;
    private final LessonProgramService lessonProgramService;
    private final DateTimeValidator dateTimeValidator;


    public ResponseMessage<StudentResponse> saveStudent(StudentRequest studentRequest) {
        AdvisoryTeacher advisoryTeacher = advisorTeacherService.getAdvisorTeacherById(studentRequest.getAdvisorTeacherId());
        uniquePropertyValidator.checkDuplicate(studentRequest.getUsername(),
                studentRequest.getSsn(),
                studentRequest.getPhoneNumber(),
                studentRequest.getEmail());

        Student student = studentMapper.mapStudentRequestToStudent(studentRequest);
        student.setAdvisoryTeacher(advisoryTeacher);
        student.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
        student.setUserRole(userRoleService.getUserRole(RoleType.STUDENT));
        student.setActive(true);
        student.setStudentNumber(getLastNumber());
        Student savedStudent = studentRepository.save(student);
        return ResponseMessage.<StudentResponse>builder()
                .object(studentMapper.mapStudentToStudentResponse(savedStudent))
                .message(SuccessMessages.STUDENT_SAVE)
                .httpStatus(HttpStatus.CREATED)
                .build();


    }


    private int getLastNumber(){
        if(!studentRepository.findStudent()){
            return 1000;
        }
        return studentRepository.getMaxStudentNumber()+1;
    }

    public Student isStudentExist(Long id){
        return studentRepository
                .findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE,id)));

    }

    public ResponseMessage changeStatus(Long id, boolean status) {
        Student student = isStudentExist(id);
        student.setActive(status);
        studentRepository.save(student);
        return ResponseMessage.builder()
                .message("Student is " + (status ? "active" : "passive"))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<StudentResponse> updateStudent(Long id, StudentRequest studentRequest) {
        Student student = isStudentExist(id);
        AdvisoryTeacher advisoryTeacher = advisorTeacherService.getAdvisorTeacherById(studentRequest.getAdvisorTeacherId());
        uniquePropertyValidator.checkUniqueProperties(student,studentRequest);

        Student studentForUpdate = studentMapper.mapStudentRequestToUpdatedStudent(studentRequest, id);
        studentForUpdate.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
        studentForUpdate.setAdvisoryTeacher(advisoryTeacher);
        studentForUpdate.setStudentNumber(student.getStudentNumber());
        studentForUpdate.setUserRole(userRoleService.getUserRole(RoleType.STUDENT));
        studentForUpdate.setActive(true);
        Student savedStudent = studentRepository.save(studentForUpdate);
        return ResponseMessage.<StudentResponse>builder()
                .object(studentMapper.mapStudentToStudentResponse(savedStudent))
                .httpStatus(HttpStatus.OK)
                .message(SuccessMessages.STUDENT_UPDATE)
                .build();


    }

    public List<StudentResponse> getStudentsByName(String studentName) {

        return studentRepository.getStudentsByNameContaining(studentName)
                .stream()
                .map(studentMapper::mapStudentToStudentResponse)
                .collect(Collectors.toList());

    }

    public List<StudentResponse> getAllByAdvisoryTeacherUserName(HttpServletRequest httpServletRequest) {
        String userName = (String) httpServletRequest.getAttribute("username");
        return studentRepository.getStudentByAdvisoryTeacher_UserName(userName)
                .stream()
                .map(studentMapper::mapStudentToStudentResponse)
                .collect(Collectors.toList());
    }


    public ResponseMessage<StudentResponse> chooseLesson(HttpServletRequest httpServletRequest, ChooseLessonProgramWithIdRequest chooseLessonProgramWithIdRequest) {
        String userName = (String) httpServletRequest.getAttribute("username");

        Student student = isStudentExistBYUserName(userName);

        Set<LessonProgram> lessonPrograms = lessonProgramService.getLessonProgramById(chooseLessonProgramWithIdRequest.getLessonProgramId());

        Set<LessonProgram> existingLessonPrograms = student.getLessonsProgramList();

        dateTimeValidator.checkLessonPrograms(lessonPrograms,existingLessonPrograms);

        existingLessonPrograms.addAll(lessonPrograms);

        student.setLessonsProgramList(existingLessonPrograms);

        Student upsdatedStudent = studentRepository.save(student);

        return ResponseMessage.<StudentResponse>builder()
                .message(SuccessMessages.LESSON_PROGRAM_ADD_TO_STUDENT)
                .object(studentMapper.mapStudentToStudentResponse(upsdatedStudent))
                .httpStatus(HttpStatus.OK)
                .build();




    }


    public Student isStudentExistBYUserName(String  username){
        Student student = studentRepository.findByUsernameEquals(username);
        if (student==null){
            throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE_USERNAME,username));
        }
        return student;
    }


    @Transactional
    public ResponseMessage deleteStudentById(Long id) {
        isStudentExist(id);
        studentRepository.deleteById1(id);
        return ResponseMessage.builder()
                .message(SuccessMessages.STUDENT_DELETE)
                .httpStatus(HttpStatus.OK)
                .build();


    }
}
