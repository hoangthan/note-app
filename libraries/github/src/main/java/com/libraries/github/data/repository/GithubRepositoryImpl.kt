package com.libraries.github.data.repository

import com.git.assessment.utils.toFailureException
import com.git.data.core.model.FailureError
import com.git.data.core.model.Result
import com.libraries.github.data.datasource.remote.GithubApiService
import com.libraries.github.data.datasource.remote.dto.toDomain
import com.libraries.github.domain.model.GithubUser
import com.libraries.github.domain.repository.GithubRepository
import com.libraries.github.domain.usecase.SearchUserError
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.statusCode
import javax.inject.Inject

internal class GithubRepositoryImpl @Inject constructor(
    private val githubApiService: GithubApiService
) : GithubRepository {

    override suspend fun searchUsers(
        query: String,
        limit: Int,
        page: Int
    ): Result<SearchUserError, List<GithubUser>> {
        val response = githubApiService.searchUsers(query = query, perPage = limit, page = page)

        return response.toResult(
            successMapper = { it.items.map { dto -> dto.toDomain() } },
            errorMapper = {
                when (it.statusCode.code) {
                    403 -> SearchUserError.RateLimitExceeded
                    else -> SearchUserError.InvalidQuery
                }
            }
        )
    }

    private inline fun <T, V, E> ApiResponse<T>.toResult(
        crossinline successMapper: (T) -> V,
        crossinline errorMapper: (ApiResponse.Failure.Error) -> E
    ): Result<E, V> where E : FailureError {
        return when (this) {
            is ApiResponse.Success -> Result.Success(successMapper(data))
            is ApiResponse.Failure.Error -> Result.Error(errorMapper(this))
            is ApiResponse.Failure.Exception -> Result.Error(toFailureException())
        }
    }
}
