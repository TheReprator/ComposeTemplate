package dev.rep.template.features.newsDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.rep.template.R
import dev.rep.template.features.newsList.domain.NewsModel
import me.tatarka.inject.annotations.Inject

@Inject
@Composable
fun NewsDetailScreen(
    newsModel: NewsModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {

    Column(Modifier.padding(30.dp).testTag(stringResource(R.string.testTag_newsDetail))) {

        Image(
            painter = painterResource(android.R.drawable.ic_menu_close_clear_cancel),
            contentDescription = "", modifier = Modifier.clickable {
                navController.popBackStack()
            })

        Text(text = newsModel.title)
    }
}