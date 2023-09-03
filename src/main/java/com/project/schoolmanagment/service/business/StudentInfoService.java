package com.project.schoolmanagment.service.business;

import com.project.schoolmanagment.entity.concretes.businnes.EducationTerm;
import com.project.schoolmanagment.entity.concretes.businnes.Lesson;
import com.project.schoolmanagment.entity.concretes.user.Student;
import com.project.schoolmanagment.payload.request.business.StudentInfoRequest;
import com.project.schoolmanagment.payload.response.business.StudentInfoResponse;
import com.project.schoolmanagment.payload.response.message.ResponseMessage;
import com.project.schoolmanagment.repository.business.StudentInfoRepository;
import com.project.schoolmanagment.service.user.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class StudentInfoService {

    private final StudentInfoRepository studentInfoRepository;
    private final EducationTermService educationTermService;
    private final LessonService lessonService;
    private final StudentService studentService;


    public ResponseMessage<StudentInfoResponse> saveStudentInfo(HttpServletRequest httpServletRequest, StudentInfoRequest studentInfoRequest) {

        String teacherUserName = (String) httpServletRequest.getAttribute("username");
        Student student =studentService.isStudentExist(studentInfoRequest.getStudentId());
        EducationTerm educationTerm = educationTermService.isEducationTermExist(studentInfoRequest.getEducationTermId());
        Lesson lesson = lessonService.isLessonExistById(studentInfoRequest.getLessonId());
        return null;

    }
}
