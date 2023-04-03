import {createNativeStackNavigator} from '@react-navigation/native-stack';
import { createDrawerNavigator } from '@react-navigation/drawer';
import { NavigationContainer, DefaultTheme } from '@react-navigation/native';
import { StatusBar } from 'expo-status-bar';
import Localizacao from '../components/Localizacao';
import Main2 from '../components/Main'
import Configs from '../components/Settings';
import Cadastro from '../components/Cadastro';
import Login from '../components/Login';

const Stack = createNativeStackNavigator();
const Drawer = createDrawerNavigator();

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
      style="light"
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
          component={DrawerRoutes}
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
             headerTintColor:'#fff',
             headerShown: false
            }
          }
         />
      </Stack.Navigator>
    </NavigationContainer>
  );

  function DrawerRoutes() {
    return (
        <Drawer.Navigator 
        screenOptions={{
          drawerStyle: {
            backgroundColor: '#c6cbef',
          },
        }}
        initialRouteName="Tela inicial">
          <Drawer.Screen 
          name = "Tela inicial" 
          component={Main2}
          options={{headerShown:false}} />
          <Drawer.Screen name="Configurações" component={Configs} />
        </Drawer.Navigator>
    )
  }
}