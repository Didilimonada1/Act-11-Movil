package com.example.practica11

import android.media.MediaPlayer
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReproductorScreen() {
    val context = LocalContext.current

    var audioUri by remember { mutableStateOf<Uri?>(null) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    // Paleta limpia en modo oscuro de alto contraste
    val fondoPrincipal = Color(0xFF0F1117)
    val superficieTarjeta = Color(0xFF1A1D26)
    val barraSuperiorColor = Color(0xFF151821)
    val textoPrincipal = Color(0xFFF3F4F6)
    val textoSecundario = Color(0xFF9CA3AF)
    val acentoPrimario = Color(0xFF3B82F6)
    val acentoPausa = Color(0xFF262B38)
    val acentoDetener = Color(0xFFEF4444)

    val selectorAudio = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            audioUri = uri
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, uri)
            mediaPlayer?.start()
            isPlaying = true

            mediaPlayer?.setOnCompletionListener {
                isPlaying = false
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
        }
    }

    Scaffold(
        containerColor = fondoPrincipal,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mini Reproductor MP3",
                        color = textoPrincipal,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = barraSuperiorColor
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(fondoPrincipal)
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = superficieTarjeta,
                tonalElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.AudioFile,
                        contentDescription = "Archivo de audio",
                        tint = acentoPrimario,
                        modifier = Modifier.size(44.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (audioUri == null) "Ningún archivo seleccionado" else "Audio cargado correctamente",
                        color = textoPrincipal,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )

                    if (audioUri != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = audioUri.toString(),
                            color = textoSecundario,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { selectorAudio.launch("audio/*") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = acentoPrimario,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Buscar MP3 en SD/Memoria",
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FloatingActionButton(
                    onClick = {
                        if (mediaPlayer != null && !isPlaying) {
                            mediaPlayer?.start()
                            isPlaying = true
                        }
                    },
                    containerColor = acentoPrimario,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Reproducir")
                }

                FloatingActionButton(
                    onClick = {
                        if (mediaPlayer != null && isPlaying) {
                            mediaPlayer?.pause()
                            isPlaying = false
                        }
                    },
                    containerColor = acentoPausa,
                    contentColor = textoPrincipal
                ) {
                    Icon(Icons.Default.Pause, contentDescription = "Pausar")
                }

                FloatingActionButton(
                    onClick = {
                        if (mediaPlayer != null) {
                            mediaPlayer?.pause()
                            mediaPlayer?.seekTo(0)
                            isPlaying = false
                        }
                    },
                    containerColor = acentoDetener,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Stop, contentDescription = "Detener")
                }
            }
        }
    }
}
