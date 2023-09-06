package com.project.schoolmanagment.service.business;

import com.project.schoolmanagment.entity.concretes.businnes.Meet;
import com.project.schoolmanagment.entity.concretes.user.AdvisoryTeacher;
import com.project.schoolmanagment.entity.concretes.user.Student;
import com.project.schoolmanagment.exception.ConflictException;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.mappers.business.MeetMappers;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.payload.request.business.MeetRequest;
import com.project.schoolmanagment.payload.response.business.MeetResponse;
import com.project.schoolmanagment.payload.response.message.ResponseMessage;
import com.project.schoolmanagment.repository.business.MeetRepository;
import com.project.schoolmanagment.service.helper.PageableHelper;
import com.project.schoolmanagment.service.user.AdvisorTeacherService;
import com.project.schoolmanagment.service.user.StudentService;
import com.project.schoolmanagment.service.validator.DateTimeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetService {

    private final MeetRepository meetRepository;
    private final AdvisorTeacherService advisorTeacherService;
    private final StudentService studentService;
    private final PageableHelper pageableHelper;
    private final DateTimeValidator dateTimeValidator;
    private final MeetMappers meetMappers;
    public ResponseMessage<MeetResponse> saveMeet(MeetRequest meetRequest, HttpServletRequest httpServletRequest) {

        String username = (String) httpServletRequest.getAttribute("username");

        AdvisoryTeacher advisoryTeacher = advisorTeacherService.getAdvisorTeacherByUsername(username);

        dateTimeValidator.checkTimeWithException(meetRequest.getStartTime(),meetRequest.getStopTime());

        for(Long studentId: meetRequest.getStudentIds()){
            studentService.isStudentExist(studentId);
            checkMeetConflict(studentId,
                    meetRequest.getDate(),
                    meetRequest.getStartTime(),
                    meetRequest.getStopTime());
        }
        List<Student> students = studentService.getStudentListById(meetRequest.getStudentIds());
        Meet meet = meetMappers.mapMeetRequestToMeet(meetRequest);
        meet.setStudentList(students);
        meet.setAdvisoryTeacher(advisoryTeacher);
        Meet savedMeet = meetRepository.save(meet);
        return ResponseMessage.<MeetResponse>builder()
                .message(SuccessMessages.MEET_SAVE)
                .object(meetMappers.mapMeetToMeetResponse(savedMeet))
                .httpStatus(HttpStatus.OK)
                .build();
    }
    private void checkMeetConflict(Long studentId, LocalDate date, LocalTime startTime, LocalTime stopTime){
        List<Meet> meets = meetRepository.findByStudentList_IdEquals(studentId);
        for(Meet meet: meets){
            LocalTime existingStartTime = meet.getStartTime();
            LocalTime existingStopTime = meet.getStopTime();

            if(meet.getDate().equals(date) && (
                    (startTime.isAfter(existingStartTime) && startTime.isBefore(existingStopTime)) ||
                            stopTime.isAfter(existingStartTime) && stopTime.isBefore(existingStopTime) ||
                            startTime.isBefore(existingStartTime) && startTime.isAfter(existingStopTime) ||
                            startTime.equals(existingStartTime) && stopTime.equals(existingStopTime))){
                throw new ConflictException(ErrorMessages.MEET_HOURS_CONFLICT);
            }

        }
    }


    public ResponseMessage<MeetResponse> updateMeet(MeetRequest meetRequest, Long meetId) {
        Meet meet = isMeetExistById(meetId);
        dateTimeValidator.checkTimeWithException(meetRequest.getStartTime(),meetRequest.getStopTime());

        if(!(meet.getDate().equals(meetRequest.getDate()) &&
                meet.getStartTime().equals(meetRequest.getStartTime()) &&
                meet.getStopTime().equals(meetRequest.getStopTime()))){
            for(Long id:meetRequest.getStudentIds()){
                checkMeetConflict(id,meetRequest.getDate(),meetRequest.getStartTime(),meetRequest.getStopTime());
            }
        }

        List<Student> students = studentService.getStudentListById(meetRequest.getStudentIds());
        Meet updateMeet = meetMappers.mapMeetUpdateRequestToMeet(meetRequest,meetId);
        updateMeet.setStudentList(students);
        updateMeet.setAdvisoryTeacher(meet.getAdvisoryTeacher());
        Meet savedMeet = meetRepository.save(updateMeet);
        return ResponseMessage.<MeetResponse>builder()
                .httpStatus(HttpStatus.OK)
                .object(meetMappers.mapMeetToMeetResponse(savedMeet))
                .build();

    }

    public Meet isMeetExistById(Long id){
        return meetRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.MEET_NOT_FOUND_MESSAGE,id)));
    }


    public List<MeetResponse> getAllMeetByAdvisorTeacherAsList(HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        AdvisoryTeacher advisoryTeacher = advisorTeacherService.getAdvisorTeacherByUsername(username);
        return meetRepository.getByAdvisoryTeacher_IdEquals(advisoryTeacher.getId())
                .stream().map(meetMappers::mapMeetToMeetResponse).collect(Collectors.toList());
    }

    public List<MeetResponse> getAllMeetByStudentAsList(HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        Student student = studentService.isStudentExistBYUserName(username);
        return meetRepository.findByStudentList_IdEquals(student.getId())
                .stream().map(meetMappers::mapMeetToMeetResponse).collect(Collectors.toList());

    }
}
