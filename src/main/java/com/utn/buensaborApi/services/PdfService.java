package com.utn.buensaborApi.services;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.utn.buensaborApi.models.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class PdfService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

    public byte[] generarFacturaPdf(Factura factura) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Agregar encabezado con datos de la empresa
        agregarEncabezadoEmpresa(document, factura.getSucursal());

        // Agregar información de la factura
        agregarInformacionFactura(document, factura);

        // Agregar datos del cliente
        agregarDatosCliente(document, factura.getCliente());

        // Agregar detalle del pedido
        agregarDetallePedido(document, factura.getPedidoVenta());

        // Agregar resumen y totales
        agregarResumen(document, factura);

        document.close();
        return baos.toByteArray();
    }

    private void agregarEncabezadoEmpresa(Document document, SucursalEmpresa sucursal) {
        Paragraph title = new Paragraph("EL BUEN SABOR")
                .setBold()
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER);

        Paragraph subtitle = new Paragraph("Sucursal: " + sucursal.getNombre())
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER);

        Paragraph direccion = new Paragraph("Dirección: " + obtenerDireccionSucursal(sucursal))
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER);

        Paragraph cuit = new Paragraph("CUIT: 30-71234567-8")
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER);

        Paragraph responsable = new Paragraph("Responsable Inscripto")
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER);

        document.add(title);
        document.add(subtitle);
        document.add(direccion);
        document.add(cuit);
        document.add(responsable);
        document.add(new Paragraph(" "));
    }

    private String obtenerDireccionSucursal(SucursalEmpresa sucursal) {
        if (sucursal.getDomicilio() != null) {
            Domicilio dom = sucursal.getDomicilio();
            return dom.getCalle() + " " + dom.getNumero() + ", " +
                    (dom.getLocalidad() != null ? dom.getLocalidad().getNombre() : "");
        }
        return "Dirección no disponible";
    }

    private void agregarInformacionFactura(Document document, Factura factura) {
        Paragraph facturaTitle = new Paragraph("FACTURA")
                .setBold()
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(facturaTitle);

        Table table = new Table(UnitValue.createPercentArray(new float[]{50, 50}));
        table.setWidth(UnitValue.createPercentValue(100));

        Cell cell1 = new Cell().add(new Paragraph("Nº Comprobante: " + factura.getNroComprobante()).setBold());
        Cell cell2 = new Cell().add(new Paragraph("Fecha: " + factura.getFechaFacturacion().format(DATE_FORMATTER)).setBold());

        table.addCell(cell1);
        table.addCell(cell2);

        document.add(table);
        document.add(new Paragraph(" "));
    }

    private void agregarDatosCliente(Document document, Cliente cliente) {
        document.add(new Paragraph("DATOS DEL CLIENTE").setBold().setFontSize(14));

        Table table = new Table(UnitValue.createPercentArray(new float[]{100}));
        table.setWidth(UnitValue.createPercentValue(100));

        Cell cell = new Cell();
        cell.setPadding(5);

        String clienteInfo = "Nombre: " + cliente.getNombre() + " " + cliente.getApellido() + "\n" +
                "Teléfono: " + cliente.getTelefono() + "\n" +
                "Email: " + cliente.getEmail();

        cell.add(new Paragraph(clienteInfo));
        table.addCell(cell);

        document.add(table);
        document.add(new Paragraph(" "));
    }

    private void agregarDetallePedido(Document document, PedidoVenta pedido) {
        document.add(new Paragraph("DETALLE DEL PEDIDO").setBold().setFontSize(14));

        Table table = new Table(UnitValue.createPercentArray(new float[]{5, 2, 2, 2}));
        table.setWidth(UnitValue.createPercentValue(100));

        // Encabezados de tabla
        Cell headerDesc = new Cell().add(new Paragraph("Descripción").setBold());
        headerDesc.setBackgroundColor(new DeviceRgb(211, 211, 211));
        headerDesc.setTextAlignment(TextAlignment.CENTER);
        table.addCell(headerDesc);

        Cell headerCant = new Cell().add(new Paragraph("Cantidad").setBold());
        headerCant.setBackgroundColor(new DeviceRgb(211, 211, 211));
        headerCant.setTextAlignment(TextAlignment.CENTER);
        table.addCell(headerCant);

        Cell headerPrecio = new Cell().add(new Paragraph("Precio Unit.").setBold());
        headerPrecio.setBackgroundColor(new DeviceRgb(211, 211, 211));
        headerPrecio.setTextAlignment(TextAlignment.CENTER);
        table.addCell(headerPrecio);

        Cell headerSubtotal = new Cell().add(new Paragraph("Subtotal").setBold());
        headerSubtotal.setBackgroundColor(new DeviceRgb(211, 211, 211));
        headerSubtotal.setTextAlignment(TextAlignment.CENTER);
        table.addCell(headerSubtotal);

        // Detalles del pedido
        for (PedidoVentaDetalle detalle : pedido.getPedidosVentaDetalle()) {
            // Descripción
            table.addCell(new Cell().add(new Paragraph(obtenerDescripcionArticulo(detalle))));

            // Cantidad
            Cell cantCell = new Cell().add(new Paragraph(String.valueOf(detalle.getCantidad())));
            cantCell.setTextAlignment(TextAlignment.CENTER);
            table.addCell(cantCell);

            // Precio unitario
            double precioUnitario = detalle.getSubtotal() / detalle.getCantidad();
            Cell precioCell = new Cell().add(new Paragraph(CURRENCY_FORMAT.format(precioUnitario)));
            precioCell.setTextAlignment(TextAlignment.RIGHT);
            table.addCell(precioCell);

            // Subtotal
            Cell subtCell = new Cell().add(new Paragraph(CURRENCY_FORMAT.format(detalle.getSubtotal())));
            subtCell.setTextAlignment(TextAlignment.RIGHT);
            table.addCell(subtCell);
        }

        document.add(table);
        document.add(new Paragraph(" "));
    }

    private String obtenerDescripcionArticulo(PedidoVentaDetalle detalle) {
        if (detalle.getPromocion() != null) {
            return "Promoción: " + detalle.getPromocion().getDenominacion();
        } else if (detalle.getArticulo() != null) {
            return detalle.getArticulo().getDenominacion();
        }
        return "Artículo sin descripción";
    }

    private void agregarResumen(Document document, Factura factura) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{50, 50}));
        table.setWidth(UnitValue.createPercentValue(50));
        table.setTextAlignment(TextAlignment.RIGHT);

        double subtotal = factura.getTotalVenta();
        if (factura.getDescuento() != null && factura.getDescuento() > 0) {
            subtotal = subtotal / (1 - factura.getDescuento() / 100);
        }

        Cell labelCell = new Cell().add(new Paragraph("Subtotal:").setBold());
        labelCell.setBorder(null);
        labelCell.setTextAlignment(TextAlignment.LEFT);
        table.addCell(labelCell);

        Cell valueCell = new Cell().add(new Paragraph(CURRENCY_FORMAT.format(subtotal)));
        valueCell.setBorder(null);
        valueCell.setTextAlignment(TextAlignment.RIGHT);
        table.addCell(valueCell);

        if (factura.getDescuento() != null && factura.getDescuento() > 0) {
            Cell descLabel = new Cell().add(new Paragraph("Descuento (" + factura.getDescuento() + "%):").setBold());
            descLabel.setBorder(null);
            descLabel.setTextAlignment(TextAlignment.LEFT);
            table.addCell(descLabel);

            double descuentoValor = subtotal * (factura.getDescuento() / 100);
            Cell descValue = new Cell().add(new Paragraph("-" + CURRENCY_FORMAT.format(descuentoValor)));
            descValue.setBorder(null);
            descValue.setTextAlignment(TextAlignment.RIGHT);
            table.addCell(descValue);
        }

        if (factura.getGastoEnvio() != null && factura.getGastoEnvio() > 0) {
            Cell envioLabel = new Cell().add(new Paragraph("Gastos de envío:").setBold());
            envioLabel.setBorder(null);
            envioLabel.setTextAlignment(TextAlignment.LEFT);
            table.addCell(envioLabel);

            Cell envioValue = new Cell().add(new Paragraph(CURRENCY_FORMAT.format(factura.getGastoEnvio())));
            envioValue.setBorder(null);
            envioValue.setTextAlignment(TextAlignment.RIGHT);
            table.addCell(envioValue);
        }

        Cell totalLabel = new Cell().add(new Paragraph("TOTAL:").setBold().setFontSize(12));
        totalLabel.setTextAlignment(TextAlignment.LEFT);
        totalLabel.setBorderTop(null);
        totalLabel.setBorderLeft(null);
        totalLabel.setBorderRight(null);
        table.addCell(totalLabel);

        Cell totalValue = new Cell().add(new Paragraph(CURRENCY_FORMAT.format(factura.getTotalVenta())).setBold().setFontSize(12));
        totalValue.setTextAlignment(TextAlignment.RIGHT);
        totalValue.setBorderTop(null);
        totalValue.setBorderLeft(null);
        totalValue.setBorderRight(null);
        table.addCell(totalValue);

        document.add(table);

        // Agregar forma de pago
        if (factura.getFormaPago() != null) {
            Paragraph formaPago = new Paragraph("Forma de pago: " + factura.getFormaPago().name())
                    .setBold()
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(formaPago);
        }
    }

    // Metodo para guardar en archivo (opcional)
    public void guardarFacturaEnArchivo(Factura factura, String rutaArchivo) throws IOException {
        PdfWriter writer = new PdfWriter(new FileOutputStream(rutaArchivo));
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        agregarEncabezadoEmpresa(document, factura.getSucursal());
        agregarInformacionFactura(document, factura);
        agregarDatosCliente(document, factura.getCliente());
        agregarDetallePedido(document, factura.getPedidoVenta());
        agregarResumen(document, factura);

        document.close();
    }

    public byte[] generarNotaCreditoPdf(Factura factura) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Agregar encabezado con datos de la empresa
        agregarEncabezadoEmpresa(document, factura.getSucursal());

        // Encabezado especial para nota de crédito
        Paragraph notaCreditoTitle = new Paragraph("NOTA DE CRÉDITO")
                .setBold()
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(notaCreditoTitle);

        // Mostrar número de comprobante de referencia (el positivo)
        int nroFacturaOriginal = Math.abs(factura.getNroComprobante());
        Paragraph referenciaFactura = new Paragraph("Referencia a factura número: " + nroFacturaOriginal)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(referenciaFactura);

        document.add(new Paragraph(" "));

        // Agregar información de la nota de crédito (sin título "FACTURA")
        agregarInformacionNotaCredito(document, factura);

        // Agregar datos del cliente
        agregarDatosCliente(document, factura.getCliente());

        // Agregar detalle del pedido
        agregarDetallePedido(document, factura.getPedidoVenta());

        // Agregar resumen y totales
        agregarResumen(document, factura);

        document.close();
        return baos.toByteArray();
    }

    private void agregarInformacionNotaCredito(Document document, Factura factura) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{50, 50}));
        table.setWidth(UnitValue.createPercentValue(100));

        // Mostrar NC antes del número de comprobante
        String nroComprobante = "NC" + Math.abs(factura.getNroComprobante());
        Cell cell1 = new Cell().add(new Paragraph("Nº Comprobante: " + nroComprobante).setBold());
        Cell cell2 = new Cell().add(new Paragraph("Fecha: " + factura.getFechaFacturacion().format(DATE_FORMATTER)).setBold());

        table.addCell(cell1);
        table.addCell(cell2);

        document.add(table);
        document.add(new Paragraph(" "));
    }
}