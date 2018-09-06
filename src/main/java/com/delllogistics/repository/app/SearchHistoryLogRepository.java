package com.delllogistics.repository.app;

import com.delllogistics.entity.app.SearchHistoryLog;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Search History Log.<br/>
 * User: jiajie<br/>
 * Date: 22/11/2017<br/>
 * Time: 8:57 PM<br/>
 */
@Repository
public interface SearchHistoryLogRepository
        extends PagingAndSortingRepository<SearchHistoryLog, Long>, JpaSpecificationExecutor<SearchHistoryLog> {

    @Query(value = "SELECT content  FROM search_history_log group by content order by count(*) desc limit 5", nativeQuery = true)
    List<String> findHotSearchs();
}
