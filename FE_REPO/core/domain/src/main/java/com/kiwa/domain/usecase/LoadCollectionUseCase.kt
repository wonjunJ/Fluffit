package com.kiwa.domain.usecase

import com.kiwa.domain.repository.CollectionRepository
import javax.inject.Inject

class LoadCollectionUseCase @Inject constructor(
    private val repository: CollectionRepository
) {
    suspend operator fun invoke() = repository.loadCollection()
}
