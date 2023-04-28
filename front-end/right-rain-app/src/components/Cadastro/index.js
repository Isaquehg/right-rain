import React, { useState } from 'react';
import { View, Text, ToastAndroid, TouchableOpacity, TextInput, KeyboardAvoidingView } from 'react-native';
import { useHeaderHeight } from '@react-navigation/elements';
import FontAwesome from 'react-native-vector-icons/FontAwesome';
import styles from "./style";

export default function Cadastro() {

    const [nome, setNome] = useState('');
    const [senha, setSenha] = useState('');
    const [senha2, setSenha2] = useState('');
    const [phone, setPhone] = useState('');
    const height = useHeaderHeight();

    return (
        <KeyboardAvoidingView 
        style={styles.teclado} 
        behavior="position"
        keyboardVerticalOffset={height-160}>
        <View style={styles.container}>
            <Text style={styles.bemVindoTexto}>Faça seu cadastro!</Text>
            <View style={styles.containerForm}></View>
            <Text style = {styles.containerText}>Usuário
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
          placeholder="Novo nome de usuário"/>

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
          placeholder="Nova senha"/>
            <FontAwesome 
                style={styles.icones}
                name="lock"
                size={20}
                />
          <TextInput 
          style={styles.entradaTexto} 
          secureTextEntry={true}
          onChangeText={setSenha2}
          value={senha2} 
          placeholder="Confirme a senha"/>

          <Text style = {styles.containerText}>Telefone</Text>
          <FontAwesome 
                style={styles.icones}
                name="phone"
                size={20}
                />
          <TextInput 
          keyboardType='number-pad'
          maxLength={11}
          style={styles.entradaTexto} 
          onChangeText={setPhone}
          value={phone} 
          placeholder="Telefone"/>

          <TouchableOpacity onPress={() => verificaDados(nome, senha, senha2, phone)} style={styles.botao}>
           <Text style = {styles.containerTextAc}>Finalizar</Text>
          </TouchableOpacity>
        </View>
        </KeyboardAvoidingView>
    )

    function verificaDados(nome, senha, senha2, phone){
        const customData = require('../Json/index.json');
        if(nome == '' || senha != senha2 || senha == '' || senha2 == '' || phone.length != 11){
            return ToastAndroid.show('Um ou mais dados inválidos!', ToastAndroid.SHORT);
        }else{
            return ToastAndroid.show('Usuário criado!', ToastAndroid.SHORT);
        }
    }
}