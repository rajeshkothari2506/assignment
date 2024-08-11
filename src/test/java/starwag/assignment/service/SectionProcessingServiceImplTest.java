package starwag.assignment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import starwag.assignment.dto.SectionDTO;
import starwag.assignment.exception.NoSuchSectionPresentException;
import starwag.assignment.model.Section;
import starwag.assignment.repository.SectionRepository;

@ExtendWith(MockitoExtension.class)
public class SectionProcessingServiceImplTest {


  @InjectMocks
  SectionProcessingServiceImpl underTest;

  @Mock
  SectionRepository sectionRepository;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(underTest, "fileFullPath", "src/test/resources/section.xlsx");
  }

  @Test
  public void processFile_verifyParse() {
    underTest.processFile("testJoB");
    verify(sectionRepository, times(1)).saveAll(anyList());
  }


  @Test
  public void testGetInformationByJobID() {
    Section mockSection = new Section();
    mockSection.setSectionName("Section 1");
    when(sectionRepository.findByJobId("testJob")).thenReturn(List.of(mockSection));
    List<SectionDTO> sectionDTOs = underTest.getInformationByJobID("testJob");
    assertEquals(1, sectionDTOs.size());
    assertEquals("Section 1", sectionDTOs.get(0).getName());
  }

  @Test
  public void testGetInformationByCodeID() {
    Section mockSection = new Section();
    mockSection.setSectionName("Section 1");
    when(sectionRepository.findByFirstClassCodeOrSecondClassCode("test", "test")).thenReturn(List.of(mockSection));
    List<SectionDTO> sectionDTOs = underTest.getInformationByCodeID("test");
    assertEquals(1, sectionDTOs.size());
    assertEquals("Section 1", sectionDTOs.get(0).getName());
  }

  @Test
  public void testGetInformationBySectionName() {
    Section mockSection = new Section();
    mockSection.setSectionName("Section 1");
    when(sectionRepository.findBySectionName("test_section")).thenReturn(List.of(mockSection));
    List<SectionDTO> sectionDTOs = underTest.getInformationBySectionName("test_section");
    assertEquals(1, sectionDTOs.size());
    assertEquals("Section 1", sectionDTOs.get(0).getName());
  }

  @Test
  public void test_GetInformationBySectionName_NoData() {
    when(sectionRepository.findBySectionName("test_Section")).thenReturn(List.of());
    Assertions.assertThrows(NoSuchSectionPresentException.class, () -> underTest.getInformationBySectionName("test_Section"));
  }

  @Test
  public void test_GetInformationByCodeID_NoData() {
    when(sectionRepository.findByFirstClassCodeOrSecondClassCode("TC1", "TC1")).thenReturn(List.of());
    Assertions.assertThrows(NoSuchSectionPresentException.class, () -> underTest.getInformationByCodeID("TC1"));
  }

  @Test
  public void test_GetInformationByJobID_NoData() {
    when(sectionRepository.findByJobId("test_job")).thenReturn(List.of());
    Assertions.assertThrows(NoSuchSectionPresentException.class, () -> underTest.getInformationByJobID("test_job"));
  }

  @Test
  public void test_GetAllSections_NoData() {
    when(sectionRepository.findAll()).thenReturn(List.of());
    Assertions.assertThrows(NoSuchSectionPresentException.class, () -> underTest.getAllSections());
  }

  @Test
  public void testProcessFile_InvalidFileFormat() {
    ReflectionTestUtils.setField(underTest, "fileFullPath", "src/test/resources/section.xlsxxx");

    IllegalArgumentException thrown = assertThrows(
        IllegalArgumentException.class,
        () -> underTest.processFile("testJoB")
    );
    assertEquals("Unsupported file format: section.xlsxxx", thrown.getMessage());
  }


}
