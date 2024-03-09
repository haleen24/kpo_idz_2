package ru.hse.restaurant.data.repository

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.hse.restaurant.data.entity.OrderToDishJoinWithDish
import ru.hse.restaurant.data.entity.Dish

@Repository
interface OrderToDishRepository : CrudRepository<Dish, Int> {

    @Query("INSERT INTO \"order_to_dish\" (user_id, order_id, dish_id, quantity)\n" +
            "VALUES (:user_id, :order_id, :dish_id, :quantity)\n" +
            "ON CONFLICT (order_id, dish_id) DO UPDATE SET quantity = excluded.quantity + \"order_to_dish\".quantity\n" +
            "RETURNING \"order_to_dish\".id")
    fun insert(
        @Param("user_id") userId: Int,
        @Param("order_id") orderId: Int,
        @Param("dish_id") dishId: Int,
        @Param("quantity") quantity: Int
    ): Int

    @Query("SELECT *\n" +
            "FROM \"order_to_dish\"\n" +
            "         JOIN \"menu\" ON dish_id = menu.id\n" +
            "WHERE order_id = :id")
    fun findAllDishesByOrderId(@Param("id") orderId: Int): List<OrderToDishJoinWithDish>
}