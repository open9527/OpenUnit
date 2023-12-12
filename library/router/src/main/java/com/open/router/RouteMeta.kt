package com.open.router

class RouteMeta(
    val destination: Class<*>,
    val type: RouteType,
){
    override fun toString(): String {
        return "RouteMeta(destination=$destination, type=$type)"
    }
}