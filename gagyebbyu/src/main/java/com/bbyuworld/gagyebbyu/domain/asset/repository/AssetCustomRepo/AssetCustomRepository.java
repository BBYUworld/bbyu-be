package com.bbyuworld.gagyebbyu.domain.asset.repository.AssetCustomRepo;

public interface AssetCustomRepository {

    Long findTotalAssetsForCouple(Long coupleId);

    double findAverageAssetsForEligibleCouples(int startAge, int endAge, long startIncome, long endIncome);

}
