package com.ortim.core.fileSystem;

import com.ortim.component.MessageService;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class StorageProperties {

    private final MessageService messageService;

    private final String location;

    public StorageProperties(MessageService messageService) {
        this.messageService = messageService;
        location = messageService.getMessage("file.upload.directory", null);
    }
}
