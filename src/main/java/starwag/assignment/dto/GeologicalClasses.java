package starwag.assignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GeologicalClasses {
  @Schema(description = "class name",example = "GEO Class 1")
  private String name;
  @Schema(description = "class code",example = "GC1")
  private String code;
}
