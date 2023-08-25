package com.project.schoolmanagment.service.user;

import com.project.schoolmanagment.payload.request.user.DeanRequest;
import com.project.schoolmanagment.payload.response.message.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.DeanResponse;
import com.project.schoolmanagment.repository.user.DeanRepository;
import com.project.schoolmanagment.service.helper.PageableHelper;
import com.project.schoolmanagment.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeanService {

    private final DeanRepository deanRepository;

    private final UserRoleService userRoleService;

    private final UniquePropertyValidator uniquePropertyValidator;

    private final PageableHelper pageableHelper;

    public ResponseMessage<DeanResponse> saveDean(DeanRequest deanRequest) {

        uniquePropertyValidator.checkDuplicate(deanRequest.getUsername(),
                deanRequest.getSsn(),deanRequest.getPhoneNumber());


return null;

    }
}
