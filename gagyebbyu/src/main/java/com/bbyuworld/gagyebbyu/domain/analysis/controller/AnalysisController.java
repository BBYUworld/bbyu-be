package com.bbyuworld.gagyebbyu.domain.analysis.controller;

import com.bbyuworld.gagyebbyu.domain.analysis.dto.response.AnalysisAssetCategoryDto;
import com.bbyuworld.gagyebbyu.domain.analysis.service.AnalysisService;
import com.bbyuworld.gagyebbyu.global.jwt.RequireJwtToken;
import com.bbyuworld.gagyebbyu.global.jwt.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;

    /**
     * 커플 자산 카테고리별 퍼센트 조회
     * @return 자산 카테고리별 금액과 퍼센트
     */
    @GetMapping(path = "/couple-asset", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireJwtToken
    public ResponseEntity<List<AnalysisAssetCategoryDto>> getAssetPercentage() {
        return ResponseEntity.ok(analysisService.getAssetPercentage(UserContext.getUserId()));
    }
}
