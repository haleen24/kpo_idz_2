package ru.hse.restaurant.data.repository

import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.hse.restaurant.data.entity.Review

@Repository
interface ReviewRepository : CrudRepository<Review, Int> {

    @Modifying
    @Query("INSERT INTO \"review\" (user_id, dish_id, order_id, rate, review)\n" +
            "VALUES (:user_id, :dish_id, :order_id, :rate, :review)\n" +
            "ON CONFLICT (user_id, order_id, dish_id) DO UPDATE SET rate=:rate,\n" +
            "                                                       review=:review\n" +
            "\n" +
            "\n")
    fun insertReview(
        @Param("user_id") userId: Int,
        @Param("dish_id") dishId: Int,
        @Param("order_id") orderId: Int,
        @Param("rate") rate: Int,
        @Param("review") review: String
    )

    @Query("SELECT *\n" +
            "FROM \"review\"\n" +
            "WHERE dish_id = :id")
    fun findAllByDishId(@Param("id") dishId: Int): List<Review>

    @Modifying
    @Query("DELETE\n" +
            "FROM \"review\"\n" +
            "WHERE id = :id")
    fun removeById(@Param("id") id: Int)
}