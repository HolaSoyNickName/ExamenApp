package com.cursokotlin.examen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cursokotlin.examen.ui.theme.ExamenTheme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            MapsInitializer.initialize(applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        enableEdgeToEdge()
        setContent {
            ExamenTheme {
                PerfilScreenEstiloModerno()
            }
        }
    }
}

// Modelo de lenguaje para recursos locales
data class LenguajeItem(
    val nombre: String,
    @DrawableRes val iconoRes: Int,
    val fondoColor: Color
)

// Ola suave para la transición inferior del banner
class CurvaOlaShape : Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height - 30f)
            cubicTo(
                size.width * 0.70f, size.height - 50f,
                size.width * 0.35f, size.height + 5f,
                0f, size.height - 20f
            )
            close()
        }
        return Outline.Generic(path)
    }
}

@Composable
fun PerfilScreenEstiloModerno() {
    var mostrarCreditos by remember { mutableStateOf(false) }

    // Control dinámico de scroll para que no pelee con el mapa
    var scrollHabilitado by remember { mutableStateOf(true) }

    // Paleta de colores consistente
    val fondoApp = Color(0xFFF8F9FE)
    val moradoPrincipal = Color(0xFF5E2BB8)
    val moradoChipFondo = Color(0xFFF3E8FF)
    val moradoChipTexto = Color(0xFF6B21A8)
    val textoOscuro = Color(0xFF111827)
    val textoGris = Color(0xFF64748B)
    val tarjetaBlanca = Color.White

    // LISTA DE 10 LENGUAJES NATIVOS DE DRAWABLE
    val lenguajes = listOf(
        LenguajeItem("Python", R.drawable.ic_python, Color(0xFFEFF6FF)),
        LenguajeItem("Java", R.drawable.ic_java, Color(0xFFF5F3FF)),
        LenguajeItem("JavaScript", R.drawable.ic_javascript, Color(0xFFFDF2F8)),
        LenguajeItem("C#", R.drawable.ic_csharp, Color(0xFFFAF5FF)),
        LenguajeItem("C++", R.drawable.ic_cpp, Color(0xFFEFF6FF)),
        LenguajeItem("PHP", R.drawable.ic_php, Color(0xFFF0FDF4)),
        LenguajeItem("Kotlin", R.drawable.ic_kotlin, Color(0xFFFEFCE8)),
        LenguajeItem("TypeScript", R.drawable.ic_typescript, Color(0xFFEFF6FF)),
        LenguajeItem("Ruby", R.drawable.ic_ruby, Color(0xFFFFF1F2)),
        LenguajeItem("Dart", R.drawable.ic_dart, Color(0xFFECFEFF))
    )

    Scaffold(
        containerColor = fondoApp,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarCreditos = true },
                containerColor = moradoPrincipal,
                contentColor = Color.White,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Créditos y opciones",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState(), enabled = scrollHabilitado)
        ) {

            // 1. HEADER: BANNER + DEGRADADO POTENCIADO + AVATAR
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp)
            ) {
                // TU BANNER LOCAL (banner.jpg)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.banner),
                        contentDescription = "Campus UPP",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CurvaOlaShape())
                    )

                    // Sombra superior para legibilidad
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.50f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    // DEGRADADO DESDE LA LÍNEA DIVISORA HACIA ARRIBA (AUMENTADO)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(146.dp)
                            .align(Alignment.BottomCenter)
                            .clip(CurvaOlaShape())
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.45f),
                                        Color.Black.copy(alpha = 0.85f)
                                    )
                                )
                            )
                    )

                    // Barra superior: Chip UPP y Botón Compartir
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // CHIP UPP (CORREGIDO: Sin altura fija para que NO se corte "de Pachuca")
                        Surface(
                            color = Color.Black.copy(alpha = 0.50f),
                            shape = RoundedCornerShape(26.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(start = 6.dp, end = 14.dp, top = 5.dp, bottom = 5.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .background(moradoPrincipal, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "UPP",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Universidad Politécnica",
                                        color = Color.White,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        lineHeight = 14.sp
                                    )
                                    Text(
                                        text = "de Pachuca",
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium,
                                        lineHeight = 13.sp
                                    )
                                }
                            }
                        }

                        Surface(
                            color = Color.Black.copy(alpha = 0.50f),
                            shape = CircleShape,
                            modifier = Modifier
                                .size(42.dp)
                                .clickable { /* Compartir */ }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Compartir",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                // Avatar y Chip UPP Software alineados en la base del header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .align(Alignment.BottomStart),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // TU FOTO DE PERFIL LOCAL (foto_perfil.jpg)
                    Box(modifier = Modifier.size(118.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.foto_perfil),
                            contentDescription = "Foto de perfil",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(114.dp)
                                .shadow(8.dp, CircleShape)
                                .clip(CircleShape)
                                .border(4.dp, Color.White, CircleShape)
                        )

                        // Botón de cámara
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(34.dp)
                                .shadow(4.dp, CircleShape)
                                .background(moradoPrincipal, CircleShape)
                                .border(2.dp, Color.White, CircleShape)
                                .clickable { /* Cambiar foto */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Cambiar foto",
                                tint = Color.White,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }

                    // Chip lateral "🎓 UPP Software"
                    Surface(
                        color = moradoChipFondo,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        Text(
                            text = "🎓  UPP Software",
                            color = moradoChipTexto,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            // Separación limpia y simétrica
            Spacer(modifier = Modifier.height(20.dp))

            // 2. INFORMACIÓN DEL PERFIL (Márgenes simétricos 24.dp)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Jose Daniel Nolasco Gómez",
                        color = textoOscuro,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.5).sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Verificado",
                        tint = moradoPrincipal,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Apasionado por el Desarrollado Mobile & Web",
                    color = moradoPrincipal,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Mi nombre es Daniel. En el ámbito profesional, mi mayor interés y motivación académica se centran en el desarrollo de aplicaciones móviles y web, como además tengo un gran pasión por los videojuegos y al mejor equipo de México las Poderosas Águilas del Ámerica.",
                    color = textoGris,
                    fontSize = 13.5.sp,
                    lineHeight = 22.sp
                )
            }

            // Separación entre bloques
            Spacer(modifier = Modifier.height(28.dp))

            // 3. LENGUAJES DE PROGRAMACIÓN
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "</>",
                            color = moradoPrincipal,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "LENGUAJES DE PROGRAMACIÓN",
                            color = textoOscuro,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Text(
                        text = "Desliza →",
                        color = Color(0xFF94A3B8),
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Carrusel con los 10 lenguajes locales
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp)
                ) {
                    items(lenguajes) { item ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = item.fondoColor,
                            modifier = Modifier.height(46.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 14.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = item.iconoRes),
                                    contentDescription = item.nombre,
                                    modifier = Modifier.size(20.dp),
                                    contentScale = ContentScale.Fit
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = item.nombre,
                                    color = textoOscuro,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Separación antes del mapa
            Spacer(modifier = Modifier.height(30.dp))

            // 4. MAPA EN RELIEVE CON ZOOM Y CONTROLES (Margen de 24.dp)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "UBICACIÓN INSTITUCIONAL",
                        color = textoOscuro,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = tarjetaBlanca,
                    shadowElevation = 6.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(255.dp)
                ) {
                    // Se conecta la interacción para evitar conflicto de gestos
                    MapaUbicacionInteractivo(
                        onInteraccion = { interactuando ->
                            scrollHabilitado = !interactuando
                        }
                    )
                }
            }

            // Espacio final holgado para el scroll
            Spacer(modifier = Modifier.height(85.dp))
        }
    }

    // DIÁLOGO DE CRÉDITOS
    if (mostrarCreditos) {
        AlertDialog(
            onDismissRequest = { mostrarCreditos = false },
            containerColor = tarjetaBlanca,
            shape = RoundedCornerShape(24.dp),
            title = {
                Text(
                    text = "Créditos del Proyecto",
                    color = textoOscuro,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
            },
            text = {
                Column {
                    Text(text = "Desarrollador:", color = textoGris, fontSize = 12.sp)
                    Text(
                        text = "Jose Daniel Nolasco Gómez",
                        color = moradoPrincipal,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Institución:", color = textoGris, fontSize = 12.sp)
                    Text(
                        text = "Universidad Politécnica de Pachuca (UPP)",
                        color = textoOscuro,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Carrera: Ingeniería en Software",
                        color = textoGris,
                        fontSize = 13.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { mostrarCreditos = false },
                    colors = ButtonDefaults.buttonColors(containerColor = moradoPrincipal),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Entendido", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
fun MapaUbicacionInteractivo(
    onInteraccion: (Boolean) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val uppUbicacion = LatLng(19.979840759029038, -98.6846539186816)
    val posicionCamara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(uppUbicacion, 15.6f)
    }

    // Cuando el mapa deja de moverse, se vuelve a permitir el scroll de la pantalla
    LaunchedEffect(posicionCamara.isMoving) {
        if (!posicionCamara.isMoving) {
            onInteraccion(false)
        }
    }

    var tipoMapaActual by remember { mutableStateOf(MapType.TERRAIN) }

    // El Box detecta el toque en el mapa para darle el 100% de prioridad a los gestos
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent(PointerEventPass.Initial)
                        if (event.changes.any { it.pressed }) {
                            onInteraccion(true)
                        } else if (!posicionCamara.isMoving) {
                            onInteraccion(false)
                        }
                    }
                }
            }
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = posicionCamara,
            properties = MapProperties(mapType = tipoMapaActual),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                zoomGesturesEnabled = true,
                scrollGesturesEnabled = true,
                scrollGesturesEnabledDuringRotateOrZoom = true,
                rotationGesturesEnabled = true,
                tiltGesturesEnabled = true,
                compassEnabled = false,
                myLocationButtonEnabled = false
            )
        ) {
            Marker(
                state = MarkerState(position = uppUbicacion),
                title = "UPP - Universidad Politécnica de Pachuca",
                snippet = "Carretera Pachuca - Cd. Sahagún km 20"
            )
        }

        // CONTROLES FLOTANTES A LA DERECHA (ZOOM + / -, CAPAS Y CENTRAR)
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BotonMapaFlotante(
                icon = Icons.Default.Add,
                contentDescription = "Acercar Zoom"
            ) {
                coroutineScope.launch {
                    posicionCamara.animate(CameraUpdateFactory.zoomIn())
                }
            }

            BotonMapaFlotante(
                icon = Icons.Default.Remove,
                contentDescription = "Alejar Zoom"
            ) {
                coroutineScope.launch {
                    posicionCamara.animate(CameraUpdateFactory.zoomOut())
                }
            }

            BotonMapaFlotante(
                icon = Icons.Default.Layers,
                contentDescription = "Alternar tipo de mapa"
            ) {
                tipoMapaActual = when (tipoMapaActual) {
                    MapType.TERRAIN -> MapType.HYBRID
                    MapType.HYBRID -> MapType.NORMAL
                    else -> MapType.TERRAIN
                }
            }

            BotonMapaFlotante(
                icon = Icons.Default.MyLocation,
                contentDescription = "Re-centrar en UPP"
            ) {
                coroutineScope.launch {
                    posicionCamara.animate(
                        CameraUpdateFactory.newLatLngZoom(uppUbicacion, 16f)
                    )
                }
            }
        }

        // CHIP FLOTANTE INFERIOR CON ESTADO DEL MAPA
        Surface(
            color = Color.White.copy(alpha = 0.94f),
            shape = RoundedCornerShape(14.dp),
            shadowElevation = 4.dp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color(0xFF10B981), CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = when (tipoMapaActual) {
                        MapType.TERRAIN -> "🏔️ Relieve 3D • Campus UPP"
                        MapType.HYBRID -> "🛰️ Vista Satélite • UPP"
                        else -> "🗺️ Mapa Plano • UPP"
                    },
                    color = Color(0xFF1E293B),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun BotonMapaFlotante(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    Surface(
        color = Color.White,
        shape = CircleShape,
        shadowElevation = 4.dp,
        modifier = Modifier
            .size(38.dp)
            .clickable { onClick() }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = Color(0xFF334155),
                modifier = Modifier.size(19.dp)
            )
        }
    }
}