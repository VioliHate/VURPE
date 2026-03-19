package it.study.bruvio.vurpe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.study.bruvio.vurpe.dto.response.PayloadResponse;
import it.study.bruvio.vurpe.dto.response.StatusCountDTO.DashBoardStatsDTO;
import it.study.bruvio.vurpe.service.DashboardService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/call")
@RequiredArgsConstructor
public class DashBoardStatsController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard-stats")
    public ResponseEntity<PayloadResponse<DashBoardStatsDTO>> getDashboardStats() {
        DashBoardStatsDTO stats = dashboardService.getDashboardMetrics();
        PayloadResponse<DashBoardStatsDTO> response = PayloadResponse.success(stats,
                "Dashboard metrics arrivati correttamente");
        return ResponseEntity.ok(response);

    }

}
