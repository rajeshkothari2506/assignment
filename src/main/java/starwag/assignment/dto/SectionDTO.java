package starwag.assignment.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SectionDTO {

  @Schema(description = "section name ",example = "Section 1")
  private String name;

  @Schema(description = "class information of section ")
  private List<GeologicalClasses> geologicalClasses;
}
