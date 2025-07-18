package dev.rep.template.root

import android.app.Application
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
import dev.rep.template.di.CommonAppComponent
import dev.rep.template.di.create
import dev.rep.template.features.newsList.presentation.ui.NewsListNavigation
import dev.rep.template.root.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val applicationComponent = from(application)
        val component = AndroidActivityComponent.create(this, applicationComponent)

        enableEdgeToEdge()
        setContent {
            RootComposable(component.routeFactories)
        }
    }

    private fun from(context: Application): CommonAppComponent {
        return (context.applicationContext as ApplicationProvider).appComponent
    }
}

interface ApplicationProvider {
    val appComponent: CommonAppComponent
}

@Composable
fun RootComposable(routeFactories: Set<AppRouteFactory>) {
    AppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            RootNavigation(
                routeFactories,
                Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun RootNavigation(
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

