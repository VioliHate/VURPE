package it.study.bruvio.vurpe.dto.response;

import java.util.List;

import lombok.Data;

public record StatusCountDTO(
        Object status,
        long count) {

    @Data
    public static class DashBoardStatsDTO {
        public List<StatusCountDTO> filesStats;
        public List<StatusCountDTO> tasksStats;
        public List<StatusCountDTO> DataRecordStats;

    }

}
