package com.example.ejerciciodcmm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PantallaResultado(
    datos: DatosEmpleado,
    navController: NavController
    ) {
    // Simulamos los cálculos
    val retencionIRPF = when (datos.grupoProfesional) {
        "Directivo" -> 0.25
        "Técnico" -> 0.20
        "Administrativo" -> 0.15
        else -> 0.10
    }

    val deducciones = if (datos.estadoCivil == "Casado/a") 500.0 else 200.0
    val salarioNeto = datos.salarioBruto - (datos.salarioBruto * retencionIRPF) + deducciones

    Surface(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text("💰 Resultados del cálculo", style = MaterialTheme.typography.headlineSmall)
            Text("Salario bruto: ${datos.salarioBruto} €")
            Text("Retención IRPF: ${(retencionIRPF * 100).toInt()} %")
            Text("Deducciones: ${deducciones} €")
            Text("Salario neto aproximado: ${"%.2f".format(salarioNeto)} €")

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text("⬅️ Volver")
            }
        }
    }
}
