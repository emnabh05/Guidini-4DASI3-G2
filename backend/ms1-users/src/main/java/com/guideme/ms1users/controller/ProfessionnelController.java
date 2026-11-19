package com.guideme.ms1users.controller;

import com.guideme.ms1users.dto.ProfessionnelResponse;
import com.guideme.ms1users.service.ProfessionnelSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professionnels")
@RequiredArgsConstructor
public class ProfessionnelController {

    private final ProfessionnelSearchService service;

    @GetMapping
    public List<ProfessionnelResponse> rechercher(@RequestParam(required = false) String type,
                                                  @RequestParam(required = false) String langue) {
        return service.rechercher(type, langue);
    }
}