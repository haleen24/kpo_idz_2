package ru.hse.restaurant.data.repository

import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.hse.restaurant.data.entity.Order
import java.util.*

@Repository
interface OrderRepository : CrudRepository<Order, Int> {


    @Query(
        "INSERT INTO \"order\" (user_id, status, price, time)\n" +
                "VALUES (:#{#order.userId}, :#{#order.status}, :#{#order.price}, :#{#order.time })\n" +
                "RETURNING \"order\".id"
    )
    fun insertOrder(@Param("order") order: Order): Int

    @Query(
        "SELECT *\n" +
                "FROM \"order\"\n" +
                "WHERE user_id = :userId\n" +
                "  and status = 'processing'"
    )
    fun findLastChangeable(@Param("userId") userId: Int): Order?

    @Query(
        "SELECT *\n" +
                "FROM \"order\"\n" +
                "WHERE user_id = :user_id\n" +
                "ORDER BY \"order\".id DESC"
    )
    fun findAllByUserId(@Param("user_id") userId: Int): Iterable<Order>

    @Modifying
    @Query(
        "UPDATE \"order\"\n" +
                "SET price=:#{#\n" +
                "order.price}, time =:#{#\n" +
                "order.time}\n" +
                "WHERE id=:#{#\n" +
                "order.id}"
    )
    fun update(order: Order)

    @Modifying
    @Query(
        "UPDATE \"order\"\n" +
                "SET status=:status\n" +
                "WHERE id = :order_id"
    )
    fun updateStatus(@Param("order_id") orderId: Int, @Param("status") status: String)

    @Modifying
    @Query(
        "SELECT status\n" +
                "FROM \"order\"\n" +
                "WHERE id = :id"
    )
    fun getStatus(@Param("id") orderId: Int): String?


    @Query(
        "SELECT \"order\".time\n" +
                "FROM \"order\"\n" +
                "WHERE id = :id"
    )
    fun getTimeById(@Param("id") id: Int): List<Int>

    @Query(
        "SELECT \"order\".id\n" +
                "FROM \"order\"\n" +
                "WHERE status IN ('processing', 'cooking')"
    )
    fun findAllNotDone(): Iterable<Int>

    @Query(
        "SELECT *\n" +
                "FROM \"order\"\n" +
                "WHERE id = :order_id "
    )
    override fun findById(@Param("order_id") orderId: Int): Optional<Order>
}