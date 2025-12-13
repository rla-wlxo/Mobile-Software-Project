package com.codelab.basiclayouts

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codelab.basiclayouts.ui.theme.*

/**
 * 퀴즈 화면 (글래스모피즘)
 */
@Composable
fun QuizScreen(
    topic: QuizTopic,
    onFinishQuiz: (QuizResult, List<WrongAnswer>) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val soundManager = rememberSoundManager()

    val topicQuestions = remember(topic) {
        questions.filter { it.topicId == topic.id }
    }

    if (topicQuestions.isEmpty()) {
        GradientBackground {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                GlassCard {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "이 주제에 대한 문제가 없습니다.",
                            style = MaterialTheme.typography.titleMedium,
                            color = GlassTextPrimary
                        )
                        Spacer(Modifier.height(16.dp))
                        GlassButton(text = "뒤로가기", onClick = onCancel)
                    }
                }
            }
        }
        return
    }

    var index by rememberSaveable { mutableStateOf(0) }
    var score by rememberSaveable { mutableStateOf(0) }
    val wrongAnswers = remember { mutableStateListOf<WrongAnswer>() }
    val topicName = stringResource(topic.titleRes)

    val current = topicQuestions[index]
    val progress = (index + 1).toFloat() / topicQuestions.size

    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(40.dp))

            // 헤더
            GlassCard {
                Column {
                    Text(
                        text = topicName,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = GlassTextPrimary
                    )
                    Spacer(Modifier.height(12.dp))

                    // 진행률 바
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progress)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(GlassPrimary, GlassSecondary)
                                    )
                                )
                        )
                    }

                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "문제 ${index + 1} / ${topicQuestions.size}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GlassTextSecondary
                    )
                }
            }

            // 문제
            GlassCard(modifier = Modifier.weight(1f)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = current.text,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 32.sp
                        ),
                        color = GlassTextPrimary
                    )
                }
            }

            // 보기
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                current.options.forEachIndexed { i, option ->
                    QuizOptionButton(
                        text = option,
                        optionNumber = i + 1,
                        onClick = {
                            if (i == current.answerIndex) {
                                score++
                                soundManager.playCorrect(context)
                            } else {
                                soundManager.playWrong(context)
                                wrongAnswers.add(
                                    WrongAnswer(
                                        question = current,
                                        userAnswerIndex = i,
                                        topicName = topicName
                                    )
                                )
                            }

                            if (index == topicQuestions.lastIndex) {
                                val result = QuizResult(
                                    topic = topic,
                                    score = score,
                                    total = topicQuestions.size
                                )
                                onFinishQuiz(result, wrongAnswers.toList())
                            } else {
                                index++
                            }
                        }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // 그만하기 버튼
            GlassButton(
                text = "그만하기",
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
fun QuizOptionButton(
    text: String,
    optionNumber: Int,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "optionScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .background(GlassSurface)
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.6f),
                        Color.White.copy(alpha = 0.2f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                isPressed = true
                onClick()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 번호 배지
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(GlassPrimary, GlassSecondary)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = optionNumber.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
            }

            Spacer(Modifier.width(16.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp
                ),
                color = GlassTextPrimary,
                modifier = Modifier.weight(1f)
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
 * 결과 화면 (글래스모피즘)
 */
@Composable
fun ResultScreen(
    result: QuizResult,
    wrongAnswerCount: Int,
    onRestart: () -> Unit,
    onBackToHome: () -> Unit,
    onViewWrongAnswers: () -> Unit
) {
    val percentage = (result.score * 100) / result.total

    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 결과 카드
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🎉",
                        style = MaterialTheme.typography.displayLarge,
                        fontSize = 80.sp
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "퀴즈 완료!",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = GlassTextPrimary
                    )

                    Spacer(Modifier.height(24.dp))

                    // 점수
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(RoundedCornerShape(70.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(GlassPrimary, GlassSecondary, GlassTertiary)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${result.score}",
                                style = MaterialTheme.typography.displayLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 48.sp
                                ),
                                color = Color.White
                            )
                            Text(
                                text = "/ ${result.total}",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "$percentage%",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = GlassTextPrimary
                    )

                    if (wrongAnswerCount > 0) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "오답: ${wrongAnswerCount}개",
                            style = MaterialTheme.typography.bodyLarge,
                            color = GlassTextSecondary
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            // 버튼들
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GlassButton(
                    text = "다시 풀기",
                    onClick = onRestart,
                    modifier = Modifier.fillMaxWidth()
                )

                if (wrongAnswerCount > 0) {
                    GlassButton(
                        text = "오답 노트 보기",
                        onClick = onViewWrongAnswers,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                GlassButton(
                    text = "메인으로",
                    onClick = onBackToHome,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}