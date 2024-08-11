package starwag.assignment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import starwag.assignment.model.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {
  List<Section> findByJobId(String jobId);

  List<Section> findByFirstClassCodeOrSecondClassCode(String firstClassCode, String secondClassCode);

  List<Section> findBySectionName(String sectionName);
}
