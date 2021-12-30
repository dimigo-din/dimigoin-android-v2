package `in`.dimigo.dimigoin.ui

import `in`.dimigo.dimigoin.ui.theme.DimigoinTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DimigoinTheme {
                Surface(color = MaterialTheme.colors.background) {
                }
            }
        }
    }
}
