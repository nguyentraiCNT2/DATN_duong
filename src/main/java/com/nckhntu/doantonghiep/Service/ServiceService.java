package com.nckhntu.doantonghiep.Service;

import com.nckhntu.doantonghiep.DTO.ServiceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServiceService {
    Page<ServiceDTO> getAllService(Pageable pageable);
    ServiceDTO getServiceById(Long id);
    ServiceDTO addService(ServiceDTO serviceDTO);
    ServiceDTO updateService(ServiceDTO serviceDTO);
    void deleteService(Long id);
}
