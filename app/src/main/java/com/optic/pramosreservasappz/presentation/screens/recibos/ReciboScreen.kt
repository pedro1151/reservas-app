package com.optic.pramosreservasappz.presentation.screens.recibos

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.business.recibos.ReceiptType
import com.optic.pramosreservasappz.domain.model.sales.types.SaleType
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.components.PrimaryTopBar
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.inicio.SalesViewModel
import com.optic.pramosreservasappz.presentation.screens.newsale.NewSaleViewModel
import com.optic.pramosreservasappz.presentation.screens.recibos.footer.LoadingBox
import com.optic.pramosreservasappz.presentation.screens.recibos.footer.ReceiptFooter
import com.optic.pramosreservasappz.presentation.screens.recibos.footer.awaitPreDrawReady
import com.optic.pramosreservasappz.presentation.screens.recibos.pdf.PDFBbitmapGenerator
import com.optic.pramosreservasappz.presentation.screens.recibos.selector.ReceiptSelectorMenu
import com.optic.pramosreservasappz.presentation.screens.recibos.tiposrecibos.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ReciboScreen(
    navController: NavHostController,
    saleId: Int,
    isAuthenticated: Boolean = false,
    salesViewModel: NewSaleViewModel
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

    fun isReceiptReady(): Boolean {
        return oneSaleState is Resource.Success &&
                businessState is Resource.Success
    }

    suspend fun captureReceiptBitmap(
        view: View
    ): Bitmap? {

        view.awaitPreDrawReady()

        val width =
            if (view.width > 0) {
                view.width
            } else {
                1080
            }

        val widthSpec =
            MeasureSpec.makeMeasureSpec(
                width,
                MeasureSpec.EXACTLY
            )

        val maxReceiptHeight = 12000

        val heightSpec =
            MeasureSpec.makeMeasureSpec(
                maxReceiptHeight,
                MeasureSpec.AT_MOST
            )

        view.measure(
            widthSpec,
            heightSpec
        )

        if (
            view.measuredWidth <= 0 ||
            view.measuredHeight <= 0
        ) {
            return null
        }

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

        val canvas = Canvas(bitmap)
        canvas.drawColor(android.graphics.Color.WHITE)
        view.draw(canvas)

        return bitmap
    }

    val newSaleRoute =
        when (salesViewModel.saleFlowType) {
            SaleType.RAPID -> ClientScreen.CompleteSaleStepTwo.route
            SaleType.COMPLETE -> ClientScreen.CompleteSaleStepOne.route
            null -> ClientScreen.CompleteSaleStepTwo.route
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

                    if (!isReceiptReady()) {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Espere a que el recibo termine de cargar"
                            )
                        }
                        return@ReceiptFooter
                    }

                    val view = receiptViewRef.value

                    if (view == null) {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Espere un momento..."
                            )
                        }
                        return@ReceiptFooter
                    }

                    scope.launch {
                        try {
                            val bitmap =
                                captureReceiptBitmap(view)

                            if (bitmap == null) {
                                snackbarHostState.showSnackbar(
                                    "El recibo aún no está listo"
                                )
                                return@launch
                            }

                            val uri =
                                withContext(Dispatchers.IO) {
                                    PDFBbitmapGenerator.generateForShare(
                                        context = context,
                                        bitmap = bitmap,
                                        fileName = "recibo_$saleId.pdf"
                                    )
                                }

                            bitmap.recycle()

                            if (uri == null) {
                                snackbarHostState.showSnackbar(
                                    "No se pudo preparar el PDF"
                                )
                                return@launch
                            }

                            val whatsappIntent =
                                Intent(Intent.ACTION_SEND).apply {
                                    type = "application/pdf"
                                    putExtra(Intent.EXTRA_STREAM, uri)
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Te comparto el recibo de la venta."
                                    )
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    setPackage("com.whatsapp")
                                }

                            val canOpenWhatsapp =
                                whatsappIntent.resolveActivity(
                                    context.packageManager
                                ) != null

                            if (canOpenWhatsapp) {
                                context.startActivity(whatsappIntent)
                            } else {
                                val shareIntent =
                                    Intent(Intent.ACTION_SEND).apply {
                                        type = "application/pdf"
                                        putExtra(Intent.EXTRA_STREAM, uri)
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            "Te comparto el recibo de la venta."
                                        )
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    }

                                context.startActivity(
                                    Intent.createChooser(
                                        shareIntent,
                                        "Compartir recibo"
                                    )
                                )
                            }

                        } catch (e: Exception) {
                            Log.e(
                                "PDF_SHARE_ERROR",
                                e.message ?: "Error al compartir",
                                e
                            )

                            snackbarHostState.showSnackbar(
                                e.message ?: "Error al compartir"
                            )
                        }
                    }
                },
                onDownloadClick = {

                    if (!isReceiptReady()) {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Espere a que el recibo termine de cargar"
                            )
                        }
                        return@ReceiptFooter
                    }

                    val view = receiptViewRef.value

                    if (view == null) {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Espere un momento..."
                            )
                        }
                        return@ReceiptFooter
                    }

                    scope.launch {
                        try {
                            val bitmap =
                                captureReceiptBitmap(view)

                            if (bitmap == null) {
                                snackbarHostState.showSnackbar(
                                    "El recibo aún no está listo"
                                )
                                return@launch
                            }

                            val success =
                                withContext(Dispatchers.IO) {
                                    PDFBbitmapGenerator.generate(
                                        context = context,
                                        bitmap = bitmap,
                                        fileName = "recibo_$saleId.pdf"
                                    )
                                }

                            bitmap.recycle()

                            snackbarHostState.showSnackbar(
                                if (success) {
                                    "PDF descargado"
                                } else {
                                    "No se pudo generar PDF"
                                }
                            )

                        } catch (e: Exception) {
                            Log.e(
                                "PDF_ERROR",
                                e.message ?: "Error al exportar",
                                e
                            )

                            snackbarHostState.showSnackbar(
                                e.message ?: "Error al exportar"
                            )
                        }
                    }
                },
                onNewSaleClick = {
                    salesViewModel.resetCreateSaleWithItemsState()
                    salesViewModel.clearSelectedProducts()
                    salesViewModel.resetStepOneState()
                    if (salesViewModel.isRapidSale()){
                        salesViewModel.updateSaleFlowType(SaleType.RAPID)
                    }
                    else{
                        salesViewModel.updateSaleFlowType(SaleType.COMPLETE)
                    }
                    navController.navigate(
                        newSaleRoute  // volver a nueva venta segun el tipo de venta
                    )
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        when (val saleState = oneSaleState) {

            is Resource.Loading -> {
                LoadingBox(paddingValues)
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

                when (val bState = businessState) {

                    is Resource.Loading -> {
                        LoadingBox(paddingValues)
                    }

                    is Resource.Failure -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No se pudo cargar el negocio")
                        }
                    }

                    is Resource.Success -> {

                        val businessData = bState.data

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
                                                .verticalScroll(scrollState)
                                        ) {

                                            AnimatedContent(
                                                targetState = receiptType,
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
                                                                it * direction
                                                            } +
                                                                    fadeIn(
                                                                        tween(320)
                                                                    )
                                                            ).togetherWith(
                                                            slideOutHorizontally(
                                                                tween(420)
                                                            ) {
                                                                -it * direction / 3
                                                            } +
                                                                    fadeOut(
                                                                        tween(260)
                                                                    )
                                                        ).using(
                                                            SizeTransform(false)
                                                        )
                                                },
                                                label = "receipt_transition"
                                            ) { currentType ->

                                                when (currentType) {

                                                    ReceiptType.STANDARD ->
                                                        ReciboEstandar(
                                                            sale = saleState.data,
                                                            business = businessData,
                                                            paddingValues = PaddingValues(0.dp)
                                                        )

                                                    ReceiptType.DARK ->
                                                        ReciboDark(
                                                            sale = saleState.data,
                                                            business = businessData,
                                                            paddingValues = PaddingValues(0.dp)
                                                        )

                                                    ReceiptType.COSMETIC ->
                                                        ReciboCosmetic(
                                                            sale = saleState.data,
                                                            business = businessData,
                                                            paddingValues = PaddingValues(0.dp)
                                                        )

                                                    ReceiptType.DRUG ->
                                                        ReciboDrug(
                                                            sale = saleState.data,
                                                            business = businessData,
                                                            paddingValues = PaddingValues(0.dp)
                                                        )

                                                    ReceiptType.PANADERIA ->
                                                        ReciboPanaderia(
                                                            sale = saleState.data,
                                                            business = businessData,
                                                            paddingValues = PaddingValues(0.dp)
                                                        )

                                                    ReceiptType.GAMMING ->
                                                        ReciboGamming(
                                                            sale = saleState.data,
                                                            business = businessData,
                                                            paddingValues = PaddingValues(0.dp)
                                                        )

                                                    ReceiptType.METAL ->
                                                        ReciboMetal(
                                                            sale = saleState.data,
                                                            business = businessData,
                                                            paddingValues = PaddingValues(0.dp)
                                                        )

                                                    ReceiptType.JUGUETERIA ->
                                                        ReciboJugueteria(
                                                            sale = saleState.data,
                                                            business = businessData,
                                                            paddingValues = PaddingValues(0.dp)
                                                        )

                                                    ReceiptType.ECO ->
                                                        ReciboEcologico(
                                                            sale = saleState.data,
                                                            business = businessData,
                                                            paddingValues = PaddingValues(0.dp)
                                                        )

                                                    ReceiptType.FUTURE ->
                                                        ReciboFuture(
                                                            sale = saleState.data,
                                                            business = businessData,
                                                            paddingValues = PaddingValues(0.dp)
                                                        )

                                                    ReceiptType.RESTO ->
                                                        ReciboResto(
                                                            sale = saleState.data,
                                                            business = businessData,
                                                            paddingValues = PaddingValues(0.dp)
                                                        )

                                                    ReceiptType.RESTO1 ->
                                                        ReciboResto1(
                                                            sale = saleState.data,
                                                            business = businessData,
                                                            paddingValues = PaddingValues(0.dp)
                                                        )
                                                }
                                            }
                                        }
                                    }
                                }
                            },
                            update = { view ->
                                receiptViewRef.value = view
                            }
                        )
                    }

                    else -> {
                        LoadingBox(paddingValues)
                    }
                }
            }

            else -> {
                LoadingBox(paddingValues)
            }
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