package com.ortim.ServiceInterface;

import com.ortim.core.results.DataResult;
import com.ortim.core.results.Result;
import com.ortim.dto.response.UserResponse;
import com.ortim.model.User;
import com.ortim.security.request.RegisterRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserServiceInterface extends UserDetailsService {

    DataResult<UserResponse> createUser(RegisterRequest userRequest);

    DataResult<UserResponse> getUser(String email);

    DataResult<List<UserResponse>> getUsers();

    DataResult<User> findUserByUsername(String username);

    DataResult<UserResponse> findById(Long id);

    Result changeUserPassword(String password, String newPassword);

    DataResult<List<? extends GrantedAuthority>> getActiveUserRoles();

    DataResult<List<? extends GrantedAuthority>> userAuthorities(Long id);

    DataResult<UserResponse> getCurrentUser();

    Result updateUser(RegisterRequest userRequest);

    Result deleteOwnUser();

    Result deleteUser(Long id);

    Result addRoleToUser(Long userId, Long roleId);

    Result deleteRoleFromUser(Long userId, Long roleId);

}
