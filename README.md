# KuringHouse-Android

#### 쿠링 팀원들만을 위한 Messanger(Voice + something) 앱

## PlayStore 링크 
https://play.google.com/store/apps/details?id=com.yeonkyu.kuringhouse

## Tech Stack & Libraries

### Architecture
- #### Clean Architecture
  - Data Layer : 
    - `RepositoryImpl`, `Source`, `Model`, `Mapper` 등으로 구성
    - SharedPreference, SendBird sdk 에 대한 의존성을 갖는 유일한 Layer
  - Domain Layer : 
    - `UseCase`, `Repository`, `Model` 등으로 구성
    - Data Layer에 Repository 인터페이스로 접근하여 의존성을 역전
  - Presentation Layer : 
    - `UI`, `ViewModel` 등으로 구성
- MVVM
- Hilt

### AAC Library
- Lifecycle(LiveData)
- DataBinding
- ViewModel

### Async
- Coroutines + Flow

### Network
- SendBird Call
- OkHttp3 & Retrofit2

### other Library
- Glide
- Timber
- LeakCanary
