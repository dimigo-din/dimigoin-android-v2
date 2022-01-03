package `in`.dimigo.dimigoin.di

import `in`.dimigo.dimigoin.data.datasource.DimigoinApiService
import `in`.dimigo.dimigoin.data.repository.UserRepositoryImpl
import `in`.dimigo.dimigoin.data.util.AuthenticationInterceptor
import `in`.dimigo.dimigoin.domain.repository.UserRepository
import `in`.dimigo.dimigoin.domain.usecase.user.UserLoginUseCase
import `in`.dimigo.dimigoin.viewmodel.LoginViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModules = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(AuthenticationInterceptor())
            .build()
    }
    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.dimigo.in/")
            .client(get())
            .build()
    }
    single { get<Retrofit>().create(DimigoinApiService::class.java) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single { UserLoginUseCase(get()) }
}

val presentationModules = module {
    viewModel { LoginViewModel(get()) }
}