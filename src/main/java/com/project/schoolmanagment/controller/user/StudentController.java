package com.project.schoolmanagment.controller.user;

import com.project.schoolmanagment.payload.request.business.ChooseLessonProgramWithIdRequest;
import com.project.schoolmanagment.payload.request.user.StudentRequest;
import com.project.schoolmanagment.payload.response.message.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.StudentResponse;
import com.project.schoolmanagment.service.user.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<StudentResponse> saveStudent(@Valid @RequestBody StudentRequest studentRequest){
        return studentService.saveStudent(studentRequest);
    }


    @GetMapping("/changeStatus")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage changeStatus(@RequestParam Long id, @RequestParam boolean status){
        return studentService.changeStatus(id,status);
    }

    @PatchMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<StudentResponse> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentRequest studentRequest){
        return studentService.updateStudent(id, studentRequest);
    }

    @GetMapping("/getStudentsByName")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public List<StudentResponse> getStudentsByName (@RequestParam(name = "name") String studentName){
        return studentService.getStudentsByName(studentName);
    }

    @GetMapping("/getAllByAdvisoryTeacherUserName")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public List<StudentResponse> getAllByAdvisoryTeacherUserName (HttpServletRequest httpServletRequest){
        return studentService.getAllByAdvisoryTeacherUserName(httpServletRequest);
    }


    @PostMapping("/chooseLesson")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public ResponseMessage<StudentResponse> chooseLesson(HttpServletRequest httpServletRequest,
                                                         @RequestBody @Valid ChooseLessonProgramWithIdRequest chooseLessonProgramWithIdRequest){

        return studentService.chooseLesson(httpServletRequest, chooseLessonProgramWithIdRequest);

    }

}
