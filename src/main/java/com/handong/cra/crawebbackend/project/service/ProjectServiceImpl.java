package com.handong.cra.crawebbackend.project.service;


import com.handong.cra.crawebbackend.exception.project.ProjectSemesterParseException;
import com.handong.cra.crawebbackend.file.domain.S3ImageCategory;
import com.handong.cra.crawebbackend.file.service.S3ImageService;
import com.handong.cra.crawebbackend.exception.project.ProjectNotFoundException;
import com.handong.cra.crawebbackend.project.domain.Project;
import com.handong.cra.crawebbackend.project.domain.ProjectOrderBy;
import com.handong.cra.crawebbackend.project.dto.CreateProjectDto;
import com.handong.cra.crawebbackend.project.dto.DetailProjectDto;
import com.handong.cra.crawebbackend.project.dto.ListProjectDto;
import com.handong.cra.crawebbackend.project.dto.UpdateProjectDto;
import com.handong.cra.crawebbackend.project.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {


    private final ProjectRepository projectRepository;
    private final S3ImageService s3ImageService;

    @Override
    @Transactional
    public CreateProjectDto createProject(CreateProjectDto createProjectDto) {

        if (createProjectDto.getSemester().length() > 4 || !createProjectDto.getSemester().contains("-")){
            throw new ProjectSemesterParseException();
        }
        Project project = Project.from(createProjectDto);
        project.setImageUrl(s3ImageService.transferImage(project.getImageUrl(), S3ImageCategory.PROJECT));
        project = projectRepository.save(project); // save 된 Entity 가져옴

        return CreateProjectDto.from(project);
    }

    @Override
    @Transactional
    public UpdateProjectDto updateProject(UpdateProjectDto updateProjectDto) { // TODO exception 처리 필요

        Project project = projectRepository.findById(updateProjectDto.getId()).orElseThrow(ProjectNotFoundException::new);
        String newImgUrl= "";

        // 수정됨
        if (updateProjectDto.getImageUrl().contains("temp/")){
            log.info("Project img 수정 로직 진행");
            s3ImageService.transferImage(project.getImageUrl(),S3ImageCategory.DELETED);
            newImgUrl = s3ImageService.transferImage(updateProjectDto.getImageUrl(),S3ImageCategory.PROJECT);
//            project.setImageUrl(newImgUrl);
            updateProjectDto.setImageUrl(newImgUrl);
            log.info("Project img 수정 완료");
        }

        project = project.update(updateProjectDto);

        return UpdateProjectDto.from(project);
    }

    @Override
    @Transactional
    public Boolean deleteProjectById(Long id) { // TODO exception 처리 필요
        Project project = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
        project.delete();
        s3ImageService.transferImage(project.getImageUrl(), S3ImageCategory.DELETED);
        return true;
    }

    @Override
    public DetailProjectDto getDetailProjectById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
        return DetailProjectDto.from(project);
    }

    @Override
    public List<ListProjectDto> getListProject() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream().map(ListProjectDto::from).toList();
    }

    @Override
    public List<ListProjectDto> getPaginationProject(Long page, Integer perPage, ProjectOrderBy projectOrderBy, Boolean isASC) {
        HashMap<ProjectOrderBy, String> map = new HashMap<>();
        map.put(ProjectOrderBy.DATE, "createdAt");
        map.put(ProjectOrderBy.SEMESTER, "semester");
        Sort sort = Sort.by(map.get(projectOrderBy));
        sort = (isASC) ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(Math.toIntExact(page), perPage, sort);
        Page<Project> projects = projectRepository.findAllByDeletedIsFalse(pageable);

        return projects.stream().map(ListProjectDto::from).toList();
    }
}
