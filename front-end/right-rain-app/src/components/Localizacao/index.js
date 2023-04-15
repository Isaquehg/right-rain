import React from "react";
import { View, Text } from "react-native";
import styles from "./style";
import { useRoute } from "@react-navigation/native";
import { LineChart } from 'react-native-chart-kit';

export default function Localizacao(){
  const customData = require('../Json/index.json');
  const dados = useRoute();
  const dadosJson = {
    labels: ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho'],
    datasets: [
      {
        dados: [20, 45, 28, 80, 99, 43],
        color: (opacity = 1) => `rgba(0, 0, 255, ${opacity})`, // cor da linha do gráfico
        strokeWidth: 2, // largura da linha do gráfico
      },
    ],
  };

    return(
        <View style={styles.principal}>
          <Text style={styles.textoTitulo}>Localização {dados.params.numero}</Text>
          {customData.localizacoes.map((loc) =>{
            return (
            <View>
            <Text style={styles.subtitulo}>{loc.latitude},{loc.longitude}</Text>
            </View>
            );
      })}
          <Text style = {styles.statistics}>Estatísticas</Text>
          </View>         
    )
}