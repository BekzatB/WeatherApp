package kz.data.api

interface JsonResponse<M> {
    fun toModel(): M
}