import React from "react";
import { View, Text, Button, Pressable } from "react-native";
import FontAwesome from 'react-native-vector-icons/FontAwesome';
import Marker from 'react-native-maps';
import styles from "./style"

export default function Main({navigation}){
  const customData = require('../Json/index.json');

    return(
        <View style= {styles.principal}>
        <Pressable onPress={()=> navigation.openDrawer()}>
          <FontAwesome
          style = {styles.icones}
          name="bars"
          size={25}
        />
        </Pressable>

        <Text style = {styles.textoTitulo}>RightRain</Text>
        <View style = {styles.space2} />
        <Text style = {styles.textoselect}>SELECIONE UMA LOCALIZAÇÃO</Text>
        <View style = {styles.space3} />
        
        <View style={styles.space2} /> 
        
        <Button 
        title="Localização 1 - MG"
        color='#af7a1f'
        onPress={() =>
        navigation.navigate('Localizacao', {numero: '1'})
      }/>

      <View style={styles.space} />   
        <Button 
        title="Localização 2 - SP"
        color='#af7a1f'
        onPress={() =>
        navigation.navigate('Localizacao', {numero: '2'})
      }/>
        
      <View style={styles.space} />   
        <Button 
        title="Localização 3 - RJ"
        color='#af7a1f'
        onPress={() =>
        navigation.navigate('Localizacao', {numero: '3'})
      }/>

      <View style={styles.space} />   
        <Button 
        title="Localização 4 - MT"
        color='#af7a1f'
        onPress={() =>
        navigation.navigate('Localizacao', {numero: '4'})
      }/>
        </View>
    );
}