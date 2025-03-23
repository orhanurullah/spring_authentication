package com.ortim.core.fileSystem;

import com.ortim.component.MessageService;
import com.ortim.core.results.DataResult;
import com.ortim.core.results.Result;
import com.ortim.core.results.SuccessDataResult;
import com.ortim.core.results.SuccessResult;
import com.ortim.core.utils.DbConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    private final MessageService messageService;

    @Autowired
    public FileSystemStorageService(StorageProperties properties, MessageService messageService) {
        this.messageService = messageService;
        if(properties.getLocation().trim().isEmpty()){
            throw new StorageException(this.messageService.getMessage("file.name.isEmpty.message", null));
        }
        this.rootLocation = Paths.get(properties.getLocation());
    }

    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9.\\-]", "_");
    }

    @Override
    public Result store(MultipartFile file, String fileNewName) {
        try{
            Path destinationFile = rootLocation.resolve(
                    Paths.get(fileNewName)
            ).normalize().toAbsolutePath();
            if(!destinationFile.getParent()
                    .equals(rootLocation.toAbsolutePath())){
                throw new StorageException(
                        messageService.getMessage("file.upload.directory.security.control.message", null)
                );
            }
            try(InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                return new SuccessDataResult<>(
                        destinationFile.getFileName().toString(),
                        messageService.getMessage("file.upload.success.message", null)
                );
            }catch (IOException e){
                throw new StorageException(
                        messageService.getMessage("file.upload.failed.message", null)
                );
            }
        }catch (MaxUploadSizeExceededException e){
            throw new StorageException(
                    messageService.getMessage("file.upload.maxSize.failed.message", null)
            );
        }
    }
    @Override
    public boolean isImageFile(MultipartFile file) {
        String fileName = Objects.requireNonNull(file.getOriginalFilename());
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return Stream.of("jpg", "jpeg", "png", "gif")
                .anyMatch(ext -> ext.equalsIgnoreCase(fileExtension));
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            Stream<Path> pathStream = Files.list(rootLocation);
            return pathStream.onClose(pathStream::close)
                    .filter(Files::isRegularFile)
                    .map(rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException(
                    messageService.getMessage("file.read.failed.message", null)
            );
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                            messageService.getMessage("file.read.resource.failed.message", new Object[]{filename})
                        );

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException(
                    messageService.getMessage("file.read.resource.failed.message", new Object[]{filename})
                    , e
            );
        }
    }
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void delete(String fileName) {
        try {
            Path file = load(fileName);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new StorageException(
                    messageService.getMessage("file.delete.failed.message", new Object[]{fileName})
                    , e
            );
        }
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException(
                    messageService.getMessage("file.notCreate.directory.message", null)
                    , e
            );
        }
    }
}
