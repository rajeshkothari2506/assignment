package starwag.assignment.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import starwag.assignment.dto.SectionDTO;
import starwag.assignment.service.SectionProcessingService;
import starwag.assignment.service.pdf.CreatePDFService;

@RestController
@RequestMapping("/v1/api/section")
public class SectionController {

  private static final Log LOG = LogFactory.getLog(SectionController.class);

  final
  SectionProcessingService sectionProcessingService;

  final
  CreatePDFService createPDFService;

  public SectionController(SectionProcessingService sectionProcessingService, CreatePDFService createPDFService) {
    this.sectionProcessingService = sectionProcessingService;
    this.createPDFService = createPDFService;
  }

  @Operation(summary = "API to register a job for processing xl")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Job Id Created"),

  })
  @PostMapping("/register-job")
  public ResponseEntity<String> registerJob() {
    String jobId = "Job@".concat(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    sectionProcessingService.processFile(jobId);
    return new ResponseEntity<>(jobId, HttpStatus.OK);
  }

  @Operation(summary = "API to return all section information processed via jobid ")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Section  Information Found"),
      @ApiResponse(responseCode = "404", description = "No Information available for Job ID "),
      @ApiResponse(responseCode = "500", description = "Internal server error occurred ")
  })
  @GetMapping("/jobid/{jobid}")
  public ResponseEntity<List<SectionDTO>> getInformationByJobID(@PathVariable("jobid") String jobId) {
    LOG.info("Fetching Information for JobID " + jobId);
    List<SectionDTO> sectionDTOList = sectionProcessingService.getInformationByJobID(jobId);
    return new ResponseEntity<>(sectionDTOList, HttpStatus.OK);
  }

  @Operation(summary = "API to get section infromation as per class code ")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Section Information Found for given Code "),
      @ApiResponse(responseCode = "404", description = "No Information available for Class Code "),
      @ApiResponse(responseCode = "500", description = "Internal server error occurred ")
  })
  @GetMapping("/class/code/{code}")
  public ResponseEntity<List<SectionDTO>> getInformationByCode(@PathVariable("code") String code) {
    LOG.info("Fetching Information for codeId " + code);
    List<SectionDTO> sectionDTOList = sectionProcessingService.getInformationByCodeID(code);
    return new ResponseEntity<>(sectionDTOList, HttpStatus.OK);
  }

  @Operation(summary = "API to get Section information as per section name ")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Section Information Found for given name "),
      @ApiResponse(responseCode = "404", description = "No Information available for section name "),
      @ApiResponse(responseCode = "500", description = "Internal server error occurred ")
  })
  @GetMapping("/name/{sectionname}")
  public ResponseEntity<List<SectionDTO>> getInformationBySectionName(@PathVariable("sectionname") String sectionName) {
    LOG.info("Fetching Information for sectionName " + sectionName);
    List<SectionDTO> sectionDTOList = sectionProcessingService.getInformationBySectionName(sectionName);
    return new ResponseEntity<>(sectionDTOList, HttpStatus.OK);
  }

  @Operation(summary = "API to get PDF file which display classes as per section")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "PDF file returned "),
      @ApiResponse(responseCode = "404", description = "No section present for pdf "),
      @ApiResponse(responseCode = "500", description = "Internal server error occurred ")
  })
  @GetMapping("/generate-pdf")
  public ResponseEntity<byte[]> generateChartAndPDF() {
    byte[] pdfBytes = createPDFService.generatePDF();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("attachment", "section_information.pdf");
    return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

  }

}
