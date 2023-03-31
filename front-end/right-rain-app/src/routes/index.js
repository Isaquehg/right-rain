import {createNativeStackNavigator} from '@react-navigation/native-stack';
import { NavigationContainer, DefaultTheme } from '@react-navigation/native';
import { StatusBar } from 'expo-status-bar';
import Localizacao from '../components/Localizacao';
import Main from '../components/Main'
import Cadastro from '../components/Cadastro';
import Login from '../components/Login';

const Stack = createNativeStackNavigator();

export default function Routes() {
  const Tema = {
    ...DefaultTheme,
    colors:{
      background: '#4c7031',
      headerTintColor:'#4c7031'
  }
  };

  return (
    <NavigationContainer
    theme={Tema}>
      <StatusBar 
      backgroundColor="#4c7031"
      barStyle="light-content"
      translucent={true} />
      <Stack.Navigator>
      <Stack.Screen
          name='Login'
          component={Login}
          options={{headerStyle:{
              backgroundColor:'#4c7031',},
               headerTintColor:'#fff',
               headerShown: false
              }}
          />
        <Stack.Screen
          name='RightRain'
          component={Main}
          options={{headerStyle:{
              backgroundColor:'#4c7031',},
               headerTintColor:'#fff',
               headerShown: false
              }}
          />
        <Stack.Screen 
        name='Localizacao' 
        component={Localizacao}
        options={{
          headerStyle:{
            backgroundColor:'#4c7031',
          },
             headerTintColor:'#fff',
             headerShown: false
            }
          }
         />
      <Stack.Screen 
        name='Cadastro' 
        component={Cadastro}
        options={{
          headerStyle:{
            backgroundColor:'#4c7031',
          },
             headerTintColor:'#fff'
            }
          }
         />
      </Stack.Navigator>
    </NavigationContainer>
  );
}