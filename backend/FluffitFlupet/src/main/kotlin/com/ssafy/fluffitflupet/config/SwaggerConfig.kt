package com.ssafy.fluffitflupet.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.utils.SpringDocUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.server.WebSession

@Configuration
class SwaggerConfig {
    init {
        SpringDocUtils.getConfig().addRequestWrapperToIgnore(
            WebSession::class.java,
        )
    }

    @Bean
    fun openAPI(): OpenAPI{
        return OpenAPI()
            .info(Info()
                .title("Flupet 서비스 API")
                .description("마이크로서비스에서 Flupet 관련 API를 제공"))
    }
}