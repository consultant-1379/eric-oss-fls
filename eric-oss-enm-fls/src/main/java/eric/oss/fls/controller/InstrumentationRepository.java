package eric.oss.fls.controller;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import eric.oss.fls.repository.model.Instrumentation;

public interface InstrumentationRepository extends JpaRepository<Instrumentation, Long>,JpaSpecificationExecutor<Instrumentation>{

	@Modifying
    @Query("delete from NodeInfo u where u.endRopTimeInOss = ?1")
    void deleteByEndRop(String endRop);
}
