package com.project.schoolmanagment.service.validator;


import com.project.schoolmanagment.exception.BadRequestException;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class DateTimeValidator {

	public void checkTimeWithException(LocalTime start,LocalTime end){
		if(start.isAfter(end) || start.equals(end)){
			throw new BadRequestException(ErrorMessages.TIME_NOT_VALID_MESSAGE);
		}
	}
}
