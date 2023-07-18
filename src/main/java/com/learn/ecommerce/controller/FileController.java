package com.learn.ecommerce.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.learn.ecommerce.entity.Product;
import com.learn.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.learn.ecommerce.model.UploadFileResponse;
import com.learn.ecommerce.service.FileStorageService;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/uploadImage")
    public UploadFileResponse uploadFileResponse(@RequestParam("file") MultipartFile file, @RequestParam("id") String productId) {
        String fileName = fileStorageService.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/images/")
                .path(fileName)
                .toUriString();

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceAccessException("Product with ID " + productId + " not found!"));
        product.setPicture(fileName);

        productRepository.save(product);

        return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @GetMapping("/images/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileResource(fileName);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
        .body(resource);
    }

}
