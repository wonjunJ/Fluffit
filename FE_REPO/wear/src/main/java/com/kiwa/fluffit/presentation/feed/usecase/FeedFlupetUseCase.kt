package com.kiwa.fluffit.presentation.feed.usecase

import com.kiwa.fluffit.presentation.feed.FeedRepository
import javax.inject.Inject

class FeedFlupetUseCase @Inject constructor(
    private val repository: FeedRepository
) {
    suspend operator fun invoke(foodId: Int) = repository.feedFlupet(foodId)
}
