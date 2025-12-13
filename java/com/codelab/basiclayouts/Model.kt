package com.codelab.basiclayouts

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

// 어떤 화면을 보여줄지
enum class MySootheScreen {
    Home,         // 주제 선택 화면
    Quiz,         // 퀴즈 푸는 화면
    Result,       // 결과 화면
    WrongAnswers, // 오답 노트
    Ranking       // 랭킹 화면
}

// 퀴즈 주제
data class QuizTopic(
    val id: Int,
    @DrawableRes val imageRes: Int,
    @StringRes val titleRes: Int
)

// 한 문제
data class Question(
    val id: Int,
    val topicId: Int,
    val text: String,
    val options: List<String>, // 4지선다 보기
    val answerIndex: Int       // 정답 index (0~3)
)

// 퀴즈 결과
data class QuizResult(
    val topic: QuizTopic,
    val score: Int,
    val total: Int,
    val timestamp: Long = System.currentTimeMillis()
)

// 오답 기록
data class WrongAnswer(
    val question: Question,
    val userAnswerIndex: Int,
    val topicName: String
)

// 랭킹 기록
data class RankingEntry(
    val topicName: String,
    val score: Int,
    val total: Int,
    val date: String,
    val timestamp: Long
)

// 화면에 보여줄 3개 주제
val quizTopics = listOf(
    QuizTopic(1, R.drawable.android, R.string.quizTopic1),
    QuizTopic(2, R.drawable.jetpack_compose, R.string.quizTopic2),
    QuizTopic(3, R.drawable.kotlin, R.string.quizTopic3)
)

// 예시 문제들 (topicId 1, 2, 3 각각 2문제씩)
val questions = listOf(
    Question(
        id = 1,
        topicId = 1,
        text = "안드로이드에서 UI를 선언형으로 만드는 방식은?",
        options = listOf("XML 레이아웃", "Jetpack Compose", "AIDL", "NDK"),
        answerIndex = 1
    ),
    Question(
        id = 2,
        topicId = 1,
        text = "Kotlin 파일의 확장자는 무엇인가?",
        options = listOf(".java", ".kt", ".xml", ".gradle"),
        answerIndex = 1
    ),
    Question(
        id = 3,
        topicId = 2,
        text = "안드로이드에서 이미지를 표시하는 뷰는?",
        options = listOf("TextView", "ImageView", "Button", "EditText"),
        answerIndex = 1
    ),
    Question(
        id = 4,
        topicId = 2,
        text = "앱의 테마/색상을 정의하는 곳은?",
        options = listOf("AndroidManifest.xml", "colors.xml", "layout.xml", "strings.xml"),
        answerIndex = 1
    ),
    Question(
        id = 5,
        topicId = 3,
        text = "안드로이드에서 문자열 리소스를 가져오는 방법은?",
        options = listOf("getText()", "getColor()", "stringResource()", "painterResource()"),
        answerIndex = 2
    ),
    Question(
        id = 6,
        topicId = 3,
        text = "Jetpack Compose에서 상태를 저장하는 함수는?",
        options = listOf("rememberSaveable()", "setState()", "useState()", "rememberView()"),
        answerIndex = 0
    )
)