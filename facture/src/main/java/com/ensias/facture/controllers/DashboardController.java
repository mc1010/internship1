package com.ensias.facture.controllers;

import com.ensias.facture.dto.DashboardDTO;
import com.ensias.facture.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardDTO> getDashboardStats() {
        DashboardDTO stats = dashboardService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }
}

