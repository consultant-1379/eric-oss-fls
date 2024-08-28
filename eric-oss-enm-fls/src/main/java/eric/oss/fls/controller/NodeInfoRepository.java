package eric.oss.fls.controller;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import eric.oss.fls.repository.model.NodeInfo;

public interface NodeInfoRepository extends JpaRepository<NodeInfo, Long>,JpaSpecificationExecutor<NodeInfo>{

	@Modifying
    @Query("delete from NodeInfo u where u.endRopTimeInOss = ?1")
	//@Query("delete from NodeInfo u where u.endRopTimeInOss = ( select distinct endRopTimeInOss"" from NodeInfo x where x.endRopTimeInOss < ?1 ORDER BY x.endRopTimeInOss DESC)")
    void deleteByEndRop(String endRop);
	
	
}
