package starwag.assignment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "section")
public class Section {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_generator")
  @SequenceGenerator(name = "id_generator", sequenceName = "id_seq")
  private int objId;
  @Column
  private String sectionName;
  @Column
  private String firstClassName;
  @Column
  private String firstClassCode;
  @Column
  private String secondClassName;
  @Column
  private String secondClassCode;
  @Column
  private String jobId;

  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

  public String getSectionName() {
    return sectionName;
  }

  public void setSectionName(String sectionName) {
    this.sectionName = sectionName;
  }

  public String getFirstClassName() {
    return firstClassName;
  }

  public void setFirstClassName(String firstClassName) {
    this.firstClassName = firstClassName;
  }

  public String getFirstClassCode() {
    return firstClassCode;
  }

  public void setFirstClassCode(String firstClassCode) {
    this.firstClassCode = firstClassCode;
  }

  public String getSecondClassName() {
    return secondClassName;
  }

  public void setSecondClassName(String secondClassName) {
    this.secondClassName = secondClassName;
  }

  public String getSecondClassCode() {
    return secondClassCode;
  }

  public void setSecondClassCode(String secondClassCode) {
    this.secondClassCode = secondClassCode;
  }

  @Override
  public String toString() {
    return "Section{" +
        "objId=" + objId +
        ", sectionName='" + sectionName + '\'' +
        ", firstClassName='" + firstClassName + '\'' +
        ", firstClassCode='" + firstClassCode + '\'' +
        ", secondClassName='" + secondClassName + '\'' +
        ", secondClassCode='" + secondClassCode + '\'' +
        ", jobId=" + jobId +
        '}';
  }
}
