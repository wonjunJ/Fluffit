package com.ssafy.fluffitflupet.controller

import com.ssafy.fluffitflupet.service.FoodService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.coroutines.CoroutineContext

@RestController
@RequestMapping("/food")
class FoodController(
    val foodService: FoodService
): CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

//    @GetMapping()
}