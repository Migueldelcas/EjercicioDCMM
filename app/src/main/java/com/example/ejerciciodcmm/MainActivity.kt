package com.example.ejerciciodcmm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ejerciciodcmm.ui.theme.EjercicioDCMMTheme
import android.net.Uri

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EjercicioDCMMTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavegacionApp()
                }
            }

        }
    }
}

@Composable
fun NavegacionApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "pantallaEntrada"
    ) {
        composable("pantallaEntrada") {
            PantallaEntradaDatos(
                onCalcular = { datos ->
                    navController.navigate(
                        "resultado/${datos.salarioBruto}/${datos.edad}/${Uri.encode(datos.grupoProfesional)}/${Uri.encode(datos.estadoCivil)}"
                    )
                }
            )
        }
        composable(
            "resultado/{salario}/{edad}/{grupo}/{estado}"
        ) { backStackEntry ->
            val salario = backStackEntry.arguments?.getString("salario")?.toDouble() ?: 0.0
            val edad = backStackEntry.arguments?.getString("edad")?.toInt() ?: 0
            val grupo = backStackEntry.arguments?.getString("grupo") ?: ""
            val estado = backStackEntry.arguments?.getString("estado") ?: ""

            PantallaResultado(
                datos = DatosEmpleado(
                    salarioBruto = salario,
                    edad = edad,
                    grupoProfesional = grupo,
                    estadoCivil = estado
                ),
                navController = navController
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEntradaDatos(onCalcular: (DatosEmpleado) -> Unit) {
    var salarioBruto by remember { mutableStateOf(TextFieldValue("")) }
    var edad by remember { mutableStateOf(TextFieldValue("")) }
    var grupoProfesional by remember { mutableStateOf("") }
    var expandido by remember { mutableStateOf(false) }
    var opcionesGrupo = listOf("Administrativo", "Técnico", "Directivo", "Operario")
    var estadoCivil by remember { mutableStateOf("") }
    var expandidoEstadoCivil by remember { mutableStateOf(false) }
    val opcionesEstadoCivil = listOf("Soltero/a", "Casado/a", "Divorciado/a", "Viudo/a")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = "Calculadora de salario neto",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        // Campo de texto para salario bruto
        OutlinedTextField(
            value = salarioBruto,
            onValueChange = { salarioBruto = it },
            label = { Text("Salario bruto anual (€)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        // Campo de texto para dedad
        OutlinedTextField(
            value = edad,
            onValueChange = { edad = it },
            label = { Text("edad") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )


        //Campo desplegable


        ExposedDropdownMenuBox(
            expanded = expandido,
            onExpandedChange = { expandido = !expandido }
        ) {
            OutlinedTextField(
                value = grupoProfesional,
                onValueChange = {},
                readOnly = true,
                label = { Text("Grupo profesional") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido)
                }
            )
            ExposedDropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {
                opcionesGrupo.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            grupoProfesional = opcion
                            expandido = false
                        }
                    )
                }
            }
        }

        ExposedDropdownMenuBox(
            expanded = expandidoEstadoCivil,
            onExpandedChange = { expandidoEstadoCivil = !expandidoEstadoCivil }
        ) {
            OutlinedTextField(
                value = estadoCivil,
                onValueChange = {},
                readOnly = true,
                label = { Text("Estado civil") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoEstadoCivil)
                }
            )
            ExposedDropdownMenu(
                expanded = expandidoEstadoCivil,
                onDismissRequest = { expandidoEstadoCivil = false }
            ) {
                opcionesEstadoCivil.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            estadoCivil = opcion
                            expandidoEstadoCivil = false
                        }
                    )
                }
            }

        }
        Button(
            onClick = {
                val datos = DatosEmpleado(
                    salarioBruto = salarioBruto.text.toDoubleOrNull() ?: 0.0,
                    edad = edad.text.toIntOrNull() ?: 0,
                    grupoProfesional = grupoProfesional,
                    estadoCivil = estadoCivil
                )
                onCalcular(datos)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Calcular salario neto")
        }
    }

}

