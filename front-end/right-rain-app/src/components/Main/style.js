import { StyleSheet } from "react-native";
import { color } from "react-native-reanimated";

const styles = StyleSheet.create({
    principal: {
      backgroundColor: '#4c7031',
      padding:10
    },
    textoTitulo:{
      color: '#f2ae1c',
      fontSize: 40,
      textAlign: 'center',
      fontWeight: 'bold',
      top:10
    },
    textoselect:{
        color: '#fbfcfb',
        fontSize: 24, 
        textAlign: 'center',
        fontWeight: 'bold',
        top: 10
    },
    space:{
      width: 20,
      height: 20,
    },
    space2:{
      padding:5
    },
    space3:{
      padding:10
    },
    map:{
      padding:150, 
    },
    icones:{
      top:27,
      color: '#FFF'
    }
  });

  export default styles