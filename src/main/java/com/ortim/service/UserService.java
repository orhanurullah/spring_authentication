package com.ortim.service;


import com.ortim.ServiceInterface.UserServiceInterface;
import com.ortim.component.MessageService;
import com.ortim.core.results.*;
import com.ortim.core.utils.EntityConstants;
import com.ortim.dto.response.UserResponse;
import com.ortim.model.Role;
import com.ortim.model.User;
import com.ortim.repository.RoleRepository;
import com.ortim.repository.UserRepository;
import com.ortim.security.auth.SecurityManager;
import com.ortim.security.request.RegisterRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;

    private final MessageService messageService;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    @Value("${root.email}")
    private String root;

    @Transactional
//    @CacheEvict(value = {"usersCache", "userByIdCache"}, allEntries = true)
    @Override
    public DataResult<UserResponse> createUser(RegisterRequest registerRequest) {
        String trimmedEmail = registerRequest.getEmail().trim();
        Optional<User> existingUserOpt = userRepository.findByEmail(trimmedEmail);
        if(existingUserOpt.isPresent()){
            User existingUser = existingUserOpt.get();
            if(existingUser.getIsDeleted()){
                existingUser.setIsDeleted(false);
                existingUser.setIsActive(true);
                userRepository.save(existingUser);
                return new SuccessDataResult<>(
                        convertToUserResponse(existingUser),
                        messageService.getMessage("user.reactivated.message", new Object[]{existingUser.getEmail()})
                );
            }
            return new ErrorDataResult<>(
                    null,
                    messageService.getMessage("user.email.already_register", null)
            );
        }
        try {
            String newPassword = passwordEncoder.encode(registerRequest.getPassword());
            Role role = roleRepository.findRoleByNameEqualsIgnoreCase(EntityConstants.baseRoleName);
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            User user = User.builder()
                    .email(trimmedEmail)
                    .username(trimmedEmail)
                    .password(newPassword)
                    .roles(new HashSet<>())
                    .isActive(true)
                    .isDeleted(false)
                    .roles(roles)
                    .build();
            User savedUser = userRepository.save(user);
            return new SuccessDataResult<>(
                    convertToUserResponse(savedUser),
                    messageService.getMessage("user.saved.successfully", new Object[]{savedUser.getEmail()})
            );
        } catch (Exception e) {
            return new ErrorDataResult<>(
                    null,
                    messageService.getMessage("user.saved.error", new Object[]{registerRequest.getEmail()})
            );
        }
    }

