package com.isaacdelosreyes.lazycolumnanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isaacdelosreyes.lazycolumnanimation.ui.theme.LazyColumnAnimationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LazyColumnAnimationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AnimatedLazyColumn()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnimatedLazyColumn() {

    val items = remember {
        listOf(
            ItemSealedClass.Header("January"),
            ItemSealedClass.Item("Day 1"),
            ItemSealedClass.Item("Day 2"),
            ItemSealedClass.Item("Day 3"),
            ItemSealedClass.Item("Day 4"),
            ItemSealedClass.Item("Day 5"),
            ItemSealedClass.Header("February"),
            ItemSealedClass.Item("Day 1"),
            ItemSealedClass.Item("Day 2"),
            ItemSealedClass.Item("Day 3"),
            ItemSealedClass.Item("Day 4"),
            ItemSealedClass.Item("Day 5"),
            ItemSealedClass.Header("March"),
            ItemSealedClass.Item("Day 1"),
            ItemSealedClass.Item("Day 2"),
            ItemSealedClass.Item("Day 3"),
            ItemSealedClass.Item("Day 4"),
            ItemSealedClass.Item("Day 5"),
            ItemSealedClass.Header("April"),
            ItemSealedClass.Item("Day 1"),
            ItemSealedClass.Item("Day 2"),
            ItemSealedClass.Item("Day 3"),
            ItemSealedClass.Item("Day 4"),
            ItemSealedClass.Item("Day 5"),
        )
    }

    val listState = rememberLazyListState()

    val lastVisibleItemIndex by remember {
        derivedStateOf {
            listState.layoutInfo
                .visibleItemsInfo
                .lastOrNull {
                    it.contentType == ItemTypeEnum.Item
                }?.index ?: 1
        }
    }

    val visibleHeaders by remember {
        derivedStateOf {
            listState.layoutInfo
                .visibleItemsInfo
                .filter {
                    it.contentType == ItemTypeEnum.Header
                }
                .size
        }
    }

    val visibleItems by remember {
        derivedStateOf {
            listState.layoutInfo
                .visibleItemsInfo
                .filter {
                    it.contentType == ItemTypeEnum.Item
                }
                .size
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Items visibles: $visibleItems")

        Text(text = "Header visibles: $visibleHeaders")

        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 30.dp
            ),
            modifier = Modifier.fillMaxWidth()
        ) {

            items.forEachIndexed { index, item ->

                if (item is ItemSealedClass.Header) {

                    stickyHeader(contentType = ItemTypeEnum.Header) {

                        Text(
                            text = item.title,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(vertical = 10.dp)
                        )
                    }

                } else if (item is ItemSealedClass.Item) {

                    item(contentType = ItemTypeEnum.Item) {

                        val showAnimation = (lastVisibleItemIndex != index
                                && lastVisibleItemIndex > index)
                                || listState.isAtBottom()

                        val animateAlpha by animateFloatAsState(
                            targetValue = if (showAnimation) {
                                1f

                            } else {
                                0f
                            },
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = LinearOutSlowInEasing
                            ),
                            label = ""
                        )

                        val animateScale by animateFloatAsState(
                            targetValue = if (showAnimation) {
                                1f

                            } else {
                                0.6f
                            },
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = LinearOutSlowInEasing
                            ),
                            label = ""
                        )

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .scale(animateScale)
                                .alpha(animateAlpha)
                                .height(80.dp)
                                .fillMaxWidth()
                                .clip(CircleShape)
                                .background(Color.Blue)
                        ) {
                            Text(item.title, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LazyListState.isAtBottom() = remember(this) {
    derivedStateOf {
        val visibleItemsInfo = layoutInfo.visibleItemsInfo

        if (layoutInfo.totalItemsCount == 0) {
            false

        } else {
            val lastVisibleItem = visibleItemsInfo.last()
            val viewportHeight = layoutInfo.viewportEndOffset +
                    layoutInfo.viewportStartOffset

            (lastVisibleItem.index + 1 == layoutInfo.totalItemsCount
                    && lastVisibleItem.offset + lastVisibleItem.size <=
                    viewportHeight)
        }
    }
}.value