package ru.hse.restaurant.service.kitchen

import ru.hse.restaurant.data.repository.OrderRepository

class LowTimeCook(
    private val maxTime: Int,
    private val list: MutableList<Int>,
    orderRepository: OrderRepository
) :
    AbstractCook(orderRepository) {

    override fun getCurrentId(): Int {

        var curId: Int

        synchronized(list) {

            var ind = -1

            for (item in 0..<list.size) {

                if (getTimeById(list[item]) <= maxTime) {

                    ind = item

                    break
                }
            }

            curId = if (ind == -1) {
                -1
            } else {
                list.removeAt(ind)
            }
        }
        return curId
    }

    override fun copy(): AbstractCook = LowTimeCook(maxTime, list, orderRepository)


}
