package ru.hse.restaurant.service.kitchen

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.hse.restaurant.data.repository.OrderRepository
import ru.hse.restaurant.service.api.KitchenApi

@Component
class Kitchen : KitchenApi {

    val numberOfCooks: Int = 7

    val lowTime: Int = 30

    private final var orderIdList: MutableList<Int> = mutableListOf()

    private final var cookList: MutableList<AbstractCook> = mutableListOf()

    @Autowired
    final lateinit var orderRepository: OrderRepository

    override fun addOrder(orderId: Int) {

        orderIdList.add(orderId)

    }


    override fun cancelOrder(orderId: Int): Boolean {

        val cook = cookList.indexOfFirst { it.inProcess.contains(orderId) }

        if (cook != -1) {

            try {

                cookList[cook].interrupt()

            } finally {

                cookList[cook].inProcess = mutableSetOf()

                cookList[cook] = cookList[cook].copy()

                cookList[cook].start()
            }

            orderRepository.updateStatus(orderId, "canceled")

            return true
        }

        synchronized(orderIdList) {

            if (!orderIdList.contains(orderId)) {

                return false
            }

            orderIdList.remove(orderId)

            orderRepository.updateStatus(orderId, "canceled")
        }
        return true
    }

    override fun getStatus(orderId: Int): String = orderRepository.getStatus(orderId) ?: "Not Exist"

    @PostConstruct
    private final fun mainHolder() {

        for (i in 0..<numberOfCooks - 1) {

            cookList.add(Cook(orderIdList, orderRepository))

            cookList[i].start()
        }

        cookList.add(LowTimeCook(lowTime, orderIdList, orderRepository))

        cookList.last().start()

        orderIdList.addAll(orderRepository.findAllNotDone())
    }
}