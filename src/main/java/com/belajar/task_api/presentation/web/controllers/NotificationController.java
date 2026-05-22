package com.belajar.task_api.presentation.web.controllers;

import com.belajar.task_api.application.common.Result;
import com.belajar.task_api.application.dto.responses.NotificationResponse;
import com.belajar.task_api.application.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    @GetMapping
    public CompletableFuture<ResponseEntity<Result<List<NotificationResponse>>>> getNotifications(@RequestParam String persona) {

        return notificationService.getNotifationByPersona(persona)
                .thenApply(data -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(Result.success(data, "Berhasil mengambil data notifikasi secara async"))
                );
    }
}
