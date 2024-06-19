package com.example.diplom

import android.content.Context
import android.util.Log
import androidx.room.PrimaryKey
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.rpc
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class db_Simple_data(
    val id: Int,
    val name: String
)


@Serializable
data class db_Ingredients_category(
    val id: Int,
    val created_at: String?,
    val name: String,
    val img: String?
)

@Serializable
data class db_Ingredients_types(
    val id: Int,
    val created_at: String?,
    val name: String,
    val img: String?
)

@Serializable
data class db_Ingredients(
    val id: Int,
    val name: String,
    var price: Double,
    var table1: Double,
    var table2: Double,
    var table3: Double,
    var table4: Double,
    val img: String?
)

@Serializable
data class db_Ingredient_detail(
    val id: Int,
    val created_at: String?,
    val name: String,
    val price: Double,
    val ingredients_category: db_Ingredients_category,
    val detail: String,
    val table1: Double,
    val table2: Double,
    val table3: Double,
    val table4: Double,
    val shelf_life: Int,
    val img: String?
)

@Serializable
data class db_Dish_mealtime(
    val id: Int,
    val name: String
)

@Serializable
data class db_Dish_category(
    val id: Int,
    val created_at: String?,
    val name: String,
    val img: String?
)

@Serializable
data class db_Dish(
    val id: Int,
    val created_at: String?,
    val name: String,
    val img: String?,
    val time: String,
    val desc: String,
    val mealtime_id: Int? = null,
    val category_id: Int,
    val measurement: db_Measurement? = null
)

@Serializable
data class db_Recipes(
    val id: Int,
    val created_at: String?,
    val name: String,
    val desc: String? = null,
    val count: Double,
    val price: Double,
    val table1: Float,
    val table2: Float,
    val table3: Float,
    val table4: Float,
    val recipes_and_ingredients: List<db_Recipes_and_ingredients>? = null
)

@Serializable
data class db_Recipes_and_dish(
    val recipes: db_Recipes,
    val id_dish: Int
)

@Serializable
data class db_Recipes_and_ingredients(
    val id_recipes: Int,
    val ingredients: db_Ingredients? = null,
    var count: Double,
    val required: Boolean,
    val measurement: db_Measurement? = null
)

@Serializable
data class db_Measurement(
    val id: Int,
    val created_at: String?,
    val name: String,
    val full_name: String
)

@Serializable
data class db_Users(
    val uid: String,
    val name: String?,
    val height: Int,
    val weight: Double,
    val date_of_birth: LocalDate,
    val is_male: Boolean,
    val number_of_calories: Int? = null,
    var allergies: List<db_Allergies> = emptyList(),
    var user_and_eating: MutableList<db_Users_and_eating> = mutableListOf()
) : java.io.Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}

@Serializable
data class db_Allergies(
    val id: Int,
    val name: String
)

@Serializable
data class db_Users_and_allergies(
    val id_user: Int,
    val id_allergies: Int,
    val allergies: db_Allergies? = null
)

@Serializable
data class db_Users_and_eating(
    var created_at: String? = null,
    var id_user: Int,
    var id_dish: Int,
    var id_recipes: Int,
    var id_mealtime: Int,
    var count: Double,
    var date: LocalDate,
    var dish: db_Dish? = null,
    var recipes: db_Recipes? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}

@Serializable
data class db_FilterData(
    @SerialName("user_id") var userId: Int? = null,
    @SerialName("category_ids") var categoryArr: List<Int> = emptyList(),
    @SerialName("min_price") var priceBegin: Double? = null,
    @SerialName("max_price") var priceEnd: Double? = null,
    @SerialName("max_time") var time: String? = null,
    @SerialName("search_text") var searchText: String? = null,
)

@Serializable
data class rcp_Recipes(
    var param_id_mealtime: Int,
    var param_id_dish: Int,
    var param_id_user: Int,
    var param_date: String
)

data class db_Dish_add_fragment(
    var id: Int? = null,
    val mealtime_id: Int? = null,
    val dish: db_Dish,
    val recipe: List<db_Recipes>,
    val recipe_position: Int,
    val measure: db_Measurement,
    val countDish: Double
)

@Serializable
data class rcp_Eat_unique_recipes(
    val id_dish: Int,
    val recipes: List<db_Simple_data>
)

val supabase = createSupabaseClient(
    supabaseUrl = BuildConfig.supabaseUrl,
    supabaseKey = BuildConfig.supabaseKey
) {
    install(Postgrest)
    install(Auth)
}

val allergies = listOf(
    db_Allergies(
        id = 1,
        name = "Глютен"),
    db_Allergies(
        id = 2,
        name = "Молочные продукты"),
    db_Allergies(
        id = 3,
        name = "Яйца"),
    db_Allergies(
        id = 4,
        name = "Орехи"),
    db_Allergies(
        id = 5,
        name = "Морепродукты"),
    db_Allergies(
        id = 6,
        name = "Соя"),
    db_Allergies(
        id = 7,
        name = "Фрукты"),
    )

