import com.group.pocketshelf.bookapi.GoogleBooksAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObject{
    private const val BASE_URL ="https://www.googleapis.com/books/v1/"

    fun provideRetrofit(): GoogleBooksAPI.GoogleBooksApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleBooksAPI.GoogleBooksApiService::class.java)
    }
}