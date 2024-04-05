package com.naz.libManager.mapper;

import com.naz.libManager.dto.UserRequest;
import com.naz.libManager.entity.User;
import com.naz.libManager.payload.PatronDetail;
import com.naz.libManager.payload.UserData;

public class UserMapper {
    public static User mapUserRequestToUser(User user, UserRequest userRequest){
        user.setFirstName(userRequest.lastName());
        user.setLastName(userRequest.lastName());
        user.setEmailAddress(userRequest.emailAddress());
        user.setPhoneNumber(userRequest.phoneNumber());
        return user;
    }

    public static UserData mapUserToUserData(UserData userData, User user){
        userData.setFirstName(user.getFirstName());
        userData.setLastName(user.getLastName());
        userData.setEmailAddress(user.getEmailAddress());
        userData.setPhoneNumber(user.getPhoneNumber());
        return userData;
    }

    public static PatronDetail mapUserToPatronDetail(PatronDetail patronDetail, User user){
        patronDetail.setFirstName(user.getFirstName());
        patronDetail.setLastName(user.getLastName());
        patronDetail.setEmailAddress(user.getEmailAddress());
        patronDetail.setPhoneNumber(user.getPhoneNumber());
        patronDetail.setBooksBorrowed(user.getBorrowedBooks());
        return patronDetail;
    }

}
