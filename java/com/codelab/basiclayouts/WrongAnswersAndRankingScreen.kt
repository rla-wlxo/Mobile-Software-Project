package com.codelab.basiclayouts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codelab.basiclayouts.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 오답 노트 화면 (글래스모피즘)
 */
@Composable
fun WrongAnswersScreen(
    wrongAnswers: List<WrongAnswer>,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    GradientBackground {
        Column(modifier = modifier.fillMaxSize().padding(24.dp)) {
            Spacer(Modifier.height(40.dp))

            // 헤더
            Text(
                text = "📖 오답 노트",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = GlassTextPrimary
            )

            Text(
                text = if (wrongAnswers.isEmpty()) "완벽해요! 틀린 문제가 없습니다"
                else "틀린 문제: ${wrongAnswers.size}개",
                style = MaterialTheme.typography.titleMedium,
                color = GlassTextSecondary,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(Modifier.height(24.dp))

            if (wrongAnswers.isEmpty()) {
                // 빈 상태
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    GlassCard {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = "🎉",
                                style = MaterialTheme.typography.displayLarge,
                                fontSize = 80.sp
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = "축하합니다!",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = GlassTextPrimary
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "틀린 문제가 없습니다",
                                style = MaterialTheme.typography.bodyLarge,
                                color = GlassTextSecondary
                            )
                        }
                    }
                }
            } else {
                // 오답 목록
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(wrongAnswers) { wrongAnswer ->
                        WrongAnswerGlassCard(wrongAnswer)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            GlassButton(
                text = "메인으로 돌아가기",
                onClick = onBackToHome,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
fun WrongAnswerGlassCard(wrongAnswer: WrongAnswer) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(GlassWrong.copy(alpha = 0.15f))
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        GlassWrong.copy(alpha = 0.5f),
                        GlassWrong.copy(alpha = 0.2f)
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // 주제
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = GlassWrong.copy(alpha = 0.3f)
            ) {
                Text(
                    text = wrongAnswer.topicName,
                    style = MaterialTheme.typography.labelMedium,
                    color = GlassTextPrimary,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            // 문제
            Text(
                text = wrongAnswer.question.text,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp
                ),
                color = GlassTextPrimary
            )

            Spacer(Modifier.height(16.dp))

            // 선택지들
            wrongAnswer.question.options.forEachIndexed { index, option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 번호 뱃지
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                when {
                                    index == wrongAnswer.question.answerIndex -> GlassCorrect
                                    index == wrongAnswer.userAnswerIndex -> GlassWrong
                                    else -> Color.White.copy(alpha = 0.2f)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (index + 1).toString(),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    // 선택지 텍스트
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyMedium,
                        color = when {
                            index == wrongAnswer.question.answerIndex -> GlassCorrect
                            index == wrongAnswer.userAnswerIndex -> GlassWrong
                            else -> GlassTextSecondary
                        },
                        fontWeight = when {
                            index == wrongAnswer.question.answerIndex -> FontWeight.Bold
                            index == wrongAnswer.userAnswerIndex -> FontWeight.Bold
                            else -> FontWeight.Normal
                        },
                        modifier = Modifier.weight(1f)
                    )

                    // 아이콘
                    if (index == wrongAnswer.question.answerIndex) {
                        Text(
                            text = "✓",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = GlassCorrect
                        )
                    } else if (index == wrongAnswer.userAnswerIndex) {
                        Text(
                            text = "✗",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = GlassWrong
                        )
                    }
                }
            }
        }
    }
}

/**
 * 랭킹 화면 (글래스모피즘)
 */
@Composable
fun RankingScreen(
    rankings: List<RankingEntry>,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    GradientBackground {
        Column(modifier = modifier.fillMaxSize().padding(24.dp)) {
            Spacer(Modifier.height(40.dp))

            // 헤더
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(40.dp)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "랭킹",
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = GlassTextPrimary
                )
            }

            Text(
                text = if (rankings.isEmpty()) "아직 기록이 없습니다"
                else "총 ${rankings.size}개의 기록",
                style = MaterialTheme.typography.titleMedium,
                color = GlassTextSecondary,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(Modifier.height(24.dp))

            if (rankings.isEmpty()) {
                // 빈 상태
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    GlassCard {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = "🏆",
                                style = MaterialTheme.typography.displayLarge,
                                fontSize = 80.sp
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = "아직 기록이 없습니다",
                                style = MaterialTheme.typography.headlineSmall,
                                color = GlassTextPrimary
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "퀴즈를 풀어보세요!",
                                style = MaterialTheme.typography.bodyLarge,
                                color = GlassTextSecondary
                            )
                        }
                    }
                }
            } else {
                // 랭킹 목록
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(rankings) { index, entry ->
                        RankingGlassCard(rank = index + 1, entry = entry)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            GlassButton(
                text = "메인으로 돌아가기",
                onClick = onBackToHome,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
fun RankingGlassCard(rank: Int, entry: RankingEntry) {
    val rankEmoji = when (rank) {
        1 -> "🥇"
        2 -> "🥈"
        3 -> "🥉"
        else -> null
    }

    val percentage = (entry.score * 100) / entry.total

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (rank <= 3) GlassPrimary.copy(alpha = 0.2f)
                else GlassSurface
            )
            .border(
                width = if (rank <= 3) 2.dp else 1.5.dp,
                brush = Brush.linearGradient(
                    colors = if (rank <= 3) {
                        listOf(
                            Color(0xFFFFD700).copy(alpha = 0.6f),
                            Color(0xFFFFD700).copy(alpha = 0.3f)
                        )
                    } else {
                        listOf(
                            Color.White.copy(alpha = 0.5f),
                            Color.White.copy(alpha = 0.2f)
                        )
                    }
                ),
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 순위
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(GlassPrimary, GlassSecondary)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rankEmoji ?: "$rank",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
            }

            Spacer(Modifier.width(16.dp))

            // 정보
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.topicName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = GlassTextPrimary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${entry.score}/${entry.total} 문제",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GlassTextSecondary
                )
                Text(
                    text = entry.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = GlassTextSecondary.copy(alpha = 0.7f)
                )
            }

            // 점수 배지
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = when {
                    percentage == 100 -> GlassCorrect
                    percentage >= 80 -> GlassPrimary
                    percentage >= 60 -> Color(0xFFFF9800)
                    else -> GlassWrong
                }
            ) {
                Text(
                    text = "$percentage%",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
            }
        }
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MM/dd HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}