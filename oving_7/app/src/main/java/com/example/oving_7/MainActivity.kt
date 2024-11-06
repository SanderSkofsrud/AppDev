package com.example.oving_7

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.oving_7.ui.theme.Oving_7Theme
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var movies: List<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        databaseHelper = DatabaseHelper(this)

        // Check if the database is empty before inserting movies
        if (databaseHelper.isDatabaseEmpty()) {
            // Read movies from raw resource and insert into the database
            movies = readMoviesFromRawResource()
            databaseHelper.insertMovies(movies)

            // Write movies to a local file
            writeMoviesToLocalFile(movies)
        } else {
            // Load movies from the database if needed
            movies = databaseHelper.getAllMovies().map { title ->
                // You can create a Movie object if needed
                Movie(title, "", emptyList())
            }
        }

        setContent {
            Oving_7Theme {
                val selectedColor = remember { mutableStateOf(Color.White) }
                NavigationComponent(databaseHelper, selectedColor)
            }
        }
    }

    private fun readMoviesFromRawResource(): List<Movie> {
        val movieList = mutableListOf<Movie>()
        val inputStream = resources.openRawResource(R.raw.movies)
        val reader = inputStream.bufferedReader()

        reader.useLines { lines ->
            lines.forEach { line ->
                val movie = parseMovieLine(line)
                if (movie != null) {
                    movieList.add(movie)
                }
            }
        }

        return movieList
    }

    private fun parseMovieLine(line: String): Movie? {
        val parts = line.split(";")
        if (parts.size != 3) return null

        val title = parts[0].trim()
        val director = parts[1].trim()
        val actors = parts[2].split(",").map { it.trim() }

        return Movie(title, director, actors)
    }

    private fun writeMoviesToLocalFile(movies: List<Movie>) {
        val filename = "movies_output.txt"
        val fileContents = StringBuilder()

        movies.forEach { movie ->
            fileContents.append("${movie.title};${movie.director};${movie.actors.joinToString(",")}\n")
        }

        openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(fileContents.toString().toByteArray())
        }
    }
}
