package com.khang.backendecommerce.newstruc.service;

import com.khang.backendecommerce.infrastructure.common.dto.response.BaseResponse;
import com.khang.backendecommerce.newstruc.dto.response.store.SubOrderPendingResponse;
import org.springframework.data.domain.Pageable;

public interface SubOrderManagementService {
    BaseResponse<?> getAllPendingSuborders(Pageable pageable);
}
