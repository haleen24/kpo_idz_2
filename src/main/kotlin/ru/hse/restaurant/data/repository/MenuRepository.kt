package ru.hse.restaurant.data.repository

import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.hse.restaurant.data.entity.Dish
import ru.hse.restaurant.data.entity.DishStat

@Repository
interface MenuRepository : CrudRepository<Dish, String> {

    @Query(
        "SELECT *\n" +
                "FROM \"menu\"\n" +
                "WHERE id = :id"
    )
    fun findById(@Param("id") id: Int): Dish?

    @Modifying
    @Query(
        value = "INSERT INTO \"menu\" (name, amount_of, price, seconds)\n" +
                "VALUES (:name, :amount_of, :price, :seconds)",
    )
    fun addDish(
        @Param("name") name: String,
        @Param("amount_of") amountOf: Int,
        @Param("price") price: Int,
        @Param("seconds") seconds: Int
    )

    @Query("SELECT *\n" +
            "FROM \"menu\"\n" +
            "ORDER BY id")
    override fun findAll(): Iterable<Dish>

    @Modifying
    @Query("UPDATE \"menu\"\n" +
            "SET amount_of = :amount\n" +
            "WHERE id = :id")
    fun updateById(@Param("id") id: Int, @Param("amount") amountOf: Int)

    @Modifying
    @Query("UPDATE \"menu\"\n" +
            "SET amount_of= amount_of + :amount\n" +
            "WHERE id = :id")
    fun updateOnValueById(@Param("id") id: Int, @Param("amount") amountOf: Int)

    @Modifying
    @Query("DELETE\n" +
            "FROM \"menu\"\n" +
            "WHERE id = :id ")
    fun deleteById(@Param("id") id: Int)

    @Query("SELECT COUNT(id)\n" +
            "FROM \"menu\"\n" +
            "WHERE id IN (:ids)")
    fun findAllId(@Param("ids") ids: List<Int>): Int

    @Query("SELECT COUNT(id)\n" +
            "FROM \"menu\"\n" +
            "WHERE amount_of < 0")
    fun findAllLessThanZero(): Int

    @Query("SELECT \"menu\".name\n" +
            "FROM \"menu\"\n" +
            "WHERE id = :id")
    fun getDishNameById(@Param("id") dishId: Int): String?

    @Query(
        "SELECT menu.id,\n" +
                "       menu.name,\n" +
                "       sum(coalesce(quantity, 0))              AS amount_of,\n" +
                "       avg(r.avg_rate)                         AS avg_rate,\n" +
                "\n" +
                "       sum(coalesce(quantity, 0)) * menu.price AS revenue\n" +
                "FROM menu\n" +
                "         LEFT JOIN order_to_dish ON menu.id = order_to_dish.dish_id\n" +
                "         LEFT JOIN\n" +
                "     (SELECT dish_id, avg(rate) as avg_rate\n" +
                "      FROM review\n" +
                "      GROUP BY dish_id) r ON r.dish_id = menu.id\n" +
                "         LEFT JOIN public.order\n" +
                "                   ON public.order.id = order_to_dish.order_id\n" +
                "WHERE status = 'payed'\n" +
                "GROUP BY menu.id"
    )
    fun getAllWithQuantity(): List<DishStat>
}