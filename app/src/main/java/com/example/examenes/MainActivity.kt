package com.example.examenes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.examenes.ui.theme.ExamenesTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
            ){paddingValues ->
                Screen(paddingValues)
            }
        }
    }
}

@Composable
fun Screen(paddingValues: PaddingValues){
    var id by remember { mutableStateOf("") }
    var materia by remember { mutableStateOf("") }
    var dia by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }

    var listarExamenes by remember { mutableStateOf(emptyList<Examenes>()) }

    LaunchedEffect(Unit) {
        obtenerData{ newList ->
            listarExamenes = newList
        }
    }

    Surface(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ){
            OutlinedTextField(
                value = id,
                onValueChange = {
                    id = it
                },
                label = {
                    Text(text = "ID")
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal)
            )

            OutlinedTextField(
                value = materia,
                onValueChange = {
                    materia = it
                },
                label = {
                    Text(text = "MATERIA")
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
            )

            OutlinedTextField(
                value = dia,
                onValueChange = {
                    dia = it
                },
                label = {
                    Text(text = "DIA")
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
            )

            OutlinedTextField(
                value = fecha,
                onValueChange = {
                    fecha = it
                },
                label = {
                    Text(text = "FECHA")
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
            )

            OutlinedTextField(
                value = hora,
                onValueChange = {
                    hora = it
                },
                label = {
                    Text(text = "HORA")
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
            )

            Button(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(4.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = {
                    agregarData(id, materia, dia, fecha, hora){
                        obtenerData{ newList ->
                            listarExamenes = newList

                            id = ""
                            materia = ""
                            dia = ""
                            fecha = ""
                            hora = ""

                        }
                    }
                }
            ){
                Text(text = "Agregar Examen")
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(listarExamenes){ item ->
                    Card(
                         modifier = Modifier
                             .fillMaxWidth()
                             .padding(12.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 8.dp
                        )
                    ){
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ){
                            Text(
                                modifier = Modifier
                                    .padding(8.dp),
                                text = item.ID
                            )
                            Text(
                                modifier = Modifier
                                    .padding(8.dp),
                                text = item.MATERIA
                            )
                            Text(
                                modifier = Modifier
                                    .padding(8.dp),
                                text = item.DIA
                            )
                            Text(
                                modifier = Modifier
                                    .padding(8.dp),
                                text = item.FECHA
                            )
                            Text(
                                modifier = Modifier
                                    .padding(8.dp),
                                text = item.HORA
                            )
                        }
                    }

                }
            }
        }
    }

}

fun agregarData(id: String, materia: String, dia: String, fecha: String, hora: String, onComplete: () ->Unit){
    CoroutineScope(Dispatchers.IO).launch {
        val examenesData = ExamenesData(
            spreadsheet_id = Constantes.google_sheet_id,
            sheet = Constantes.sheet,
            rows = listOf(
                listOf(id, materia, dia, fecha, hora)
            )
        )
        val reponse = RetrofitClient.webService(BaseUrl.base_url_post).agregarExamenes(examenesData)

        if (reponse.isSuccessful){
            withContext(Dispatchers.Main){
                onComplete()
            }
        }
    }
}

fun obtenerData(onDataReceived: (List<Examenes>) -> Unit){
    CoroutineScope(Dispatchers.IO).launch {
        val response = RetrofitClient.webService(BaseUrl.base_url_get).obtenerTodosExamenes()

        if(response.isSuccessful){
            withContext(Dispatchers.Main){
                onDataReceived(response.body()?.examenes ?: emptyList())
            }
        }
    }
}
