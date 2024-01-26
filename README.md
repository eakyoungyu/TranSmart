# TranSmart
TranSmart is an Android application designed to assist users in calculating the cost of money transfers from Korea to Canada.
## Tech Stack
- Android Application (Kotlin, Jetpack Compose)
- [skrape.it](https://github.com/skrapeit/skrape.it) for web scraper
- MVVM architecture with repository pattern
- Room database for data storage
- Utilizes LazyColumn, Card, and SwipeToDismiss views
## Features
- Scrapes currency rates from the web
- Allows users to add custom banks
- Enables users to calculate the cost of money transfers

## Improved Features
### 1. Pre-populated Data in Database
- Implemented pre-populated data by importing from "default-bank.db" and scraping currency rates from the web.
``` kotlin
        database = Room.databaseBuilder(context, BankDatabase::class.java, "bank.db")
            .createFromAsset("default-bank.db")
            .build()
```


## Screenshots
![TranSmart_Screenshot1_2](https://github.com/eakyoungyu/TranSmart/assets/39245582/8748380b-3152-419c-81cf-7defcc544034)
![TranSmart_Screenshot 2_3jpg](https://github.com/eakyoungyu/TranSmart/assets/39245582/e730c15a-6335-4757-9bcc-2648ac139d56)
