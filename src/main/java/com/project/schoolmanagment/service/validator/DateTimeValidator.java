package com.project.schoolmanagment.service.validator;


import com.project.schoolmanagment.entity.concretes.businnes.LessonProgram;
import com.project.schoolmanagment.exception.BadRequestException;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class DateTimeValidator {

	public void checkTimeWithException(LocalTime start,LocalTime end){
		if(start.isAfter(end) || start.equals(end)){
			throw new BadRequestException(ErrorMessages.TIME_NOT_VALID_MESSAGE);
		}
	}

	public void checkLessonPrograms(Set<LessonProgram> existLessonProgram,Set<LessonProgram> lessonProgramRequest ){
		if(existLessonProgram.isEmpty() && lessonProgramRequest.size()>1){
			checkTimeAndDayConflict(lessonProgramRequest);
		}else {
			checkTimeAndDayConflict(lessonProgramRequest);
			checkConflictWithExistingLessonProgramSet(existLessonProgram, lessonProgramRequest);
		}
	}

	private void checkTimeAndDayConflict(Set<LessonProgram> lessonPrograms){
		Set<String> uniqueLessonProgramsKey = new HashSet<>();
		for(LessonProgram lessonProgram:lessonPrograms){
			String lessomProgramKey = lessonProgram.getDay().name()+lessonProgram.getStartTime();
			if(uniqueLessonProgramsKey.contains(uniqueLessonProgramsKey)){
				throw new BadRequestException(ErrorMessages.LESSON_PROGRAM_EXIST_MESSAGE);
			}
			uniqueLessonProgramsKey.add(lessomProgramKey);
		}
	}

	private void checkConflictWithExistingLessonProgramSet(Set<LessonProgram> existingLessonProgram, Set<LessonProgram> requestedLessonProgram ){

		for(LessonProgram lessonProgramData : requestedLessonProgram){
			if(existingLessonProgram.stream().anyMatch(
					lessonProgram -> lessonProgram.getStartTime().equals(lessonProgramData.getStartTime())
					&&
							lessonProgram.getDay().name().equals(lessonProgramData.getDay().name())
			)){
				throw new BadRequestException(ErrorMessages.LESSON_PROGRAM_EXIST_MESSAGE);
			}
		}
	}



}
