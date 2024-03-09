package ru.hse.restaurant.service.kitchen

import ru.hse.restaurant.data.repository.OrderRepository

abstract class AbstractCook(val orderRepository: OrderRepository) : Thread() {

    var inProcess = mutableSetOf<Int>()

    abstract fun getCurrentId(): Int

    fun getTimeById(curId: Int): Int = orderRepository.getTimeById(curId)[0]

    override fun run() {

        try {
            while (true) {

                val curId: Int = getCurrentId()

                if (curId == -1) {
                    continue
                }

                inProcess.add(curId)

                val time = getTimeById(curId)

                cooking(time, curId)

                inProcess.remove(curId)
            }
        } catch (ex: InterruptedException) {

            currentThread().interrupt()

        }
    }

    private fun cooking(time: Int, curId: Int) {

        orderRepository.updateStatus(curId, "cooking")

        sleep(1000 * time.toLong())

        orderRepository.updateStatus(curId, "done")
    }


    abstract fun copy(): AbstractCook
}