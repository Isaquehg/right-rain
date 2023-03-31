import React from 'react';
import { View, Text } from 'react-native';
import styles from "./style";

export default function Cadastro() {
    return (
        <View style={styles.container}>
            <Text style={styles.textoIn}>Faça seu cadastro!</Text>
            <View style={styles.containerForm}></View>
          </View>
    )
}