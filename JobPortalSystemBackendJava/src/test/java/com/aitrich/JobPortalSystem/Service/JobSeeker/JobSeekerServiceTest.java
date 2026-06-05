package com.aitrich.JobPortalSystem.Service.JobSeeker;

import com.aitrich.JobPortalSystem.DTO.JobSeekerRequestDTO;
import com.aitrich.JobPortalSystem.DTO.JobSeekerResponseDTO;
import com.aitrich.JobPortalSystem.Entity.JobSeeker;
import com.aitrich.JobPortalSystem.Repository.IJobSeekerRepo;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JobSeeker Service Tests")
class JobSeekerServiceTest {

    @Mock
    private IJobSeekerRepo repository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private JobSeekerServiceImp service;

    private JobSeekerRequestDTO requestDTO;
    private JobSeekerResponseDTO responseDTO;
    private JobSeeker jobSeeker;

    @BeforeEach
    void setUp() {
        requestDTO = new JobSeekerRequestDTO();
        requestDTO.setFirstName("Aswin");
        requestDTO.setLastName("S");
        requestDTO.setEmail("aswin@test.com");
        requestDTO.setPassword("1234");
        requestDTO.setSkills(List.of("Java"));
        requestDTO.setLocation("India");

        jobSeeker = new JobSeeker();
        jobSeeker.setId(1L);
        jobSeeker.setFirstName("Aswin");
        jobSeeker.setLastName("S");
        jobSeeker.setEmail("aswin@test.com");
        jobSeeker.setPassword("1234");
        jobSeeker.setSkills(List.of("Java"));
        jobSeeker.setLocation("India");

        responseDTO = new JobSeekerResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setFirstName("Aswin");
        responseDTO.setLastName("S");
        responseDTO.setEmail("aswin@test.com");
        responseDTO.setSkills(List.of("Java"));
        responseDTO.setLocation("India");
    }


    @Nested
    @DisplayName("Create JobSeeker")
    class CreateJobSeekerTest {

        @Test
        @DisplayName("Should create a job seeker")
        void createJobSeekerTest() {

            when(modelMapper.map(requestDTO, JobSeeker.class)).thenReturn(jobSeeker);
            when(repository.save(jobSeeker)).thenReturn(jobSeeker);
            when(modelMapper.map(jobSeeker, JobSeekerResponseDTO.class)).thenReturn(responseDTO);

            JobSeekerResponseDTO result = service.createJobSeeker(requestDTO);

            assertNotNull(result);
            assertEquals(responseDTO, result);

            verify(modelMapper).map(requestDTO, JobSeeker.class);
            verify(repository).save(jobSeeker);
            verify(modelMapper).map(jobSeeker, JobSeekerResponseDTO.class);
        }
    }


    @Nested
    @DisplayName("Get JobSeeker By ID")
    class GetByIdTest {

        @Test
        @DisplayName("Should return job seeker")
        void getByIdTest() {

            Long id = jobSeeker.getId();

            when(repository.findById(id)).thenReturn(Optional.of(jobSeeker));
            when(modelMapper.map(jobSeeker, JobSeekerResponseDTO.class)).thenReturn(responseDTO);

            JobSeekerResponseDTO result = service.getJobSeekerById(id);

            assertNotNull(result);
            assertEquals(responseDTO, result);

            verify(repository).findById(id);
        }
    }

    @Nested
    @DisplayName("Get All JobSeekers")
    class GetAllTest {

        @Test
        @DisplayName("Should return all job seekers")
        void getAllTest() {

            when(repository.findAll()).thenReturn(List.of(jobSeeker));
            when(modelMapper.map(jobSeeker, JobSeekerResponseDTO.class)).thenReturn(responseDTO);

            List<JobSeekerResponseDTO> result = service.getAllJobSeekers();

            assertNotNull(result);
            assertEquals(1, result.size());

            verify(repository).findAll();
        }
    }


    @Nested
    @DisplayName("Update JobSeeker")
    class UpdateTest {

        @Test
        @DisplayName("Should update job seeker")
        void updateTest() {

            Long id = jobSeeker.getId();

            when(repository.findById(id)).thenReturn(Optional.of(jobSeeker));
            when(repository.save(jobSeeker)).thenReturn(jobSeeker);
            when(modelMapper.map(jobSeeker, JobSeekerResponseDTO.class)).thenReturn(responseDTO);

            JobSeekerResponseDTO result = service.updateJobSeeker(id, requestDTO);

            assertNotNull(result);
            assertEquals(responseDTO, result);

            verify(repository).findById(id);
            verify(repository).save(jobSeeker);
        }
    }


    @Nested
    @DisplayName("Delete JobSeeker")
    class DeleteTest {

        @Test
        @DisplayName("Should delete job seeker")
        void deleteTest() {

            Long id = jobSeeker.getId();

            service.deleteJobSeeker(id);

            verify(repository).deleteById(id);
        }
    }
}
