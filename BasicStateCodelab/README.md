# State in Jetpack Compose Codelab

This folder contains the source code for the [State in Jetpack Compose codelab](https://developer.android.com/codelabs/jetpack-compose-state).


In this codelab, you will explore patterns for working with state in a declarative world by building a Wellness application. You’ll learn how to think about state and events in Compose, the mechanism Compose uses to track state changes and update the UI accordingly with APIs such as mutableStateOf and how you can use your ViewModels in a Compose app. 

## License

```
Copyright 2022 The Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Jetpack Compose의 상태를 사용하는 것과 관련한 핵심 개념 학습

# 학습할 내용
- Jetpack Compose UI에서 상태 및 이벤트를 고려하는 방법
- Compose에서 상태를 사용하여 화면에 표시할 요소를 결정하는 방법
- 상태 호이스팅의 정의
- Stateful 및 Stateless Composable function의 작동 방식
- Compose에서 ```State<T>``` API를 사용하여 상태를 자동으로 추적하는 방법
- Composable function에서 메모리 및 내부 상태가 작동하는 방식 : ```remember``` 및 ```rememberSaveable``` API 사용
- 목록 및 상태를 사용하는 방법 : ```mutableStateListOf``` 및 ```toMutableStateList``` API 사용
- Compose와 함께 ```ViewModel```을 사용하는 방법

| 빌드할 항목 | 주요 기능 |
|-|-|
![](https://velog.velcdn.com/images/jlkim909/post/605de800-c84c-4c5c-84f3-576236d126b8/image.png) | 1. 물 섭취량을 추적하는 워터 카운터 ..........................................................................<br/> 2. 하루 동안 해야할 웰니스 작업 목록


# Compose에서의 상태
앱의 '상태'는 시간이 지남에 따라 변할 수 있는 값으로 이는 매우 광범위한 정의로서 Room 데이터베이스로부터 클래스 변수까지 모든 항목이 포함된다.

Android 앱에서는 사용자에게 상태가 표시된다. 다음은 Android 앱 상태의 몇 가지 예시이다.
- 채팅 앱에서 가장 최근에 수신된 메시지
- 사용자의 프로필 사진
- 항목 목록의 스크롤 위치

> 상태에 따라 특정 시점에 UI에 표시되는 항목이 결정된다.

Android 앱에서는 이벤트에 대한 응답으로 상태가 업데이트 된다.
Compose에서 상태 관리는 상태와 이벤트가 서로 상호작용하는 방식을 이해하는 것이 중요하다.

# Compose의 이벤트
이벤트는 애플리케이션 외부 또는 내부에서 생성되는 입력으로 아래 예시를 봐보자
- 버튼 누르기 등으로 UI와 상호작용하는 사용자
- 기타 요인(ex : 새 값을 전송하는 센서 또는 네트워크 응답)

> 앱 상태로 UI에 표시할 항목에 관한 설명이 제공되고, 이벤트를 통해 상태와 UI가 변경된다.

## UI Update Loop
![](https://velog.velcdn.com/images/jlkim909/post/d9586eea-ca2f-4073-95a0-d5997df87670/image.png)

- Event : 이벤트는 사용자 또는 프로그램이 다른 부분에 의해 생성 된다.
- Update State : 이벤트 핸들러가 UI에서 사용하는 상태를 변경한다.
- Display State : 새로운 상태를 표시하도록 UI가 업데이트 된다.

# Composable function의 메모리
Compose 앱은 Composable function을 호출하여 데이터를 UI로 변환한다. 컴포저블을 실행할 때 Compose에서 빌드한 UI에 관한 설명을 **컴포지션**이라고 한다. 상태가 변경되면 Compose는 영향을 받는 Composable function을 새상태로 다시 실행하여 업데이트된 UI가 만들어진다(리컴포지션). 또한 Compose는 데이터가 변경된 구성요소만 재구성하기 때문에 각 컴포저블에 필요한 데이터를 확인한다.
- 컴포지션 : 컴포저블을 실행할 때 Jetpack Compose에서 빌드한 UI에 관한 설명
- 초기 컴포지션 : 처음 컴포저블을 실행하여 컴포지션을 만든다.
- 리컴포지션 : 데이터가 변경될 때 컴포지션을 업데이트하기 위해 컴포저블을 다시 실행하는 것을 말한다.

업데이트를 받을 경우 리컴포지션을 예약할 수 있도록 Compose가 추적할 상태를 알아야한다.

## 리컴포지션 예약
Compose에는 특정 상태를 읽는 컴포저블의 리컴포지션을 예약하는 상태 추적 시스템을 통해 Compose가 세분화되어 전체 UI가 아닌 변경해야 하는 컴포저블만 재구성할 수 있다. 이 작업은 '쓰기'(즉, 상태 변경)뿐만 아니라 상태에 대한 '읽기'도 추적하여 실행된다.

Compose의 ```State``` 및 ```MutableState``` 유형을 사용하여 Compose에서 상태를 관찰할 수 있도록 한다.
Compose는 상태 ```value```속성을 읽는 각 컴포저블을 추적하고 그 ```value```가 변경되면 리컴포지션을 트리거 한다. 
```mutableStateOf``` 함수를 사용하여 관찰 가능한 ```MutableState```를 만들 수 있으며, 이 함수는 초깃값을 ```State``` 객체에 래핑된 매개변수로 수신한 다음, ```value```의 값을 관찰 가능한 상태로 만든다.

예시
```kotlin
@Composable
fun WaterCounter(modifier: Modifier = Modifier) {
   Column(modifier = modifier.padding(16.dp)) {
       val count: MutableState<Int> = mutableStateOf(0)

       Text("You've had ${count.value} glasses.")
        Button(onClick = { count.value++ }, Modifier.padding(top = 8.dp)) {
           Text("Add one")
       }
   }
}
```
- count가 초깃값이 0 인 mutableStatOf API를 사용
- mutableStateOf가 ```value```를 업데이트하여 상태를 업데이트할 수 있고 Compose는 value를 읽는 함수에 리컴포지션을 트리거 한다.

> 리컴포지션 예약은 잘 작동하나 리컴포지션이 발생하면 count 변수가 다시 0으로 초기화되기 때문에 이를 유지할 방법이 필요하다.

## remember
이를 위해서 remember composable 인라인 함수를 사용할 수 있다.
```remember```로 계산된 값은 컴포지션에 저장되고 저장된 값은 리컴포지션 간에 유지된다.

> remember 사용은 private val 속성이 객체에서 실행하는 것과 같은 방식으로 단일 객체를 컴포지션에 저장하는 메커니즘으로 생각하면 된다.

일반적으로 ```remember```와 ```mutableStateOf```는 컴포저블 내에서 함께 사용된다.
```kotlin
val count: MutableState<Int> = remember { mutableStateOf(0) }
var count by remember { mutableStateOf(0) }
```

📌`by` 키워드를 사용하여 count를 var로 정의할 수 있다. 이럴 경우 MutableState의 ```value``` 속성을 명시적으로 참조하지 않고도 count를 간접적으로 읽고 변경할 수 있다.

> 참고 : 이미 LiveData, StateFlow, Flow, RxJava의 Observable과 같은 다른 관찰 가능한 유형을 사용하여 상태를 앱에 저장하고 있을 수 있다. Compose에서 이 상태를 사용하고 상태가 변경될 때 자동으로 재구성하도록 하려면 이를 `State<T>`에 매핑해야 한다. <br/> 이를 위해 설계된 확장 함수가 있으므로 Compose 및 기타 라이브러리 문서에서 이를 찾아야 한다.

# 상태 기반 UI
Compose는 선언형 UI 프레임워크이다. 상태가 변경 될 때 UI구성요소를 삭제하거나 visivilty 상태를 변경하는 대신 특정 상태의 조건에서 UI가 어떻게 표현되어야 하는지를 설명한다. 재구성이 호출되고 UI가 업데이트된 결과, 컴포저블이 결국 컴포지션을 시작하거나 종료할 수 있다.

[컴포저블 수명 주기](https://developer.android.com/jetpack/compose/lifecycle?hl=ko#lifecycle-overview)

![](https://velog.velcdn.com/images/jlkim909/post/40169dda-16bc-4e2c-aa4f-88b9bfecd71f/image.png)

- 뷰 시스템과 마찬가지로 뷰를 수동으로 업데이트하는 복잡성을 방지할 수 있다.
- 새 상태에 따라 뷰를 업데이트하는 일이 자동으로 발생하므로(개발자가 기억할 필요 없음) 오류도 적게 발생한다.

# 컴포지션의 remember
```remember```는 컴포지션에 객체를 저장하고, ```remember```가 호출되는 소스 위치가 리컴포지션 중에 다시 호출되지 않으면 객체를 삭제한다.

```remember```를 사용하여 객체를 저장하는 컴포저블에는 내부 상태가 포함되어 있어 컴포저블을 스테이트풀(Stateful)로 만든다. 
- 호출자가 상태를 제어할 필요가 없고 상태를 직접 관리하지 않아도 상태를 사용할 수 있는 경우에 유용하다.
- 내부 상태를 갖는 컴포저블은 재사용 가능성이 적고 테스트하기가 더 어려운 경향이 있다.

```kotlin
@Composable
fun WaterCounter(modifier: Modifier = Modifier) {
   Column(modifier = modifier.padding(16.dp)) {
       var count by remember { mutableStateOf(0) }
       if (count > 0) {
           var showTask by remember { mutableStateOf(true) }
           if (showTask) {
               WellnessTaskItem(
                   onClose = { showTask = false },
                   taskName = "Have you taken your 15 minute walk today?"
               )
           }
           Text("You've had $count glasses.")
       }

       Row(Modifier.padding(top = 8.dp)) {
           Button(onClick = { count++ }, enabled = count < 10) {
               Text("Add one")
           }
           Button(
               onClick = { count = 0 },
               Modifier.padding(start = 8.dp)) {
                   Text("Clear water count")
           }
       }
   }
}
```
이 코드로 예시를 들면 Add one 버튼을 누를 때 마다 count는 증가되는 걸 알 수 있다. 
여기서 count > 0 라면, showTask를 컴포지션에 저장하는데, 만약 clear 버튼을 누른다면 count가 0이 되서 if (count > 0) 문을 실행하지 않게 되어 showTask가 호출되지 않았기 때문에 showTask를 삭제하게 된다. 결과적으로 다시 count를 증가시키면 showTask = true 인 상태로 컴포지션에 다시 저장하게 된다.


# Compose에서 상태 복원
만약, 디바이스를 가로모드로 회전 시킨다면 Activity는 재생성되기 때문에 앞에서 선언한 count가 다시 0으로 선언되게 된다.
이런 경우를 의도한게 아니라면 ```rememberSaveable```을 사용하여 activity가 재생성 되어도 상태를 유지할 수 있도록 해줄 수 있다.
- ```rememberSaveable```은 `Bundle`에 저장할 수 있는 모든 값을 자동으로 저장한다.
- `rememberSaveable`은 Activity 및 프로세스 재생성 전반에 걸처서도 상태를 유지할 수 있다.

📌 앱의 상태 및 UX 요구사항에 따라 `remember`를 사용할지 `rememberSaveable`을 사용할지 고려해야 한다.

# State hoisting
상태를 보유하지 않은 컴포저블을 Stateless 컴포저블이라고 하며 상태 state hoisting을 사용하면 쉽게 만들 수 있다.

📌 Stateless란 ? : Composable 함수에서 모든 상태를 추출할 수 있는 경우에 Stateless 컴포저블 함수라고 한다.

State hoisting은 UDF(단방향 데이터 흐름) 아키텍처를 Compose에서 구현하는 방법이다.
- 단일 소스 저장소 : 상태를 복제하는 대신 옮겼기 때문에 소스 저장소가 하나만 있다(버그 방지에 도움이 된다)
- 공유 가능 : 끌어올린 상태를 여러 컴포저블과 공유할 수 있다.
- Interceptable : Stateless 컴포저블의 호출자는 상태를 변경하기 전에 이벤트를 무시할지 수정할지 결정할 수 있다.
- 분리됨 : stateless composable function의 상태는 어디에든(ex. ViewModel) 저장할 수 있다.

📌 Stateful과 Stateless 비교
`Stateful` : 시간이 지남에 따라 변할 수 있는 상태를 소유하고 있는 컴포저블
`Stateless` : 상태를 소유하지 않는 컴포저블. 즉, 새 상태를 보유하거나 정의하거나 수정하지 않는다.

예시
stateless 컴포저블
```kotlin
@Composable
fun StatelessCounter(count: Int, onIncrement: () -> Unit, modifier: Modifier = Modifier) {
   Column(modifier = modifier.padding(16.dp)) {
       if (count > 0) {
           Text("You've had $count glasses.")
       }
       Button(onClick = onIncrement, Modifier.padding(top = 8.dp), enabled = count < 10) {
           Text("Add one")
       }
   }
}
```
Stateful 컴포저블
```kotlin
@Composable
fun StatefulCounter() {
    var waterCount by remember { mutableStateOf(0) }

    var juiceCount by remember { mutableStateOf(0) }

    StatelessCounter(waterCount, { waterCount++ })
    StatelessCounter(juiceCount, { juiceCount++ })
}
```

이런식으로 코드를 작성하면 물과 주스의 잔 개수를 계산할 때 독립된 두 가지 상태를 표시할 수 있다.

따라서 다음과 같은 장점이 있다.
1. Stateless 컴포저블을 재사용할 수 있다.
예를 들어 juiceCount가 수정되면 StatefulCounter가 리컴포지션이 되고, StatefulCounter는 juiceCount를 읽는 함수만 식별하고 트리거하여 리컴포지션을 수행한다. 즉, waterCount를 읽는 컴포지션은 리컴포지션되지 않는다.

2. Composable Stateful 함수는 여러 컴포저블 함수에 같은 상태를 제공할 수 있다.
```kotlin
@Composable
fun StatefulCounter() {
   var count by remember { mutableStateOf(0) }

   StatelessCounter(count, { count++ })
   AnotherStatelessMethod(count, { count *= 2 })
}
```

# Observable MutableList
Compose에서 관찰할 수 있는 `MutableList` 객체를 만들어야 한다. 
- Compose가 항목이 추가되거나 목록에서 삭제될 때 변경사항을 추적하여 UI를 재구성할 수 있다.

`mutableStateListOf`를 사용하여 객체를 만들거나 확장 함수 `mutableStateListOf()`를 사용한다.
- `mutableStateListOf` 및 `mutableStateListOf` 함수는 `SnapshotStateList<T>` 유형의 객체를 반환한다.

1. `getWellnessTasks()`를 호출하여 목록 리스트를 불러와 `toMutableStateList`를 사용하여 목록을 만든다.
```kotlin
@Composable
fun WellnessScreen(modifier: Modifier = Modifier) {
   Column(modifier = modifier) {
       StatefulCounter()

       val list = remember { getWellnessTasks().toMutableStateList() }
       WellnessTasksList(list = list, onCloseTask = { task -> list.remove(task) })
   }
}

private fun getWellnessTasks() = List(30) { i -> WellnessTask(i, "Task # $i") }
```

```kotlin
val list = remember { mutableStateListOf<WellnessTask>() }
list.addAll(getWellnessTasks())
```
- 위와 같이 목록을 만든 다음 리스트에 추가하면 예기치 않은 리컴포지션이 발생하고 UI성능이 최적화 되지 않을 수 있다.

2. `WellnessTaskList` 함수 수정, onCloseTask를 추가하고 WellnessTaskItem에 전달
```kotlin
@Composable
fun WellnessTasksList(
   list: List<WellnessTask>,
   onCloseTask: (WellnessTask) -> Unit,
   modifier: Modifier = Modifier
) {
   LazyColumn(modifier = modifier) {
       items(
           items = list,
           key = { task -> task.id }
       ) { task ->
           WellnessTaskItem(taskName = task.label, onClose = { onCloseTask(task) })
       }
   }
}
```
- `items` 메서드는 `key` 매개변수를 받고, 각 상목의 상태는 목록에 있는 위치를 기준으로 키가 지정된다. 만약 삭제로 인해 위치가 변경된 item은 list 데이터 세트가 변경될 때 문제가 발생한다. 
- 위와 같은 문제는 `WellnessTaskItem`의 `id`를 각 항목의 키로 사용하면 쉽게 해결이 가능하다.

3. `WellnessTaskItem`을 수정한다. `WellnessTaskItem` 컴포저블에 checkedState를 선언하여 Stateful 컴포저블로 만들어준다.
```kotlin
@Composable
fun WellnessTaskItem(
   taskName: String, onClose: () -> Unit, modifier: Modifier = Modifier
) {
   var checkedState by rememberSaveable { mutableStateOf(false) }

   WellnessTaskItem(
       taskName = taskName,
       checked = checkedState,
       onCheckedChange = { newValue -> checkedState = newValue },
       onClose = onClose,
       modifier = modifier,
   )
}
```

![](https://velog.velcdn.com/images/jlkim909/post/a4f8e4b8-9dae-4ef9-bf6b-b37e79043ab6/image.png)
위 코드는 그림처럼 동작하게 된다.

📌list를 `rememberSaveable()`를 사용하여 저장하려고 하면 런타임 예외가 발생한다.
- custom saver를 제공해야 하기 때문
- 긴 직렬화 또는 역직렬화가 필요한 복잡한 데이터 구조나 대량의 데이터를 저장하는 데는 `rememberSaveable()`을 사용하지 않도록 한다.

# ViewModel의 State
- ViewModel은 UI State 및 앱의 다른 계층에 위치한 비지니스 로직에 대한 엑세스를 제공한다.
- ViewModel은 컴포지션보다 더 긴 수명을 가지고 있다.

📌 ViewModel은 화면 수준의 컴포저블에서 사용하는 것을 권장한다.
- acitivity, fragment, navigation에서 호출되는 루트 컴포저블 근처에서 사용해야 한다.
- ViewModel은 다른 컴포저블로 전달해서는 안되며, 필요한 데이터나 함수를 매개변수를 통해 전달해야한다.

📌주의 : ViewModel은 컴포지션의 일부가 아니기 때문에 메모리 누수가 발생할 수 있으므로 컴포저블에서 만든 State를 보유해서는 안 된다.

## 목록 이전 및 메서드 삭제
1. UI State, list를 ViewModel로 이전하고 비지니스 로직도 ViewModel로 추출해본다.
- WellnessViewModel 클래스 생성
- `getWellnessTasks()`를 `WellnessViewModel`로 이동
- `toMutableStateList`를 사용하여 내부 `_tasks` 변수를 정의하고 `tasks` 목록으로 노출하여 ViewModel 외부에서 수정할 수 없도록 만든다.
- remove() 함수 구현
```kotlin
class WellnessViewModel : ViewModel() {
    private val _tasks = getWellnessTasks().toMutableStateList()
    val tasks: List<WellnessTask>
        get() = _tasks

   fun remove(item: WellnessTask) {
       _tasks.remove(item)
   }
}

private fun getWellnessTasks() = List(30) { i -> WellnessTask(i, "Task # $i") }
```

2. WellnessScreen 컴포저블의 매개변수로 `viewModel()`을 호출하여 `wellnessViewModel` ViewModel을 객체화 한다.
```kotlin
@Composable
fun WellnessScreen(
    modifier: Modifier = Modifier,
    wellnessViewModel: WellnessViewModel = viewModel()
) {
   Column(modifier = modifier) {
       StatefulCounter()

       WellnessTasksList(
           list = wellnessViewModel.tasks,
           onCloseTask = { task -> wellnessViewModel.remove(task) })
   }
}
```
📌`viewModel()`? 기존 `ViewModel`을 반환하거나 지정한 범위에서 새 ViewModel을 생성한다. ViewModel 객체는 범위가 활성화되어 있는 동안 유지된다. 
- 에를 들어 컴포저블이 Activity에서 사용되는 경우 `viewModel()`은 Activity가 완료되거나 프로세스가 종료될 때까지 동일한 객체를 반환한다.






