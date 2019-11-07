### DottSample

> Problem Statement: Fetch Venues using FourSquare APi and display on the google map

> Tech Stack Used : Architecture Components, Kotlin, Dagger2, Retrofit, Junit, Mockito
> Architecture followed: MVVM

Points to Note:

- There are a few TODO in the app, kindly check
- Junit tests are written to showcase, not everything is tested
- LocalDataSource is not capped, unlimited items can be cached
- One Activity with 3 fragments are used (check ui package)
- I used maps long back and revised for this sample (ignore silly mistakes in map related code)
- UI layer is not doing business logic, ViewModel is testable and it is performing business logic
- Fragments are interacting with each other via Activity ViewModel
