Compose에서 즉시 제공되는 컴포저블과 수정자를 통해 실제 디자인을 구현하는 방법을 알아보자

# 학습할 내용
- 수정자를 사용하여 컴포저블을 보강하는 방법
- Column 밒 LazyRow와 같은 표준 레이아웃 구성요소로 하위 컴포저블을 배치하는 방법
- 정렬과 배치로 상위 요소 내에서 하위 컴포저블의 위치를 변경하는 방법
- Scaffold 및 하단 탐색과 같은 Material 컴포저블을 사용하여 포괄적인 레이아웃을 만드는 방법
- 슬롯 API를 사용하여 유연한 컴포저블을 빌드하는 방법
- 다양한 화면 구성에 맞는 레이아웃을 빌드하는 방법

|미리보기| 회전
|-|-
![](https://velog.velcdn.com/images/jlkim909/post/4730d185-c49e-4096-b76d-bb1169292042/image.png) |![](https://velog.velcdn.com/images/jlkim909/post/0bb129e7-97af-4b76-965f-60e16e89ced2/image.png)

디자인을 분석 해보기
```
> 화면의 메인 콘텐츠
	> 검색창
    	> 검색 아이콘
        > 내용
    > "신체의 조화" 섹션
    	> 섹션제목
        > 원형 이미지 리스트(횡스크롤)
        	> 아이템
        		> 원형 이미지
            	> 내용
    > "즐겨찾는 컬렉션" 섹션
    	> 섹션 제목
        > 카드뷰 리스트(2xn)(횡스크롤)
        	> 카드 뷰
            	> 이미지
                > 내용
> 하단 네비게이션
	> 네이게이션 아이템
    	> 아이콘
        > 내용
```

# 검색창
```kotlin
@Composable
fun SearchBar(
   modifier: Modifier = Modifier
) {
   TextField(
       value = "",
       onValueChange = {},
       leadingIcon = {
           Icon(
               imageVector = Icons.Default.Search,
               contentDescription = null
           )
       },
       colors = TextFieldDefaults.colors(
           unfocusedContainerColor = MaterialTheme.colorScheme.surface,
           focusedContainerColor = MaterialTheme.colorScheme.surface
       ),
       placeholder = {
           Text(stringResource(R.string.placeholder_search))
       },
       modifier = modifier
           .fillMaxWidth()
           .heightIn(min = 56.dp)
   )
}
```
- TextField의 leadingIcon 매개변수로 아이콘 세팅
- modifier.heightIn() 함수를 통해 최대 높이 56dp로 설정
- 텍스트 필드의 배경색을 조정하기 위해 ```colors```속성 설정

# 신체의 조화 섹션 아이템
```kotlin
@Composable
fun AlignYourBodyElement(
   @DrawableRes drawable: Int,
   @StringRes text: Int,
   modifier: Modifier = Modifier
) {
   Column(
       modifier = modifier,
       horizontalAlignment = Alignment.CenterHorizontally
   ) {
       Image(
           painter = painterResource(drawable),
           contentDescription = null,
           contentScale = ContentScale.Crop,
           modifier = Modifier
               .size(88.dp)
               .clip(CircleShape)
       )
       Text(
           text = stringResource(text),
           modifier = Modifier.paddingFromBaseline(top = 24.dp, bottom = 8.dp),
           style = MaterialTheme.typography.bodyMedium
       )
   }
}
```
```Column```의 horizontalAlignment를 통해 가운데 정렬
Image가 원형으로 들어가야하기 때문에 clip 수정자 사용, size 수정자를 사용해서 88dp로 설정
Text는 paddingFromBaseline()을 통해 이미지 아래 24dp, 바텀으로부터 8dp 만큼의 패딩을 준다
@DrawableRes, @StringRes 애노테이션 사용 이유 : 코드 가독성을 향상시키고, 정적 분석 도구에 리소스 ID에 관한 정보를 정확히 전달할 수 있다. 코드의 의도를 명시적으로 전달할 수 있기 때문에 유지보수성을 높일 수 있다.

# 즐겨찾는 섹션 카드 뷰
```kotlin
@Composable
fun FavoriteCollectionCard(
   @DrawableRes drawable: Int,
   @StringRes text: Int,
   modifier: Modifier = Modifier
) {
   Surface(
       shape = MaterialTheme.shapes.medium,
       color = MaterialTheme.colorScheme.surfaceVariant,
       modifier = modifier
   ) {
       Row(
           verticalAlignment = Alignment.CenterVertically,
           modifier = Modifier.width(255.dp)
       ) {
           Image(
               painter = painterResource(drawable),
               contentDescription = null,
               contentScale = ContentScale.Crop,
               modifier = Modifier.size(80.dp)
           )
           Text(
               text = stringResource(text),
               style = MaterialTheme.typography.titleMedium,
               modifier = Modifier.padding(horizontal = 16.dp)
           )
       }
   }
}
```
Surface의 shape에 MaterialTheme.shapes.medium을 통해 라운드 테두리를 적용

# 신체의 조화 섹션 리스트 만들기
```kotlin
@Composable
fun AlignYourBodyRow(
   modifier: Modifier = Modifier
) {
   LazyRow(
       horizontalArrangement = Arrangement.spacedBy(8.dp),
       contentPadding = PaddingValues(horizontal = 16.dp),
       modifier = modifier
   ) {
       items(alignYourBodyData) { item ->
           AlignYourBodyElement(item.drawable, item.text)
       }
   }
}
```
horizontalArrangement = Arrangement.spacedBy(8.dp)을 통하여 각 아이템 항목에 간격을 주고, modifier를 통해 padding 주는게 아닌(잘리는 현상 발생) contentPadding을 통하여 패딩을 주면 스크롤시 잘리는 현상을 막을 수 있다.

# 즐겨찾는 컬렉션 섹션 리스트 만들기
```kotlin
@Composable
fun FavoriteCollectionsGrid(
   modifier: Modifier = Modifier
) {
   LazyHorizontalGrid(
       rows = GridCells.Fixed(2),
       contentPadding = PaddingValues(horizontal = 16.dp),
       horizontalArrangement = Arrangement.spacedBy(16.dp),
       verticalArrangement = Arrangement.spacedBy(16.dp),
       modifier = modifier.height(168.dp)
   ) {
       items(favoriteCollectionsData) { item ->
           FavoriteCollectionCard(item.drawable, item.text, Modifier.height(80.dp))
       }
   }
}
```
```LazyHorizontalGrid```를 통해 그리드를 형성하여 리스트를 만들고, GridCells.Fiexed(2)를 사용하여 2개의 열로 고정

# 각 섹션에 타이틀을 포함한 컨테이너 만들기
```kotlin
@Composable
fun HomeSection(
   @StringRes title: Int,
   modifier: Modifier = Modifier,
   content: @Composable () -> Unit
) {
   Column(modifier) {
       Text(
           text = stringResource(title),
           style = MaterialTheme.typography.titleMedium,
           modifier = Modifier
               .paddingFromBaseline(top = 40.dp, bottom = 16.dp)
               .padding(horizontal = 16.dp)
       )
       content()
   }
}
```
컴포저블의 슬롯으로 ```content``` 매개변수를 사용할 수 있으며, 후행 람다를 사용하여 콘텐츠 슬롯을 채울 수 있다. 만약 컴포저블이 채울 수 있는 슬롯을 여러 개 제공한다면 더 큰 컴포저블 컨테이너에서 각각의 기능을 나타내는 의미 있는 이름을 지정하면 된다. 예를 들어 ```TopAppBar```는 ```title```, ```navigationIcon```, ```actions``` 슬롯을 제공한다.


📌슬롯 기반 레이아웃이란? 개발자가 원하는데로 채울 수 있도록 UI에 빈 공간을 남겨 둔다. 슬롯 기반 레이아웃을 사용하면 보다 유연한 레이아웃을 만들 수 있다.

# 홈 화면 구성하기
```kotlin
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
   Column(
       modifier
           .verticalScroll(rememberScrollState())
   ) {
       Spacer(Modifier.height(16.dp))
       SearchBar(Modifier.padding(horizontal = 16.dp))
       HomeSection(title = R.string.align_your_body) {
           AlignYourBodyRow()
       }
       HomeSection(title = R.string.favorite_collections) {
           FavoriteCollectionsGrid()
       }
       Spacer(Modifier.height(16.dp))
   }
}
```
지금까지 만든 컴포저블을 이용하여 홈 화면에 배치하도록 한다. 이때 Column에서 수정자를 통해 스크롤을 수정으로 추가해주어야 한다. Lazy의 경우 자동으로 추가 되지만, Lazy 컴포저블은 목록에 포함된 요소가 많거나 로드를 해야할 경우 사용하기 때문에 일반 Column 컴포저블을 사용한다.
```rememberScrollState```를 사용하여 스크롤을 달아줬다.

# 하단 네이비게이션 바 만들기
```kotlin
@Composable
private fun SootheBottomNavigation(modifier: Modifier = Modifier) {
   NavigationBar(
       containerColor = MaterialTheme.colorScheme.surfaceVariant,
       modifier = modifier
   ) {
       NavigationBarItem(
           icon = {
               Icon(
                   imageVector = Icons.Default.Spa,
                   contentDescription = null
               )
           },
           label = {
               Text(stringResource(R.string.bottom_navigation_home))
           },
           selected = true,
           onClick = {}
       )
       NavigationBarItem(
           icon = {
               Icon(
                   imageVector = Icons.Default.AccountCircle,
                   contentDescription = null
               )
           },
           label = {
               Text(stringResource(R.string.bottom_navigation_profile))
           },
           selected = false,
           onClick = {}
       )
   }
}
```
Compose Material 라이브러리의 일부인 NavigationBar 컴포저블을 이용하여 하단 바를 만들었으며, 콘텐츠 색상과 하단 바의 대비를 위해 ContainerColor를 변경시켜준다.

# 전체 화면 구현
```kotlin
@Composable
fun MySootheAppPortrait() {
   MySootheTheme {
       Scaffold(
           bottomBar = { SootheBottomNavigation() }
       ) { padding ->
           HomeScreen(Modifier.padding(padding))
       }
   }
}
```
MySootheAppPortrait 컴포저블은 최상위 수준 컴포저블이므로 다음과 같은 작업이 포함되어야 한다.
- ```MySootheTheme``` Material 테마를 적용
- Scaffold를 추가
- 하단 바가 ```SootheBottomNavigation```컴포저블이 되도록 설정
- 콘텐츠가 ```HomeScreen``` 컴포저블이 되도록 설정

📌```Scaffold``` 컴포저블? Material Design을 구현하는 앱을 위한 최상위 수준 컴포저블을 제공한다. 여기에는 다양한 Material 개념의 슬롯이 포함되어 있다. 

# 가로 모드 네이게이션 바 만들기
```kotlin
@Composable
private fun SootheNavigationRail(modifier: Modifier = Modifier) {
   NavigationRail(
       modifier = modifier.padding(start = 8.dp, end = 8.dp),
       containerColor = MaterialTheme.colorScheme.background,
   ) {
       Column(
           modifier = modifier.fillMaxHeight(),
           verticalArrangement = Arrangement.Center,
           horizontalAlignment = Alignment.CenterHorizontally
       ) {
           NavigationRailItem(
               icon = {
                   Icon(
                       imageVector = Icons.Default.Spa,
                       contentDescription = null
                   )
               },
               label = {
                   Text(stringResource(R.string.bottom_navigation_home))
               },
               selected = true,
               onClick = {}
           )
           Spacer(modifier = Modifier.height(8.dp))
           NavigationRailItem(
               icon = {
                   Icon(
                       imageVector = Icons.Default.AccountCircle,
                       contentDescription = null
                   )
               },
               label = {
                   Text(stringResource(R.string.bottom_navigation_profile))
               },
               selected = false,
               onClick = {}
           )
       }
   }
}
```
기본적으로 하단 바 만들 때와 유사하지만, ```NavigationRail```과 ```NavigationRailItem```을 사용하며, 크기 조절을 해줄 필요가 있다.

# 가로 모드 구현
```kotlin
@Composable
fun MySootheAppLandscape() {
   MySootheTheme {
       Surface(color = MaterialTheme.colorScheme.background) {
           Row {
               SootheNavigationRail()
               HomeScreen()
           }
       }
   }
}
```
```Scaffold```를 사용하지 않고 Row를 사용한다.

# 가로 모드 지원
```calculateWindowSizeClass()``` 함수를 사용하여 휴대전화의 창 크기를 확인한다.
창 크기 클래스 너비에는 Compact(소형), Medium(중형), Expanded(확장형) 세 가지가 있고, 앱이 세로 모드인 경우 소형 너비이고, 가로 모드일 경우에는 확정형 너비이다. 
MySootheApp 컴포저블에서 WindowSizeClass를 가져와 업데이트 할 수 있도록 만들어 준다.
![](https://velog.velcdn.com/images/jlkim909/post/aded73f7-f40e-4975-ad37-a044679ee221/image.png)

```kotlin
@Composable
fun MySootheApp(windowSize: WindowSizeClass) {
   when (windowSize.widthSizeClass) {
       WindowWidthSizeClass.Compact -> {
           MySootheAppPortrait()
       }
       WindowWidthSizeClass.Expanded -> {
           MySootheAppLandscape()
       }
   }
}
```
이 후, ```setContent()``` 에서 ```calculateWindowSizeClass()```로 설정된 클래스를 만들고 MySootheApp()에 전달하도록 한다.

📌calculateWindowSize()는 아직 실험 단계이므로 @OptIn(ExperimentalMaterial3WindowSizeClassApi::class) 를 통해 클래스를 선택해야 한다.
```kotlin
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
```
