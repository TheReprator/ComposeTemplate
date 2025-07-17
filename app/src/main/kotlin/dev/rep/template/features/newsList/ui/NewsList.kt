package dev.rep.template.features.newsList.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import dev.rep.template.features.newsDetail.NewsDetailNavigation
import dev.rep.template.features.newsList.domain.NewsModel
import dev.rep.template.features.newsList.presentation.NewsListEffect
import dev.rep.template.features.newsList.presentation.NewsListAction
import dev.rep.template.features.newsList.presentation.NewsListState
import dev.rep.template.features.newsList.presentation.NewsListViewModel
import me.tatarka.inject.annotations.Inject

@Inject
@Composable
fun NewsListScreen(
    newsListViewModel: NewsListViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val state: NewsListState by newsListViewModel.uiState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    val effect = remember(newsListViewModel.sideEffect, lifecycleOwner) {
        newsListViewModel.sideEffect.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }

    LaunchedEffect(effect) {
        effect.collect { action ->
            when (action) {
                is NewsListEffect.NavigateToDetail -> {
                    navController.navigate(NewsDetailNavigation(action.newsModel))
                }

                is NewsListEffect.ShowError -> {

                }
            }
        }
    }

    LaunchedEffect(Unit) {
        newsListViewModel.onAction(NewsListAction.FetchNews)
    }

    NewsListScreen(state, {
        newsListViewModel.onAction(NewsListAction.NavigateToDetail(it))
    }, {
        newsListViewModel.onAction(NewsListAction.RetryFetchNews)
    }, modifier)
}

@Composable
fun NewsListScreen(
    state: NewsListState,
    newsItemClick: (NewsModel) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.padding(30.dp)) {

        if (state.newsLoading) {
            return NewsListLoader()
        }

        if (state.isError) {
            return NewsListRetry(onRetry)
        }

        NewsListDisplay(newsItemClick, state.newsList)
    }
}

@Composable
fun NewsListLoader(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(Modifier.fillMaxSize())
}


@Composable
fun NewsListRetry(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Button(onClick = {
            onRetry()
        }) {
            Text(text = "Retry", Modifier.padding(10.dp))
        }
        Text(text = "An Error occurred")
    }
}


@Composable
fun NewsListDisplay(
    newsItemClick: (NewsModel) -> Unit,
    newsList: List<NewsModel>,
    modifier: Modifier = Modifier
) {
    val rememberClick by rememberUpdatedState(newsItemClick)
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        state = rememberLazyListState(),
    ) {
        newsList.forEach {
            item(key = it.id) {
                Text(
                    text = it.title, Modifier
                        .padding(10.dp)
                        .clickable {
                            rememberClick(it)
                        })
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )
            }
        }
    }
}
