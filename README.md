# ComposeWaveLoading



# 1. 使用方式

在 root 的 `build.gradle` 中引入 `jitpack`，
```groovy
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
在 module 的 `build.gradle` 中引入 `ComposeWaveLoading` 的最新版本
```groovy
dependencies {
    implementation 'com.github.vitaviva:ComposeWaveLoading:$latest_version'
}
```

# 2. API 设计思想


```kotlin

Box {
    WaveLoading (
        progress = 0.5f // 0f ~ 1f
    ) {
        Image(
          painter = painterResource(id = R.drawable.logo_tiktok),
          contentDescription = ""
        )
    }
}

```
传统的 UI 开发方式中，设计这样一个波浪控件，一般会使用自定义 View 并将 Image 等作为属性传入。 而在 Compose 中，我们让 `WaveLoading` 和 `Image` 以组合的方式使用，这样的 API 更加灵活，`WaveLoding` 的内部可以是 `Image`，也可以是 `Text` 亦或是其他 `Composable`。波浪动画不拘泥于某一特定 Composable， 任何 Composable 都可以以波浪动画的形式展现, **通过 Composable 的组合使用，我们扩大了 “能力” 的覆盖范围。**


# 3. API 参数介绍

```kotlin
@Composable
fun WaveLoading(
    modifier: Modifier = Modifier,
    foreDrawType: DrawType = DrawType.DrawImage,
    backDrawType: DrawType = rememberDrawColor(color = Color.LightGray),
    @FloatRange(from = 0.0, to = 1.0) progress: Float = 0f,
    @FloatRange(from = 0.0, to = 1.0) amplitude: Float = defaultAmlitude,
    @FloatRange(from = 0.0, to = 1.0) velocity: Float = defaultVelocity,
    content: @Composable BoxScope.() -> Unit
) { ... }
```

|参数| 说明|
|--|--|
|progress| 加载进度 |
|foreDrawType| 波浪图的绘制类型: `DrawColor` 或者 `DrawImage` |
|backDrawType| 波浪图的背景绘制|
|amplitude| 波浪的振幅， 0f ~ 1f 表示振幅在整个绘制区域的占比|
|velocity|波浪移动的速度|
|content| 子Composalble|

```kotlin
sealed interface DrawType {
    object None : DrawType
    object DrawImage : DrawType
    data class DrawColor(val color: Color) : DrawType
}
```
如上，DrawType 有三种类型:

- None： 不进行绘制
- DrawColor：使用单一颜色绘制
- DrawImage：按照原样绘制

以下面这个 `Image` 为例, 体会一下不同 DrawType 的组合效果

|index|backDrawType|foreDrawType | 说明 |
|--|--|--| --|
|1|DrawImage| DrawImage| 背景灰度，前景原图|
|2|DrawColor(Color.LightGray)|DrawImage|背景单色，前景原图 |
|3|DrawColor(Color.LightGray)|DrawColor(Color.Cyan)| 背景单色，前景单色|
|4|None|DrawColor(Color.Cyan)| 无背景，前景单色|

<img src="/screenshot/screenshot_1.gif" width="480">

<img src="/screenshot/screenshot_2.gif" width="480">
