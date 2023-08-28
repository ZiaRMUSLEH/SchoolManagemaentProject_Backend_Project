package com.project.schoolmanagment.payload.mappers.user;

import com.project.schoolmanagment.entity.concretes.user.Admin;
import com.project.schoolmanagment.payload.request.user.AdminRequest;
import com.project.schoolmanagment.payload.response.user.AdminResponse;
import org.springframework.stereotype.Component;

/**
 * mapper for Admin
 * @author musleh
 * @version 0.0.1
 */
@Component
public class AdminMapper {

    /**
     *
     * @param adminRequest
     * @return
     */
    public Admin mapAdminRequestToAdmin(AdminRequest adminRequest){
        return Admin.builder()
                .username(adminRequest.getUsername())
                .name(adminRequest.getName())
                .surname(adminRequest.getSurname())
                .password(adminRequest.getPassword())
                .ssn(adminRequest.getSsn())
                .birthDay(adminRequest.getBirthDay())
                .birthPlace(adminRequest.getBirthPlace())
                .phoneNumber(adminRequest.getPhoneNumber())
                .gender(adminRequest.getGender())
                .build();
    }

    /**
     *
     * @param admin
     * @return
     */

    public AdminResponse mapAdminToAdminResponse(Admin admin){
        return AdminResponse.builder()
                .userId(admin.getId())
                .username(admin.getUsername())
                .name(admin.getName())
                .surname(admin.getSurname())
                .phoneNumber(admin.getPhoneNumber())
                .gender(admin.getGender())
                .birthDay(admin.getBirthDay())
                .birthPlace(admin.getBirthPlace())
                .ssn(admin.getSsn())
                .build();
    }


}
