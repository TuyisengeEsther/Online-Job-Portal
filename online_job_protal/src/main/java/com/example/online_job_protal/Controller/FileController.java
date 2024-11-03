package com.example.online_job_protal.Controller;

import com.example.online_job_protal.Model.FileModel;
import com.example.online_job_protal.Service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Controller
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/upload")
    public String uploadForm(Model model) {
        List<FileModel> files = fileService.getAllFiles(); // Get all files
        model.addAttribute("files", files); // Add files to the model
        return "upload"; // Return the name of the upload HTML file (upload.html)
    }

    @PostMapping("/upload")
    public String uploadFile(MultipartFile file, Model model) {
        try {
            FileModel savedFile = fileService.uploadFile(file);
            model.addAttribute("successMessage", "File uploaded successfully: " + savedFile.getResumeFileName());
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Failed to upload file: " + e.getMessage());
        }
        return "redirect:/upload"; // Redirect back to upload form to refresh the list
    }

    @GetMapping("/files/download/{id}")
    public void downloadFile(@PathVariable Long id, HttpServletResponse response) {
        System.out.println("Download request for file ID: " + id); // Debug line
        FileModel fileModel = fileService.getFile(id);
        if (fileModel != null) {
            File file = new File(fileModel.getResumeFileUrl());
            response.setContentType(fileModel.getResumeFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileModel.getResumeFileName() + "\"");

            try (FileInputStream inputStream = new FileInputStream(file);
                 OutputStream outputStream = response.getOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Handle the case when fileModel is null
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // or another appropriate response
        }
    }
}
