
# Simple React Native Camera for Android - KISS

This library is a very simple Android camera component for React Native.
It was made to show a portrait camera view. Take a picture as fast as possible.

## Getting started

1. `npm install react-native-camera-android-simple`
2. `react-native link`

## Usage

All you need is to `import` the `react-native-camera-android-simple` module and then use the
`<Camera/>` tag.

```javascript
// 2 types: 'front' and 'back'
<Camera
  ref={(instance) => {
    this.camera = instance;
  }}
  style={styles.preview}
  type={type}
/>

// to capture a picture
//
this.camera.capture({metadata: options})
      .then((data) => console.log(data))
      .catch(err => console.error(err));
````

#### `type`
[`front`, `back`]

#### `data`
 ```json
    {
        path: "linkt to image file"
    }
```
