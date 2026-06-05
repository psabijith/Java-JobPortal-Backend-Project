//package com.aitrich.JobPortalSystem.Service.Job;
//
//
//import com.aitrich.JobPortalSystem.DTO.JobDTO;
//import com.aitrich.JobPortalSystem.Entity.Company;
//import com.aitrich.JobPortalSystem.Entity.Job;
//import com.aitrich.JobPortalSystem.Repository.IJobRepo;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.modelmapper.ModelMapper;
//
//import java.time.LocalDate;
//import java.util.*;
//
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//
//@ExtendWith(MockitoExtension.class)
//public class JobServiceTest {
//
//
//    @Mock
//    private IJobRepo jobRepo;
//    @Mock
//    private ModelMapper modelMapper;
//    @InjectMocks
//    private JobServiceImpl jobSerivce;
//
//    private JobDTO dto;
//    private Job job;
//
//    @BeforeEach
//    void setUp() {
//        dto = new JobDTO();
//        job = new Job();
//
//        dto.setCompany(new Company());
//        dto.setDescription("Software Developer");
//        dto.setPostedDate(LocalDate.now());
//        dto.setEndDate(LocalDate.now());
//
//        List<String> list = new ArrayList<>();
//
//        list.add("Java");
//        list.add("Spring");
//        list.add("SpringBoot");
//        dto.setSkills(list);
//        dto.setExperience("1 year ");
//        dto.setSalary(40000);
//
//
//
//    }
//
//    @Nested
//    @DisplayName("Post a Job")
//    class CreateJobTest{
//
//        @Test
//        @DisplayName("Should add a job")
//        void createJob() {
//
//            when(modelMapper.map(dto, Job.class)).thenReturn(job);
//            when(jobRepo.save(job)).thenReturn(job);
//            when(modelMapper.map(job, JobDTO.class)).thenReturn(dto);
//
//            JobDTO actualResult = jobSerivce.createJob(dto);
//
//            assertNotNull(actualResult);
//            assertEquals(dto, actualResult);
//
//            verify(modelMapper).map(dto, Job.class);
//            verify(jobRepo).save(job);
//            verify(modelMapper).map(job, JobDTO.class);
//        }
//    }
//
//    @Nested
//    @DisplayName("")
//    class GetJobById{
//        @Test
//        @DisplayName("Should find a job")
//        void getJobByIdTest(){
//            Long id = job.getJobId();
//            when(jobRepo.findById(id)).thenReturn(Optional.of(job));
//            Job actualResult = jobSerivce.getJobById(id);
//
//            assertNotNull(actualResult);
//            assertEquals(job, actualResult);
//
//            verify(jobRepo).findById(id);
//        }
//    }
//
//    @Nested
//    @DisplayName("Should return all jobs")
//    class GetAllJobTest{
//        @Test
//        @DisplayName("Should return all applications")
//        void getAllJobsTest(){
//            when(jobRepo.findAll()).thenReturn(List.of(job));
//
//            List<Job> actualResult = jobSerivce.listAllJob();
//
//            assertNotNull(actualResult);
//            assertEquals(1, actualResult.size());
//            assertEquals(job, actualResult.get(0));
//            verify(jobRepo).findAll();
//        }
//    }
//
//    @Nested
//    @DisplayName("Update job")
//    class UpdateJobTest{
//        @Test
//        @DisplayName("Should update a job")
//        void updateJobTest(){
//            JobDTO jobDto = new JobDTO();
//            when(jobRepo.save(job)).thenReturn(job);
//            Job actualResult = jobSerivce.updateJob(job.getJobId(),jobDto);
//            assertNotNull(actualResult);
//            assertEquals(job, actualResult);
//
//            verify(jobRepo).save(job);
//        }
//    }
//
//    @Nested
//    @DisplayName("Delete job")
//    class DeleteJobTest{
//        @Test
//        @DisplayName("Should delete a job")
//        void deleteJobTest(){
//            Long id = job.getJobId();
//            jobSerivce.deleteJobById(id);
//            verify(jobRepo).deleteById(id);
//        }
//    }
//
//}
