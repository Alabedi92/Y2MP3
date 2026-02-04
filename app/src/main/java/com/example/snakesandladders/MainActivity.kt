package com.example.snakesandladders

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

private val snakes = mapOf(
    16 to 6,
    47 to 26,
    49 to 11,
    56 to 53,
    62 to 19,
    64 to 60,
    87 to 24,
    93 to 73,
    95 to 75,
    98 to 78
)

private val ladders = mapOf(
    1 to 38,
    4 to 14,
    9 to 31,
    21 to 42,
    28 to 84,
    36 to 44,
    51 to 67,
    71 to 91,
    80 to 100
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    GameScreen()
                }
            }
        }
    }
}

@Composable
private fun GameScreen() {
    var playerPosition by remember { mutableStateOf(1) }
    var lastRoll by remember { mutableStateOf(0) }
    var message by remember { mutableStateOf("ارمِ النرد لبدء اللعبة") }
    var moves by remember { mutableStateOf(0) }
    var isWinner by remember { mutableStateOf(false) }

    fun applyMove(start: Int, roll: Int): Pair<Int, String> {
        if (start == 100) return 100 to "لقد وصلت بالفعل للنهاية!"

        val tentative = (start + roll).coerceAtMost(100)
        val snakeTail = snakes[tentative]
        val ladderTop = ladders[tentative]

        return when {
            snakeTail != null -> snakeTail to "لدغة أفعى! عدت إلى المربع $snakeTail"
            ladderTop != null -> ladderTop to "سُلَّم! صعدت إلى المربع $ladderTop"
            else -> tentative to "تحركت إلى المربع $tentative"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "لعبة السلم والثعبان",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "الموقع الحالي: $playerPosition", fontSize = 20.sp)
        Text(text = "آخر رمية: $lastRoll", fontSize = 18.sp)
        Text(text = "عدد الحركات: $moves", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = message, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = {
                    if (isWinner) {
                        playerPosition = 1
                        lastRoll = 0
                        moves = 0
                        isWinner = false
                        message = "تمت إعادة اللعب. ارمِ النرد!"
                    } else {
                        val roll = Random.nextInt(1, 7)
                        lastRoll = roll
                        val (newPosition, status) = applyMove(playerPosition, roll)
                        playerPosition = newPosition
                        moves += 1
                        message = status
                        if (newPosition == 100) {
                            isWinner = true
                            message = "مبروك! فزت خلال $moves حركة. اضغط لإعادة اللعب."
                        }
                    }
                }
            ) {
                Text(text = if (isWinner) "إعادة اللعب" else "ارمِ النرد")
            }
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                onClick = {
                    playerPosition = 1
                    lastRoll = 0
                    moves = 0
                    isWinner = false
                    message = "تمت إعادة اللعب. ارمِ النرد!"
                }
            ) {
                Text(text = "بدء جديد")
            }
        }
    }
}