//    @Cacheable(value = "userByEmailCache", key = "#email")
    @Override
    public DataResult<UserResponse> getUser(String email){
        if(email.equalsIgnoreCase(root) && !Objects.requireNonNull(SecurityManager.getCurrentUser()).getEmail().equals(root)){
            return new ErrorDataResult<>(
                    messageService.getMessage("user.rootEmailQuery.message", null)
            );
        }
        try{
            var user = userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
            return new SuccessDataResult<>(
                    convertToUserResponse(user),
                    messageService.getMessage("success.data.message", null)
            );
        }catch (EntityNotFoundException e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.not_found", new Object[]{email})
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

//    @Cacheable(value = "usersCache")
    @Override
    public DataResult<List<UserResponse>> getUsers(){
        try{
            var users = userRepository.allActiveUsers().stream().filter(u -> !u.getEmail().equals(root)).toList();
            if(users.isEmpty()){
                return new ErrorDataResult<>(
                        messageService.getMessage("error.not_found", null)
                );
            }
            var dtoUsers = users.stream().map(UserService::convertToUserResponse).toList();
            return new SuccessDataResult<>(
                    dtoUsers,
                    messageService.getMessage("success.data.message", null)
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public DataResult<User> findUserByUsername(String username) {
        try{
            User user = userRepository.findByEmail(username).orElseThrow(EntityNotFoundException::new);
            return new SuccessDataResult<>(
                    user,
                    messageService.getMessage("success.data.message", null)
            );
        }catch (EntityNotFoundException e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.not_found", new Object[]{username})
            );
        } catch (Exception e) {
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }


//    @Cacheable(value = "userByIdCache", key = "#id")
    @Override
    public DataResult<UserResponse> findById(Long id) {
        try{
            var user = this.userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            if(user.getEmail().equalsIgnoreCase(root)){
                return new ErrorDataResult<>(
                        messageService.getMessage("user.rootEmailQuery.message", null)
                );
            }
            return new SuccessDataResult<>(
                    convertToUserResponse(user),
                    messageService.getMessage("success.data.message", null)
            );
        }catch (EntityNotFoundException e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.not_found", new Object[]{id})
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Transactional
    @Override
    public Result changeUserPassword(String password, String newPassword) {
        try{
            User user = userRepository.findByEmail(Objects.requireNonNull(SecurityManager.getCurrentUser()).getEmail()).orElse(null);
            if(user == null){
                return new ErrorResult(
                        messageService.getMessage("user.not.found.message", null)
                );
            }
            if(!user.getPassword().equalsIgnoreCase(passwordEncoder.encode(password))){
                return new ErrorResult(
                        messageService.getMessage("user.password.is.false.message", new  Object[]{user.getEmail()})
                );
            }
            String encoderPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encoderPassword);
            userRepository.save(user);
            return new SuccessResult(
                    messageService.getMessage("user.change.password.true", null)
            );
        }catch (Exception e){
            return new ErrorResult(
                    messageService.getMessage("user.change.password.false", null)
            );
        }
    }

    @Transactional
    @Override
    public Result updateUser(RegisterRequest userRequest) {
        try{
            User user = Objects.requireNonNull(SecurityManager.getCurrentUser());
            userRepository.save(user);
            return new SuccessResult(
                    messageService.getMessage("user.update.true", null)
            );
        }catch (Exception e){
            return new ErrorResult(
                    messageService.getMessage("user.update.false", null)
            );
        }
    }

    @Transactional
    @Override
    public Result deleteOwnUser() {
        try{
            var user = userRepository.findByEmailAddress(Objects.requireNonNull(SecurityManager.getCurrentUser()).getEmail()).orElseThrow(EntityNotFoundException::new);
            if (user.getRoles().stream().anyMatch(e -> e.getName().equalsIgnoreCase(EntityConstants.primaryRoleName))){
                return new ErrorResult(
                        messageService.getMessage("user.delete.hasRoleRoot.false", null)
                );
            }
            if(user.getRoles().stream().anyMatch(e -> e.getName().equalsIgnoreCase(EntityConstants.secondaryRoleName))){
                return new ErrorResult(
                        messageService.getMessage("user.delete.hasRoleAdmin.false", null)
                );
            }
            user.setIsActive(false);
            user.setIsDeleted(true);
            return new SuccessResult(
                    messageService.getMessage("user.delete.hasRoleOnlyUser.true", null)
            );
        }catch (Exception e){
            return new ErrorResult(messageService.getMessage("error.message", null));
        }
    }

    @Transactional
//    @CacheEvict(value = {"usersCache", "userByIdCache", "userByEmailCache"}, key = "#id")
    @Override
    public Result deleteUser(Long id) {
        try{
            var user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            if(user.getEmail().equals(root)){
                return new ErrorResult(
                        messageService.getMessage("user.delete.hasRoleRoot.false", null)
                );
            }
            this.userRepository.changeUserIsActive(user.getId(), false);
            this.userRepository.changeUserIsDeleted(user.getId(), true);
            return new SuccessResult(
                    messageService.getMessage("admin.delete.user.true", null)
            );
        }catch (EntityNotFoundException e){
            return new ErrorResult(
                    messageService.getMessage("error.not_found", new Object[]{id})
            );
        }catch (Exception e){
            return new ErrorResult(
                    messageService.getMessage("admin.delete.user.false", null)
            );
        }
    }

    @Transactional
    @Override
    public Result addRoleToUser(Long userId, Long roleId) {
        try{
            User user = userRepository.findById(userId)
                    .orElse(null);
            if(user == null){
                return new ErrorResult(
                        messageService.getMessage("error.not_found", new Object[]{userId})
                );
            }
            Role role = roleRepository.findById(roleId)
                    .orElse(null);
            if(role == null){
                return new ErrorResult(
                        messageService.getMessage("error.not_found", new Object[]{roleId})
                );
            }
            if(user.getRoles().contains(role)){
                return new ErrorResult(
                        messageService.getMessage("role.reAssigned.false.message", new Object[]{role.getName()})
                );
            }
            if (role.getName().equalsIgnoreCase(EntityConstants.primaryRoleName)) {
                return new ErrorResult(
                        messageService.getMessage("role.assigned.false.message", new Object[]{role.getName()})
                );
            }
            user.getRoles().add(role);
            userRepository.save(user);
            return new SuccessResult(
                    messageService.getMessage("role.assigned.true.message", new Object[]{role.getName()})
            );
        }catch (Exception e) {
            return new ErrorResult(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Transactional
    @Override
    public Result deleteRoleFromUser(Long userId, Long roleId) {
        try{
            var user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
            if(user.getEmail().equalsIgnoreCase(root)){
                return new ErrorResult(
                        messageService.getMessage("admin.never.delete.role.fromAdmin.message", new Object[]{user.getEmail()})
                );
            }
            var role = roleRepository.findById(roleId).orElse(null);
            if(role == null){
                return new ErrorResult(
                        messageService.getMessage("error.not_found", new Object[]{roleId})
                );
            }
            var roles = user.getRoles();
            if(role.getName().equalsIgnoreCase(EntityConstants.baseRoleName)){
                return new ErrorResult(
                        messageService.getMessage("admin.delete.roleUser.from.user.false", null)
                );
            }
            if(roles.stream().noneMatch(e -> e.getName().equals(role.getName()))){
                return new ErrorResult(
                        messageService.getMessage("user.hasNot.role.message", new Object[]{role.getName()})
                );
            }
            roles.remove(role);
            userRepository.save(user);
            return new SuccessResult(
                    messageService.getMessage("admin.delete.role.success.message", new Object[]{role.getName()})
            );
        }catch (EntityNotFoundException e){
            return new ErrorResult(
                    messageService.getMessage("error.not_found", new Object[]{userId})
            );
        }catch (Exception e){
            return new ErrorResult(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public DataResult<List<? extends GrantedAuthority>> userAuthorities(Long id){
        try{
            User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            if(user.getEmail().equalsIgnoreCase(root) && !Objects.requireNonNull(SecurityManager.getCurrentUser()).getEmail().equalsIgnoreCase(root)){
                return new ErrorDataResult<>(
                        messageService.getMessage("user.rootEmailQuery.message", null)
                );
            }
            var roles = user.getRoles().stream().map(role ->
                    new SimpleGrantedAuthority("ROLE_"+ role.getName())).toList();
            return new SuccessDataResult<>(
                    roles,
                    messageService.getMessage("success.data.message", null)
            );
        }catch(EntityNotFoundException e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.not_found", new Object[]{id})
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public DataResult<List<? extends GrantedAuthority>> getActiveUserRoles() {
        try{
            String email = Objects.requireNonNull(SecurityManager.getCurrentUser()).getEmail();
            var roles = userRepository.findByEmailAddress(email).orElseThrow(EntityNotFoundException::new).getRoles().stream().map(role ->
                    new SimpleGrantedAuthority("ROLE_"+ role.getName())).toList();
            return new SuccessDataResult<>(
                    roles,
                    messageService.getMessage("success.data.message", null)
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public DataResult<UserResponse> getCurrentUser() {
        try{
            User user = SecurityManager.getCurrentUser();
            if(user != null){
                return new SuccessDataResult<>(
                        convertToUserResponse(user),
                        messageService.getMessage("token.validateUser.message", new Object[]{user.getEmail()})
                );
            }else{
                return new ErrorDataResult<>(
                        messageService.getMessage("token.notValidateUser.message", null)
                );
            }

        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    private static UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .name(user.getName())
                .lastName(user.getLastName())
                .socialAccounts(user.getSocialAccounts())
                .isActive(user.getIsActive())
                .isDeleted(user.getIsDeleted())
                .build();
    }

    private User convertToUserRequest(RegisterRequest userRequest) {
        return User.builder()
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();
    }

    public String generateUniqueUsername(String email) {
        String baseUsername = email.split("@")[0];
        String username = baseUsername;
        int counter = 1;

        while (userRepository.existsByUsername(username)) {
            username = baseUsername + counter;
            counter++;
        }

        return username;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElse(null);
        if(user == null){
            throw new UsernameNotFoundException(
                    messageService.getMessage("invalid.user.login.data", null)
            );
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), user.getAuthorities());
    }
}
