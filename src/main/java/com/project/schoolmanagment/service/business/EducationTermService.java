package com.project.schoolmanagment.service.business;

import com.project.schoolmanagment.exception.ConflictException;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.mappers.business.EducationTermMapper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.request.business.EducationTermRequest;
import com.project.schoolmanagment.payload.response.business.EducationTermResponse;
import com.project.schoolmanagment.payload.response.message.ResponseMessage;
import com.project.schoolmanagment.repository.bussiness.EducationTermRepository;
import com.project.schoolmanagment.service.helper.PageableHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EducationTermService {

    private final EducationTermRepository educationTermRepository;

    private final PageableHelper pageableHelper;

    private final EducationTermMapper educationTermMapper;



    public ResponseMessage<EducationTermResponse>saveEducationTerm (EducationTermRequest educationTermRequest){

        validateEducationTermDates(educationTermRequest);

        return null;

    }

    private void validateEducationTermDates(EducationTermRequest educationTermRequest) {

        if (educationTermRepository.existsByTermAndYear(educationTermRequest.getTerm(), educationTermRequest.getStartDate().getYear())) {

            throw new ResourceNotFoundException(ErrorMessages.EDUCATION_TERM_IS_ALREADY_EXIST_BY_TERM_AND_YEAR_MESSAGE);
        }

    }

}
