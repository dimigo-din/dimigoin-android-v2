package `in`.dimigo.dimigoin.di

import `in`.dimigo.dimigoin.data.datasource.DimigoinApiService
import `in`.dimigo.dimigoin.data.datasource.FirebaseCloudMessagingService
import `in`.dimigo.dimigoin.data.datasource.LocalSharedPreferenceManager
import `in`.dimigo.dimigoin.data.datasource.SchoolScheduleDataSource
import `in`.dimigo.dimigoin.data.repository.MealRepositoryImpl
import `in`.dimigo.dimigoin.data.repository.PlaceRepositoryImpl
import `in`.dimigo.dimigoin.data.repository.ScheduleRepositoryImpl
import `in`.dimigo.dimigoin.data.repository.UserRepositoryImpl
import `in`.dimigo.dimigoin.data.util.AuthenticationInterceptor
import `in`.dimigo.dimigoin.data.util.TokenAuthenticator
import `in`.dimigo.dimigoin.domain.repository.MealRepository
import `in`.dimigo.dimigoin.domain.repository.PlaceRepository
import `in`.dimigo.dimigoin.domain.repository.ScheduleRepository
import `in`.dimigo.dimigoin.domain.repository.UserRepository
import `in`.dimigo.dimigoin.domain.usecase.meal.GetGradeMealTimeUseCase
import `in`.dimigo.dimigoin.domain.usecase.meal.GetMyMealTimeUseCase
import `in`.dimigo.dimigoin.domain.usecase.meal.GetTodayMealUseCase
import `in`.dimigo.dimigoin.domain.usecase.meal.GetWeeklyMealUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.*
import `in`.dimigo.dimigoin.domain.usecase.schedule.GetScheduleUseCase
import `in`.dimigo.dimigoin.domain.usecase.schedule.GetTimetableUseCase
import `in`.dimigo.dimigoin.domain.usecase.user.GetMyIdentityUseCase
import `in`.dimigo.dimigoin.domain.usecase.user.UserLoginUseCase
import `in`.dimigo.dimigoin.viewmodel.*
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
    single {
        val okHttpClient =
            OkHttpClient.Builder().build() // existing client holds security information
        SchoolScheduleDataSource(okHttpClient)
    }
    single { FirebaseCloudMessagingService() }

    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<PlaceRepository> { PlaceRepositoryImpl(get(), get()) }
    single<MealRepository> { MealRepositoryImpl(get()) }
    single<ScheduleRepository> { ScheduleRepositoryImpl(get(), get(), get()) }

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
    single { GetTodayMealUseCase(get()) }
    single { GetMyMealTimeUseCase(get(), get()) }
    single { GetGradeMealTimeUseCase(get(), get()) }
    single { GetScheduleUseCase(get()) }
    single { GetTimetableUseCase(get(), get()) }
}

val presentationModules = module {
    viewModel { LoginViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { MainViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { PlaceSelectorViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { MealViewModel(get(), get()) }
    viewModel { MealTimeViewModel(get(), get()) }
    viewModel { ScheduleViewModel(get(), get(), get()) }
    viewModel { MyInfoViewModel(get()) }
}
