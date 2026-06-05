package com.aitrich.JobPortalSystem.Service.JobSeeker;

import com.aitrich.JobPortalSystem.DTO.JobSeekerRequestDTO;
import com.aitrich.JobPortalSystem.DTO.JobSeekerResponseDTO;
import com.aitrich.JobPortalSystem.Entity.JobSeeker;
import com.aitrich.JobPortalSystem.Entity.User;
import com.aitrich.JobPortalSystem.Enums.Role;
import com.aitrich.JobPortalSystem.Repository.IJobSeekerRepo;
import com.aitrich.JobPortalSystem.Repository.IUserRepo;
import com.aitrich.JobPortalSystem.Security.OwnershipUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JobSeekerServiceImp implements IJobSeekerService {

    private final IJobSeekerRepo repository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final IUserRepo userRepo;

    @Override
    public JobSeekerResponseDTO createJobSeeker(JobSeekerRequestDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.JOBSEEKER);
        userRepo.save(user);

        JobSeeker js = modelMapper.map(dto, JobSeeker.class);
        js.setPassword(user.getPassword());
        js.setActive(true);
        return modelMapper.map(repository.save(js), JobSeekerResponseDTO.class);
    }

    @Override
    public JobSeekerResponseDTO getJobSeekerById(Long id) {
        JobSeeker js = repository.findById(id).orElseThrow(() -> new RuntimeException("JobSeeker not found"));
        OwnershipUtils.check(js.getEmail());
        return modelMapper.map(js, JobSeekerResponseDTO.class);
    }

    @Override
    public JobSeekerResponseDTO updateJobSeeker(Long id, JobSeekerRequestDTO dto) {
        JobSeeker js = repository.findById(id).orElseThrow(() -> new RuntimeException("JobSeeker not found"));
        OwnershipUtils.check(js.getEmail());
        js.setFirstName(dto.getFirstName());
        js.setLastName(dto.getLastName());
        js.setSkills(dto.getSkills());
        js.setLocation(dto.getLocation());
        return modelMapper.map(repository.save(js), JobSeekerResponseDTO.class);
    }

    @Override
    public void deleteJobSeeker(Long id) {
        JobSeeker js = repository.findById(id).orElseThrow(() -> new RuntimeException("JobSeeker not found"));
        OwnershipUtils.check(js.getEmail());
        repository.deleteById(id);
    }

    @Override
    public void uploadResume(Long id, MultipartFile file) {
        JobSeeker js = repository.findById(id).orElseThrow(() -> new RuntimeException("Jobseeker not found"));
        OwnershipUtils.check(js.getEmail());
        if (!file.getContentType().equals("application/pdf"))
            throw new RuntimeException("Only PDF allowed");
        try {
            String fileName = id + "_" + file.getOriginalFilename();
            Path path = Paths.get("uploads/resumes/" + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            js.setResumeUrl(path.toString());
            repository.save(js);
        } catch (IOException e) {
            throw new RuntimeException("File upload failed");
        }
    }

    @Override
    public void deleteResume(Long id) {
        JobSeeker js = repository.findById(id).orElseThrow(() -> new RuntimeException("JobSeeker not found"));
        OwnershipUtils.check(js.getEmail());
        try {
            if (js.getResumeUrl() != null) {
                Files.deleteIfExists(Paths.get(js.getResumeUrl()));
                js.setResumeUrl(null);
                repository.save(js);
            }
        } catch (IOException e) {
            throw new RuntimeException("File deletion failed");
        }
    }

    @Override
    public JobSeekerResponseDTO deactivateJobSeeker(Long id) {
        JobSeeker js = repository.findById(id).orElseThrow(() -> new RuntimeException("JobSeeker not found"));
        OwnershipUtils.check(js.getEmail());
        js.setActive(false);
        return modelMapper.map(repository.save(js), JobSeekerResponseDTO.class);
    }

    @Override
    public JobSeekerResponseDTO activateJobSeeker(Long id) {
        JobSeeker js = repository.findById(id).orElseThrow(() -> new RuntimeException("JobSeeker not found"));
        OwnershipUtils.check(js.getEmail());
        js.setActive(true);
        return modelMapper.map(repository.save(js), JobSeekerResponseDTO.class);
    }

    // ── read-only ─────────────────────────────────────────────────────────────

    @Override public List<JobSeekerResponseDTO> getAllJobSeekers() {
        return repository.findAll().stream().map(js -> modelMapper.map(js, JobSeekerResponseDTO.class)).toList();
    }
    @Override public Page<JobSeekerResponseDTO> getAllJobSeekersPaginated(Pageable p) {
        return repository.findAll(p).map(js -> modelMapper.map(js, JobSeekerResponseDTO.class));
    }
    @Override public List<JobSeekerResponseDTO> getActiveJobSeekers() {
        return repository.findByActive(true).stream().map(js -> modelMapper.map(js, JobSeekerResponseDTO.class)).toList();
    }
    @Override public List<JobSeekerResponseDTO> getJobSeekersByLocation(String location) {
        return repository.findByLocation(location).stream().map(js -> modelMapper.map(js, JobSeekerResponseDTO.class)).toList();
    }
    @Override public List<JobSeekerResponseDTO> getJobSeekersBySkill(String skill) {
        return repository.findBySkill(skill).stream().map(js -> modelMapper.map(js, JobSeekerResponseDTO.class)).toList();
    }
    @Override public List<JobSeekerResponseDTO> searchJobSeekersByName(String name) {
        return repository.findByNameContaining(name).stream().map(js -> modelMapper.map(js, JobSeekerResponseDTO.class)).toList();
    }
    @Override public boolean existsByEmail(String email) { return repository.existsByEmail(email); }
    @Override public Map<String, Long> getJobSeekerStatistics() {
        Map<String, Long> s = new HashMap<>();
        s.put("totalJobSeekers", repository.count());
        s.put("activeJobSeekers", repository.countByActive(true));
        s.put("inactiveJobSeekers", repository.countByActive(false));
        s.put("withResume", (long) repository.findJobSeekersWithResume().size());
        s.put("withoutResume", (long) repository.findJobSeekersWithoutResume().size());
        return s;
    }
    @Override public List<JobSeekerResponseDTO> getJobSeekersWithResume() {
        return repository.findJobSeekersWithResume().stream().map(js -> modelMapper.map(js, JobSeekerResponseDTO.class)).toList();
    }
    @Override public List<JobSeekerResponseDTO> getJobSeekersWithoutResume() {
        return repository.findJobSeekersWithoutResume().stream().map(js -> modelMapper.map(js, JobSeekerResponseDTO.class)).toList();
    }
}
