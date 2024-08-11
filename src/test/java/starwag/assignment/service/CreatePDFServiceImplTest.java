package starwag.assignment.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import starwag.assignment.dto.GeologicalClasses;
import starwag.assignment.dto.SectionDTO;
import starwag.assignment.service.pdf.CreatePDFServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CreatePDFServiceImplTest {

  @Mock
  SectionProcessingService sectionProcessingService;

  @InjectMocks
  CreatePDFServiceImpl impl;


  @Test
  public void testGeneratePDF() {
    List<SectionDTO> mockSectionList = new ArrayList<>();
    mockSectionList.add(new SectionDTO("Section 1", List.of(new GeologicalClasses("Geo Class 1", "GC1"))));
    mockSectionList.add(new SectionDTO("Section 2", List.of(new GeologicalClasses("Geo Class 2", "GC2"))));
    when(sectionProcessingService.getAllSections()).thenReturn(mockSectionList);
    byte[] pdfBytes = impl.generatePDF();
    verify(sectionProcessingService, times(1)).getAllSections();
    Assert.notNull(pdfBytes, "The PDF byte array should not be null");
    Assertions.assertTrue(pdfBytes.length > 0);
  }


}

