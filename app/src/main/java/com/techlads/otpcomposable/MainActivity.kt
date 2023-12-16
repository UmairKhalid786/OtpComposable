package com.techlads.otpcomposable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techlads.otpcomposable.ui.theme.OtpComposableTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OtpComposableTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Enter OTP", style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.height(16.dp))
                        CodeInput(4)
                    }
                }
            }
        }
    }
}

@Composable
fun CodeInput(codeLength: Int) {
    val code = remember { mutableStateOf("") }

    val focusRequester = FocusRequester()
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        BasicTextField(value = code.value,
            onValueChange = { if (it.length <= codeLength) code.value = it },
            Modifier.focusRequester(focusRequester = focusRequester),
            decorationBox = {
                CodeInputDecoration(code.value, codeLength)
            })
    }
}

@Composable
private fun CodeInputDecoration(code: String, length: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        for (i in 0 until length) {
            val text = if (i < code.length) code[i].toString() else ""
            CodeChar(text, i == code.length)
        }
    }
}

@Composable
private fun CodeChar(text: String, focused: Boolean = false) {
    val toggled = remember {
        mutableStateOf(false)
    }

    val animatedOpacity by animateFloatAsState(
        if (toggled.value) {
            0.0f
        } else {
            0.4f
        }, label = "padding"
    )

    LaunchedEffect(Unit) {
        do {
            delay(500)
            toggled.value = toggled.value.not()
        } while (true)
    }

    Box(
        modifier = Modifier
            .background(
                color = colorScheme.secondary.copy(alpha = 0.2f), shape = shapes.small
            )
            .border(
                border = BorderStroke(
                    2.dp,
                    color = colorScheme.primary.copy(alpha = if (focused) animatedOpacity else 0f)
                ),
                shape = shapes.small
            )
            .width(56.dp)
            .aspectRatio(1f)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(targetState = text, label = "") {
            Text(
                text = it,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall
            )
        }

        if (focused) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(2.dp)
                    .fillMaxHeight(0.5f)
                    .background(colorScheme.primary.copy(alpha = animatedOpacity))
            )
        }
    }
}

@Preview
@Composable
fun PreviewInput() {
    CodeInput(4)
}

@Preview
@Composable
private fun CharCodePreview() {
    OtpComposableTheme {
        Column {
            CodeChar("1")
            CodeChar("2", true)
        }
    }
}