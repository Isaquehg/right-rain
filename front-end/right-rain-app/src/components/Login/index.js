import React, { useState } from 'react';
import { View, Text, ToastAndroid, TouchableOpacity, TextInput } from 'react-native';
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
          style = {styles.containerText}>E-Mail
          </Text>

          <TextInput 
          style={styles.entradaTexto} 
          onChangeText={setNome}
          value={nome} 
          placeholder="Digite seu email"/>
          <Text style = {styles.containerText}>Senha</Text>
          
          <TextInput 
          style={styles.entradaTexto} 
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
    const customData = require('./ex2.json');
  
    if(customData.nome.localeCompare(nome) == 0){
        console.log(senha);
        console.log(customData.senha);
        return navigation.navigate('RightRain');
    }else{
        return ToastAndroid.show('Usuário ou senha inválida!', ToastAndroid.SHORT);
      }
    }
}