package `in`.dimigo.dimigoin.di

import `in`.dimigo.dimigoin.data.datasource.DimigoinApiService
import `in`.dimigo.dimigoin.data.datasource.LocalSharedPreferenceManager
import `in`.dimigo.dimigoin.data.repository.MealRepositoryImpl
import `in`.dimigo.dimigoin.data.repository.PlaceRepositoryImpl
import `in`.dimigo.dimigoin.data.repository.UserRepositoryImpl
import `in`.dimigo.dimigoin.data.util.AuthenticationInterceptor
import `in`.dimigo.dimigoin.data.util.TokenAuthenticator
import `in`.dimigo.dimigoin.domain.repository.MealRepository
import `in`.dimigo.dimigoin.domain.repository.PlaceRepository
import `in`.dimigo.dimigoin.domain.repository.UserRepository
import `in`.dimigo.dimigoin.domain.usecase.meal.GetMyMealTimeUseCase
import `in`.dimigo.dimigoin.domain.usecase.meal.GetWeeklyMealUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.AddFavoriteAttendanceLogUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.GetAllPlacesUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.GetCurrentPlaceUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.GetFavoriteAttendanceLogsUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.GetRecommendedBuildingsUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.RemoveFavoriteAttendanceLogUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.SetCurrentPlaceUseCase
import `in`.dimigo.dimigoin.domain.usecase.user.GetMyIdentityUseCase
import `in`.dimigo.dimigoin.domain.usecase.user.UserLoginUseCase
import `in`.dimigo.dimigoin.viewmodel.LoginViewModel
import `in`.dimigo.dimigoin.viewmodel.MainViewModel
import `in`.dimigo.dimigoin.viewmodel.MealTimeViewModel
import `in`.dimigo.dimigoin.viewmodel.MealViewModel
import `in`.dimigo.dimigoin.viewmodel.PlaceSelectorViewModel
import `in`.dimigo.dimigoin.viewmodel.SplashViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModules = module {
    single { LocalSharedPreferenceManager(androidContext()) }
    single {
        OkHttpClient.Builder()
            .addInterceptor(AuthenticationInterceptor(get()))
            .authenticator(TokenAuthenticator(get()))
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

    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<PlaceRepository> { PlaceRepositoryImpl(get(), get()) }
    single<MealRepository> { MealRepositoryImpl(get()) }

    single { UserLoginUseCase(get()) }
    single { GetMyIdentityUseCase(get()) }
    single { GetAllPlacesUseCase(get()) }
    single { GetCurrentPlaceUseCase(get()) }
    single { SetCurrentPlaceUseCase(get()) }
    single { GetFavoriteAttendanceLogsUseCase(get()) }
    single { AddFavoriteAttendanceLogUseCase(get()) }
    single { RemoveFavoriteAttendanceLogUseCase(get()) }
    single { GetRecommendedBuildingsUseCase(get()) }
    single { GetWeeklyMealUseCase(get()) }
    single { GetMyMealTimeUseCase(get(), get()) }
}

val presentationModules = module {
    viewModel { LoginViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { MainViewModel(get(), get(), get()) }
    viewModel { PlaceSelectorViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { MealViewModel(get(), get()) }
    viewModel { MealTimeViewModel(get()) }
}
