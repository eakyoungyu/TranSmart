# TranSmart
TranSmart is an Android application designed to assist users in calculating the cost of money transfers from Korea to Canada.
## Tech Stack
- Android Application (Kotlin, Jetpack Compose)
- MVVM architecture with repository pattern
- Hilt
- Room database
- Navigation & WebView
- WorkManager & Notification
- [skrape.it](https://github.com/skrapeit/skrape.it): web scraper
- [Charts](https://github.com/dautovicharis/Charts): currency rate chart
## Features
- Scrape currency rates every day by using WorkManager
- Send a notification if today is the lowest rate of the week
- Provide web pages by using WebView
- Allow users to add custom banks
- Enable users to calculate the cost of money transfers

## Improved Features
### 1. Pre-populated Data in Database
- Implemented pre-populated data by importing from "default-bank.db" and scraping currency rates from the web.
``` kotlin
        database = Room.databaseBuilder(context, BankDatabase::class.java, "bank.db")
            .createFromAsset("default-bank.db")
            .build()
```


## Screenshots
![TranSmart0409_1](https://github.com/eakyoungyu/TranSmart/assets/39245582/64748626-041c-4ada-a342-36d1626e11f7)
![TranSmart0409_2](https://github.com/eakyoungyu/TranSmart/assets/39245582/6ba11615-558a-4859-9ef9-60cd8960d54b)
