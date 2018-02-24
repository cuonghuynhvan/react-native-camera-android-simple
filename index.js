/* eslint-disable class-methods-use-this */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
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
