package com.example.oving_7

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "movies.db"
        const val DATABASE_VERSION = 2 // Incremented version from 1 to 2

        const val TABLE_MOVIE = "movie"
        const val COLUMN_MOVIE_ID = "_id"
        const val COLUMN_MOVIE_TITLE = "title"
        const val COLUMN_MOVIE_DIRECTOR = "director"

        const val TABLE_ACTOR = "actor"
        const val COLUMN_ACTOR_ID = "_id"
        const val COLUMN_ACTOR_NAME = "name"

        const val TABLE_MOVIE_ACTOR = "movie_actor"
        const val COLUMN_MA_MOVIE_ID = "movie_id"
        const val COLUMN_MA_ACTOR_ID = "actor_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createMovieTable = """
            CREATE TABLE $TABLE_MOVIE (
                $COLUMN_MOVIE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_MOVIE_TITLE TEXT NOT NULL UNIQUE,
                $COLUMN_MOVIE_DIRECTOR TEXT NOT NULL
            );
        """.trimIndent()

        val createActorTable = """
            CREATE TABLE $TABLE_ACTOR (
                $COLUMN_ACTOR_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ACTOR_NAME TEXT NOT NULL UNIQUE
            );
        """.trimIndent()

        val createMovieActorTable = """
            CREATE TABLE $TABLE_MOVIE_ACTOR (
                $COLUMN_MA_MOVIE_ID INTEGER NOT NULL,
                $COLUMN_MA_ACTOR_ID INTEGER NOT NULL,
                UNIQUE($COLUMN_MA_MOVIE_ID, $COLUMN_MA_ACTOR_ID),
                FOREIGN KEY($COLUMN_MA_MOVIE_ID) REFERENCES $TABLE_MOVIE($COLUMN_MOVIE_ID),
                FOREIGN KEY($COLUMN_MA_ACTOR_ID) REFERENCES $TABLE_ACTOR($COLUMN_ACTOR_ID)
            );
        """.trimIndent()

        db.execSQL(createMovieTable)
        db.execSQL(createActorTable)
        db.execSQL(createMovieActorTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // Drop existing tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS $TABLE_MOVIE_ACTOR")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_ACTOR")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_MOVIE")
            onCreate(db)
        }
    }

    fun isDatabaseEmpty(): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_MOVIE", null)
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        db.close()
        return count == 0
    }

    fun insertMovies(movies: List<Movie>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            for (movie in movies) {
                // Insert movie with conflict handling
                val movieValues = ContentValues().apply {
                    put(COLUMN_MOVIE_TITLE, movie.title)
                    put(COLUMN_MOVIE_DIRECTOR, movie.director)
                }
                val movieId = db.insertWithOnConflict(
                    TABLE_MOVIE,
                    null,
                    movieValues,
                    SQLiteDatabase.CONFLICT_IGNORE
                )

                val actualMovieId = if (movieId == -1L) {
                    // Movie already exists, get its ID
                    getMovieIdByTitle(movie.title, db)
                } else {
                    movieId
                }

                for (actorName in movie.actors) {
                    // Insert actor with conflict handling
                    val actorValues = ContentValues().apply {
                        put(COLUMN_ACTOR_NAME, actorName)
                    }
                    val actorId = db.insertWithOnConflict(
                        TABLE_ACTOR,
                        null,
                        actorValues,
                        SQLiteDatabase.CONFLICT_IGNORE
                    )

                    val actualActorId = if (actorId == -1L) {
                        // Actor already exists, get its ID
                        getActorIdByName(actorName, db)
                    } else {
                        actorId
                    }

                    // Insert into movie_actor table with conflict handling
                    val maValues = ContentValues().apply {
                        put(COLUMN_MA_MOVIE_ID, actualMovieId)
                        put(COLUMN_MA_ACTOR_ID, actualActorId)
                    }
                    db.insertWithOnConflict(
                        TABLE_MOVIE_ACTOR,
                        null,
                        maValues,
                        SQLiteDatabase.CONFLICT_IGNORE
                    )
                }
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
        db.close()
    }

    private fun getMovieIdByTitle(title: String, db: SQLiteDatabase): Long {
        val cursor = db.query(
            TABLE_MOVIE,
            arrayOf(COLUMN_MOVIE_ID),
            "$COLUMN_MOVIE_TITLE = ?",
            arrayOf(title),
            null,
            null,
            null
        )
        val movieId = if (cursor.moveToFirst()) {
            cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_MOVIE_ID))
        } else {
            -1L
        }
        cursor.close()
        return movieId
    }

    private fun getActorIdByName(name: String, db: SQLiteDatabase): Long {
        val cursor = db.query(
            TABLE_ACTOR,
            arrayOf(COLUMN_ACTOR_ID),
            "$COLUMN_ACTOR_NAME = ?",
            arrayOf(name),
            null,
            null,
            null
        )
        val actorId = if (cursor.moveToFirst()) {
            cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ACTOR_ID))
        } else {
            -1L
        }
        cursor.close()
        return actorId
    }

    // Existing methods to retrieve data remain unchanged
    fun getAllMovies(): List<String> {
        val db = readableDatabase
        val movieList = mutableListOf<String>()
        val cursor = db.query(
            TABLE_MOVIE,
            arrayOf(COLUMN_MOVIE_TITLE),
            null,
            null,
            null,
            null,
            null
        )

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val title = it.getString(it.getColumnIndexOrThrow(COLUMN_MOVIE_TITLE))
                    movieList.add(title)
                } while (it.moveToNext())
            }
        }
        db.close()
        return movieList
    }

    fun getMoviesByDirector(director: String): List<String> {
        val db = readableDatabase
        val movieList = mutableListOf<String>()
        val cursor = db.query(
            TABLE_MOVIE,
            arrayOf(COLUMN_MOVIE_TITLE),
            "$COLUMN_MOVIE_DIRECTOR = ?",
            arrayOf(director),
            null,
            null,
            null
        )

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val title = it.getString(it.getColumnIndexOrThrow(COLUMN_MOVIE_TITLE))
                    movieList.add(title)
                } while (it.moveToNext())
            }
        }
        db.close()
        return movieList
    }

    fun getActorsByMovie(title: String): List<String> {
        val db = readableDatabase
        val actorList = mutableListOf<String>()

        val query = """
            SELECT a.$COLUMN_ACTOR_NAME
            FROM $TABLE_ACTOR a
            INNER JOIN $TABLE_MOVIE_ACTOR ma ON a.$COLUMN_ACTOR_ID = ma.$COLUMN_MA_ACTOR_ID
            INNER JOIN $TABLE_MOVIE m ON m.$COLUMN_MOVIE_ID = ma.$COLUMN_MA_MOVIE_ID
            WHERE m.$COLUMN_MOVIE_TITLE = ?
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(title))

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val name = it.getString(it.getColumnIndexOrThrow(COLUMN_ACTOR_NAME))
                    actorList.add(name)
                } while (it.moveToNext())
            }
        }
        db.close()
        return actorList
    }
}
