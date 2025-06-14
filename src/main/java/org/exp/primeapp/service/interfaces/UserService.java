package org.exp.primeapp.service.interfaces;

import org.exp.primeapp.models.dto.request.UserReq;
import org.exp.primeapp.models.dto.request.UserUpdateReq;
import org.exp.primeapp.models.dto.responce.UserRes;

import java.util.List;

public interface UserService {
    UserRes createUser(UserReq userReq);

    List<UserRes> getAllUsers();

    UserRes getByUserId(Long userId);

    UserRes updateUser(Long user_id, UserUpdateReq userReq);

    void updateUser_Active(Long userId);
}
