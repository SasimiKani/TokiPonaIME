package com.tokiponaime.tokiponaime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokiponaime.tokiponaime.ui.theme.TokiPonaIMETheme
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    companion object {
        const val MAX_PAGE = 5

        const val REF_PAGE_00 = "Tap \"System\"."
        const val REF_PAGE_01 = "Tap \"Keyboard\"."
        const val REF_PAGE_02 = "Tap \"On-screen Keyboard\"."
        const val REF_PAGE_03 = "Turn on \"TokiPonaIME\"."
        const val REF_PAGE_04 = "Setup is complete."
        const val REF_PAGE_404 = "Unknown Page"
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TokiPonaIMETheme {
                val navController = rememberNavController() // NavController の作成
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost( // NavHost の追加
                        navController = navController,
                        startDestination = "pager", // 開始画面の設定
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("pager") { // pager 画面の定義
                            PagerScreen(navController)
                        }
                        composable("next") { // next 画面の定義
                            NextScreen()
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerScreen(navController: NavController) {
    val pagerState = rememberPagerState(pageCount = { MainActivity.MAX_PAGE })
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        PageContent(page = page, navController = navController, pagerState = pagerState)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PageContent(page: Int, navController: NavController, pagerState: PagerState, modifier: Modifier = Modifier) { // navController を引数に追加
    var isButtonClicked by remember { mutableStateOf(false) }
    if (page != MainActivity.MAX_PAGE) {
        LaunchedEffect(key1 = isButtonClicked) { // LaunchedEffect を使用してコルーチンを起動
            if (isButtonClicked) {
                pagerState.animateScrollToPage(page + 1)
                isButtonClicked = false
            }
        }
    }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val imageRes = when (page) {
            0 -> R.drawable.ref_00
            1 -> R.drawable.ref_01
            2 -> R.drawable.ref_02
            3 -> R.drawable.ref_03
            4 -> R.drawable.ref_04
            else -> R.drawable.ref_01
        }
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Page $page Image",
            modifier = Modifier
                .fillMaxHeight(0.7f)
                .fillMaxWidth(),
            contentScale = ContentScale.Inside
        )
        Text(
            text = when (page) {
                0 -> MainActivity.REF_PAGE_00
                1 -> MainActivity.REF_PAGE_01
                2 -> MainActivity.REF_PAGE_02
                3 -> MainActivity.REF_PAGE_03
                4 -> MainActivity.REF_PAGE_04
                else -> MainActivity.REF_PAGE_404
            },
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )
        if (page == MainActivity.MAX_PAGE - 1) { // 最後のページ
            Button(onClick = { navController.navigate("next") }) { // ボタンクリックで次の画面に遷移
                Text("Complete")
            }
        } else {
            Button(onClick = {
                isButtonClicked = true
            }) {
                Text("Next")
            }
        }
    }
}

@Composable
fun NextScreen() {
    val context = LocalContext.current
    val activity = context as? MainActivity
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            activity?.finishAffinity()
        }) {Text("Close")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PageContentPreview() {
    TokiPonaIMETheme {
        //PageContent(page = 0)
    }
}