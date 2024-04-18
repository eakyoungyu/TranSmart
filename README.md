<p align="center">
  <img src="https://github.com/eakyoungyu/TranSmart/assets/39245582/ddbc112f-09d6-4bcd-99e6-4fe8ca50727c" alt="transmart" width="200" height="200"/>
</p>

# TranSmart

TranSmart is an **Android application** designed to assist users in calculating the cost of money transfers from Korea to Canada.
## Tech Stack
- Android Application (Kotlin, Jetpack Compose)
- MVVM architecture with repository pattern
- Hilt
- Room database
- Navigation & WebView
- WorkManager & Notification
## Libraries
- [skrape.it](https://github.com/skrapeit/skrape.it): web scraper
- [Charts](https://github.com/dautovicharis/Charts): currency rate chart
## Features
- Scrape currency rates every day by using **WorkManager**
- Send a **notification** if today is the lowest rate of the week
- Provide web pages by using **WebView**
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
### 2. Send a notification based on the result of PeriodicWorkRequest
#### Advantages of Using WorkManager
- **Background Execution**: Ideal for managing periodic tasks.
- **Reliability**: Continues task execution even if the app is closed or the device restarts.
- **Constraints**: Allows setting conditions like network availability before execution.
#### PeriodicWorkRequest vs OneTimeWorkRequest
| Feature                         | PeriodicWorkRequest                               | OneTimeWorkRequest                              |
|---------------------------------|---------------------------------------------------|-------------------------------------------------|
| **Execution Frequency**         | Repeats at intervals                              | Executes once                                   |
| **Continuity**                  | Continues until cancelled or conditions unmet     | Completes after execution                        |
| **Work Type**                   | Only supports unique work                         | Supports chaining multiple tasks together       |


``` kotlin
    private fun dailySyncWork() {
        val initialDelay = DateUtils.calculateDelayUntil(8, 0)
        Log.d(TAG, "Add scheduled work: initial delay: $initialDelay")

        val syncRequest = PeriodicWorkRequestBuilder<ExchangeRateSyncWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            WorkConstants.TAG_SYNC_EXCHANGE_RATE,
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }
```
### 3. Support multi-language by using string.xml
-  Automatically adjust the app's language to match the Android system language settings.
![image](https://github.com/eakyoungyu/TranSmart/assets/39245582/8fbd5aba-45d6-4bd9-90e8-3ec95aef7037)
``` kotlin
    BankRowView(
        name = stringResource(id = R.string.label_bank_name),
        rate = stringResource(id = R.string.label_bank_rate),
        fee = stringResource(id = R.string.label_bank_fee),
        total = stringResource(id = R.string.label_bank_total)
    )
```
### 4. Use Hilt & HiltWorker for Dependency Injection
``` kotlin
@Module
@InstallIn(SingletonComponent::class)
class WorkModule {
    @Singleton
    @Provides
    fun provideWorkHandler(@ApplicationContext context: Context): WorkHandler {
        return WorkHandler(context)
    }
}
```



## Screenshots
![TranSmart0409_1](https://github.com/eakyoungyu/TranSmart/assets/39245582/64748626-041c-4ada-a342-36d1626e11f7)
![TranSmart0409_2](https://github.com/eakyoungyu/TranSmart/assets/39245582/6ba11615-558a-4859-9ef9-60cd8960d54b)
