// Handle form submission
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('resumeForm');
    if (form) {
        form.addEventListener('submit', handleFormSubmit);
    }
});

/**
 * Handle form submission and call the backend API
 */
async function handleFormSubmit(event) {
    event.preventDefault();
    
    // Show loading overlay
    const loadingOverlay = document.getElementById('loadingOverlay');
    if (loadingOverlay) loadingOverlay.classList.remove('hidden');
    
    // Collect form data
    const formData = {
        name: document.getElementById('name').value.trim(),
        email: document.getElementById('email').value.trim(),
        phone: document.getElementById('phone').value.trim(),
        skills: document.getElementById('skills').value.trim(),
        education: document.getElementById('education').value.trim(),
        experience: document.getElementById('experience').value.trim(),
        projects: document.getElementById('projects').value.trim(),
        careerObjective: document.getElementById('careerObjective').value.trim()
    };
    
    try {
        // Call backend API (JSON output)
        const response = await fetch('https://ai-resume-builder-621364977101.asia-south1.run.app/api/generate-resume', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(formData)
        });
        
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        
        const resumeData = await response.json();
        
        // Save response for preview page
        sessionStorage.setItem('resumeData', JSON.stringify(resumeData));
        
        // Redirect to preview page
        window.location.href = 'preview.html';
        
    } catch (error) {
        console.error('Error generating resume:', error);
        alert('Failed to generate resume. Ensure backend is running on http://192.168.1.5:8081');
    }

    // Hide loading overlay
    if (loadingOverlay) loadingOverlay.classList.add('hidden');
}

/**
 * Display the generated resume on the preview page
 */
function displayResume(data) {
    const resumeContent = document.getElementById('resumeContent');
    if (!resumeContent) return;
    
    // Build resume HTML
    let html = `
        <div class="contact-info">
            <h1>${escapeHtml(data.name || '')}</h1>
            <p>${escapeHtml(data.email || '')}</p>
            <p>${escapeHtml(data.phone || '')}</p>
        </div>
    `;
    
    // Career Objective
    if (data.careerObjective) {
        html += `
            <div class="summary">
                <h2>Career Objective</h2>
                <p>${formatText(data.careerObjective)}</p>
            </div>
        `;
    }
    
    // Professional Summary
    if (data.summary) {
        html += `
            <div class="summary">
                <h2>Professional Summary</h2>
                <p>${formatText(data.summary)}</p>
            </div>
        `;
    }
    
    // Skills (camelCase)
    if (data.skillsSection) {
        html += `
            <div class="section-content">
                <h2>Skills</h2>
                ${formatText(data.skillsSection)}
            </div>
        `;
    }
    
    // Education
    if (data.educationSection) {
        html += `
            <div class="section-content">
                <h2>Education</h2>
                ${formatText(data.educationSection)}
            </div>
        `;
    }
    
    // Experience
    if (data.experienceSection) {
        html += `
            <div class="section-content">
                <h2>Experience</h2>
                ${formatText(data.experienceSection)}
            </div>
        `;
    }
    
    // Projects
    if (data.projectsSection) {
        html += `
            <div class="section-content">
                <h2>Projects</h2>
                ${formatText(data.projectsSection)}
            </div>
        `;
    }
    
    resumeContent.innerHTML = html;
}

/**
 * Format text into paragraphs and bullet points
 */
function formatText(text) {
    if (!text) return '';
    
    let formatted = escapeHtml(text);
    const lines = formatted.split('\n');
    const result = [];
    let inList = false;
    
    for (let line of lines) {
        line = line.trim();
        
        if (!line) {
            if (inList) {
                result.push('</ul>');
                inList = false;
            }
            result.push('<br>');
            continue;
        }
        
        // Bullet points (• or -)
        const bulletMatch = line.match(/^[•\-]\s+(.+)$/);
        if (bulletMatch) {
            if (!inList) {
                result.push('<ul>');
                inList = true;
            }
            result.push(`<li>${bulletMatch[1]}</li>`);
            continue;
        }
        
        // Normal paragraph
        if (inList) {
            result.push('</ul>');
            inList = false;
        }
        
        result.push(`<p style="margin: 6px 0; line-height: 1.6;">${line}</p>`);
    }
    
    if (inList) result.push('</ul>');
    
    return result.join('');
}

/**
 * Escape HTML (security)
 */
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}
