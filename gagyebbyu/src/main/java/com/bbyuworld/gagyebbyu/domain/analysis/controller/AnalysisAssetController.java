package com.bbyuworld.gagyebbyu.domain.analysis.controller;

import com.bbyuworld.gagyebbyu.domain.analysis.dto.response.AnalysisAssetCategoryDto;
import com.bbyuworld.gagyebbyu.domain.analysis.dto.response.AnnualAssetDto;
import com.bbyuworld.gagyebbyu.domain.analysis.entity.AnnualAsset;
import com.bbyuworld.gagyebbyu.domain.analysis.service.AnalysisAssetService;
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
public class AnalysisAssetController {

    private final AnalysisAssetService analysisAssetService;

    /**
     * 커플 자산 카테고리별 퍼센트 조회
     * @return 자산 카테고리별 금액과 퍼센트
     */
    @GetMapping(path = "/couple-asset", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireJwtToken
    public ResponseEntity<List<AnalysisAssetCategoryDto>> getAssetPercentage() {
        return ResponseEntity.ok(analysisAssetService.getAssetPercentage(UserContext.getUserId()));
    }

    @GetMapping(path = "/couple-asset/annual", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequireJwtToken
    public ResponseEntity<List<AnnualAssetDto>> getAnnualAssets() {
        return ResponseEntity.ok(analysisAssetService.getAnnualAsset(UserContext.getUserId()));
    }

}
