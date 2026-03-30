package it.study.bruvio.vurpe.service;

import org.springframework.stereotype.Service;

import it.study.bruvio.vurpe.dto.response.StatusCountDTO.DashBoardStatsDTO;
import it.study.bruvio.vurpe.repository.AsyncTaskRepository;
import it.study.bruvio.vurpe.repository.DataRecordRepository;
import it.study.bruvio.vurpe.repository.FilesRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final AsyncTaskRepository taskRepo;
    private final FilesRepository fileRepo;
    private final DataRecordRepository dataRecordRepo;

    public DashBoardStatsDTO getDashboardMetrics() {
        DashBoardStatsDTO dto = new DashBoardStatsDTO();
        dto.setTasksStats(taskRepo.countTasksByStatus());
        dto.setFilesStats(fileRepo.countFilesByStatus());
        dto.setDataRecordStats(dataRecordRepo.countDataRecordsByRiskFlag());
        return dto;
    }

}
