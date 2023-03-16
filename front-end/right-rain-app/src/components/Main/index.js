import React from "react";
import { View, Text, Button } from "react-native";
import styles from "./style"

export default function Main({navigation}){
    return(
        <View style= {styles.principal}>
        <Text style = {styles.textoselect}>SELECT A LOCATION</Text>
        <View style={styles.space2} /> 
        <Button 
        title="Location 1 - MG"
        onPress={() =>
        navigation.navigate('Localizacao', {title: 'MG'})
      }/>
      <View style={styles.space} /> 
        <Button 
        title="Location 2 - SP"></Button>
        <View style={styles.space} /> 
        <Button 
        title="Location 3 - RJ"/>
        <View style={styles.space} /> 
        <Button 
        title="Location 4 - MT"></Button> 
        </View>
    );
}