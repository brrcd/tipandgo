package com.tip_n_go.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tip_n_go.data.exception.ResponseError
import com.tip_n_go.data.incoming.Brand
import com.tip_n_go.data.incoming.Country
import com.tip_n_go.data.incoming.Organization
import com.tip_n_go.data.incoming.UnitResponseResult
import com.tip_n_go.data.outgoing.CreateBrand
import com.tip_n_go.data.outgoing.CreateOrganization
import com.tip_n_go.data.outgoing.UpdateOrganization
import com.tip_n_go.repository.api.DataSource
import com.tip_n_go.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class EditOrganizationViewModel(private val repository: DataSource) : BaseViewModel() {

    private val _userBrandsLiveData = MutableLiveData<UiState<List<Brand>>>()
    val userBrandsLiveData: LiveData<UiState<List<Brand>>> get() = _userBrandsLiveData
    private val _getCountriesLiveData = MutableLiveData<UiState<List<Country>>>()
    val getCountriesLiveData: LiveData<UiState<List<Country>>> get() = _getCountriesLiveData
    private val _organizationLiveData = MutableLiveData<UiState<Organization>>()
    val organizationLiveData: LiveData<UiState<Organization>> get() = _organizationLiveData
    private val _brandLiveData = MutableLiveData<UiState<Brand>>()
    val brandLiveData: LiveData<UiState<Brand>> get() = _brandLiveData
    private val _createBrandLiveData = MutableLiveData<UiState<UnitResponseResult>>()
    val createBrandLiveData: LiveData<UiState<UnitResponseResult>> get() = _createBrandLiveData
    private val _updateBrandLiveData = MutableLiveData<UiState<UnitResponseResult>>()
    val updateBrandLiveData: LiveData<UiState<UnitResponseResult>> get() = _updateBrandLiveData
    private val _createOrgLiveData = MutableLiveData<UiState<UnitResponseResult>>()
    val createOrgLiveData: LiveData<UiState<UnitResponseResult>> get() = _createOrgLiveData
    private val _updateOrgLiveData = MutableLiveData<UiState<UnitResponseResult>>()
    val updateOrgLiveData: LiveData<UiState<UnitResponseResult>> get() = _updateOrgLiveData
    override val errorLiveData: LiveData<UiState<ResponseError>> get() = _errorLiveData

    fun getOrganization(organizationHash: String) {
        launchOnIo {
            val brands = repository.getUserBrands()
            _userBrandsLiveData.postValue(UiState.Success(brands))
            val data = repository.getOrganizationByHash(organizationHash)
            _organizationLiveData.postValue(UiState.Success(data))
        }
    }

    fun createBrand(brandName: String) {
        val job = viewModelScope.launch(Dispatchers.IO) {
            repository.createBrand(CreateBrand(brandName))
        }
        job.invokeOnCompletion {
            launchOnIo {
                val data = repository.getUserBrands()
                _userBrandsLiveData.postValue(UiState.Success(data))
                _createBrandLiveData.postValue(UiState.Success(UnitResponseResult(
                    createBrandSuccess = true
                )))
            }
        }
    }

    fun updateBrand(brand: Brand){
        val job = viewModelScope.launch(Dispatchers.IO) {
            repository.updateBrand(brand)
        }
        job.invokeOnCompletion {
            launchOnIo {
                val data = repository.getUserBrands()
                _userBrandsLiveData.postValue(UiState.Success(data))
                _updateBrandLiveData.postValue(UiState.Success(
                    UnitResponseResult(
                    updateBrandSuccess = true
                )
                ))
            }
        }
    }

    fun getBrand(brandHash: String){
        launchOnIo {
            val data = repository.getBrandByHash(brandHash)
            _brandLiveData.postValue(UiState.Success(data))
        }
    }

    fun updateOrganization(
        organizationHash: String,
        brandHash: String,
        name: String,
        legalName: String,
        phone: String,
        website: String,
        email: String,
        address: String
    ) {
        launchOnIo {
            val data = repository.updateOrganization(
                UpdateOrganization(
                    organizationHash,
                    brandHash,
                    name,
                    legalName,
                    phone,
                    website,
                    email,
                    address
                )
            )
            _organizationLiveData.postValue(UiState.Success(data))
            _updateOrgLiveData.postValue(UiState.Success(
                UnitResponseResult(updateOrganizationSuccess = true)
            ))
        }
    }

    fun createOrganization(
        brandHash: String,
        name: String,
        legalName: String,
        phone: String,
        website: String,
        email: String,
        address: String,
        countryId: Int
    ) {
        launchOnIo {
            val data = repository.createNewOrganization(
                CreateOrganization(
                    brandHash,
                    name,
                    legalName,
                    phone,
                    website,
                    email,
                    address,
                    countryId
                )
            )
            _organizationLiveData.postValue(UiState.Success(data))
            _createOrgLiveData.postValue(UiState.Success(
                UnitResponseResult(createOrganizationSuccess = true)
            ))
        }
    }

    fun getUserBrands() {
        launchOnIo {
            val data = repository.getUserBrands()
            _userBrandsLiveData.postValue(UiState.Success(data))
        }
    }

    fun getCountries() {
        launchOnIo {
            val data = repository.getCountries()
            _getCountriesLiveData.postValue(UiState.Success(data))
        }
    }

    fun uploadLogo(brandHash: String, file: File) {
        launchOnIo {
            val data = repository.uploadBrandLogo(brandHash, file)
            _brandLiveData.postValue(UiState.Success(data))
        }
    }
}
