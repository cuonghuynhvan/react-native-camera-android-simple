/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  View,
  Button
} from 'react-native';
import Camera from 'react-native-camera-android-simple';

const Type = {
  FRONT: 'front',
  BACK: 'back'
};

export default class Example extends Component {

  constructor(props) {
    super(props);
    this._onCapturePress = this._onCapturePress.bind(this);
    this._onReversePress = this._onReversePress.bind(this);

    this.state = {
      type: Type.BACK
    };
  }

  _onReversePress() {
    const {type} = this.state;
    this.setState({
      type: type===Type.BACK ? Type.FRONT : Type.BACK
    });
  }

  _onCapturePress() {
    const options = {};
    this.camera.capture({metadata: options})
      .then((data) => {
        console.log(data);
      })
      .catch(err => onError(err));
  }

  _renderCameraButtons() {
    return (
      <View
        style={styles.captureContainer}
        accessibilityLabel="Camera_Container"
      >
        <View style={styles.captureChildContainer} />
        <View style={styles.circleBorder}>
          <Button
            onPress={this._onCapturePress}
            style={styles.captureButton}
            title="[capture]"
            color="#841584"
            accessibilityLabel="Learn more about this purple button"
          />
        </View>
        <View style={styles.captureChildContainer}>
          <Button
            style={styles.reverseCameraButton}
            onPress={this._onReversePress}
            title="[Change]"
            color="#999999"
            accessibilityLabel="Learn more about this purple button"
          />
        </View>
      </View>
    );
  }

  render() {
    const {type} = this.state;
    return (
      <View
        style={styles.preview}
      >
        <Camera
          ref={(instance) => {
            this.camera = instance;
          }}
          style={styles.preview}
          type={type}
        />

        {this._renderCameraButtons()}

      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1
  },
  preview: {
    flex: 1
  },
  captureContainer: {
    position: 'absolute',
    flexDirection: 'row',
    left: 0,
    right: 0,
    bottom: 40,
    alignItems: 'center',
    zIndex: 1
  },
  captureChildContainer: {
    flex: 1,
    alignItems: 'flex-start'
  },
  captureButton: {
    borderRadius: 64,
    borderWidth: 0,
    width: 53,
    height: 53,
    backgroundColor: '#ffffff',
    paddingLeft: 0,
    paddingRight: 0
  },
  reverseCameraButton: {
    borderWidth: 0,
    marginLeft: 34,
    width: 53,
    height: 53,
    paddingLeft: 0,
    paddingRight: 0
  },
  circleBorder: {
    width: 59,
    height: 59,
    justifyContent: 'center',
    alignItems: 'center',
    borderWidth: 1,
    borderRadius: 64,
    borderColor: '#ffffff',
    backgroundColor: 'transparent'
  },
});

AppRegistry.registerComponent('Example', () => Example);
