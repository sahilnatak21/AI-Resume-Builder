package com.example.resume.service;

import com.example.resume.model.ResumeResponse;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generatePdf(ResumeResponse res) {
        try {
            Document document = new Document();
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, output);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font sectionFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font textFont = new Font(Font.FontFamily.HELVETICA, 11);

            // NAME
            Paragraph name = new Paragraph(res.getName(), titleFont);
            name.setAlignment(Element.ALIGN_CENTER);
            document.add(name);

            document.add(new Paragraph(res.getEmail(), textFont));
            document.add(new Paragraph(res.getPhone(), textFont));
            document.add(Chunk.NEWLINE);

            // Helper function
            addSection(document, "Career Objective", res.getCareerObjective(), sectionFont, textFont);
            addSection(document, "Professional Summary", res.getSummary(), sectionFont, textFont);
            addSection(document, "Skills", res.getSkillsSection(), sectionFont, textFont);
            addSection(document, "Education", res.getEducationSection(), sectionFont, textFont);
            addSection(document, "Experience", res.getExperienceSection(), sectionFont, textFont);
            addSection(document, "Projects", res.getProjectsSection(), sectionFont, textFont);

            document.close();
            return output.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF: " + e.getMessage());
        }
    }

    private void addSection(Document doc, String title, String content, Font sectionFont, Font textFont)
            throws DocumentException {
        if (content != null && !content.isBlank()) {
            doc.add(new Paragraph(title, sectionFont));
            doc.add(new Paragraph(content, textFont));
            doc.add(Chunk.NEWLINE);
        }
    }
}
