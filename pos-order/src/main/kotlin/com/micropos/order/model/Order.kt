package com.micropos.order.model

import com.micropos.carts.dto.ItemDto
import com.micropos.carts.dto.OrderDto
import com.micropos.carts.model.Item

enum class OrderStatus {
    Pending,
    Processing,
    Completed,
    Cancelled,
    Unknown,
}

data class Order(
    val id: String,
    val items: List<Item>,
    val status: OrderStatus
) {
    fun toDto(): OrderDto {
        val dto = OrderDto()
        dto.id = id
        dto.status = status.name
        dto.items = items.map {
            val item = ItemDto()
            item.product = it.product
            item.quantity = it.quantity.toBigDecimal()
            item
        }
        return dto
    }

    companion object {
        fun fromDto(dto: OrderDto): Order {
            return Order(
                id = dto.id,
                items = dto.items.map { Item(it.product, it.quantity.intValueExact()) },
                status = try {
                    OrderStatus.valueOf(dto.status)
                } catch (e: IllegalArgumentException) {
                    OrderStatus.Unknown
                }
            )
        }
    }
}
