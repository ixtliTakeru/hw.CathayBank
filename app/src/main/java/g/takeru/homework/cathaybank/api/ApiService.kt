package g.takeru.homework.cathaybank.api

import g.takeru.homework.cathaybank.datamodel.PlantResult
import g.takeru.homework.cathaybank.datamodel.AreaResult
import retrofit2.http.GET

interface ApiService {
    // https://data.taipei/api/v1/dataset/5a0e5fbb-72f8-41c6-908e-2fb25eff9b8a?scope=resourceAquire
    @GET("5a0e5fbb-72f8-41c6-908e-2fb25eff9b8a?scope=resourceAquire")
    suspend fun getAreaList(
//        @Query("scope") scope: String
    ): AreaResult

    // https://data.taipei/api/v1/dataset/f18de02f-b6c9-47c0-8cda-50efad621c14?scope=resourceAquire
    @GET("f18de02f-b6c9-47c0-8cda-50efad621c14?scope=resourceAquire")
    suspend fun getPlantList(
//        @Query("scope") scope: String
    ): PlantResult
}