package starwag.assignment.controller;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import starwag.assignment.dto.SectionDTO;
import starwag.assignment.service.SectionProcessingService;
import starwag.assignment.service.pdf.CreatePDFService;

@ExtendWith(MockitoExtension.class)
public class SectionControllerTest {

  @Mock
  private SectionProcessingService sectionProcessingService;
  @Mock
  private CreatePDFService createPDFService;
  @InjectMocks
  private SectionController sectionController;

  @Test
  void getByJobIDTest() {
    when(sectionProcessingService.getInformationByJobID("test")).thenReturn(getSectionList());
    ResponseEntity<List<SectionDTO>> sectionDTOList = sectionController.getInformationByJobID("test");
    Assertions.assertEquals(1, sectionDTOList.getBody().size());
  }

  @Test
  void getByCodeIDTest() {
    when(sectionProcessingService.getInformationByCodeID("test")).thenReturn(getSectionList());
    ResponseEntity<List<SectionDTO>> sectionDTOList = sectionController.getInformationByCode("test");
    List<SectionDTO> sectionDTOS = sectionDTOList.getBody();
    Assertions.assertEquals(1, sectionDTOS.stream().filter(a -> a.getName().equals("test_section")).toList().size());
  }

  @Test
  void getBySectionNameTest() {
    when(sectionProcessingService.getInformationBySectionName("test")).thenReturn(getSectionList());
    ResponseEntity<List<SectionDTO>> sectionDTOList = sectionController.getInformationBySectionName("test");
    Assertions.assertEquals(1, sectionDTOList.getBody().size());
  }

  @Test
  void registerJobTest() {
    ResponseEntity<String> jobId = sectionController.registerJob();
    Assertions.assertNotNull(jobId.getBody());
  }

  @Test
  void generateChartAndPDFTest() {
    when(createPDFService.generatePDF()).thenReturn(new byte[]{});
    ResponseEntity<byte[]> pdf = sectionController.generateChartAndPDF();
    Assertions.assertEquals(pdf.getStatusCode(), HttpStatus.OK);
    Assertions.assertEquals(pdf.getHeaders().getContentType(), MediaType.APPLICATION_PDF);
  }


  public List<SectionDTO> getSectionList() {
    List<SectionDTO> sectionDTOList = new ArrayList<>();
    SectionDTO sectionDTO = new SectionDTO();
    sectionDTO.setName("test_section");
    sectionDTOList.add(sectionDTO);
    return sectionDTOList;
  }
}