class Supabase {
    suspend fun getMealtime(): List<db_Simple_data> {
        return supabase.postgrest["mealtime"].select().decodeList<db_Simple_data>()
    }

    suspend fun getMeasurement(): List<db_Simple_data> {
        return supabase.postgrest["measurement"].select().decodeList<db_Simple_data>()
    }

    suspend fun getEatUniqueRecipes(): List<rcp_Eat_unique_recipes>{
        val json = supabase.postgrest.rpc("get_dish_recipes_by_user", mapOf("p_id_user" to App.sessionData!!.id))

        if (json.data == "null") {
            return emptyList()
        }

        return json.decodeList<rcp_Eat_unique_recipes>()
    }

    suspend fun getIngredients(type_id: Int): List<db_Ingredients> {
        return supabase.postgrest["ingredients"].select(){
            filter {
                eq("type_id", type_id)
            }
        }.decodeList<db_Ingredients>()
    }

    suspend fun getCategoryIngredients(): List<db_Ingredients_category> {
        return supabase.postgrest["ingredients_category"].select().decodeList<db_Ingredients_category>()
    }

    suspend fun getTypeIngredients(category_id: Int): List<db_Ingredients_types> {
        return supabase.postgrest["ingredients_types"].select(){
            filter {
                eq("category_id", category_id)
            }
        }.decodeList<db_Ingredients_types>()
    }

    suspend fun getCurrentIngredient(ingredient_id: Int): db_Ingredient_detail {
        val columns = Columns.raw("""id, created_at, name, price, ingredients_category (id, created_at, name, img), detail, table1, table2, table3, table4, shelf_life, img""".trimIndent())

        val test = supabase.from("ingredients")
            .select(columns = columns){
            filter {
                eq("id", ingredient_id)
            }
        }

        Log.d("supabase", test.data)

        return test.decodeSingle<db_Ingredient_detail>()
    }

    suspend fun getDish(): List<db_Dish> {
        return supabase.postgrest["dish"].select().decodeList<db_Dish>()
    }

    suspend fun testt(fd: db_FilterData): List<db_Dish> {
        Log.d("supatest", fd.toString())
        val columns = Columns.raw("""*, recipes_and_dish!inner(recipes(price))""".trimIndent())
        val test = supabase.from("dish").select(columns = columns){
            filter {
                if (fd.categoryArr.isNotEmpty()) {
                    db_Dish::category_id isIn fd.categoryArr
                }
                if (fd.priceBegin != null) {
                    if (fd.priceEnd != null) {
                        and {
                            gte("recipes_and_dish.recipes.price", fd.priceBegin as Double)
                            lte("recipes_and_dish.recipes.price", fd.priceEnd as Double)
                        }
                    } else {
                        gte("recipes_and_dish.recipes.price", fd.priceBegin as Double)
                    }
                } else if (fd.priceEnd != null) {
                    lte("recipes_and_dish.recipes.price", fd.priceEnd as Double)
                }
                fd.time?.let { time ->
                    lte("time", time)
                }
                fd.searchText?.let { searchText ->
                    if (searchText.trim() != "") {
                        Log.d("Supatest text", searchText)
                        db_Dish::name ilike "%${searchText.trim()}%"
                    }
                }
            }
        }

        Log.d("Supabase", test.component1())
        Log.d("Supabase", test.data)

        return test.decodeList<db_Dish>()
    }

    suspend fun getDishWithFilter(params: db_FilterData): List<db_Dish> {
        val filteredParams = params.copy(userId = null)

        val json = supabase.postgrest.rpc("get_dish_details", filteredParams)

        if (json.data == "null") {
            return emptyList()
        }

        return json.decodeList<db_Dish>()
    }

    suspend fun getBookmarkDishWithFilter(params: db_FilterData): List<db_Dish> {
        val json = supabase.postgrest.rpc("get_dish_details", params)
        if (json.data == "null") {
            return emptyList()
        }

        return json.decodeList<db_Dish>()
    }

    suspend fun getUserDishWithFilter(params: db_FilterData): List<db_Dish> {
        val json = supabase.postgrest.rpc("get_user_created_dish_details", params)
        if (json.data == "null") {
            return emptyList()
        }

        return json.decodeList<db_Dish>()
    }

    suspend fun getCurrentDish(dish_id: Int): db_Dish {
        val columns = Columns.raw("""*, measurement (id, created_at, name, full_name)""".trimIndent())

        val test = supabase.postgrest["dish"].select(columns = columns){
            filter {
                eq("id", dish_id)
            }
        }

        Log.d("Supabase", test.data)

        return test.decodeSingle<db_Dish>()
    }

    suspend fun getRecipes(dish_id: Int): List<db_Recipes_and_dish> {
        val columns = Columns.raw("""recipes (*), id_dish""".trimIndent())

        return supabase.from("recipes_and_dish")
            .select(columns = columns){
                filter {
                    eq("id_dish", dish_id)
                }
            }.decodeList<db_Recipes_and_dish>()
    }

