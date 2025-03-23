package com.ortim.core.fileSystem;

import com.ortim.core.results.Result;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    Result store(MultipartFile file, String fileNewName);

    Stream<Path> loadAll();

    Path load(String fileName);

    Resource loadAsResource(String fileName);

    boolean isImageFile(MultipartFile file);

    void deleteAll();

    void delete(String fileName);
}
