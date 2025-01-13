package com.example.doctor.controller;

import com.example.doctor.dto.response.DoctorCountDTO;
import com.example.doctor.services.IDoctorProxyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/doctor/proxy")
public class DoctorProxyController {
    private final IDoctorProxyService proxyService;

    public DoctorProxyController(IDoctorProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @GetMapping("/doctorExists/{doctorId}")
    public boolean doctorExists(@PathVariable long doctorId){
        return proxyService.isDoctorExistsOrNot(doctorId);
    }

    @GetMapping("/doctorCount")
    public DoctorCountDTO getDoctorCount(){
        return proxyService.totalDoctorCount();
    }
}
