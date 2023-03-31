import React, { useState } from 'react';
import { View, Text, ToastAndroid, TouchableOpacity, TextInput } from 'react-native';
import FontAwesome from 'react-native-vector-icons/FontAwesome';
import styles from "./style";

export default function Login({navigation}) {
  const [nome, setNome] = useState('');
  const [senha, setSenha] = useState('');

  return (
    <View style={styles.container}>
        <View>
            <Text style={styles.bemVindoTexto}>Bem vindo ao RightRain!</Text>
        </View>

        <View style = {styles.containerForm}>
          <Text 
          style = {styles.containerText}>Usuário
          </Text>
            <FontAwesome 
                style={styles.icones}
                name="user-o"
                size={20}
                />
          <TextInput 
          style={styles.entradaTexto} 
          onChangeText={setNome}
          value={nome} 
          placeholder="Digite o nome de usuário"/>


          <Text style = {styles.containerText}>Senha</Text>
          <FontAwesome 
                style={styles.icones}
                name="lock"
                size={20}
                />
          <TextInput 
          style={styles.entradaTexto} 
          secureTextEntry={true}
          onChangeText={setSenha}
          value={senha} 
          placeholder="Digite sua senha"/>

          <TouchableOpacity onPress={() => verificaUsuario(nome, senha)} style={styles.botao}>
            <Text style = {styles.containerTextAc}>Acessar</Text>
          </TouchableOpacity>

          <TouchableOpacity 
          onPress={() => navigation.navigate('Cadastro')} 
          style={styles.botao}>
           <Text style = {styles.containerTextAc}>Cadastrar</Text>
          </TouchableOpacity>
        </View>
     </View>
  );

  function verificaUsuario(nome, senha){
    const customData = require('../Json/index.json');
  
    if(customData.nome == nome && customData.senha == senha){
        return navigation.navigate('RightRain');
    }else{
        return ToastAndroid.show('Usuário ou senha inválida!', ToastAndroid.SHORT);
      }
    }
}