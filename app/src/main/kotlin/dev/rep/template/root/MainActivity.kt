package dev.rep.template.root

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.rep.template.di.AndroidActivityComponent
import dev.rep.template.di.AndroidApplicationComponent
import dev.rep.template.di.create
import dev.rep.template.features.newsList.ui.NewsListNavigation
import dev.rep.template.root.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val applicationComponent = AndroidApplicationComponent.from(this)
        val component = AndroidActivityComponent.create(this, applicationComponent)

        enableEdgeToEdge()
        setContent {
            AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RootComposable(
                        component.routeFactories,
                        Modifier.padding(innerPadding)
                    )
                }

            }
        }
    }
}

@Composable
fun RootComposable(
    routeFactories: Set<AppRouteFactory>,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NewsListNavigation,
        modifier = modifier,
    ) {
        create(
            factories = routeFactories,
            navController = navController,
        )
    }
}

private fun AndroidApplicationComponent.Companion.from(context: Context): AndroidApplicationComponent {
    return (context.applicationContext as App).component
}