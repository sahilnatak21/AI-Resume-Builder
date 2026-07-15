package com.example.resume.model;

public class ResumeResponse {

    private String name;
    private String email;
    private String phone;

    private String summary = "";
    private String skillsSection = "";
    private String educationSection = "";
    private String experienceSection = "";
    private String projectsSection = "";
    private String careerObjective = "";

    // Empty Constructor
    public ResumeResponse() {}

    // Full Constructor (optional)
    public ResumeResponse(String name, String email, String phone, String summary,
                          String skillsSection, String educationSection,
                          String experienceSection, String projectsSection,
                          String careerObjective) {

        this.name = name;
        this.email = email;
        this.phone = phone;
        this.summary = summary;
        this.skillsSection = skillsSection;
        this.educationSection = educationSection;
        this.experienceSection = experienceSection;
        this.projectsSection = projectsSection;
        this.careerObjective = careerObjective;
    }

    // ---------------------------
    // Getters & Setters
    // ---------------------------

    public String getName() { return name; }
    public void setName(String name) { this.name = safe(name); }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = safe(email); }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = safe(phone); }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = safe(summary); }

    public String getSkillsSection() { return skillsSection; }
    public void setSkillsSection(String skillsSection) { this.skillsSection = safe(skillsSection); }

    public String getEducationSection() { return educationSection; }
    public void setEducationSection(String educationSection) { this.educationSection = safe(educationSection); }

    public String getExperienceSection() { return experienceSection; }
    public void setExperienceSection(String experienceSection) { this.experienceSection = safe(experienceSection); }

    public String getProjectsSection() { return projectsSection; }
    public void setProjectsSection(String projectsSection) { this.projectsSection = safe(projectsSection); }

    public String getCareerObjective() { return careerObjective; }
    public void setCareerObjective(String careerObjective) { this.careerObjective = safe(careerObjective); }

    // ---------------------------
    // Utility: Avoid null values
    // ---------------------------
    private String safe(String value) {
        return (value == null) ? "" : value.trim();
    }
}
