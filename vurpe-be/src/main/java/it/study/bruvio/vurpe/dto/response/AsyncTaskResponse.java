package it.study.bruvio.vurpe.dto.response;

import it.study.bruvio.vurpe.entity.AsyncTask;
import it.study.bruvio.vurpe.entity.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record AsyncTaskResponse(
        UUID id,
        UUID file_id,
        TaskStatus status,
        String error_message,
        LocalDateTime created_at,
        LocalDateTime completed_at
) {
    public static AsyncTaskResponse fromEntity(AsyncTask task){
        return new AsyncTaskResponse(
                task.getId(),
                task.getFile_id(),
                task.getStatus(),
                task.getError_message(),
                task.getCreated_at(),
                task.getCompleted_at()
        );
    }
}
