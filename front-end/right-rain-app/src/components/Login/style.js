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
      bottom: 100,
      borderRadius: 50,
      bottom: 240,
      marginTop: 10
    },
    containerForm: {
      top: 50,
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
      bottom: 230,
      marginBottom: 12,
      height: 40,
      borderBottomWidth: 2
    },
    bemVindoTexto: {
      top: 30,
      left: 10,
      color: '#FFF',
      fontWeight: 'bold',
      fontSize: 30
    }
  });

  export default styles;