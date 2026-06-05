package com.aitrich.JobPortalSystem.Service.Application;

import com.aitrich.JobPortalSystem.DTO.ApplicationPostDTO;
import com.aitrich.JobPortalSystem.DTO.ApplicationResponseDTO;
import com.aitrich.JobPortalSystem.Entity.Application;
import com.aitrich.JobPortalSystem.Entity.Job;
import com.aitrich.JobPortalSystem.Entity.JobSeeker;
import com.aitrich.JobPortalSystem.Enums.Status;
import com.aitrich.JobPortalSystem.Repository.IApplicationRepo;
import com.aitrich.JobPortalSystem.Repository.IJobRepo;
import com.aitrich.JobPortalSystem.Repository.IJobSeekerRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    IApplicationRepo applicationRepo;

    @Mock
    IJobRepo jobRepo;

    @Mock
    IJobSeekerRepo jobSeekerRepo;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    ApplicationService service;

    @Test
    void postApplication_test() {

        ApplicationPostDTO dto = new ApplicationPostDTO();
        dto.setJobSeekerId(1L);
        dto.setJobId(2L);
        dto.setStatus(Status.APPROVED);

        JobSeeker js = new JobSeeker();
        js.setId(1L);

        Job job = new Job();
        job.setJobId(2L);

        when(jobSeekerRepo.findById(1L)).thenReturn(Optional.of(js));
        when(jobRepo.findById(2L)).thenReturn(Optional.of(job));

        ApplicationPostDTO res = service.postApplication(dto);

        assertNotNull(res);
        verify(applicationRepo).save(any(Application.class));
    }

    @Test
    void getApplicationById_test() {

        Application app = new Application();
        app.setId(1L);
        app.setStatus(Status.APPROVED);
        app.setAppliedDate(LocalDate.now());

        JobSeeker js = new JobSeeker();
        js.setId(5L);
        app.setJobSeeker(js);

        when(applicationRepo.findById(1L)).thenReturn(Optional.of(app));

        ApplicationResponseDTO res = service.getApplicationById(1L);

        assertEquals(1L, res.getId());
        assertEquals(5L, res.getJobSeekerId());
    }

    @Test
    void getAllApplications_test() {

        Application app = new Application();
        app.setId(1L);

        JobSeeker js = new JobSeeker();
        js.setId(2L);
        app.setJobSeeker(js);

        when(applicationRepo.findAll()).thenReturn(List.of(app));

        List<ApplicationResponseDTO> list = service.getAllApplications();

        assertEquals(1, list.size());
    }

    @Test
    void deleteApplication_test() {

        service.deleteApplicationById(1L);

        verify(applicationRepo).deleteById(1L);
    }

    @Test
    void updateApplication_test() {

        Application app = new Application();
        app.setId(1L);
        app.setStatus(Status.APPROVED);

        when(applicationRepo.findById(1L)).thenReturn(Optional.of(app));

        ApplicationPostDTO dto = new ApplicationPostDTO();
        dto.setStatus(Status.APPROVED);

        ApplicationResponseDTO res = service.updateApplication(dto, 1L);

        assertEquals(Status.APPROVED, res.getStatus());
    }

    @Test
    void searchByJobSeeker_test() {

        Application app = new Application();
        app.setId(1L);

        JobSeeker js = new JobSeeker();
        js.setId(10L);
        app.setJobSeeker(js);

        when(applicationRepo.findByJobSeeker_Id(10L)).thenReturn(List.of(app));

        List<ApplicationResponseDTO> res = service.searchApplicationByJobSeekerId(10L);

        assertEquals(1, res.size());
    }

    @Test
    void setStatus_test() {

        Application app = new Application();
        app.setId(1L);

        when(applicationRepo.findById(1L)).thenReturn(Optional.of(app));

        when(modelMapper.map(any(), eq(ApplicationPostDTO.class)))
                .thenReturn(new ApplicationPostDTO());

        ApplicationPostDTO res = service.setStatus("APPROVED", 1L);

        assertNotNull(res);
    }

    @Test
    void getApprovedApplications_test() {

        Application app = new Application();
        app.setId(1L);
        app.setStatus(Status.APPROVED);

        JobSeeker js = new JobSeeker();
        js.setId(3L);
        app.setJobSeeker(js);

        when(applicationRepo.findByStatus()).thenReturn(List.of(app));

        List<ApplicationResponseDTO> res = service.getApprovedApplications();

        assertEquals(1, res.size());
    }
}