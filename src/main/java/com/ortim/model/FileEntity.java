package com.ortim.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;
import java.util.UUID;

@Entity
@Table(name = "files")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity extends BaseModel {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name="path_name", nullable = false, unique = true)
    private String pathUniqueName;

    @Column(name="file_name", nullable = false, unique = true)
    @Size(min = 3, max = 255)
    private String fileName;

    @Column(name="file_type", nullable = false)
    @Size(min = 3, max = 255)
    private String fileType;

    @Lob
    @Column(name = "data", nullable = false)
    private byte[] data;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @PrePersist
    public void generatePathUniqueName() {
        this.pathUniqueName = UUID.randomUUID().toString();
    }
}
