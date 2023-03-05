package com.example.begin.repository

import com.example.begin.domain.UserInfo
import org.springframework.beans.factory.annotation.Configurable
import org.springframework.data.jpa.repository.JpaRepository

@Configurable
interface UserRepository:JpaRepository<UserInfo, Long> {

    fun findByName(name: String): UserInfo?

}