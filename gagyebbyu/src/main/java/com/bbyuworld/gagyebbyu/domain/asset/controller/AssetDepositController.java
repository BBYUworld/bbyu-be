package com.bbyuworld.gagyebbyu.domain.asset.controller;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetDepositDto;
import com.bbyuworld.gagyebbyu.domain.asset.service.assetDeposit.AssetDepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asset-deposits")
@RequiredArgsConstructor
public class AssetDepositController {
    private final AssetDepositService assetDepositService;

    /**
     * hidden이 0인 값들을 불러오는 거 1: 숨김 2: 보이기
     *
     * @return List<AssetDepositDto> 형태의 사용자가 보이게 설정한 자산 리스트
     */
    @GetMapping
    public ResponseEntity<List<AssetDepositDto>> getAllAssetDeposits() {
        return ResponseEntity.ok(assetDepositService.getAllAssetDeposit());
    }

    /**
     * 은행별 자산을 반환
     *
     * @param bank 은행명
     * @return List 형식으로 해당 은행의 자산 목록 반환
     */
    @GetMapping("/bank/{bank}")
    public ResponseEntity<List<AssetDepositDto>> getAssetDepositsByBank(@PathVariable String bank) {
        return ResponseEntity.ok(assetDepositService.getAssetDepositByBank(bank));
    }

    /**
     * 입출금 계좌, 예금, 적금, (주식?) 의 자산 목록을 반환
     *
     * @param type 위에 적힌 3~4개의 타입
     * @return List 형식의 예적금, 입출금, 주식 계좌들
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<AssetDepositDto>> getAssetDepositsByType(@PathVariable String type) {
        return ResponseEntity.ok(assetDepositService.getAssetDepositByType(type));
    }

    /**
     * 은행별 자산의 종류에 따른 자산 목록 반환
     *
     * @param bank 은행명
     * @param type 자산종류
     * @return List 형식의 자산 내역
     */
    @GetMapping("/bank/{bank}/type/{type}")
    public ResponseEntity<List<AssetDepositDto>> getAssetDepositsByBankAndType(
            @PathVariable String bank,
            @PathVariable String type) {
        return ResponseEntity.ok(assetDepositService.getAssetDepositByBankAndType(bank, type));
    }

    /**
     * 최대 자산이 들어있는 계좌를 반환
     *
     * @return AssetDeposit 최대 금액이 들어있는 계좌 (여기서 type 써서 세분화해도 좋을 듯?)
     */
    @GetMapping("/max")
    public ResponseEntity<AssetDepositDto> getMaxAssetDeposit() {
        return ResponseEntity.ok(assetDepositService.getMaxAssetDeposit());
    }

    /**
     * 숨겨져 있는 계좌가 "n"개 있습니다! 에서 n을 보여주려고 만듦
     *
     * @return Long 몇 개
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getCountByHidden() {
        return ResponseEntity.ok(assetDepositService.getCountByHidden());
    }

    /**
     * Asset ID를 통해 관리하기 위한 코드
     * 클라이언트한태 보여줄 계획은 없음
     *
     * @param assetId IC로 자동 증가되니까 다른거로 변경해도 되고 아니여도 되는데 Asset하고 연동하려고 ID로 함
     * @return ID에 맞는 Deposit 정보
     */
    @GetMapping("/asset/{assetId}")
    public ResponseEntity<AssetDepositDto> getAssetDepositsByAssetId(@PathVariable Long assetId) {
        return ResponseEntity.ok(assetDepositService.getAssetDepositByAssetId(assetId));
    }

    /**
     * 입력하기 위한 코드인데
     * Asset에서 Tranaction을 사용해서 한번에 입력하는게 더 좋을 것 같음
     *
     * @param assetDepositDto 입력할 계좌 정보
     * @return boolean
     */
    @PostMapping
    public ResponseEntity<AssetDepositDto> createAssetDeposit(@RequestBody AssetDepositDto assetDepositDto) {
        // 사용하면 여기 만들어야 함
        return ResponseEntity.ok().build();
    }

    /**
     * 자산에 변경이 있으면 update하기 위한 api
     * 생성과 마찬가지
     *
     * @param asset_deposit_id asset의 PK
     * @param assetDepositDto
     * @return
     */
    @PutMapping("/{asset_deposit_id}")
    public ResponseEntity<AssetDepositDto> updateAssetDeposit(
            @PathVariable Long asset_deposit_id,
            @RequestBody AssetDepositDto assetDepositDto) {
        // update 사용하면 추가 필요
        return ResponseEntity.ok().build();
    }

    /**
     * delete를 위한 api
     * 생성과 마찬가지
     *
     * @param asset_deposit_id asset의 PK
     * @return
     */
    @DeleteMapping("/{asset_deposit_id}")
    public ResponseEntity<Void> deleteAssetDeposit(@PathVariable Long asset_deposit_id) {
        // delete 사용하면 추가 필요
        return ResponseEntity.noContent().build();
    }
}