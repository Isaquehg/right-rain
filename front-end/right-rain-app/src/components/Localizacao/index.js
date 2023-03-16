import React from "react";
import { View, Text } from "react-native";
import styles from "./style";

export default function Localizacao(){
  const customData = require('./ex2.json');
    return(
        <View style={styles.principal}>
          <Text>{customData.id} {customData.nome}</Text>
          </View>
    )
}