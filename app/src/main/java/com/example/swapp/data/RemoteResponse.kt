package com.example.swapp.data

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

data class CharacterResponse(
    val count: Int,
    val result: List<Character>
)

data class Character(
    val name: String,
    val height: String,
    val mass: String,
    @Json(name = "hair_color") val hairColor: String,
    @Json(name = "skin_color") val skinColor: String,
    @Json(name = "birth_year") val birthYear: String,
    @Json(name = "eye_color") val eyeColor: String,
    val gender: String,
    @Json(name = "homeworld") val homeWorld: String,
    val films: List<String>,
    val species: List<String>,
    val vehicles: List<String>,
    @Json(name = "created") val createdDate: String,
    @Json(name = "edited") val editedDate: String,
    val url: String
)

data class Planet(
    val name: String,
    val residents: List<String>
)

@JsonClass(generateAdapter = true)
data class PeopleResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Person>
)

data class CharacterDetailHolder(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<CharacterDetail>
)

data class PeopleEntity(
    val count: Int,
    val next: String?,
    val previous: String?,
    val characters: MutableList<CharacterEntity>
)


data class Person(
    val name: String,
    @Json(name = "rotation_period") val rotationPeriod: String?,
    @Json(name = "orbital_period") val orbitalPeriod: String?,
    val height: String,
    val diameter: String?,
    val climate: String?,
    val gravity: String?,
    val terrain: String?,
    @Json(name = "birth_year") val birthYear: String?,
    @Json(name = "surface_water") val surfaceWater: String?,
    @Json(name = "homeworld") var homeworld: String? = null,
    val population: String?,
    val residents: List<String>?,
    val films: List<String>?,
    val species: List<String>?,
    var filmsIds: MutableList<String>? = mutableListOf(),
//    var filmMap: HashMap<String, String>? = hashMapOf(),
    var speciesId: String?,
    val created: String?,
    val edited: String?,
    val url: String?
)

@JsonClass(generateAdapter = true)
data class FilmResponse(
    @Json(name = "title") var title: String? = null,
    @Json(name = "episode_id") var episodeId: Int? = null,
    @Json(name = "opening_crawl") var openingCrawl: String? = null,
    @Json(name = "director") var director: String? = null,
    @Json(name = "producer") var producer: String? = null,
    @Json(name = "release_date") var releaseDate: String? = null,
    @Json(name = "characters") var characters: List<String> = arrayListOf(),
    @Json(name = "planets") var planets: List<String> = arrayListOf(),
    @Json(name = "starships") var starships: List<String> = arrayListOf(),
    @Json(name = "vehicles") var vehicles: List<String> = arrayListOf(),
    @Json(name = "species") var species: List<String> = arrayListOf(),
    @Json(name = "created") var created: String? = null,
    @Json(name = "edited") var edited: String? = null,
    @Json(name = "url") var url: String? = null
)

@JsonClass(generateAdapter = true)
data class SpecieResponse(
    @Json(name = "name") var name: String? = null,
    @Json(name = "classification") var classification: String? = null,
    @Json(name = "designation") var designation: String? = null,
    @Json(name = "average_height") var averageHeight: String? = null,
    @Json(name = "skin_colors") var skinColors: String? = null,
    @Json(name = "hair_colors") var hairColors: String? = null,
    @Json(name = "eye_colors") var eyeColors: String? = null,
    @Json(name = "average_lifespan") var averageLifespan: String? = null,
    @Json(name = "homeworld") var homeworld: String? = null,
    @Json(name = "language") var language: String? = null,
    @Json(name = "people") var people: List<String> = arrayListOf(),
    @Json(name = "films") var films: List<String> = arrayListOf(),
    @Json(name = "created") var created: String? = null,
    @Json(name = "edited") var edited: String? = null,
    @Json(name = "url") var url: String? = null
)

data class SpeciesEntity(
    val speciesName: String?,
    val speciesLang: String?,
    val homeWorld: String? = null
)

data class HomeWorldEntity(
    val name: String? = null,
    val terrain: String? = null
)

@Serializable
@Parcelize
data class CharacterEntity(
    var speciesId: String? = null,
    var planetId: String?,
    var filmsIds: List<String>? = listOf(),
    var name: String?,
    var birthYear: String?,
    var height: String?,
    var speciesName: String? = "",
    var speciesLang: String? = "",
    var homeWorldName: String? = "",
    var homeWorldTerrain: String? = "",
    var planetPopulation: String? = "",
    var filmMap: HashMap<String, String> = hashMapOf()
): Parcelable

data class CharacterDetail(
    var speciesId: String? = null,
    var planetId: String?,
    var filmsIds: List<String>? = listOf(),
    var name: String?,
    var birthYear: String?,
    var height: String?,
    var speciesName: String? = "",
    var speciesLang: String? = "",
    var homeWorldName: String? = "",
    var homeWorldTerrain: String? = "",
    var planetPopulation: String? = "",
    var filmMap: HashMap<String, String> = hashMapOf()
)

@Serializable
data class DetailScreen(
//    val characterEntity: CharacterEntity
    val name: String
)

@Serializable
object HomeScreen
