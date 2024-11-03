package com.example.online_job_protal.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "files") // Name of the table in the database
public class FileModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String resumeFileName; // Name of the resume file

    @Column(nullable = true)
    private String resumeFileType; // Type of the resume file (e.g., pdf, docx)

    @Column(nullable = true)
    private String resumeFileUrl; // URL or path for downloading the file

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResumeFileName() {
        return resumeFileName;
    }

    public void setResumeFileName(String resumeFileName) {
        this.resumeFileName = resumeFileName;
    }

    public String getResumeFileType() {
        return resumeFileType;
    }

    public void setResumeFileType(String resumeFileType) {
        this.resumeFileType = resumeFileType;
    }

    public String getResumeFileUrl() {
        return resumeFileUrl;
    }

    public void setResumeFileUrl(String resumeFileUrl) {
        this.resumeFileUrl = resumeFileUrl;
    }
}