    suspend fun getRecipesIngredients(id_recipes: Int): List<db_Recipes_and_ingredients> {
        val columns = Columns.raw("""id_recipes, ingredients (id, name, price, table1, table2, table3, table4, img), count, required, measurement (id, created_at, name, full_name)""".trimIndent())

        val test = supabase.from("recipes_and_ingredients")
            .select(columns = columns){
                filter {
                    eq("id_recipes", id_recipes)
                }
            }

        Log.d("supabase", test.data)

        return test.decodeList<db_Recipes_and_ingredients>()
    }

    suspend fun getDishCategory(): List<db_Dish_category> {
        return supabase.from("dish_category").select(){
            order(column = "name", order = Order.ASCENDING)
        }.decodeList<db_Dish_category>()
    }

    suspend fun deleteEat(id: Int) {
        supabase.from("user_and_eating").delete {
            filter {
                eq("id", id)
            }
        }
    }

    suspend fun updateEat(id: Int, newCount: Double): db_Users_and_eating {
        val columns = Columns.raw("""*, dish!inner(*, measurement(*)), recipes!inner(id, created_at, name, count, price, table1, table2, table3, table4, recipes_and_ingredients!inner(*))""".trimIndent())

        val newEat = supabase.from("user_and_eating").update({
            db_Users_and_eating::count setTo newCount
        }){
            select(columns = columns)
            filter {
                db_Users_and_eating::id eq id
            }
        }.decodeSingle<db_Users_and_eating>()

        return newEat
    }

    suspend fun insertEat(eat: db_Users_and_eating): db_Users_and_eating {
        val columns = Columns.raw("""*, dish!inner(*, measurement(*)), recipes!inner(id, created_at, name, count, price, table1, table2, table3, table4, recipes_and_ingredients!inner(*))""".trimIndent())

        val newEat = supabase.from("user_and_eating").insert(eat){
            select(columns = columns)
        }.decodeSingle<db_Users_and_eating>()

        return newEat
    }

    suspend fun signIn(context: Context, userEmail: String, userPassword: String) : Exception? {
        try {
            supabase.auth.signInWith(Email) {
                email = userEmail
                password = userPassword
            }
            saveToken(context)
            return null
        } catch (e: Exception) {
            return e
        }
    }

    suspend fun signUpNewUser(
        context: Context,
        data1: RegistrationViewModel.ContactInfo,
        data2: RegistrationViewModel.PhysicalInfo,
        data3: RegistrationViewModel.AllergyInfo
    ) {
        supabase.auth.signUpWith(Email) {
            email = data1.email
            password = data1.password
        }
        saveToken(context)
        var newUser = db_Users(
            supabase.auth.retrieveUserForCurrentSession(updateSession = true).id,
            name = data1.name,
            height = data2.height,
            weight = data2.weight,
            date_of_birth = data2.dateOfBirth,
            is_male = data2.isMale
        )
        Log.d("newTest", newUser.toString())
        newUser = supabase.from("users").insert(newUser) {
            select()
        }.decodeSingle<db_Users>()
        val allergyIds = data3.allergies.mapNotNull { userAllergy ->
            allergies.find { it.name == userAllergy }?.id
        }
        Log.d("newTest", allergyIds.toString())

        if (allergyIds.size > 0) {
            val userAllergies = allergyIds.map { allergyId ->
                db_Users_and_allergies(
                    id_user = newUser.id!!,
                    id_allergies = allergyId
                )
            }

            Log.d("newTest", userAllergies.toString())
            supabase.from("users_and_allergies").insert(userAllergies)
        }
    }

    suspend fun getUser(uid: String): db_Users {
        return supabase.from("users").select(){
            filter {
                eq("uid", supabase.auth.retrieveUserForCurrentSession(updateSession = true).id)
            }
        }.decodeSingle<db_Users>()
    }

    suspend fun getUserDetail(): db_Users {
        val columns = Columns.raw("""*, allergies(*), user_and_eating(*, dish!inner(*, measurement(*)), recipes!inner(id, created_at, name, count, price, table1, table2, table3, table4, recipes_and_ingredients!inner(*)))""".trimIndent())

        val test = supabase.from("users").select(columns = columns){
            filter {
                eq("uid", supabase.auth.retrieveUserForCurrentSession(updateSession = true).id)
            }
        }

        return test.decodeSingle<db_Users>()
    }

    fun saveToken(context: Context) {
        val accessToken = supabase.auth.currentAccessTokenOrNull()
        val sharedPref = SharedPreferenceHelper(context)
        sharedPref.saveStringData("accessToken",accessToken)
    }

    fun getToken(context: Context): String? {
        val sharedPref = SharedPreferenceHelper(context)
        return sharedPref.getStringData("accessToken")
    }

    suspend fun checkEmail(email: String): Boolean{
        return supabase.postgrest.rpc("check_email_exists", mapOf("p_email" to email)).data == "true"
    }

    suspend fun checkRecipe(params: rcp_Recipes): List<Int> {
        val json = supabase.postgrest.rpc("get_unique_recipe_ids", params)

        if (json.data == "null") {
            return emptyList()
        }

        return json.decodeList<Int>()
    }
}