package com.codelab.basiclayouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codelab.basiclayouts.ui.theme.MySootheTheme
import java.text.SimpleDateFormat
import java.util.*

/**
 * 랭킹 화면
 */
@Composable
fun RankingScreen(
    rankings: List<RankingEntry>,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        // 헤더
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = Color(0xFFFFD700), // 금색
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "랭킹",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Text(
            text = "총 ${rankings.size}개의 기록",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(16.dp))

        // 랭킹 목록이 없을 때
        if (rankings.isEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "아직 기록이 없습니다",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "퀴즈를 풀어보세요!",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        } else {
            // 랭킹 목록
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(rankings) { index, entry ->
                    RankingCard(
                        rank = index + 1,
                        entry = entry
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // 뒤로가기 버튼
        Button(
            onClick = onBackToHome,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("메인으로 돌아가기")
        }
    }
}

/**
 * 랭킹 카드 아이템
 */
@Composable
fun RankingCard(
    rank: Int,
    entry: RankingEntry,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (rank) {
        1 -> Color(0xFFFFD700).copy(alpha = 0.2f) // 금색
        2 -> Color(0xFFC0C0C0).copy(alpha = 0.2f) // 은색
        3 -> Color(0xFFCD7F32).copy(alpha = 0.2f) // 동색
        else -> MaterialTheme.colorScheme.surface
    }

    val rankIcon = when (rank) {
        1 -> "🥇"
        2 -> "🥈"
        3 -> "🥉"
        else -> "$rank"
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
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
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rankIcon,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.width(16.dp))

            // 정보
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.topicName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "점수: ${entry.score}/${entry.total} (${getPercentage(entry.score, entry.total)}%)",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = entry.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 점수 배지
            Surface(
                shape = MaterialTheme.shapes.small,
                color = when {
                    getPercentage(entry.score, entry.total) == 100 -> Color(0xFF4CAF50)
                    getPercentage(entry.score, entry.total) >= 80 -> Color(0xFF2196F3)
                    getPercentage(entry.score, entry.total) >= 60 -> Color(0xFFFF9800)
                    else -> Color(0xFFF44336)
                }
            ) {
                Text(
                    text = "${getPercentage(entry.score, entry.total)}%",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

/**
 * 점수 퍼센트 계산
 */
private fun getPercentage(score: Int, total: Int): Int {
    return if (total > 0) (score * 100) / total else 0
}

/**
 * 타임스탬프를 날짜 문자열로 변환
 */
fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

// 프리뷰
@Preview(showBackground = true)
@Composable
fun RankingScreenPreview() {
    MySootheTheme {
        RankingScreen(
            rankings = listOf(
                RankingEntry(
                    topicName = "Android 기초",
                    score = 10,
                    total = 10,
                    date = "2025-11-20 14:30",
                    timestamp = System.currentTimeMillis()
                ),
                RankingEntry(
                    topicName = "Kotlin 문법",
                    score = 8,
                    total = 10,
                    date = "2025-11-19 10:15",
                    timestamp = System.currentTimeMillis() - 86400000
                ),
                RankingEntry(
                    topicName = "Jetpack Compose",
                    score = 7,
                    total = 10,
                    date = "2025-11-18 16:45",
                    timestamp = System.currentTimeMillis() - 172800000
                )
            ),
            onBackToHome = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyRankingScreenPreview() {
    MySootheTheme {
        RankingScreen(
            rankings = emptyList(),
            onBackToHome = {}
        )
    }
}