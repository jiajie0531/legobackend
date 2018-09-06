package com.delllogistics.repository.sys;

import com.delllogistics.entity.sys.SysFile;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysFileRepository extends PagingAndSortingRepository<SysFile, Long> {

    List<SysFile> findAllByIdIsLessThan(long size);
}
