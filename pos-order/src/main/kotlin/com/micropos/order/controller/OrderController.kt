package com.micropos.order.controller

import com.micropos.carts.api.OrderApi
import com.micropos.carts.dto.ItemDto
import com.micropos.carts.dto.OrderDto
import com.micropos.order.model.OrderStatus
import com.micropos.order.service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class OrderController : OrderApi {
    @Autowired
    lateinit var orderService: OrderService

    override fun createOrder(items: MutableList<ItemDto>?): ResponseEntity<OrderDto> {
        items?.let {
            val order = orderService.createOrder(it)
            return ResponseEntity.ok(order.toDto())
        }
        return ResponseEntity.internalServerError().build()
    }

    override fun getAllOrders(): ResponseEntity<MutableList<OrderDto>> {
        val orders = orderService.getAllOrders()
        return ResponseEntity.ok(orders.map { it.toDto() }.toMutableList())
    }

    override fun getOrderById(orderId: String?): ResponseEntity<OrderDto> {
        orderId?.let {
            val order = orderService.getOrderById(it)
            return order?.let { o -> ResponseEntity.ok(o.toDto()) } ?: ResponseEntity.notFound().build()
        }
        return ResponseEntity.notFound().build()
    }

    override fun removeOrderById(orderId: String?): ResponseEntity<OrderDto> {
        orderId?.let {
            val order = orderService.deleteOrder(it)
            return order?.let { o -> ResponseEntity.ok(o.toDto()) } ?: ResponseEntity.notFound().build()
        }
        return ResponseEntity.notFound().build()
    }

    override fun updateOrderStatusById(orderId: String?, status: String?): ResponseEntity<OrderDto> {
        if (orderId == null || status == null) {
            return ResponseEntity.badRequest().build()
        }
        val order = orderService.updateOrderStatus(orderId, OrderStatus.valueOf(status))
        return order?.let { o -> ResponseEntity.ok(o.toDto()) } ?: ResponseEntity.notFound().build()
    }
}
