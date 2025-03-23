package com.ortim.repository;

import com.ortim.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileEntityRepository extends JpaRepository<FileEntity, Long> {

    Optional<FileEntity> findByPathUniqueName(String pathUniqueName);
}
