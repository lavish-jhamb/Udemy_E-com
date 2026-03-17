package com.ecommerce.project.service.interfaces;

import com.ecommerce.project.payload.request.RejectionRequest;
import com.ecommerce.project.payload.request.SellerApplicationRequest;
import com.ecommerce.project.payload.response.dto.SellerApplicationResponse;

import java.util.List;

public interface SellerService {

    SellerApplicationResponse apply(SellerApplicationRequest request, String username);

    List<SellerApplicationResponse> getPendingApplications();

    SellerApplicationResponse approve(Long applicationId);

    SellerApplicationResponse reject(Long applicationId, RejectionRequest rejectionRequest);
}
