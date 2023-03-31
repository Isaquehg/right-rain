import React from "react";
import { View, Text } from "react-native";
import styles from "./style";

export default function Localizacao(){
  const customData = require('../Json/index.json');
    return(
        <View style={styles.principal}>
          <Text>{customData.id} {customData.nome}</Text>
          </View>
    )
}