package com.nckhntu.doantonghiep.Controller.Admin;

import com.nckhntu.doantonghiep.DTO.ServiceDTO;
import com.nckhntu.doantonghiep.Service.ServiceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/services")
public class ServiceController {
    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping
    public String listServices(Model model,
                               @RequestParam(defaultValue = "0") int page,  // Trang mặc định là 0
                               @RequestParam(defaultValue = "20") int size) { // Mặc định mỗi trang 20 dịch vụ
        Page<ServiceDTO> servicePage = serviceService.getAllService(PageRequest.of(page, size));
        model.addAttribute("services", servicePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", servicePage.getTotalPages());
        return "Admin/Service/list"; // Đường dẫn đến template Thymeleaf
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("service", new ServiceDTO());
        return "Admin/Service/add";
    }

    @PostMapping("/add")
    public String addService(@ModelAttribute ServiceDTO serviceDTO, Model model) {
        try {
            serviceService.addService(serviceDTO);
            return "redirect:/admin/services";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "Admin/Service/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        ServiceDTO service = serviceService.getServiceById(id);
        model.addAttribute("service", service);
        return "Admin/Service/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateService(@PathVariable Long id,@ModelAttribute ServiceDTO serviceDTO, Model model) {
        try {
            serviceDTO.setId(id);
            serviceService.updateService(serviceDTO);
            return "redirect:/admin/services";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "Admin/Service/edit";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return "redirect:/admin/services";
    }
}
