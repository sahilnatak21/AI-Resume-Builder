package com.example.resume.controller;

import com.example.resume.model.ResumeRequest;
import com.example.resume.model.ResumeResponse;
import com.example.resume.service.ResumeService;
import com.example.resume.service.PdfService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private PdfService pdfService;

    // ---------------------------------------------
    // 1️⃣ OLD ENDPOINT → Returns JSON ONLY
    // ---------------------------------------------
    @PostMapping("/generate-resume")
    public ResponseEntity<ResumeResponse> generateResume(@RequestBody ResumeRequest request) {
        return ResponseEntity.ok(resumeService.generateResume(request));
    }
    @GetMapping("/check")
    public String check(){
        return "Okkkkk";
    }

    // ---------------------------------------------
    // 2️⃣ NEW ENDPOINT → Returns PDF DOWNLOAD
    // ---------------------------------------------
    @PostMapping("/download-resume")
    public ResponseEntity<byte[]> downloadResume(@RequestBody ResumeRequest request) {

        // 1. First generate resume data using Gemini
        ResumeResponse response = resumeService.generateResume(request);

        // 2. Convert that data into PDF bytes
        byte[] pdfBytes = pdfService.generatePdf(response);

        // 3. Return the PDF file to frontend
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=resume.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}


