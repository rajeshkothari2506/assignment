package starwag.assignment.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import io.micrometer.common.util.StringUtils;
import starwag.assignment.dto.GeologicalClasses;
import starwag.assignment.dto.SectionDTO;
import starwag.assignment.exception.NoSuchSectionPresentException;
import starwag.assignment.model.Section;
import starwag.assignment.repository.SectionRepository;

@Service
public class SectionProcessingServiceImpl implements SectionProcessingService {

  private static final Log LOG = LogFactory.getLog(SectionProcessingServiceImpl.class);

  final
  SectionRepository sectionRepository;

  @Value(value = "${file.location}")
  private String fileFullPath;

  public SectionProcessingServiceImpl(SectionRepository sectionRepository) {
    this.sectionRepository = sectionRepository;
  }

  @Async
  @Override
  public void processFile(String jobId) {
    LOG.info("Processing file in async with JobID " + jobId);
    File file = new File(fileFullPath);
    String fileName = file.getAbsoluteFile().getName();
    if (fileName.endsWith(".xlsx")) {
      processExcelFile(file, jobId);
    } else {
      throw new IllegalArgumentException("Unsupported file format: " + fileName);
    }

  }

  @Override
  public List<SectionDTO> getInformationByJobID(String jobId) {
    List<Section> sectionList = sectionRepository.findByJobId(jobId);
    if (sectionList.isEmpty()) {
      throw new NoSuchSectionPresentException("Do not find Information For jobId " + jobId);
    } else {
      return mapSectionToDTO(sectionList);
    }
  }

  @Override
  public List<SectionDTO> getInformationByCodeID(String codeId) {
    List<Section> sectionList = sectionRepository.findByFirstClassCodeOrSecondClassCode(codeId, codeId);
    if (sectionList.isEmpty()) {
      throw new NoSuchSectionPresentException("Do not find Information For codeID " + codeId);
    } else {
      return mapSectionToDTO(sectionList);
    }
  }

  @Override
  public List<SectionDTO> getInformationBySectionName(String sectionName) {
    List<Section> sectionList = sectionRepository.findBySectionName(sectionName);
    if (sectionList.isEmpty()) {
      throw new NoSuchSectionPresentException("Do not find Information For sectionName " + sectionName);
    } else {
      return mapSectionToDTO(sectionList);
    }
  }

  @Override
  public List<SectionDTO> getAllSections() {
    List<Section> sectionList = sectionRepository.findAll();
    if (sectionList.isEmpty()) {
      throw new NoSuchSectionPresentException("No Section Information Present, First Register a Job First ");
    } else {
      return mapSectionToDTO(sectionList);
    }
  }

  private List<SectionDTO> mapSectionToDTO(List<Section> sectionList) {
    List<SectionDTO> sectionDTOList = new ArrayList<>();
    for (Section section : sectionList) {
      List<GeologicalClasses> geologicalClassesList = new ArrayList<>();
      SectionDTO sectionDTO = new SectionDTO();
      sectionDTO.setName(section.getSectionName());
      if (StringUtils.isNotEmpty(section.getFirstClassName()) || StringUtils.isNotEmpty(section.getFirstClassCode())) {
        GeologicalClasses geologicalClasses1 = new GeologicalClasses(section.getFirstClassName(), section.getFirstClassCode());
        geologicalClassesList.add(geologicalClasses1);
      }
      if (StringUtils.isNotEmpty(section.getSecondClassName()) || StringUtils.isNotEmpty(section.getSecondClassCode())) {
        GeologicalClasses geologicalClasses2 = new GeologicalClasses(section.getSecondClassName(), section.getSecondClassCode());
        geologicalClassesList.add(geologicalClasses2);
      }
      sectionDTO.setGeologicalClasses(geologicalClassesList);
      sectionDTOList.add(sectionDTO);
    }
    return sectionDTOList;
  }

  private void processExcelFile(File file, String jobId) {
    List<Section> sectionList = new ArrayList<>();
    int countRows = 0;

    try (FileInputStream fileInputStream = new FileInputStream(file);
         Workbook workbook = new XSSFWorkbook(fileInputStream)) {
      Sheet sheet = workbook.getSheetAt(0);
      for (Row row : sheet) {
        if (countRows++ == 0) {
          continue; // Skip headers
        }
        Section section = new Section();
        section.setSectionName(getValueForCell(row, 0));
        section.setFirstClassName(getValueForCell(row, 1));
        section.setFirstClassCode(getValueForCell(row, 2));
        section.setSecondClassName(getValueForCell(row, 3));
        section.setSecondClassCode(getValueForCell(row, 4));
        section.setJobId(jobId);
        sectionList.add(section);
      }
      saveInformationToDB(sectionList);
      LOG.info("Number of Record Processed from Excel are " + countRows);
    } catch (IOException e) {
      LOG.error("IOException while processing file for job ID "+jobId);
      LOG.error("IOException  "+e.getMessage());
      throw new RuntimeException("Failed to process Excel file for job ID " + jobId, e);
    }
    LOG.info("Processing End for JobID " + jobId);
  }

  private void saveInformationToDB(List<Section> sectionList) {
    sectionRepository.saveAll(sectionList);
  }

  private String getValueForCell(Row row, int cellNum) {
    if (row == null || row.getCell(cellNum) == null) {
      return "";
    }
    return row.getCell(cellNum).getStringCellValue();
  }

}
