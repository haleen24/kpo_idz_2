package ru.hse.restaurant.data.repository

import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.hse.restaurant.data.entity.User

@Repository
interface UserRepository : CrudRepository<User, String> {

    @Query(
        "SELECT *\n" +
                "FROM \"user\"\n" +
                "WHERE login = :name"
    )
    fun findByName(@Param("name") login: String): User?

    @Modifying
    @Query(
        "INSERT INTO \"user\" (login, password, root)\n" +
                "VALUES (:login, :password, :root)"
    )
    fun addUser(
        @Param("login") login: String,
        @Param("password") password: String,
        @Param("root") root: String
    )

    @Query("SELECT id,login, root FROM \"user\"")
    fun getData(): List<User>
}