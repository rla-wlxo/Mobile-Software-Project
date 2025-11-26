package com.codelab.basiclayouts

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codelab.basiclayouts.ui.theme.MySootheTheme

// 퀴즈 화면
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

    // 해당 topic에 문제가 없을 때
    if (topicQuestions.isEmpty()) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "이 주제에 대한 문제가 없습니다.",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = onCancel) {
                Text("뒤로가기")
            }
        }
        return
    }

    var index by rememberSaveable { mutableStateOf(0) }
    var score by rememberSaveable { mutableStateOf(0) }
    val wrongAnswers = remember { mutableStateListOf<WrongAnswer>() }

    // 주제 이름을 미리 가져오기 (Composable 외부에서 사용하기 위해)
    val topicName = stringResource(topic.titleRes)

    val current = topicQuestions[index]

    Column(Modifier.padding(16.dp)) {
        Text(text = stringResource(topic.titleRes), style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        Text(text = "문제 ${index + 1} / ${topicQuestions.size}")
        Spacer(Modifier.height(16.dp))
        Text(text = current.text, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(16.dp))

        current.options.forEachIndexed { i, option ->
            Button(
                onClick = {
                    // 정답/오답 체크
                    if (i == current.answerIndex) {
                        score++
                        soundManager.playCorrect(context)
                    } else {
                        soundManager.playWrong(context)
                        // 오답 저장
                        wrongAnswers.add(
                            WrongAnswer(
                                question = current,
                                userAnswerIndex = i,
                                topicName = topicName  // 미리 가져온 문자열 사용
                            )
                        )
                    }

                    // 다음 문제 또는 결과 화면
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
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(option)
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = onCancel) {
            Text("그만하기")
        }
    }
}

// 결과 화면
@Composable
fun ResultScreen(
    result: QuizResult,
    wrongAnswerCount: Int,
    onRestart: () -> Unit,
    onBackToHome: () -> Unit,
    onViewWrongAnswers: () -> Unit
) {
    Column(Modifier.padding(16.dp)) {
        Text("결과", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text("주제: " + stringResource(result.topic.titleRes))
        Text("점수: ${result.score} / ${result.total}")

        if (wrongAnswerCount > 0) {
            Text("오답 개수: $wrongAnswerCount")
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = onRestart, modifier = Modifier.fillMaxWidth()) {
            Text("다시 풀기")
        }
        Spacer(Modifier.height(8.dp))

        if (wrongAnswerCount > 0) {
            Button(onClick = onViewWrongAnswers, modifier = Modifier.fillMaxWidth()) {
                Text("오답 노트 보기")
            }
            Spacer(Modifier.height(8.dp))
        }

        Button(onClick = onBackToHome, modifier = Modifier.fillMaxWidth()) {
            Text("메인으로")
        }
    }
}

// 간단 프리뷰용 더미 데이터
@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    MySootheTheme {
        QuizScreen(
            topic = quizTopics.first(),
            onFinishQuiz = { _, _ -> },
            onCancel = {}
        )
    }
}