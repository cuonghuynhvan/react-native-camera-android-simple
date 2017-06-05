import React, { PropTypes, Component } from 'react';
import { NativeModules, requireNativeComponent, View } from 'react-native';

const CameraModule = NativeModules.AndroidCameraModule;

export default class Camera extends Component {

  capture(metaoption) {
    return CameraModule.capturePicture();
  }

  render() {
    return (<AndroidCameraView {...this.props} />);
  }
}

Camera.propTypes = {
  ...View.propTypes,
  type: PropTypes.string
};

const AndroidCameraView = requireNativeComponent('AndroidCameraView', Camera);
