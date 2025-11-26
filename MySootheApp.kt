package com.codelab.basiclayouts

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.Modifier

@Composable
fun MySootheApp(windowSize: WindowSizeClass) {
    var currentScreen by rememberSaveable { mutableStateOf(MySootheScreen.Home) }
    var currentTopic by rememberSaveable { mutableStateOf<QuizTopic?>(null) }
    var lastResult by rememberSaveable { mutableStateOf<QuizResult?>(null) }

    // 오답 노트 저장
    val allWrongAnswers = remember { mutableStateListOf<WrongAnswer>() }

    // 랭킹 저장 (최신순으로 정렬)
    val rankings = remember { mutableStateListOf<RankingEntry>() }

    // 주제 선택 시
    val onTopicSelected: (QuizTopic) -> Unit = { topic ->
        currentTopic = topic
        currentScreen = MySootheScreen.Quiz
    }

    // 퀴즈 완료 시
    val onQuizFinished: (QuizResult, List<WrongAnswer>) -> Unit = { result, wrongAnswers ->
        lastResult = result

        // 오답 추가
        allWrongAnswers.addAll(wrongAnswers)

        // 랭킹 추가
        val entry = RankingEntry(
            topicName = result.topic.id.let { id ->
                when (id) {
                    1 -> "Android 기초"
                    2 -> "Jetpack Compose"
                    3 -> "Kotlin 문법"
                    else -> "퀴즈"
                }
            },
            score = result.score,
            total = result.total,
            date = formatDate(result.timestamp),
            timestamp = result.timestamp
        )
        rankings.add(0, entry) // 최신 기록을 앞에 추가

        // 점수 높은 순으로 정렬
        rankings.sortWith(
            compareByDescending<RankingEntry> { it.score }
                .thenByDescending { it.timestamp }
        )

        currentScreen = MySootheScreen.Result
    }

    // 메인으로 돌아가기
    val onBackToHome: () -> Unit = {
        currentScreen = MySootheScreen.Home
    }

    // 퀴즈 다시 시작
    val onRestartQuiz: () -> Unit = {
        currentScreen = MySootheScreen.Quiz
    }

    // 네비게이션
    val onNavigate: (MySootheScreen) -> Unit = { screen ->
        when (screen) {
            MySootheScreen.Home -> currentScreen = MySootheScreen.Home
            MySootheScreen.WrongAnswers -> currentScreen = MySootheScreen.WrongAnswers
            MySootheScreen.Ranking -> currentScreen = MySootheScreen.Ranking
            else -> {}
        }
    }

    when (currentScreen) {
        MySootheScreen.Home -> {
            when (windowSize.widthSizeClass) {
                WindowWidthSizeClass.Expanded -> {
                    MySootheAppLandscape(
                        currentScreen = currentScreen,
                        onTopicClick = onTopicSelected,
                        onNavigate = onNavigate
                    )
                }
                else -> {
                    MySootheAppPortrait(
                        currentScreen = currentScreen,
                        onTopicClick = onTopicSelected,
                        onNavigate = onNavigate
                    )
                }
            }
        }

        MySootheScreen.Quiz -> {
            QuizScreen(
                topic = requireNotNull(currentTopic),
                onFinishQuiz = onQuizFinished,
                onCancel = onBackToHome
            )
        }

        MySootheScreen.Result -> {
            val result = requireNotNull(lastResult)
            val wrongCount = allWrongAnswers.count {
                it.question.topicId == result.topic.id
            }

            ResultScreen(
                result = result,
                wrongAnswerCount = wrongCount,
                onRestart = onRestartQuiz,
                onBackToHome = onBackToHome,
                onViewWrongAnswers = {
                    currentScreen = MySootheScreen.WrongAnswers
                }
            )
        }

        MySootheScreen.WrongAnswers -> {
            WrongAnswersScreen(
                wrongAnswers = allWrongAnswers.toList(),
                onBackToHome = onBackToHome
            )
        }

        MySootheScreen.Ranking -> {
            RankingScreen(
                rankings = rankings.toList(),
                onBackToHome = onBackToHome
            )
        }
    }
}