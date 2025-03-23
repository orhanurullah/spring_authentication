package com.ortim.dto.response;

import com.ortim.model.Role;
import com.ortim.model.UserSocialAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;

    private String email;

    private String username;

    private String name;

    private String lastName;

    private List<UserSocialAccount> socialAccounts;

    private Set<Role> roles;

    private Boolean isActive;

    private Boolean isDeleted;

}
