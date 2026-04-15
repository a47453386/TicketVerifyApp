# 🔍 高供票務管理系統 - 票券驗證 Android App

供工作人員掃描 QR Code 即時驗證票券真偽的 Android 應用程式，
與主系統資料庫串接確認票券狀態，防止重複入場。
已實際部署於實體 Android 裝置。

## 🎬 功能展示影片
👉 [https://canva.link/1odpz69pbqo58w6]

## 🛠 使用技術
- 平台：Android（Java）
- QR Code 掃描：ZXing
- 後端串接：ASP.NET Core 8 Web API
- Git / GitHub

## ✨ 主要功能
- QR Code 即時掃描
- 串接主系統 API 驗證票券真偽
- 驗證結果即時顯示（有效／無效／已使用）
- 防止重複入場驗證機制

## 📲 部署狀態
本專案已成功部署並安裝於實體 Android 裝置，可完整執行 QR Code 掃描驗證流程。

## 🔗 相關專案
- 後端 Web API：[https://github.com/a47453386/TicketSalesSystem.git]
- 使用者購票 App：[https://github.com/a47453386/TicketSalesSystemAPP.git]

## 💻 本機執行方式
1. Clone 此專案至 Android Studio
2. 修改 API 連線位址（Base URL 設定檔）
3. 確認後端 TicketSalesSystem Web API 已啟動
4. 安裝至實體 Android 裝置執行
   （建議使用實體裝置以正常啟用相機掃描功能）

