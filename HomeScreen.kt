package com.codelab.basiclayouts

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codelab.basiclayouts.ui.theme.MySootheTheme

// 메인에서 사용할 카드 1개
@Composable
fun FavoriteCollectionCard(
    @DrawableRes drawable: Int,
    @StringRes text: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(376.dp)
        ) {
            Image(
                painter = painterResource(drawable),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp)
            )
            Text(
                text = stringResource(text),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

// 주제 카드 그리드
@Composable
fun FavoriteCollectionsGrid(
    modifier: Modifier = Modifier,
    onTopicClick: (QuizTopic) -> Unit
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(3),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth()
        .height(568.dp)
    ) {
        items(quizTopics) { topic ->
            FavoriteCollectionCard(
                drawable = topic.imageRes,
                text = topic.titleRes,
                modifier = Modifier
                    .height(80.dp)
                    .clickable { onTopicClick(topic) }
            )
        }
    }
}

// 제목 + 콘텐츠 슬롯
@Composable
fun HomeSection(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier) {
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .paddingFromBaseline(top = 40.dp, bottom = 16.dp)
                .padding(horizontal = 16.dp)
        )
        content()
    }
}

// 메인 화면 (세로 스크롤)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onTopicClick: (QuizTopic) -> Unit
) {
    Column(
        modifier.fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))

        HomeSection(title = R.string.Select) {
            FavoriteCollectionsGrid(
                onTopicClick = onTopicClick
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}

// 하단 네비게이션 바
@Composable
fun SootheBottomNavigation(
    currentScreen: MySootheScreen,
    onNavigate: (MySootheScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null
                )
            },
            label = {
                Text(text = stringResource(R.string.bottom_navigation_home))
            },
            selected = currentScreen == MySootheScreen.Home,
            onClick = { onNavigate(MySootheScreen.Home) }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.ErrorOutline,
                    contentDescription = null
                )
            },
            label = {
                Text(text = stringResource(R.string.bottom_navigation_wrong_answers))
            },
            selected = currentScreen == MySootheScreen.WrongAnswers,
            onClick = { onNavigate(MySootheScreen.WrongAnswers) }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null
                )
            },
            label = {
                Text(text = "랭킹")
            },
            selected = currentScreen == MySootheScreen.Ranking,
            onClick = { onNavigate(MySootheScreen.Ranking) }
        )
    }
}

// 세로 모드용 전체 화면
@Composable
fun MySootheAppPortrait(
    currentScreen: MySootheScreen,
    onTopicClick: (QuizTopic) -> Unit,
    onNavigate: (MySootheScreen) -> Unit
) {
    MySootheTheme {
        Scaffold(
            bottomBar = {
                SootheBottomNavigation(
                    currentScreen = currentScreen,
                    onNavigate = onNavigate
                )
            }
        ) { padding ->
            HomeScreen(
                modifier = Modifier.padding(padding),
                onTopicClick = onTopicClick
            )
        }
    }
}

// 가로 모드에서 왼쪽 레일
@Composable
fun SootheNavigationRail(
    currentScreen: MySootheScreen,
    onNavigate: (MySootheScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(modifier = modifier) {
        Column {
            NavigationRailItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null
                    )
                },
                label = {
                    Text(stringResource(R.string.bottom_navigation_home))
                },
                selected = currentScreen == MySootheScreen.Home,
                onClick = { onNavigate(MySootheScreen.Home) }
            )

            NavigationRailItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = null
                    )
                },
                label = {
                    Text(stringResource(R.string.bottom_navigation_wrong_answers))
                },
                selected = currentScreen == MySootheScreen.WrongAnswers,
                onClick = { onNavigate(MySootheScreen.WrongAnswers) }
            )

            NavigationRailItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null
                    )
                },
                label = {
                    Text("랭킹")
                },
                selected = currentScreen == MySootheScreen.Ranking,
                onClick = { onNavigate(MySootheScreen.Ranking) }
            )
        }
    }
}

// 가로 모드 전체 화면
@Composable
fun MySootheAppLandscape(
    currentScreen: MySootheScreen,
    onTopicClick: (QuizTopic) -> Unit,
    onNavigate: (MySootheScreen) -> Unit
) {
    MySootheTheme {
        Row {
            SootheNavigationRail(
                currentScreen = currentScreen,
                onNavigate = onNavigate
            )
            HomeScreen(
                onTopicClick = onTopicClick
            )
        }
    }
}

// 간단 프리뷰
@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun HomeScreenPreview() {
    MySootheTheme {
        HomeScreen(onTopicClick = {})
    }
}