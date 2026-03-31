package it.study.bruvio.vurpe.dto.response;

import it.study.bruvio.vurpe.entity.AsyncTask;
import it.study.bruvio.vurpe.entity.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record AsyncTaskResponse(
        UUID id,
        UUID fileId,
        TaskStatus status,
        String errorMessage,
        LocalDateTime createdAt,
        LocalDateTime completedAt
) {
    public static AsyncTaskResponse fromEntity(AsyncTask task){
        return new AsyncTaskResponse(
                task.getId(),
                task.getFileId(),
                task.getStatus(),
                task.getErrorMessage(),
                task.getCreatedAt(),
                task.getCompletedAt()
        );
    }
}
