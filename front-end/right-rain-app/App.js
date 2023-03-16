import { NavigationContainer } from '@react-navigation/native';
import { StatusBar } from 'expo-status-bar';
import Routes from './src/routes'
import React from 'react';

export default function App(){
  return(
    <NavigationContainer>
      <StatusBar barStyle="light-content"/>
    <Routes/>
    </NavigationContainer>
  );
  }