package ru.hse.restaurant.service.kitchen


import ru.hse.restaurant.data.repository.OrderRepository

open class Cook(private var list: MutableList<Int>, orderRepository: OrderRepository) :
    AbstractCook(orderRepository) {

    override fun getCurrentId(): Int {

        val curId: Int

        synchronized(list) {

            curId = list.removeFirstOrNull() ?: -1
        }

        return curId
    }

    override fun copy(): AbstractCook = Cook(list, orderRepository)
}


