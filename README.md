# RxBus
Simple **Relay RxBus** with similar api to [Event Bus](https://github.com/greenrobot/EventBus) without threads. <br>
The Bus makes use of PublishRelay provided by [RxRelay](https://github.com/JakeWharton/RxRelay).

#### Thanks to
[Event Bus](https://github.com/greenrobot/EventBus)<br>
[RxRelay](https://github.com/JakeWharton/RxRelay)



# Download
Add gradle dependency:
```gradle
implementation 'com.adiliqbal:rxbus:0.2'
```
or maven:
```maven
<dependency>
  <groupId>com.adiliqbal</groupId>
  <artifactId>rxbus</artifactId>
  <version>0.2</version>
  <type>pom</type>
</dependency>  
```

# Usage

Register to all events in class

1. Event class
```kotlin
class ConsumerEvent
```
2. Observe to event types
```kotlin
RxBus.observe(ConsumerEvent.class)
     .subscribe(event -> {})
```
Event will be received on both sticky and non sticky events. 

3. Post events
```kotlin
RxBus.post(new MyEvent())
RxBus.postSticky(new MyEvent())
```
