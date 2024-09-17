package com.example.swapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.wear.compose.material.CircularProgressIndicator
import com.example.swapp.data.CharacterEntity
import com.example.swapp.data.HomeScreen
import com.example.swapp.ui.people.PeopleViewModel
import com.example.swapp.ui.theme.SWAppTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.toRoute
import com.example.swapp.data.CharacterDetail
import com.example.swapp.data.CharacterDetailHolder
import com.example.swapp.data.DetailScreen
import com.example.swapp.data.PeopleEntity
import com.example.swapp.data.RemoteResultState

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SWAppTheme {
                Column(modifier = Modifier.padding(26.dp)) {
                    AppNavigation()
                }
            }
        }
    }

    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = HomeScreen) {
            composable<HomeScreen> {
                PeopleListScreen(navController)
            }

            composable<DetailScreen> { it ->
                val args = it.toRoute<DetailScreen>()
                DetailedScreen(args.name)
            }
        }
    }

    @Composable
    fun DetailedScreen(name: String) {
        val viewModel: PeopleViewModel = hiltViewModel()
        val apiResult by viewModel.apiResult.collectAsState()

        LaunchedEffect(name) {
            viewModel.searchByName(name)
        }

        when (apiResult) {
            is RemoteResultState.Loading -> {
                LoadingIndicator()
            }

            is RemoteResultState.Success -> {
                val result = (apiResult as RemoteResultState.Success<CharacterDetailHolder?>)
                    .data?.results?.firstOrNull()

                result?.let {
                    DetailedInfo(name = name, result = it)
                } ?: ErrorText("No details found for $name")
            }

            is RemoteResultState.Error -> {
                ErrorText("An error occurred while fetching details.")
            }
        }
    }

    @Composable
    fun LoadingIndicator() {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(indicatorColor = Color.Red)
        }
    }

    @Composable
    fun ErrorText(errorMessage: String) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = errorMessage, color = Color.Red)
        }
    }

    @Composable
    fun DetailedInfo(name: String, result: CharacterDetail) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.padding(16.dp))
            Text(text = "Name: $name")
            Text(text = "Height: ${result.height}")
            Text(text = "Species Name: ${result.speciesName}")
            Text(text = "Species Language: ${result.speciesLang}")

            if (!result.homeWorldName.isNullOrEmpty()) {
                Text(text = "HomeWorld Name: ${result.homeWorldName}")
                Text(text = "HomeWorld Terrain: ${result.homeWorldTerrain}")
            }

            if (result.planetPopulation?.isNotEmpty() == true) {
                Text(text = "Population: ${result.planetPopulation}")
            }

            if (result.filmMap?.isEmpty() == false) {
                DisplayMapFilm(mapData = result.filmMap)
            }
        }
    }


    @Composable
    fun DisplayMapFilm(mapData: HashMap<String, String>) {
        val result = mapData.entries.toList()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(result.size) { index ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Film Title: ${result[index].key}",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = result[index].value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 2.dp),
                        color = Color.Black
                    )
                }
            }
        }
    }


    @Composable
    fun PeopleListScreen(navController: NavHostController) {
        val viewModel: PeopleViewModel = hiltViewModel<PeopleViewModel>()
        val searchText = remember { mutableStateOf(viewModel.searchQuery.value) }
        val peoplePagingItemsFlow by viewModel.peoplePagingData.collectAsStateWithLifecycle()
        val isLoading by viewModel.loadingState.collectAsState()

        val peoplePagingItems = peoplePagingItemsFlow.collectAsLazyPagingItems()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TextField(
                value = searchText.value,
                onValueChange = { newValue ->
                    searchText.value = newValue
                    viewModel.setSearchQuery(newValue)
                },
                label = { Text("Search People") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )


            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(peoplePagingItems.itemCount) { index ->
                    peoplePagingItems[index]?.let { personItem ->
                        PersonRow(personItem) {
                            navController.navigate(DetailScreen(name = personItem.name ?: ""))
                        }
                    }
                }


                peoplePagingItems.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(indicatorColor = Color.Red)
                                }
                            }
                        }

                        loadState.append is LoadState.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(indicatorColor = Color.Red)
                                }
                            }
                        }

                        loadState.refresh is LoadState.Error -> {
                            val e = peoplePagingItems.loadState.refresh as LoadState.Error
                            item {
                                Text(
                                    text = "Error: ${e.error.localizedMessage}",
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        loadState.append is LoadState.Error -> {
                            val e = peoplePagingItems.loadState.append as LoadState.Error
                            item {
                                Text(
                                    text = "Error: ${e.error.localizedMessage}",
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }

    }

    @Composable
    fun PersonRow(person: CharacterEntity, onClick: () -> Unit) {
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .clickable {
                Log.d(TAG, "PersonRow: clicked")
                onClick()
            }) {
            Text(text = person.name ?: "", fontWeight = FontWeight.Bold, color = Color.Red)
            Text(text = "height: ${person.height ?: ""}", color = Color.Black)
            Text(text = "birthYear: ${person.birthYear ?: ""}", color = Color.Black)
        }
    }


}