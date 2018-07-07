# RxBus
Simple **Relay RxBus** with similar api to [Event Bus](https://github.com/greenrobot/EventBus) without threads. <br>
The Bus makes use of PublishRelay provided by [RxRelay](https://github.com/JakeWharton/RxRelay).

#### Thanks to
[Event Bus](https://github.com/greenrobot/EventBus)<br>
[RxRelay](https://github.com/JakeWharton/RxRelay)



# Download
Add gradle dependency:
```gradle
implementation 'com.adiliqbal:rxbus:0.1'
```
or maven:
```maven
<dependency>
  <groupId>com.adiliqbal</groupId>
  <artifactId>rxbus</artifactId>
  <version>0.1</version>
  <type>pom</type>
</dependency>  
```

# Usage
Observe to event types
```kotlin
RxBus.observe(ConsumerEvent.class)
     .subscribe(event -> {})
```
Event will be received on both sticky and non sticky events. 

**or** <br>
Register to all events in class

1. Event class
```kotlin
class ConsumerEvent
```

2. Add *@Consumer* annotation to public methods
```kotlin
@Consumer
fun consumeEvent(event : ConsumerEvent) {}
```

3. Consuming events via observerables. Once any class registers, all of its methods with @Consume are used to invoke event when onNext() of Relay is called.<br>
to register
```kotlin
RxBus.register(this)
```
and to unregister
```kotlin
RxBus.unregister(this)
```

4. Post events
```kotlin
RxBus.post(new MyEvent())
```

# License
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
