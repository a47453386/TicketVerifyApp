# 🔍 高供發票驗證系統 - 驗證端 App

一款 Android 行動應用程式，供工作人員掃描 QR Code
即時驗證票券真偽，與主系統資料庫串接確認票券狀態。

## 🎬 功能展示影片
👉 [點我觀看完整操作影片]（貼上你的 Canva 連結）

## 🛠 使用技術
- 平台：Android（Java）
- QR Code 掃描： CameraX（
- 後端串接：ASP.NET Core 8 Web API
- 版本控制：Git / GitHub

## ✨ 主要功能
- QR Code 即時掃描
- 票券真偽驗證（串接主系統 API）
- 驗證結果即時顯示（有效／無效／已使用）

## 🔗 相關專案
使用者購票端 App：[https://github.com/a47453386/TicketSalesSystemAPP.git]
Web端：[https://github.com/a47453386/TicketSalesSystem.git]

## 💻 執行方式
本專案已實際部署於 Android 實體裝置，可搭配銷售端 App 進行完整票券驗證流程。

**開發環境執行：**
1. Clone 此專案至 Android Studio
2. 修改 API 連線位址（設定檔中的 Base URL）
3. 確認後端 Web API 已啟動
4. 安裝至實體 Android 裝置執行（建議使用實體裝置以測試 QR Code 掃描）
