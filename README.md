# 悦读 - Android 小说阅读器

类似 UC 浏览器书城的小说阅读 App：在线搜书、加入书架、本地缓存阅读，内置广告过滤。

## 功能

- **在线搜书**：多书源聚合搜索（内置 4 个笔趣阁类书源，规则可扩展）
- **书架管理**：Grid 封面书架、阅读进度记录
- **阅读器**：4 套配色主题（纸张/护眼绿/夜间/冷蓝）、字号 12-32 可调、章节目录跳转、上下章切换
- **本地缓存**：章节目录与正文落库（Room），离线可读
- **广告净化**：正文抓取时剥离 script/iframe/adsbygoogle 等标签，内置广告域名黑名单

## 技术栈

Kotlin + Jetpack Compose + Room + OkHttp + Jsoup + Coil

## 构建

```bash
# 需要 JDK 17 与 Android SDK（API 34）
gradlew assembleDebug    # 调试包 app/build/outputs/apk/debug/app-debug.apk
gradlew assembleRelease  # 正式包（需自行签名）
```

## 添加自定义书源

书源存储在 Room 表 `book_sources` 中，每条规则都是 CSS 选择器：

| 字段 | 说明 |
|------|------|
| searchUrl | 搜索地址，`{{key}}` 为关键词占位符 |
| ruleSearchBookList | 搜索结果列表选择器 |
| ruleSearchName / ruleSearchAuthor | 书名 / 作者（`a@text` 格式：选择器@属性） |
| ruleChapterList | 目录列表选择器 |
| ruleContent | 正文容器选择器 |

## 已知限制

- 正式包未签名，安装请用 debug 包或自行生成 keystore 签名
- 书源网站可能改版，需更新对应选择器规则