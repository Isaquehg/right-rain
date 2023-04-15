import { StyleSheet } from "react-native";

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor:'#4c7031'
  },
    containerLogo: {
      width: '15%',
      alignSelf: 'flex-start',
      bottom: 200
    },
    botao: {
      width: '95%',
      backgroundColor: '#4c7031',
      borderRadius: 4,
      alignSelf: 'center',
      paddingVertical: 9,
      bottom: 430,
      marginTop: 20
    },
    containerText: {
      fontSize: 24,
      fontWeight: 'bold',
      bottom: 450,
      marginLeft: 10
    },
    containerForm: {
      top: 110,
      backgroundColor: '#FFF',
      borderTopLeftRadius: 25,
      borderTopRightRadius: 25,
      paddingStart: '10%',
      paddingEnd: '3%',
      padding: '70%'
    },
    containerTextAc: {
      color: '#FFF',
      alignSelf: 'center',
      fontWeight: 'bold'
    },
    InputTextStyle: {
      color: '#FFF',
      alignSelf: 'center',
    },
    entradaTexto: {
      bottom: 460,
      paddingLeft: 30,
      height: 40,
      borderBottomWidth: 2,
      marginLeft: 10,
      marginRight: 10
    },
    bemVindoTexto: {
      top: 70,
      left: 10,
      color: '#FFF',
      fontWeight: 'bold',
      fontSize: 33
    },
    icones: {
      bottom: 430,
      marginLeft: 10
    }
  });

  export default styles;