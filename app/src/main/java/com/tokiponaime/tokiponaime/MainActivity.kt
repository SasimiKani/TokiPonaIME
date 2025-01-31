package com.tokiponaime.tokiponaime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokiponaime.tokiponaime.ui.theme.TokiPonaIMETheme
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TokiPonaIMETheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val pagerState = rememberPagerState(pageCount = { 5 })
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.padding(innerPadding)
                    ) { page ->
                        PageContent(page = page)
                    }
                }
            }
        }
    }
}

@Composable
fun PageContent(page: Int, modifier: Modifier = Modifier) {
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
                .fillMaxSize()
                .padding(bottom = 30.dp), // 変更点
            contentScale = ContentScale.Inside // 変更点
        )
//        Text(
//            text = "Page ${page + 1}",
//            style = MaterialTheme.typography.headlineLarge,
//            textAlign = TextAlign.Center
//        )
        Text(
            text = when (page) {
                0 -> "@string/ref_page_00"
                1 -> "@string/ref_page_01"
                2 -> "@string/ref_page_02"
                3 -> "@string/ref_page_03"
                4 -> "@string/ref_page_04"
                else -> "@string/ref_page_404"
            },
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PageContentPreview() {
    TokiPonaIMETheme {
        PageContent(page = 0)
    }
}