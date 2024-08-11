package starwag.assignment.service;

import java.util.List;

import starwag.assignment.dto.SectionDTO;

public interface SectionProcessingService {
  void processFile(String jobId);

  List<SectionDTO> getInformationByJobID(String jobId);

  List<SectionDTO> getInformationByCodeID(String codeId);

  List<SectionDTO> getInformationBySectionName(String sectionName);

  List<SectionDTO> getAllSections();
}
