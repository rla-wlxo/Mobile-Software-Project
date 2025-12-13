package com.codelab.basiclayouts

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codelab.basiclayouts.ui.theme.*

/**
 * 글래스모피즘 주제 카드
 */
@Composable
fun GlassTopicCard(
    @DrawableRes drawable: Int,
    @StringRes text: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "cardScale"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .height(140.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(GlassSurface)
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.6f),
                        Color.White.copy(alpha = 0.2f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable {
                isPressed = true
                onClick()
            }
    ) {
        // 배경 이미지 (블러 처리)
        Image(
            painter = painterResource(drawable),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .blur(8.dp),
            alpha = 0.3f
        )

        // 텍스트
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = stringResource(text),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                ),
                color = GlassTextPrimary
            )
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}

/**
 * 메인 화면
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onTopicClick: (QuizTopic) -> Unit
) {
    GradientBackground {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(Modifier.height(40.dp))

                // 헤더
                Text(
                    text = "✨ Smart Quiz",
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = GlassTextPrimary
                )

                Text(
                    text = "주제를 선택하세요",
                    style = MaterialTheme.typography.titleMedium,
                    color = GlassTextSecondary,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(Modifier.height(24.dp))
            }

            items(quizTopics) { topic ->
                GlassTopicCard(
                    drawable = topic.imageRes,
                    text = topic.titleRes,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onTopicClick(topic) }
                )
            }

            item {
                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

/**
 * 하단 네비게이션 바 (글래스 스타일)
 */
@Composable
fun GlassBottomNavigation(
    currentScreen: MySootheScreen,
    onNavigate: (MySootheScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(GlassSurface)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.5f),
                        Color.White.copy(alpha = 0.2f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            GlassNavItem(
                icon = Icons.Default.Home,
                label = "홈",
                selected = currentScreen == MySootheScreen.Home,
                onClick = { onNavigate(MySootheScreen.Home) }
            )
            GlassNavItem(
                icon = Icons.Default.ErrorOutline,
                label = "오답",
                selected = currentScreen == MySootheScreen.WrongAnswers,
                onClick = { onNavigate(MySootheScreen.WrongAnswers) }
            )
            GlassNavItem(
                icon = Icons.Default.EmojiEvents,
                label = "랭킹",
                selected = currentScreen == MySootheScreen.Ranking,
                onClick = { onNavigate(MySootheScreen.Ranking) }
            )
        }
    }
}

@Composable
fun GlassNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy
        ),
        label = "navScale"
    )

    Column(
        modifier = Modifier
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) GlassPrimary else GlassTextSecondary,
            modifier = Modifier.size(28.dp)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) GlassTextPrimary else GlassTextSecondary,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

/**
 * 세로 모드용 전체 화면
 */
@Composable
fun MySootheAppPortrait(
    currentScreen: MySootheScreen,
    onTopicClick: (QuizTopic) -> Unit,
    onNavigate: (MySootheScreen) -> Unit
) {
    MySootheTheme {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                GlassBottomNavigation(
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

/**
 * 가로 모드용 전체 화면
 */
@Composable
fun MySootheAppLandscape(
    currentScreen: MySootheScreen,
    onTopicClick: (QuizTopic) -> Unit,
    onNavigate: (MySootheScreen) -> Unit
) {
    MySootheTheme {
        Row {
            // 왼쪽 네비게이션 레일 (글래스 스타일)
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(12.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(GlassSurface)
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GlassNavItem(
                        icon = Icons.Default.Home,
                        label = "홈",
                        selected = currentScreen == MySootheScreen.Home,
                        onClick = { onNavigate(MySootheScreen.Home) }
                    )
                    GlassNavItem(
                        icon = Icons.Default.ErrorOutline,
                        label = "오답",
                        selected = currentScreen == MySootheScreen.WrongAnswers,
                        onClick = { onNavigate(MySootheScreen.WrongAnswers) }
                    )
                    GlassNavItem(
                        icon = Icons.Default.EmojiEvents,
                        label = "랭킹",
                        selected = currentScreen == MySootheScreen.Ranking,
                        onClick = { onNavigate(MySootheScreen.Ranking) }
                    )
                }
            }

            HomeScreen(onTopicClick = onTopicClick)
        }
    }
}