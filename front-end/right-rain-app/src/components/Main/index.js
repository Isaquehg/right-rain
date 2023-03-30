import React from "react";
import { View, Text, Button, Image } from "react-native";
import styles from "./style"

export default function Main({navigation}){
    return(
        <View style= {styles.principal}>
        <Text style = {styles.textoselect}>SELECIONE UMA LOCALIZAÇÃO</Text>
        <View style={styles.space2} /> 
        
        <Button 
        title="Location 1 - MG"
        color='#af7a1f'
        onPress={() =>
        navigation.navigate('Localizacao', {title: 'MG'})
      }/>
      <View style={styles.space} /> 
        
        <Button 
        color='#af7a1f'
        title="Location 2 - SP"></Button>
        
        <View style={styles.space} /> 
        <Button 
        color='#af7a1f'
        title="Location 3 - RJ"/>

        <View style={styles.space} /> 
        <Button 
        color='#af7a1f'
        title="Location 4 - MT"></Button> 
        </View>
    );
}