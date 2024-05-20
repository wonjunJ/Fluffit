package com.kiwa.domain.usecase

import com.kiwa.domain.repository.RankingRepository
import javax.inject.Inject

class GetAgeRankingUseCase @Inject constructor(
    private val rankingRepository: RankingRepository
) {
    suspend operator fun invoke() = rankingRepository.getAgeRanking()
}
