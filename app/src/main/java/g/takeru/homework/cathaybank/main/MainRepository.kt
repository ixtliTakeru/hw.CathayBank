package g.takeru.homework.cathaybank.main

import g.takeru.homework.cathaybank.api.ApiManager
import g.takeru.homework.cathaybank.api.ApiResult
import g.takeru.homework.cathaybank.api.singleFlowApiResult
import g.takeru.homework.cathaybank.datamodel.AreaResult
import g.takeru.homework.cathaybank.datamodel.PlantResult
import kotlinx.coroutines.CoroutineDispatcher

interface MainRepository {
    suspend fun getAreaList(): ApiResult<AreaResult>
    suspend fun getPlantList(): ApiResult<PlantResult> }

class MainRepositoryImpl constructor(private val dispatcher: CoroutineDispatcher) :
    MainRepository {
    override suspend fun getAreaList(): ApiResult<AreaResult> =
        singleFlowApiResult(dispatcher) {
            ApiManager.getInstance().getAreaList()
        }

    override suspend fun getPlantList(): ApiResult<PlantResult> =
        singleFlowApiResult(dispatcher) {
            ApiManager.getInstance().getPlantList()
        }
}