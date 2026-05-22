package com.belajar.task_api.presentation.web.controllers;

import com.belajar.task_api.application.common.Result;
import com.belajar.task_api.application.dto.requests.SubmitApplicationRequest;
import com.belajar.task_api.application.dto.responses.LicenseApplicationResponse;
import com.belajar.task_api.application.services.ApplicationService;
import com.belajar.task_api.domain.entities.LicenseApplication;
import com.belajar.task_api.presentation.web.exceptions.BusinessException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/applications")
@RequiredArgsConstructor
public class ApplicationContoller {
    private final ApplicationService applicationService;

    @PostMapping("/submit-test")
    public ResponseEntity<?> submit(@Valid @RequestBody SubmitApplicationRequest request)
    {
        return  ResponseEntity.ok("ss");
        //UUID id = applicationService.submitApplication(request);
        //return ResponseEntity.ok(Map.of("id", id));
    }

    @PostMapping("/submit")
    public ResponseEntity<?>  submit_test(@Valid @RequestBody SubmitApplicationRequest request)
    {
        LicenseApplication application = applicationService.submitApplication(request);
        //throw  new BusinessException("salah ide bisnis");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(Result.success(application, "data submitted succesfully"));
    }
    @GetMapping("/my-applications")
    public ResponseEntity<?> getAllMyApplications() {
        // Panggil service untuk mengambil data
        List<LicenseApplicationResponse> data = applicationService.getAllMyApplications();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Result.success(data, "Berhasil mengambil semua data permohonan Anda"));

    }
    @GetMapping("/{id}") // <-- Menangkap ID dari URL, misal: /api/license-applications/e8a35e51-...
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        // Panggil service yang menggunakan logika Optional & Guard Clause Keamanan tadi
        LicenseApplicationResponse data = applicationService.findById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Result.success(data, "Berhasil mengambil  data permohonan Anda"));

    }
    @PostMapping("/{id}/resubmit")
    public ResponseEntity<?> resubmit(@PathVariable UUID id, @Valid @RequestBody SubmitApplicationRequest request) {
            applicationService.resubmitApplication(id, request);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Result.success(null, "Permohonan lisensi Anda berhasil diajukan kembali (Resubmitted)"));
    }
    /*
     * T Result, Exception handling,api versioning,logging  validation
     * Logging still not clear
    */
}
