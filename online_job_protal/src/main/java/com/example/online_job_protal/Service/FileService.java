package com.example.online_job_protal.Service;

import com.example.online_job_protal.Model.FileModel;

import com.example.online_job_protal.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final String uploadDir = "uploads/";

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;

        // Create the upload directory if it doesn't exist
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public FileModel uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String fileType = file.getContentType();
        String fileUrl = uploadDir + fileName;

        Path path = Paths.get(fileUrl);
        Files.copy(file.getInputStream(), path);

        FileModel fileModel = new FileModel();
        fileModel.setResumeFileName(fileName);
        fileModel.setResumeFileType(fileType);
        fileModel.setResumeFileUrl(fileUrl);

        return fileRepository.save(fileModel);
    }

    public FileModel getFile(Long id) {
        return fileRepository.findById(id).orElse(null);
    }

    public List<FileModel> getAllFiles() {
        return fileRepository.findAll(); // Fetch all files from the database
    }
}
