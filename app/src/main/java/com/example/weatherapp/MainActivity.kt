package com.example.weatherapp

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.TextureView
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.bumptech.glide.Glide
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {



     private  lateinit var weatherInfoTextView:TextView;
    private lateinit var appBarConfiguration: AppBarConfiguration
private lateinit var binding: ActivityMainBinding
private lateinit var icon:ImageView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = ActivityMainBinding.inflate(layoutInflater)
     setContentView(binding.root)


        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        weatherInfoTextView = findViewById(R.id.weather_info)
        icon = findViewById(R.id.icon)
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()

            // Call the function from your activity or fragment
            lifecycleScope.launch {
                val data = getWeatherData("kabul","AF", "b1ab1c8d9c2eeca357d6b712e491119b")
                // Process the weather data here
                println(data)
                val gson = Gson()
                val weatherResponse = gson.fromJson(data, WeatherResponse::class.java)
                 println(weatherResponse)

                Snackbar.make(view, "get data", Snackbar.LENGTH_LONG).setAction("Action", null).show()
                weatherInfoTextView.text ="description - " + weatherResponse.weather[0].description
                var iconName = weatherResponse.weather[0].icon;
                Glide.with(applicationContext)
                    .load("https://openweathermap.org/img/wn/$iconName@2x.png")
                    .into(icon)
            }
        }


    }
override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when(item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment_content_main)
    return navController.navigateUp(appBarConfiguration)
            || super.onSupportNavigateUp()
    }
}



// Define a function to make the API request
suspend fun getWeatherData(city:String,countryCode:String, apiKey: String): String {
    return withContext(Dispatchers.IO) {
//        https://api.openweathermap.org/data/2.5/weather?q={city name},{country code}&appid={API key}
//        https://api.openweathermap.org/data/2.5/weather?q=kabul,af&appid=b1ab1c8d9c2eeca357d6b712e491119b
//        val apiUrl = "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=$apiKey"
        val apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=$city,$countryCode&appid=$apiKey"

        URL(apiUrl).readText()
    }
}




data class WeatherResponse(
    val coord: Coord,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Int,
    val sys: Sys,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)

data class Coord(
    val lon: Double,
    val lat: Double
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

data class Wind(
    val speed: Double,
    val deg: Int
)

data class Clouds(
    val all: Int
)

data class Sys(
    val type: Int,
    val id: Int,
    val message: Double,
    val country: String,
    val sunrise: Int,
    val sunset: Int
)
