import React from "react";
import { View, Text, Button } from "react-native";
import FontAwesome from 'react-native-vector-icons/FontAwesome';
import MapView, { Marker } from 'react-native-maps';
import styles from "./style"

export default function Main({navigation}){
  const customData = require('../Json/index.json');

    return(
        <View style= {styles.principal}>
        <FontAwesome
        style = {styles.icones}
        name="bars"
        size={25}
        />
        <Text style = {styles.textoTitulo}>RightRain</Text>
        <View style = {styles.space2} />
        <Text style = {styles.textoselect}>SELECIONE UMA LOCALIZAÇÃO</Text>
        <View style = {styles.space3} />
        
        <MapView style={styles.map}> 
          <Marker
            coordinate={{
              latitude: 0,
              longitude: 0,
            }}
            />
            </MapView>
        <View style={styles.space2} /> 
        
        <Button 
        title="Localização 1 - MG"
        color='#af7a1f'
        onPress={() =>
        navigation.navigate('Localizacao', {title: 'MG'})
      }/>
      <View style={styles.space} /> 
        
        <Button 
        color='#af7a1f'
        title="Localização 2 - SP"></Button>
        
        <View style={styles.space} /> 
        <Button 
        color='#af7a1f'
        title="Localização 3 - RJ"/>

        <View style={styles.space} /> 
        <Button 
        color='#af7a1f'
        title="Localização 4 - MT"></Button> 
        </View>
    );
}