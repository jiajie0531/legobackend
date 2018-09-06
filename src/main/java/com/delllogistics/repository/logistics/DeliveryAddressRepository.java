package com.delllogistics.repository.logistics;

import com.delllogistics.entity.order.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryAddressRepository
        extends PagingAndSortingRepository<DeliveryAddress, Long>, JpaSpecificationExecutor<DeliveryAddress> {

}

