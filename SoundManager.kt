package com.codelab.basiclayouts

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes

/**
 * 정답/오답 사운드를 재생하는 매니저
 */
class SoundManager private constructor() {

    private var mediaPlayer: MediaPlayer? = null

    companion object {
        @Volatile
        private var instance: SoundManager? = null

        fun getInstance(): SoundManager {
            return instance ?: synchronized(this) {
                instance ?: SoundManager().also { instance = it }
            }
        }
    }

    /**
     * 정답 사운드 재생
     */
    fun playCorrect(context: Context) {
        playSound(context, R.raw.correct)
    }

    /**
     * 오답 사운드 재생
     */
    fun playWrong(context: Context) {
        playSound(context, R.raw.wrong)
    }

    /**
     * 사운드 파일 재생
     */
    private fun playSound(context: Context, @RawRes soundRes: Int) {
        try {
            // 기존 재생 중인 사운드 정리
            mediaPlayer?.apply {
                if (isPlaying) {
                    stop()
                }
                release()
            }

            // 새 MediaPlayer 생성 및 재생
            mediaPlayer = MediaPlayer.create(context, soundRes)?.apply {
                setOnCompletionListener { mp ->
                    mp.release()
                    mediaPlayer = null
                }
                setOnErrorListener { mp, what, extra ->
                    android.util.Log.e("SoundManager", "Error playing sound: what=$what, extra=$extra")
                    mp.release()
                    mediaPlayer = null
                    true
                }
                start()
            }

            if (mediaPlayer == null) {
                android.util.Log.e("SoundManager", "Failed to create MediaPlayer for resource: $soundRes")
            }
        } catch (e: Exception) {
            android.util.Log.e("SoundManager", "Error playing sound", e)
            e.printStackTrace()
        }
    }

    /**
     * 모든 리소스 정리 (앱 종료 시 호출)
     */
    fun release() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            release()
        }
        mediaPlayer = null
    }
}

/**
 * Composable에서 쉽게 사용하기 위한 헬퍼 함수
 */
@androidx.compose.runtime.Composable
fun rememberSoundManager(): SoundManager {
    return androidx.compose.runtime.remember { SoundManager.getInstance() }
}

/**
 * 확장 함수로 더 편하게 사용
 */
fun SoundManager.playCorrectSound(context: Context) = playCorrect(context)
fun SoundManager.playWrongSound(context: Context) = playWrong(context)