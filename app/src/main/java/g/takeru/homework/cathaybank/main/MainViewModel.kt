package g.takeru.homework.cathaybank.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import g.takeru.homework.cathaybank.datamodel.Area
import g.takeru.homework.cathaybank.datamodel.Plant
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(application: Application,
                    private val repository: MainRepository
) : AndroidViewModel(application) {

    private val _areaList = MutableLiveData<List<Area>>()
    val areaList: LiveData<List<Area>> = _areaList

    private val _plantList = MutableLiveData<List<Plant>>()
    val plantList: LiveData<List<Plant>> = _plantList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _serverError = MutableLiveData<String>()
    val serverError: LiveData<String> = _serverError

    private val _connectError = MutableLiveData<String>()
    val connectError: LiveData<String> = _connectError

    fun getAreaList() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            repository.getAreaList()
                .doOnSuccess {
                    Timber.d("Area size: ${it?.result?.results?.size}")
                    _areaList.postValue(it!!.result.results)
                }.doOnApiFailure {
                    if (!it.isNullOrEmpty()) _serverError.postValue(it)
                }.doOnConnectFailure {
                    it?.let { _connectError.postValue(it.message) }
                }.doOnComplete {
                    _isLoading.postValue(false)
                }
                .proceed()
        }
    }

    fun getPlantList(areaName: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            repository.getPlantList()
                .doOnSuccess {
                    Timber.d("Plant size: ${it?.result?.results?.size}")
                    _plantList.postValue(getPlantListByAreaName(it!!.result.results, areaName))
                }.doOnApiFailure {
                    if (!it.isNullOrEmpty()) _serverError.postValue(it)
                }.doOnConnectFailure {
                    it?.let { _connectError.postValue(it.message) }
                }.doOnComplete {
                    _isLoading.postValue(false)
                }
                .proceed()
        }
    }

    fun getArea(AreaNo: String): Area? {
        return areaList.value?.find { it.e_no == AreaNo }
    }

    fun getPlain(plainId: Int): Plant? {
        return plantList.value?.find { it._id == plainId }
    }

    private fun getPlantListByAreaName(list: List<Plant>,
                                       areaName: String): List<Plant> {
        return list.filter { it.F_Location.contains(areaName) }
    }
}