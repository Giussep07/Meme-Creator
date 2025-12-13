package com.giussepr.memecreator

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform