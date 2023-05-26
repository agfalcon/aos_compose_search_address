package kgb.plum.searchaddresspractice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kgb.plum.searchaddresspractice.ui.theme.SearchAddressPracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchAddressPracticeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val viewModel: MainViewModel = viewModel()
    val state = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Crossfade(targetState = state.value) {
            when(it){
                false -> {
                    viewModel.setAddress(AddressInfo.address)
                    Column() {
                        OutlinedTextField(
                            value = viewModel.address.observeAsState(AddressInfo.address).value,
                            onValueChange = { viewModel.setAddress(it) },
                            label = { Text("주소를 입력해주세요.") },
                            readOnly = true,
                            modifier = Modifier.clickable(
                                onClick = {
                                    state.value = true
                                },
                                enabled = true
                            ),
                            enabled = false
                        )
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            label = { Text("상세주소") }
                        )
                    }
                }
                 true -> {
                     AndroidView(factory = {
                         WebView(it).apply {
                             settings.javaScriptEnabled = true
                             addJavascriptInterface(AddressBridgeInterface(state = state), "Android")
                             webViewClient = object : WebViewClient() {
                                 override fun onPageFinished(view: WebView, url: String) {
                                     loadUrl("javascript:sample2_execDaumPostcode();")
                                 }
                             }
                             loadUrl("https://whale-7993e.web.app/")
                         }
                     },
                         modifier = Modifier.fillMaxSize().padding(32.dp)
                     )
                 }
            }
        }

    }

}


class AddressBridgeInterface(val state: MutableState<Boolean>){
    @JavascriptInterface
    fun processDATA(data: String) {
        Log.d("테스트", "되나?")
        Log.d("테스트" , data)
        AddressInfo.address = data
        state.value = false
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SearchAddressPracticeTheme {

    }
}