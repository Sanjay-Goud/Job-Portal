package com.sanjay.job_portal.config;

import com.sanjay.job_portal.entity.Job;
import com.sanjay.job_portal.entity.User;
import com.sanjay.job_portal.repository.JobRepository;
import com.sanjay.job_portal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Only initialize if database is empty
        if (userRepository.count() > 0) {
            System.out.println("Database already contains data. Skipping initialization.");
            return;
        }

        System.out.println("Initializing sample data...");

        // Create Employer Users
        User employer1 = createEmployer("employer@techcorp.com", "password123",
                "John Smith", "Tech Corp", "9876543210");
        User employer2 = createEmployer("hr@google.com", "password123",
                "Sarah Johnson", "Google", "9876543211");
        User employer3 = createEmployer("recruiter@amazon.com", "password123",
                "Mike Wilson", "Amazon", "9876543212");

        // Create Sample Applicant (for testing)
        createApplicant("jobseeker@test.com", "password123",
                "Alice Brown", "Java, Spring Boot, React, MySQL");

        // Create Jobs
        createJob(employer1, "Senior Full Stack Developer",
                "We are looking for an experienced Full Stack Developer to join our growing team. You will work on cutting-edge projects using modern technologies and collaborate with talented engineers.",
                "Tech Corp", "Hyderabad, India", Job.JobType.FULL_TIME,
                "Java, Spring Boot, React, PostgreSQL, REST API, Docker",
                "Senior", "800000", "1500000", 2);

        createJob(employer2, "Frontend Developer",
                "Join our team to build beautiful and responsive user interfaces. Work with the latest frontend technologies and modern design systems.",
                "Google", "Bangalore, India", Job.JobType.FULL_TIME,
                "React, TypeScript, HTML, CSS, JavaScript, Redux, Tailwind",
                "Mid", "600000", "1200000", 3);

        createJob(employer3, "Backend Developer",
                "We need a skilled Backend Developer to build robust and scalable APIs for our cloud-based applications.",
                "Amazon", "Mumbai, India", Job.JobType.FULL_TIME,
                "Node.js, Express, MongoDB, REST API, Microservices, AWS",
                "Mid", "700000", "1300000", 2);

        createJob(employer1, "Java Developer Intern",
                "Great opportunity for students to learn and work on real-world Java projects. Gain hands-on experience with enterprise applications.",
                "Tech Corp", "Remote", Job.JobType.INTERNSHIP,
                "Java, Spring Framework, MySQL, Git",
                "Entry", "20000", "40000", 5);

        createJob(employer2, "DevOps Engineer",
                "Looking for a DevOps Engineer to manage our cloud infrastructure and implement CI/CD pipelines.",
                "Google", "Pune, India", Job.JobType.FULL_TIME,
                "AWS, Docker, Kubernetes, Jenkins, Terraform, Linux",
                "Senior", "1000000", "1800000", 1);

        createJob(employer3, "Data Scientist",
                "Join our data science team to work on machine learning models and extract valuable insights from large datasets.",
                "Amazon", "Hyderabad, India", Job.JobType.FULL_TIME,
                "Python, Machine Learning, TensorFlow, Pandas, SQL, Statistics",
                "Mid", "900000", "1600000", 2);

        createJob(employer1, "UI/UX Designer",
                "Creative UI/UX Designer needed to design intuitive and beautiful user experiences for our products.",
                "Tech Corp", "Bangalore, India", Job.JobType.FULL_TIME,
                "Figma, Adobe XD, Sketch, User Research, Prototyping",
                "Mid", "500000", "1000000", 1);

        createJob(employer2, "Mobile App Developer",
                "Develop cross-platform mobile applications using React Native for millions of users.",
                "Google", "Delhi, India", Job.JobType.FULL_TIME,
                "React Native, JavaScript, iOS, Android, Redux, Firebase",
                "Mid", "700000", "1400000", 2);

        createJob(employer3, "QA Automation Engineer",
                "Automate testing processes and ensure the highest quality standards for our software products.",
                "Amazon", "Noida, India", Job.JobType.CONTRACT,
                "Selenium, Java, TestNG, Cucumber, API Testing",
                "Mid", "600000", "1100000", 1);

        createJob(employer1, "Product Manager",
                "Lead product development and strategy for our flagship products. Work with cross-functional teams.",
                "Tech Corp", "Mumbai, India", Job.JobType.FULL_TIME,
                "Product Management, Agile, User Stories, Market Research, Analytics",
                "Senior", "1500000", "2500000", 1);

        createJob(employer2, "Cloud Architect",
                "Design and implement scalable cloud solutions using cutting-edge technologies.",
                "Google", "Hyderabad, India", Job.JobType.FULL_TIME,
                "AWS, Azure, GCP, Microservices, System Design",
                "Senior", "1800000", "3000000", 1);

        createJob(employer3, "Python Developer",
                "Build backend services and APIs using Python and modern frameworks.",
                "Amazon", "Bangalore, India", Job.JobType.FULL_TIME,
                "Python, Django, Flask, PostgreSQL, REST API",
                "Mid", "650000", "1250000", 3);

        System.out.println("Sample data initialization completed!");
        System.out.println("\n=== Login Credentials ===");
        System.out.println("Employer 1: employer@techcorp.com / password123");
        System.out.println("Employer 2: hr@google.com / password123");
        System.out.println("Employer 3: recruiter@amazon.com / password123");
        System.out.println("Job Seeker: jobseeker@test.com / password123");
        System.out.println("========================\n");
    }

    private User createEmployer(String email, String password, String fullName,
                                String company, String phone) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(fullName);
        user.setCompany(company);
        user.setPhone(phone);
        user.setRole(User.Role.EMPLOYER);
        user.setEnabled(true);
        return userRepository.save(user);
    }

    private User createApplicant(String email, String password, String fullName, String skills) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(fullName);
        user.setSkills(skills);
        user.setRole(User.Role.APPLICANT);
        user.setEnabled(true);
        return userRepository.save(user);
    }

    private void createJob(User employer, String title, String description,
                           String company, String location, Job.JobType jobType,
                           String skills, String experienceLevel,
                           String salaryMin, String salaryMax, int openings) {
        Job job = new Job();
        job.setTitle(title);
        job.setDescription(description);
        job.setCompany(company);
        job.setLocation(location);
        job.setJobType(jobType);
        job.setRequiredSkills(skills);
        job.setExperienceLevel(experienceLevel);
        job.setSalaryMin(new BigDecimal(salaryMin));
        job.setSalaryMax(new BigDecimal(salaryMax));
        job.setOpenings(openings);
        job.setStatus(Job.JobStatus.ACTIVE);
        job.setEmployer(employer);

        job.setRequirements("Bachelor's degree in Computer Science or related field. " +
                "Relevant years of experience. Strong problem-solving and communication skills.");
        job.setResponsibilities("Develop high-quality software. Collaborate with team members. " +
                "Write clean, maintainable code. Participate in code reviews.");

        jobRepository.save(job);
    }
}