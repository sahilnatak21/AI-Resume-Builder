package com.example.resume.service;

import com.example.resume.model.ResumeRequest;
import com.example.resume.model.ResumeResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ResumeService {

    @Value("${GEMINI_API_KEY:}")
    private String apiKey;

    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1/models/gemini-3.5-flash:generateContent?key=";


    // ====================================================================================
    // ✔ FIXED VERSION OF parseResume()
    // ====================================================================================
    private ResumeResponse parseResume(String rawJson, ResumeRequest req) {

        JSONObject root = new JSONObject(rawJson);

        // Extract model text safely
        String text;
        try {
            text = root
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");
        } catch (Exception e) {
            throw new RuntimeException("Error extracting Gemini response: " + rawJson);
        }

        // Remove markdown fences
        text = text.replace("```json", "")
                .replace("```", "")
                .trim();

        // Extract JSON block
        int start = text.indexOf("{");
        int end = text.lastIndexOf("}");
        if (start == -1 || end == -1) {
            throw new RuntimeException("Gemini did not return valid JSON: " + text);
        }

        String cleanJson = text.substring(start, end + 1);
        JSONObject json = new JSONObject(cleanJson);

        // ====================================================================================
        // BUILD ResumeResponse OBJECT
        // ====================================================================================
        ResumeResponse res = new ResumeResponse();
        res.setName(req.getName());
        res.setEmail(req.getEmail());
        res.setPhone(req.getPhone());

        res.setSummary(json.optString("summary"));
        res.setCareerObjective(json.optString("careerObjective"));

        // ====================================================================================
        // ✔ FIX 1 — FORMAT SKILLS JSON → HUMAN TEXT
        // ====================================================================================
        if (json.has("skills_section")) {
            JSONObject skills = json.getJSONObject("skills_section");
            StringBuilder sb = new StringBuilder();

            for (String category : skills.keySet()) {
                sb.append(category).append(":\n");

                JSONArray arr = skills.optJSONArray(category);
                if (arr != null) {
                    for (int i = 0; i < arr.length(); i++) {
                        sb.append("• ").append(arr.getString(i)).append("\n");
                    }
                }
                sb.append("\n");
            }

            res.setSkillsSection(sb.toString());
        }

        // ====================================================================================
        // ✔ FIX 2 — EDUCATION JSON → PLAIN TEXT
        // ====================================================================================
        if (json.has("education_section")) {
            JSONObject edu = json.getJSONObject("education_section");
            String formattedEducation =
                    edu.optString("degree", "") + ", " +
                            edu.optString("university", "") + " (" +
                            edu.optString("years", "") + ")";
            res.setEducationSection(formattedEducation);
        }

        // ====================================================================================
        // ✔ FIX 3 — EXPERIENCE JSON → BULLET LIST
        // ====================================================================================
        if (json.has("experience_section")) {
            JSONObject exp = json.getJSONObject("experience_section");
            StringBuilder sb = new StringBuilder();

            // Job Title & Company
            sb.append(exp.optString("title", ""))
                    .append(" — ").append(exp.optString("company", ""))
                    .append(" (").append(exp.optString("years", "")).append(")\n\n");

            // Bullet points
            JSONArray points = exp.optJSONArray("responsibilities");
            if (points != null) {
                for (int i = 0; i < points.length(); i++) {
                    sb.append("• ").append(points.getString(i)).append("\n");
                }
            }

            res.setExperienceSection(sb.toString());
        }

        // ====================================================================================
        // ✔ FIX 4 — PROJECTS JSON → BULLETS
        // ====================================================================================
        if (json.has("projects_section")) {
            JSONObject proj = json.getJSONObject("projects_section");
            StringBuilder sb = new StringBuilder();

            sb.append(proj.optString("title", "")).append("\n\n");

            JSONArray desc = proj.optJSONArray("description");
            if (desc != null) {
                for (int i = 0; i < desc.length(); i++) {
                    sb.append("• ").append(desc.getString(i)).append("\n");
                }
            }

            sb.append("\nTechnologies: ")
                    .append(proj.optString("technologies", ""));

            res.setProjectsSection(sb.toString());
        }

        return res;
    }

    // ====================================================================================
    // RESUME GENERATION
    // ====================================================================================
    public ResumeResponse generateResume(ResumeRequest req) {
        String prompt = buildPrompt(req);
        String output = callGemini(prompt);
        return parseResume(output, req);
    }

    // ====================================================================================
    // PROMPT FOR GEMINI
    // ====================================================================================
    private String buildPrompt(ResumeRequest req) {
        return """
        Generate a highly detailed professional resume in JSON format.

        JSON STRUCTURE:
        {
          "summary": "",
          "skills_section": {},
          "education_section": {},
          "experience_section": {},
          "projects_section": {},
          "careerObjective": ""
        }

        RULES:
        - Use JSON objects for skills, experience, education, projects.
        - Expand experience into bullet points.
        - Expand projects descriptions into bullet points.
        - Do NOT shorten user input.

        USER INPUT:
        Name: %s
        Email: %s
        Phone: %s

        Skills:
        %s

        Education:
        %s

        Experience:
        %s

        Projects:
        %s

        Career Objective:
        %s

        Return ONLY JSON.
        """
                .formatted(
                        req.getName(), req.getEmail(), req.getPhone(),
                        req.getSkills(), req.getEducation(), req.getExperience(),
                        req.getProjects(), req.getCareerObjective()
                );
    }

    // ====================================================================================
    // CALL GEMINI API
    // ====================================================================================
    private String callGemini(String prompt) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("GEMINI_API_KEY is not configured on the server");
        }

        RestTemplate rest = new RestTemplate();

        JSONObject body = new JSONObject();
        JSONArray contents = new JSONArray();

        contents.put(new JSONObject().put(
                "parts",
                new JSONArray().put(new JSONObject().put("text", prompt))
        ));

        body.put("contents", contents);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response = rest.exchange(
                GEMINI_URL + apiKey,
                HttpMethod.POST,
                new HttpEntity<>(body.toString(), headers),
                String.class
        );

        return response.getBody();
    }
}
