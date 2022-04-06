# KuringHouse-Android

쿠링 팀원들만을 위한 Messanger(Voice + something) 앱

## Architecture
- ### Clean Architecture
  - Data : RepositoryImpl, Source, Model, Mapper 등으로 구성. SharedPreference, SendBird sdk 에 대한 의존성을 갖는 유일한 Layer
  - Domain : UseCase, Repository, Mapper 등으로 구성. Data에는 Repository 인터페이스로 접근하여 의존성을 역전
  - Presentation : UI, ViewModel 등으로 구성
- ### MVVM
- ### Hilt

