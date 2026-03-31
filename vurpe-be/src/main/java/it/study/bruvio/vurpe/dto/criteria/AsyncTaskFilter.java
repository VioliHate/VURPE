package it.study.bruvio.vurpe.dto.criteria;

import it.study.bruvio.vurpe.entity.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record AsyncTaskFilter(
        UUID id,
        UUID fileId,
        TaskStatus status,
        String errorMessage,
        LocalDateTime createdAt,
        LocalDateTime completedAt,
        Boolean notCompletedAt
        ) {

    public boolean isEmpty() {
        return id == null && fileId== null && status == null && errorMessage== null && createdAt == null && completedAt == null && notCompletedAt == null;
    }
}
