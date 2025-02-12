package com.nckhntu.doantonghiep.IMPL;

import com.nckhntu.doantonghiep.DTO.ServiceDTO;
import com.nckhntu.doantonghiep.Entity.LogEntity;
import com.nckhntu.doantonghiep.Entity.ServiceEntity;
import com.nckhntu.doantonghiep.Entity.UserEntity;
import com.nckhntu.doantonghiep.Repository.LogRepository;
import com.nckhntu.doantonghiep.Repository.ServiceRepository;
import com.nckhntu.doantonghiep.Repository.UserRepository;
import com.nckhntu.doantonghiep.Service.ServiceService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceIMPL implements ServiceService {
    private final ServiceRepository serviceRepository;
    private final ModelMapper modelMapper;
    private final LogRepository logRepository;
    private final UserRepository userRepository;

    public ServiceIMPL(ServiceRepository serviceRepository, ModelMapper modelMapper, LogRepository logRepository, UserRepository userRepository) {
        this.serviceRepository = serviceRepository;
        this.modelMapper = modelMapper;
        this.logRepository = logRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<ServiceDTO> getAllService(Pageable pageable) {
        try {
            Page<ServiceEntity> entities = serviceRepository.findAll(pageable);
                // Lọc danh sách không có deleteAt
            List<ServiceDTO> dtoList = entities.stream()
                    .filter(entity -> entity.getDeleteAt() == null)
                    .map(entity -> modelMapper.map(entity, ServiceDTO.class))
                    .toList();
//            String email = SecurityContextHolder.getContext().getAuthentication().getName();
//            UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
//
//            LogEntity log = new LogEntity();
//            log.setUserId(user);
//            log.setAction("Người dùng "+user.getFullName()+" với email là "+user.getEmail()+" đã truy cập vào trang danh sách dịch vụ!");
//            log.setCreatedAt(Timestamp.from(Instant.now()));
//            logRepository.save(log);
            return new PageImpl<>(dtoList, pageable, dtoList.size());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ServiceDTO getServiceById(Long id) {
        try {
            ServiceEntity serviceEntity = serviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy dịch dụ nào tương tự"));
            ServiceDTO dto = modelMapper.map(serviceEntity, ServiceDTO.class);
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
//            UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
//
//            LogEntity log = new LogEntity();
//            log.setUserId(user);
//            log.setAction("Người dùng "+user.getFullName()+" với email là "+user.getEmail()+" đã xem chi tiết thông tin dịch vụ "+serviceEntity.getName());
//            log.setCreatedAt(Timestamp.from(Instant.now()));
//            logRepository.save(log);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ServiceDTO addService(ServiceDTO serviceDTO) {
        try {
            boolean isMatchName = serviceRepository.existsByNameAndDeleteAtIsNull(serviceDTO.getName());
            if (isMatchName) {
                throw new RuntimeException("Dịch vụ này đã tồn tại.");
            }
            ServiceEntity serviceEntity = modelMapper.map(serviceDTO, ServiceEntity.class);
            serviceEntity = serviceRepository.save(serviceEntity);
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
//            UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
//
//            LogEntity log = new LogEntity();
//            log.setUserId(user);
//            log.setAction("Người dùng "+user.getFullName()+" với email là "+user.getEmail()+" đã thêm mới dịch vụ "+serviceEntity.getName());
//            log.setCreatedAt(Timestamp.from(Instant.now()));
//            logRepository.save(log);
            return modelMapper.map(serviceEntity, ServiceDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ServiceDTO updateService(ServiceDTO serviceDTO) {
        try {
            ServiceEntity serviceEntity = serviceRepository.findById(serviceDTO.getId()).orElseThrow(() -> new RuntimeException("Không tìm thấy dịch vụ nào tương tự"));
            boolean isMatchName = serviceRepository.existsByNameAndDeleteAtIsNull(serviceDTO.getName());
            if (isMatchName) {
                throw new RuntimeException("Dịch vụ này đã tồn tại.");
            }
            serviceEntity.setName(serviceDTO.getName());
            serviceEntity.setDescription(serviceDTO.getDescription());
            serviceEntity.setDuration(serviceDTO.getDuration());
            serviceRepository.save(serviceEntity);
            return modelMapper.map(serviceEntity, ServiceDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteService(Long id) {
        try {
            ServiceEntity serviceEntity = serviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy dịch vụ nào tương tự"));
            serviceEntity.setDeleteAt(Timestamp.from(Instant.now()));
            serviceRepository.save(serviceEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
