package com.ortim.repository;

import com.ortim.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String username);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    @Query("select u from User u where u.isDeleted = false")
    List<User> allActiveUsers();

    @Query("select u from User u where u.isDeleted = true")
    List<User> allDeletedUsers();

    @Query("select u from User u where u.email = :email")
    Optional<User> findByEmailAddress(String email);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("Update User user SET user.password = :password where user.email = :email")
    void changeUserPassword(@Param("password") String password, @Param("email") String email);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("Update User user SET user.isActive = :state where user.id = :id")
    void changeUserIsActive(@Param("id") Long id, @Param("state") boolean state);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("Update User user SET user.isDeleted = :state where user.id = :id")
    void changeUserIsDeleted(@Param("id") Long id, @Param("state") boolean state);

    List<User> findUserByIsDeletedTrue();

}
