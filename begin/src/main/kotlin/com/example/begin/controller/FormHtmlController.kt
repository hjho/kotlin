package com.example.begin.controller

import com.example.begin.domain.UserInfo
import com.example.begin.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class FormHtmlController {

    val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    lateinit var userRepository: UserRepository

//    @RequestMapping(value = ["/form"], method = [RequestMethod.GET])
//    fun index(): String = "form/index"

    @RequestMapping(value = ["/form", "/form/{userId}"], method = [RequestMethod.GET])
    fun get(@PathVariable("userId") userId: Long?, model: Model
    ): String {

        val userInfo = if(userId == null) {
            UserInfo(null, null, null)
//            userInfo2.name = "테스터"
//            userInfo2.email = "tester@test.com"
//            userInfo2.password = "tester"
        } else {
            userRepository.findByIdOrNull(userId)
        }

        log.info("SELECT {}", userInfo)
        model.addAttribute("user", userInfo)
        return "form/index"
    }

    @RequestMapping(value = ["/form"], method = [RequestMethod.POST])
    fun post(userInfo: UserInfo?, model: Model
    ): String {
        if(userInfo?.name == null) {
            model.addAttribute("user", UserInfo())
            return "form/index"
        }
        userInfo?.let {
            log.info("INSERT {}", it)
            val bcrypt = BCryptPasswordEncoder(11)
            it.pwd = bcrypt.encode(it.pwd)
            userRepository.save(userInfo)
        }
        model.addAttribute("user", UserInfo())
        return "form/index"
    }

}