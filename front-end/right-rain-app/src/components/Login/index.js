import React from 'react';
import { View, Text, Image } from 'react-native';
import styles from "./style";

export default function Login() {
  return (
    <View style={styles}>
        <View>
            <Image
                source={require('../../assets/userlogo.png')}
                style={{width: '100%'}}
                resizeMode="contain"
        />
        </View>
     </View>
  );
}
