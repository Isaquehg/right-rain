import { StyleSheet } from "react-native";

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor:'#4c7031'
  },
    containerLogo: {
      width: '15%',
      alignSelf: 'flex-start',
      bottom: 220
    },
    botao: {
      width: '100%',
      backgroundColor: '#4c7031',
      borderRadius: 4,
      alignSelf: 'center',
      paddingVertical: 9,
      bottom: 170,
      marginTop: 14
    },
    containerText: {
      fontSize: 24,
      fontWeight: 'bold',
      borderRadius: 50,
      bottom: 240,
      marginTop: 5
    },
    containerForm: {
      top: 100,
      backgroundColor: '#FFF',
      borderTopLeftRadius: 25,
      borderTopRightRadius: 25,
      paddingStart: '3%',
      paddingEnd: '3%',
      padding: '60%'
    },
    containerTextAc: {
      color: '#FFF',
      alignSelf: 'center',
      fontWeight: 'bold'
    },
    InputTextStyle: {
      color: '#FFF',
      alignSelf: 'center'
    },
    entradaTexto: {
      bottom: 250,
      marginBottom: 12,
      paddingLeft: 30,
      borderBottomWidth: 2
    },
    bemVindoTexto: {
      top: 60,
      left: 10,
      color: '#FFF',
      fontWeight: 'bold',
      fontSize: 33
    },
    icones: {
      bottom: 227
    }
  });

  export default styles;