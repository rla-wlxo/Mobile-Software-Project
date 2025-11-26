package com.codelab.basiclayouts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codelab.basiclayouts.ui.theme.MySootheTheme

/**
 * 오답 노트 화면
 */
@Composable
fun WrongAnswersScreen(
    wrongAnswers: List<WrongAnswer>,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        // 헤더
        Text(
            text = "오답 노트",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "틀린 문제: ${wrongAnswers.size}개",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(16.dp))

        // 오답 목록이 없을 때
        if (wrongAnswers.isEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🎉 축하합니다!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "틀린 문제가 없습니다.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        } else {
            // 오답 목록
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(wrongAnswers) { wrongAnswer ->
                    WrongAnswerCard(wrongAnswer)
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
 * 오답 카드 아이템
 */
@Composable
fun WrongAnswerCard(
    wrongAnswer: WrongAnswer,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 주제
            Text(
                text = "주제: ${wrongAnswer.topicName}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onErrorContainer
            )

            Spacer(Modifier.height(8.dp))

            // 문제
            Text(
                text = wrongAnswer.question.text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            // 보기들
            wrongAnswer.question.options.forEachIndexed { index, option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    // 번호
                    Text(
                        text = "${index + 1}. ",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    // 선택지 텍스트
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyMedium,
                        color = when {
                            index == wrongAnswer.question.answerIndex -> Color(0xFF2E7D32) // 정답 - 초록색
                            index == wrongAnswer.userAnswerIndex -> Color(0xFFC62828) // 오답 - 빨간색
                            else -> MaterialTheme.colorScheme.onSurface
                        },
                        fontWeight = when {
                            index == wrongAnswer.question.answerIndex -> FontWeight.Bold
                            index == wrongAnswer.userAnswerIndex -> FontWeight.Bold
                            else -> FontWeight.Normal
                        }
                    )

                    // 표시
                    if (index == wrongAnswer.question.answerIndex) {
                        Text(
                            text = " ✓ 정답",
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodySmall
                        )
                    } else if (index == wrongAnswer.userAnswerIndex) {
                        Text(
                            text = " ✗ 내 선택",
                            color = Color(0xFFC62828),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

// 프리뷰
@Preview(showBackground = true)
@Composable
fun WrongAnswersScreenPreview() {
    MySootheTheme {
        WrongAnswersScreen(
            wrongAnswers = listOf(
                WrongAnswer(
                    question = questions[0],
                    userAnswerIndex = 0,
                    topicName = "Android 기초"
                ),
                WrongAnswer(
                    question = questions[1],
                    userAnswerIndex = 2,
                    topicName = "Kotlin 문법"
                )
            ),
            onBackToHome = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyWrongAnswersPreview() {
    MySootheTheme {
        WrongAnswersScreen(
            wrongAnswers = emptyList(),
            onBackToHome = {}
        )
    }
}