package com.project.schoolmanagment.controller.user;

import com.project.schoolmanagment.payload.request.user.ChooseLessonTeacherRequest;
import com.project.schoolmanagment.payload.request.user.TeacherRequest;
import com.project.schoolmanagment.payload.response.message.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.TeacherResponse;
import com.project.schoolmanagment.service.user.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<TeacherResponse>saveTeacher(@Valid @RequestBody TeacherRequest teacherRequest){
        return teacherService.saveTeacher(teacherRequest);
    }

    @PutMapping("/update/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<TeacherResponse> updateTeacher (@RequestBody TeacherRequest teacherRequest,
                                                           @PathVariable Long userId){
        return teacherService.updateTeacher(teacherRequest, userId);
    }

    @GetMapping("/getTeacherByName")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public List<TeacherResponse> getTeacherByName (@RequestParam(name = "name") String  teacherName ){
        return teacherService.getTeacherByName(teacherName);
    }


    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public List<TeacherResponse> getAllTeacher(){
        return teacherService.getAllTeacher();
    }


    @PostMapping("/chooseLesson")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<TeacherResponse> chooseLesson(@RequestBody @Valid ChooseLessonTeacherRequest chooseLessonTeacherRequest){
        return teacherService.chooseLesson(chooseLessonTeacherRequest);
    }


}
