package com.learn.ecommerce.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.learn.ecommerce.FileStorageProperties;
import com.learn.ecommerce.exception.FileNotFoundException;
import com.learn.ecommerce.exception.FileStorageException;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties prop) {
        this.fileStorageLocation = Paths.get(prop.getUploadDir())
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            throw new FileStorageException("Gagal membuat direktori", e);
        }
    }

    public String storeFile(MultipartFile file) {
        try {
            String fileNameExtension = StringUtils
                    .getFilenameExtension(StringUtils.cleanPath(file.getOriginalFilename()));
            String fileName = UUID.randomUUID().toString() + "." + fileNameExtension;
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new FileStorageException("Gagal menyimpan file", e);
        }
    }

    public Resource loadFileResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName);
            UrlResource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                throw new FileNotFoundException("File tidak ditemukan");
            }

            return resource;
        } catch (Exception e) {
            throw new FileNotFoundException("File tidak ditemukan", e);
        }
    }

}
