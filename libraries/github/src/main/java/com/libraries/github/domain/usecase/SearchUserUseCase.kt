package com.libraries.github.domain.usecase

import com.git.data.core.model.FailureError
import com.git.data.core.model.Result
import com.libraries.github.domain.model.GithubUser
import com.libraries.github.domain.repository.GithubRepository
import javax.inject.Inject

sealed interface SearchUserError : FailureError {
    object RateLimitExceeded : SearchUserError
    object InvalidQuery : SearchUserError
}

data class SearchUserParam(
    val keyword: String,
    val limit: Int,
    val page: Int,
)

class SearchUserUseCase @Inject constructor(
    private val githubRepository: GithubRepository
) {

    suspend operator fun invoke(param: SearchUserParam): Result<SearchUserError, List<GithubUser>> {
        return githubRepository.searchUsers(
            query = param.keyword,
            limit = param.limit,
            page = param.page,
        )
    }
}
