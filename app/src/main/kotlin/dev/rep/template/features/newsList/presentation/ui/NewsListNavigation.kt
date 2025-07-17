package dev.rep.template.features.newsList.presentation.ui

import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.rep.template.features.newsList.presentation.NewsListViewModel
import dev.rep.template.root.AppRouteFactory
import kotlinx.serialization.Serializable
import me.tatarka.inject.annotations.Inject

@Serializable
object NewsListNavigation

@Inject
class NewsListRouteFactory(
    private val viewModelFactory: (SavedStateHandle) -> NewsListViewModel,
) : AppRouteFactory {

    override fun NavGraphBuilder.create(
        navController: NavController,
        modifier: Modifier
    ) {
        composable<NewsListNavigation> { _ ->
            val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
            NewsListScreen(viewModel, navController, modifier)
        }
    }
}