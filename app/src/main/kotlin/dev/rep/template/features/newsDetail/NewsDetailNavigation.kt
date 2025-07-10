package dev.rep.template.features.newsDetail

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.rep.template.features.newsList.domain.NewsModel
import dev.rep.template.root.AppRouteFactory
import dev.rep.template.util.serializableType
import kotlinx.serialization.Serializable
import me.tatarka.inject.annotations.Inject
import kotlin.reflect.typeOf

@Serializable
data class NewsDetailNavigation(val newsDetail: NewsModel)

@Inject
class NewsListDetailRouteFactory : AppRouteFactory {

    override fun NavGraphBuilder.create(navController: NavController, modifier: Modifier) {

        composable<NewsDetailNavigation>(typeMap = mapOf(typeOf<NewsModel>() to serializableType<NewsModel>())) { backStackEntry ->
            val route = backStackEntry.toRoute<NewsDetailNavigation>().newsDetail
            NewsDetailScreen(route, navController, modifier)
        }
    }
}