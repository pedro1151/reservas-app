package com.optic.pramosfootballappz.domain.useCase.trivias

import com.optic.pramosfootballappz.domain.repository.TriviasRepository


class GetDificultysUC constructor(private val repository: TriviasRepository) {
    suspend operator fun invoke() = repository.getDificultys()
}