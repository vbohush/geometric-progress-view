# GeometricProgressView

![](screenshot/screenshot.gif)

## Usage

```xml
<net.bohush.geometricprogressview.GeometricProgressView
    android:id="@+id/progressView"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```

## Customize
```java
GeometricProgressView progressView = (GeometricProgressView) findViewById(R.id.progressView);
progressView.setType(GeometricProgressView.TYPE.KITE);
progressView.setNumberOfAngles(6);
progressView.setColor(Color.parseColor("#00897b"));
progressView.setDuration(1000);
progressView.setFigurePadding(getResources().getDimensionPixelOffset(R.dimen.figure_padding));
```
## Download
Gradle

```javascript
dependencies {
  compile 'net.bohush.geometricprogressview:geometricprogressview:1.0.0'
}
```
or just copy [GeometricProgressView.java](https://github.com/vbohush/geometric-progress-view/blob/master/geometricprogressview/src/main/java/net/bohush/geometricprogressview/GeometricProgressView.java) to your project

## License

    Copyright 2017 Viktor Bohush

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
