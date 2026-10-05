package com.baggageservice.controller;

import com.baggageservice.service.BaggageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/baggage")
public class BaggageController {
    private final BaggageService baggageService;




}
