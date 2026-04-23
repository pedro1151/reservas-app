package com.optic.pramosreservasappz.presentation.screens.recibos

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.View.MeasureSpec
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddShoppingCart
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.business.completebusiness.BusinessCompleteResponse
import com.optic.pramosreservasappz.domain.model.business.recibos.ReceiptType
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.components.PrimaryTopBar
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.recibos.pdf.PDFBbitmapGenerator
import com.optic.pramosreservasappz.presentation.screens.recibos.selector.ReceiptSelectorMenu
import com.optic.pramosreservasappz.presentation.screens.recibos.tiposrecibos.*
import kotlinx.coroutines.launch

@Composable
fun ReciboScreen(
    navController: NavHostController,
    saleId: Int,
    isAuthenticated: Boolean = false
) {

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val viewModel: ReciboViewModel = hiltViewModel()

    val oneSaleState by viewModel.oneSaleState
    val businessState by viewModel.businessState
    val receiptType by viewModel.receiptType

    val receiptViewRef = remember {
        mutableStateOf<View?>(null)
    }

    LaunchedEffect(saleId) {
        viewModel.getSaleById(saleId)
        viewModel.getBusinessById(1, 1)
    }

    Scaffold(
        topBar = {
            PrimaryTopBar(
                title = "Recibo",
                navController = navController,
                selectorAction = {
                    ReceiptSelectorMenu(
                        currentType = receiptType,
                        onTypeSelected = {
                            viewModel.setReceiptType(it)
                        }
                    )
                }
            )
        },

        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },

        bottomBar = {
            ReceiptFooter(

                onWhatsappClick = {

                },

                onDownloadClick = {

                    val view = receiptViewRef.value

                    if (view == null) {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Espere un momento..."
                            )
                        }
                        return@ReceiptFooter
                    }

                    view.post {

                        try {

                            val width =
                                if (view.width > 0)
                                    view.width
                                else
                                    1080

                            val widthSpec =
                                MeasureSpec.makeMeasureSpec(
                                    width,
                                    MeasureSpec.EXACTLY
                                )

                            val heightSpec =
                                MeasureSpec.makeMeasureSpec(
                                    0,
                                    MeasureSpec.UNSPECIFIED
                                )

                            view.measure(
                                widthSpec,
                                heightSpec
                            )

                            view.layout(
                                0,
                                0,
                                view.measuredWidth,
                                view.measuredHeight
                            )

                            val bitmap =
                                Bitmap.createBitmap(
                                    view.measuredWidth,
                                    view.measuredHeight,
                                    Bitmap.Config.ARGB_8888
                                )

                            val canvas =
                                Canvas(bitmap)

                            view.draw(canvas)

                            val success =
                                PDFBbitmapGenerator.generate(
                                    context = context,
                                    bitmap = bitmap,
                                    fileName = "recibo_$saleId.pdf"
                                )

                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    if (success)
                                        "PDF descargado"
                                    else
                                        "No se pudo generar PDF"
                                )
                            }

                        } catch (e: Exception) {

                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "Error al exportar"
                                )
                            }
                        }
                    }
                },

                onNewSaleClick = {
                    navController.navigate(
                        ClientScreen.CompleteSaleStepTwo.route
                    )
                }
            )
        },

        containerColor = MaterialTheme.colorScheme.background

    ) { paddingValues ->

        when (val state = oneSaleState) {

            is Resource.Loading -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is Resource.Failure -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No se pudo cargar la venta")
                }
            }

            is Resource.Success -> {

                val businessData =
                    when (val bState = businessState) {
                        is Resource.Success -> bState.data
                        else -> null
                    }

                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),

                    factory = { ctx ->

                        ComposeView(ctx).apply {

                            receiptViewRef.value = this

                            setContent {

                                val scrollState =
                                    rememberScrollState()

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .verticalScroll(
                                            scrollState
                                        )
                                ) {

                                    AnimatedContent(
                                        targetState =
                                        receiptType,

                                        transitionSpec = {

                                            val direction =
                                                if (
                                                    targetState.ordinal >
                                                    initialState.ordinal
                                                ) 1 else -1

                                            (
                                                    slideInHorizontally(
                                                        tween(420)
                                                    ) {
                                                        it *
                                                                direction
                                                    } +
                                                            fadeIn(
                                                                tween(
                                                                    320
                                                                )
                                                            )
                                                    ).togetherWith(
                                                    slideOutHorizontally(
                                                        tween(420)
                                                    ) {
                                                        -it *
                                                                direction / 3
                                                    } +
                                                            fadeOut(
                                                                tween(
                                                                    260
                                                                )
                                                            )
                                                ).using(
                                                    SizeTransform(
                                                        false
                                                    )
                                                )
                                        },

                                        label =
                                        "receipt_transition"

                                    ) { currentType ->

                                        when (
                                            currentType
                                        ) {

                                            ReceiptType.STANDARD ->
                                                ReciboEstandar(
                                                    sale =
                                                    state.data,
                                                    business =
                                                    businessData,
                                                    paddingValues =
                                                    PaddingValues(
                                                        0.dp
                                                    )
                                                )

                                            ReceiptType.DARK ->
                                                ReciboDark(
                                                    sale =
                                                    state.data,
                                                    business =
                                                    businessData,
                                                    paddingValues =
                                                    PaddingValues(
                                                        0.dp
                                                    )
                                                )

                                            ReceiptType.COSMETIC ->
                                                ReciboCosmetic(
                                                    sale =
                                                    state.data,
                                                    business =
                                                    businessData,
                                                    paddingValues =
                                                    PaddingValues(
                                                        0.dp
                                                    )
                                                )

                                            ReceiptType.DRUG ->
                                                ReciboDrug(
                                                    sale =
                                                    state.data,
                                                    business =
                                                    businessData,
                                                    paddingValues =
                                                    PaddingValues(
                                                        0.dp
                                                    )
                                                )

                                            ReceiptType.PANADERIA ->
                                                ReciboPanaderia(
                                                    sale =
                                                    state.data,
                                                    business =
                                                    businessData,
                                                    paddingValues =
                                                    PaddingValues(
                                                        0.dp
                                                    )
                                                )

                                            ReceiptType.GAMMING ->
                                                ReciboGamming(
                                                    sale =
                                                    state.data,
                                                    business =
                                                    businessData,
                                                    paddingValues =
                                                    PaddingValues(
                                                        0.dp
                                                    )
                                                )

                                            ReceiptType.METAL ->
                                                ReciboMetal(
                                                    sale =
                                                    state.data,
                                                    business =
                                                    businessData,
                                                    paddingValues =
                                                    PaddingValues(
                                                        0.dp
                                                    )
                                                )

                                            ReceiptType.JUGUETERIA ->
                                                ReciboJugueteria(
                                                    sale =
                                                    state.data,
                                                    business =
                                                    businessData,
                                                    paddingValues =
                                                    PaddingValues(
                                                        0.dp
                                                    )
                                                )

                                            ReceiptType.ECO ->
                                                ReciboEcologico(
                                                    sale =
                                                    state.data,
                                                    business =
                                                    businessData,
                                                    paddingValues =
                                                    PaddingValues(
                                                        0.dp
                                                    )
                                                )

                                            ReceiptType.FUTURE ->
                                                ReciboFuture(
                                                    sale =
                                                    state.data,
                                                    business =
                                                    businessData,
                                                    paddingValues =
                                                    PaddingValues(
                                                        0.dp
                                                    )
                                                )

                                            ReceiptType.RESTO ->
                                                ReciboResto(
                                                    sale =
                                                    state.data,
                                                    business =
                                                    businessData,
                                                    paddingValues =
                                                    PaddingValues(
                                                        0.dp
                                                    )
                                                )

                                            ReceiptType.RESTO1 ->
                                                ReciboResto1(
                                                    sale =
                                                    state.data,
                                                    business =
                                                    businessData,
                                                    paddingValues =
                                                    PaddingValues(
                                                        0.dp
                                                    )
                                                )
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
            }

            else -> {}
        }
    }
}

/* ------------------------------------------------ */
/* FOOTER                                           */
/* ------------------------------------------------ */

@Composable
private fun ReceiptFooter(
    onWhatsappClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onNewSaleClick: () -> Unit
) {

    val primary = Color(0xFFE91E63)
    val background = Color(0xFFF8F4F6)

    Surface(
        shadowElevation = 10.dp,
        color = Color.White
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(
                    horizontal = 14.dp,
                    vertical = 12.dp
                ),
            horizontalArrangement =
            Arrangement.spacedBy(10.dp)
        ) {

            FooterButton(
                modifier = Modifier.weight(1f),
                title = "WhatsApp",
                icon = Icons.Outlined.Share,
                onClick = onWhatsappClick,
                primary = primary,
                background = background
            )

            FooterButton(
                modifier = Modifier.weight(1f),
                title = "Descargar",
                icon = Icons.Outlined.Download,
                onClick = onDownloadClick,
                primary = primary,
                background = background
            )

            FooterButton(
                modifier = Modifier.weight(1f),
                title = "Nueva Venta",
                icon = Icons.Outlined.AddShoppingCart,
                onClick = onNewSaleClick,
                primary = primary,
                background = background
            )
        }
    }
}

@Composable
private fun FooterButton(
    modifier: Modifier = Modifier,
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    primary: Color,
    background: Color
) {

    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(62.dp),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.2.dp, primary),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = background,
            contentColor = primary
        )
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        primary.copy(alpha = 0.10f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )
            }

            Spacer(Modifier.width(8.dp))

            Text(
                text = title,
                fontSize = 12.sp
            )
        }
    }
}