/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codelab.basiclayouts

import android.os.Bundle
import android.widget.GridLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codelab.basiclayouts.ui.theme.MySootheTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            MySootheApp(windowSizeClass)
        }
    }
}

// 높이 : 56dp
@Composable
fun SearchBar(
    modifier: Modifier = Modifier
) {
    TextField(
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        value = "",
        onValueChange = {},
        placeholder = {
            Text(stringResource(id = R.string.placeholder_search))
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
    )
}

//@Preview(showBackground = true)
//@Composable
//private fun SearchBarPreview(){
//    MySootheTheme {
//        SearchBar(Modifier.padding(8.dp))
//    }
//}

// 이미지 크기 88dp
// 텍스트 마진 : top 24dp, bottom 8dp
@Composable
fun AlignYourBodyElement(
    @DrawableRes drawable:Int,
    @StringRes title:Int,
    modifier:Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = drawable),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
        )
        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .paddingFromBaseline(
                    top = 24.dp,
                    bottom = 8.dp
                )
        )
    }
}

//@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
//@Composable
//fun AlignYourBodyElementPreview(){
//    MySootheTheme {
//        AlignYourBodyElement(
//            R.drawable.ab1_inversions,
//            R.string.ab1_inversions,
//            Modifier.padding(8.dp)
//        )
//    }
//}

// 카드 너비 : 255dp
// 이미지 크기 : 80dp
// 텍스트 마진 : horizontal 16dp
@Composable
fun FavoriteCollectionCard(
    @DrawableRes drawable:Int,
    @StringRes title:Int,
    modifier:Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(255.dp)
        ){
            Image(
                painter = painterResource(id = drawable),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp)
            )
            Text(
                text = stringResource(id = title),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

//@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
//@Composable
//fun FavoriteCollectionCardPreview() {
//    MySootheTheme {
//        FavoriteCollectionCard(
//            R.drawable.fc2_nature_meditations,
//            R.string.fc2_nature_meditations,
//            modifier = Modifier.padding(8.dp)
//        )
//    }
//}

// 간격 : 8dp
// 리스트 패딩 : 16dp
@Composable
private fun AlignYourBodyRow(
    modifier:Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        items(alignYourBodyData){ item ->
            AlignYourBodyElement(
                drawable = item.drawable,
                title = item.text
            )
        }
    }
}

//@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
//@Composable
//fun AlignYourBodyRowPreview(){
//    MySootheTheme {
//        AlignYourBodyRow()
//    }
//}

// 리스트 패딩 16dp
// 행,열 간격 16dp
// 리스트 높이 168dp
// 아이템 높이 80dp
@Composable
fun FavoriteCollectionsGrid(
    modifier:Modifier = Modifier
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        content = {
            items(favoriteCollectionsData){item ->
                FavoriteCollectionCard(
                    drawable = item.drawable,
                    title = item.text,
                    modifier = Modifier.height(80.dp)
                )
            }
        },
        modifier = modifier
            .height(168.dp)
    )
}

//@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
//@Composable
//fun FavoriteCollectionsGridPreview(){
//    MySootheTheme {
//        FavoriteCollectionsGrid()
//    }
//}

// 타이틀 마진 : top 40dp bottom 16dp
// 타이틀 패딩 : horizontal 16dp
@Composable
fun HomeSection(
    @StringRes title:Int,
    modifier:Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .paddingFromBaseline(
                    top = 40.dp,
                    bottom = 16.dp
                )
        )
        content()
    }
}

// 배치 간격 16dp
// 검색창 패딩 16dp
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ){
        Spacer(modifier = Modifier.height(16.dp))
        SearchBar(Modifier
            .padding(horizontal = 16.dp)
        )
        HomeSection(title = R.string.align_your_body){
            AlignYourBodyRow()
        }
        HomeSection(title = R.string.favorite_collections,){
            FavoriteCollectionsGrid()
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

//@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
//@Composable
//fun HomeScreenPreview(){
//    MySootheTheme {
//        HomeScreen()
//    }
//}

// Step: Bottom navigation - Material
@Composable
private fun SootheBottomNavigation(
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = {
                Icon(
                    imageVector = Icons.Default.Spa,
                    contentDescription = null
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.bottom_navigation_home)
                )
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.bottom_navigation_profile)
                )
            }
        )
    }
}

//@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
//@Composable
//fun BottomNavigationPreview() {
//    MySootheTheme { SootheBottomNavigation(Modifier.padding(top = 24.dp)) }
//}

// Step: MySoothe App - Scaffold
@Composable
fun MySootheAppPortrait() {
    MySootheTheme {
        Scaffold(
            bottomBar = {
                SootheBottomNavigation()
            }
        ) {padding ->
            HomeScreen(Modifier.padding(padding))
        }
    }
}

//@Preview(widthDp = 360, heightDp = 640)
//@Composable
//fun MySoothePortraitPreview() {
//    MySootheAppPortrait()
//}

// Step: Bottom navigation - Material
@Composable
private fun SootheNavigationRail(
    modifier: Modifier = Modifier
) {
    NavigationRail(
        modifier = modifier.padding(
            start = 8.dp,
            end = 8.dp
        ),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            NavigationRailItem(
                selected = true,
                onClick = {},
                icon = {
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.bottom_navigation_home)
                    )
                }
            )
            NavigationRailItem(
                selected = false,
                onClick = {},
                icon = {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.bottom_navigation_profile)
                    )
                }
            )
        }
    }
}

// Step: Landscape Mode
@Composable
fun MySootheAppLandscape() {
    MySootheTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ){
            Row{
                SootheNavigationRail()
                HomeScreen()
            }
        }
    }
}

@Preview(widthDp = 640, heightDp = 360)
@Composable
fun MySootheLandscapePreview() {
    MySootheAppLandscape()
}

// Step: MySoothe App
@Composable
fun MySootheApp(windowSize: WindowSizeClass) {
    when(windowSize.widthSizeClass){
        WindowWidthSizeClass.Compact -> {
            MySootheAppPortrait()
        }
        WindowWidthSizeClass.Expanded -> {
            MySootheAppLandscape()
        }
    }
}

private val alignYourBodyData = listOf(
    R.drawable.ab1_inversions to R.string.ab1_inversions,
    R.drawable.ab2_quick_yoga to R.string.ab2_quick_yoga,
    R.drawable.ab3_stretching to R.string.ab3_stretching,
    R.drawable.ab4_tabata to R.string.ab4_tabata,
    R.drawable.ab5_hiit to R.string.ab5_hiit,
    R.drawable.ab6_pre_natal_yoga to R.string.ab6_pre_natal_yoga
).map { DrawableStringPair(it.first, it.second) }

private val favoriteCollectionsData = listOf(
    R.drawable.fc1_short_mantras to R.string.fc1_short_mantras,
    R.drawable.fc2_nature_meditations to R.string.fc2_nature_meditations,
    R.drawable.fc3_stress_and_anxiety to R.string.fc3_stress_and_anxiety,
    R.drawable.fc4_self_massage to R.string.fc4_self_massage,
    R.drawable.fc5_overwhelmed to R.string.fc5_overwhelmed,
    R.drawable.fc6_nightly_wind_down to R.string.fc6_nightly_wind_down
).map { DrawableStringPair(it.first, it.second) }

private data class DrawableStringPair(
    @DrawableRes val drawable: Int,
    @StringRes val text: Int
)
