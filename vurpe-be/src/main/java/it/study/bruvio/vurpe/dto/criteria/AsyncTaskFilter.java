package it.study.bruvio.vurpe.dto.criteria;

import it.study.bruvio.vurpe.entity.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record AsyncTaskFilter(
        UUID id,
        UUID file_id,
        TaskStatus status,
        String error_message,
        LocalDateTime created_at,
        LocalDateTime completed_at,
        Boolean not_completed_at
        ) {

    public boolean isEmpty() {
        return id == null && file_id== null && status == null && error_message== null && created_at == null && completed_at == null && not_completed_at == null;
    }
}
