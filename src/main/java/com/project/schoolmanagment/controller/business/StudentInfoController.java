package com.project.schoolmanagment.controller.business;

import com.project.schoolmanagment.payload.request.business.StudentInfoRequest;
import com.project.schoolmanagment.payload.response.business.StudentInfoResponse;
import com.project.schoolmanagment.payload.response.message.ResponseMessage;
import com.project.schoolmanagment.service.business.StudentInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/studentInfo")
@RequiredArgsConstructor
public class StudentInfoController {

    private StudentInfoService studentInfoService;


    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @PostMapping("/save")
    public ResponseMessage<StudentInfoResponse> saveStudentInfo(HttpServletRequest httpServletRequest,
                                                                @RequestBody @Valid StudentInfoRequest studentInfoRequest){
        return studentInfoService.saveStudentInfo(httpServletRequest, studentInfoRequest);


    }


}
