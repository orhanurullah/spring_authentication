package com.ortim.ServiceInterface;

import com.ortim.core.results.Result;
import org.springframework.web.multipart.MultipartFile;

public interface FileEntityServiceInterface {

    Result addProfilePhoto(Long userId, MultipartFile file);

    Result deleteProfilePhoto(String fileName);
}
