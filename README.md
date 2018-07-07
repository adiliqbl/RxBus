# RxBus
Simple **Relay RxBus** with similar api to [Event Bus](https://github.com/greenrobot/EventBus) without threads. <br>
The Bus makes use of PublishRelay provided by [RxRelay](https://github.com/JakeWharton/RxRelay).

#### Thanks to
[Event Bus](https://github.com/greenrobot/EventBus)<br>
[RxRelay](https://github.com/JakeWharton/RxRelay)


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
