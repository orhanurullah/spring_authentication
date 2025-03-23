package com.ortim.service;

import com.ortim.ServiceInterface.FileEntityServiceInterface;
import com.ortim.component.MessageService;
import com.ortim.core.fileSystem.StorageService;
import com.ortim.core.results.ErrorResult;
import com.ortim.core.results.Result;
import com.ortim.core.results.SuccessResult;
import com.ortim.model.FileEntity;
import com.ortim.model.User;
import com.ortim.repository.FileEntityRepository;
import com.ortim.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileEntityService implements FileEntityServiceInterface {

    private final FileEntityRepository fileEntityRepository;

    private final UserRepository userRepository;

    private final StorageService storageService;

    private final MessageService messageService;

    @Override
    public Result addProfilePhoto(Long userId, MultipartFile file) {
        try{
            User user = userRepository.findById(userId).orElse(null);
            if(user == null){
                return new ErrorResult(
                       messageService.getMessage("user.not.found.message", null)
                );
            }
            if(file.isEmpty()){
                return new ErrorResult(messageService.getMessage("file.empty.message", new Object[]{file.getOriginalFilename()}));
            }
            if(!storageService.isImageFile(file)){
                return new ErrorResult(
                        messageService.getMessage("file.not.image.message", null)
                );
            }
            FileEntity fileEntity = FileEntity.builder()
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .data(file.getBytes())
                    .user(user)
                    .build();
            storageService.store(file,fileEntity.getPathUniqueName());
            user.getProfilePhotos().add(fileEntity);
            userRepository.save(user);
            fileEntityRepository.save(fileEntity);

            return new SuccessResult(
                    messageService.getMessage("image.uploaded.true.message", new Object[]{file.getOriginalFilename()})
            );
        } catch (Exception e) {
            return new ErrorResult(
                    messageService.getMessage("image.uploaded.false.message", null)
            );
        }
    }

    @Override
    public Result deleteProfilePhoto(String fileName) {
        try {
            FileEntity fileEntity = fileEntityRepository.findByPathUniqueName(fileName).orElse(null);
            if (fileEntity == null) {
                return new ErrorResult(
                        messageService.getMessage("image.file.not.db.message", new Object[]{fileName})
                );
            }
            storageService.delete(fileName);
            fileEntityRepository.delete(fileEntity);
            return new SuccessResult(
                    messageService.getMessage("image.deleted.true.message", new Object[]{fileName})
            );
        } catch (Exception e) {
            return new ErrorResult(
                    messageService.getMessage("image.deleted.false.message", new Object[]{fileName})
            );
        }
    }
}
