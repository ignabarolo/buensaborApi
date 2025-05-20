package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.*;
import com.utn.buensaborApi.repositories.*;
import com.utn.buensaborApi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.utn.buensaborApi.enums.*;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DataInitializerServices {

    private final clienteRepository clienteRepository;
    private final paisRepository paisRepository;
    private final provinciaRepository provinciaRepository;
    private final localidadRepository localidadRepository;
    private final empresaRepository empresaRepository;
    private final sucursalRepository sucursalRepository;
    private final domicilioRepository domicilioRepository;
    private final CategoriaArticuloRepository categoriaRepository;
    private final ArticuloInsumoRepository articuloInsumoRepository;
    private final UnidadMedidaRepository unidadMedidaRepository;
    private final ArticuloManufacturadoRepository articuloManufacturadoRepository;
    private final ArticuloManufacturadoDetalleRepository articuloManufacturadoDetalleRepository;
    private final SucursalInsumoRepository SucursalInsumoRepository;
    private final usuarioRepository usuarioRepository;
    private final empleadoRepository empleadoRepository;
    private final PromocionRepository promocionRepository;
    private final PromocionDetalleRepository promocionDetalleRepository;
    private final PedidoVentaRepository pedidoVentaRepository;
    private final PedidoVentaDetalleRepository pedidoVentaDetalleRepository;
    private final FacturaRepository facturaRepository;
    private final DatoMercadoPagoRopository datoMercadoPagoRepository;
    @Transactional
    public void initializeData() {
        // Crear el país y las provincias
        Pais pais1 = new Pais();
        pais1.setNombre("Argentina");
        paisRepository.save(pais1);

        Provincia provincia1 = new Provincia();
        provincia1.setNombre("Mendoza");
        provincia1.setPais(pais1);

        Provincia provincia2 = new Provincia();
        provincia2.setNombre("Buenos Aires");
        provincia2.setPais(pais1);

        provinciaRepository.save(provincia1);
        provinciaRepository.save(provincia2);

        // Crear localidades
        Localidad localidad1 = new Localidad();
        localidad1.setNombre("Lujan de Cuyo");
        localidad1.setProvincia(provincia1);

        Localidad localidad2 = new Localidad();
        localidad2.setNombre("Godoy Cruz");
        localidad2.setProvincia(provincia1);

        localidadRepository.save(localidad1);
        localidadRepository.save(localidad2);

        // Crear empresa y sucursales
        Empresa empresa = new Empresa();
        empresa.setNombre("El Buen Sabor");
        empresa.setCuil(20586935);
        empresa.setRazonSocial("buenSaboeSA");
        empresaRepository.save(empresa);

        SucursalEmpresa sucursal = new SucursalEmpresa();
        sucursal.setNombre("Sucursal Luján");
        sucursal.setHoraApertura(String.valueOf(LocalTime.of(9, 0)));
        sucursal.setHoraCierre(String.valueOf(LocalTime.of(22, 0)));
        sucursal.setEmpresa(empresa);
        
        sucursalRepository.save(sucursal);

        // Crear un domicilio para la sucursal
        Domicilio domicilio = new Domicilio();
        domicilio.setCalle("Av. Las Heras");
        domicilio.setNumero(1234);
        domicilio.setLocalidad(localidad1);

        domicilioRepository.save(domicilio);
        sucursal.setDomicilio(domicilio);
        sucursalRepository.save(sucursal);

        // Crear un usuario para el cliente
        Usuario usuarioCliente = new Usuario();
        usuarioCliente.setAuth0id("auth0_cliente_123");
        usuarioCliente.setNombreUsuario("cliente123");
        usuarioRepository.save(usuarioCliente);

        // Crear un domicilio para el cliente
        Domicilio domicilioCliente = new Domicilio();
        domicilioCliente.setCalle("Av. Buenos Aires");
        domicilioCliente.setNumero(123);
        domicilioCliente.setLocalidad(localidad1);
        domicilioRepository.save(domicilioCliente);

        // Crear y asociar el cliente
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan");
        cliente.setApellido("Pérez");
        cliente.setTelefono("123456789");
        cliente.setEmail("cliente@email.com");
        cliente.setFechaDeNacimiento(LocalDate.of(1990, 5, 15));
        cliente.setUsuario(usuarioCliente);
        cliente.setDomicilio(domicilioCliente);
        clienteRepository.save(cliente);

        // Crear un usuario para el empleado
        Usuario usuarioEmpleado = new Usuario();
        usuarioEmpleado.setAuth0id("auth0_empleado_456");
        usuarioEmpleado.setNombreUsuario("empleado456");
        usuarioRepository.save(usuarioEmpleado);

        // Crear un domicilio para el empleado
        Domicilio domicilioEmpleado = new Domicilio();
        domicilioEmpleado.setCalle("Calle San Martín");
        domicilioEmpleado.setNumero(456);
        domicilioEmpleado.setLocalidad(localidad2);
        domicilioRepository.save(domicilioEmpleado);

        // Crear y asociar el empleado
        Empleado empleado = new Empleado();
        empleado.setNombre("Ana");
        empleado.setApellido("García");
        empleado.setTelefono("987654321");
        empleado.setEmail("empleado@email.com");
        empleado.setRol(Rol.CAJERO);
        empleado.setUsuario(usuarioEmpleado);
        empleado.setDomicilio(domicilioEmpleado);
        empleado.setSucursal(sucursal);
        empleadoRepository.save(empleado);


        // Crear categorías
        CategoriaArticulo categoriaBebidas = new CategoriaArticulo();
        categoriaBebidas.setDenominacion("Bebidas");

        CategoriaArticulo categoriaVerdura = new CategoriaArticulo();
        categoriaVerdura.setDenominacion("Verdura");

        CategoriaArticulo categoriaMenu = new CategoriaArticulo();
        categoriaMenu.setDenominacion("Menu");

        CategoriaArticulo categoriaLacteos = new CategoriaArticulo();
        categoriaLacteos.setDenominacion("Lacteos");

        CategoriaArticulo categoriaConservas= new CategoriaArticulo();
        categoriaConservas.setDenominacion("Conservas");

        categoriaRepository.save(categoriaBebidas);
        categoriaRepository.save(categoriaVerdura);
        categoriaRepository.save(categoriaMenu);
        categoriaRepository.save(categoriaLacteos);
        categoriaRepository.save(categoriaConservas);

        //Subcategoria
        CategoriaArticulo subcategoriaPizza = new CategoriaArticulo();
        subcategoriaPizza.setDenominacion("Pizza");
        subcategoriaPizza.setCategoriaPadre(categoriaMenu);

        categoriaRepository.save(subcategoriaPizza);

        // Crear unidades de medida
        UnidadMedida litros = new UnidadMedida();
        litros.setDenominacion("Litros");

        UnidadMedida gramos = new UnidadMedida();
        gramos.setDenominacion("Gramos");

        UnidadMedida unidad = new UnidadMedida();
        unidad.setDenominacion("Unidad");
        unidadMedidaRepository.save(litros);
        unidadMedidaRepository.save(gramos);
        unidadMedidaRepository.save(unidad);

        // Crear insumos
        ArticuloInsumo cocaCola = new ArticuloInsumo();
        cocaCola.setDenominacion("Coca Cola");
        cocaCola.setUnidadMedida(litros);
        cocaCola.setEsParaElaborar(false);
        cocaCola.setPrecioCompra(20.0);
        cocaCola.setPrecioVenta(50.0);
        cocaCola.setCategoria(categoriaBebidas);

        ArticuloInsumo harina = new ArticuloInsumo();
        harina.setDenominacion("Harina");
        harina.setUnidadMedida(gramos);
        harina.setEsParaElaborar(true);
        harina.setPrecioCompra(10.0);
        harina.setPrecioVenta(15.0);
        harina.setCategoria(categoriaConservas);

        ArticuloInsumo salsa = new ArticuloInsumo();
        salsa.setDenominacion("Salsa");
        salsa.setUnidadMedida(gramos);
        salsa.setEsParaElaborar(true);
        salsa.setPrecioCompra(8.0);
        salsa.setPrecioVenta(12.0);
        salsa.setCategoria(categoriaConservas);
        articuloInsumoRepository.save(salsa);

        ArticuloInsumo muzzarella = new ArticuloInsumo();
        muzzarella.setDenominacion("Muzzarella");
        muzzarella.setUnidadMedida(gramos);
        muzzarella.setEsParaElaborar(true);
        muzzarella.setPrecioCompra(25.0);
        muzzarella.setPrecioVenta(35.0);
        muzzarella.setCategoria(categoriaLacteos);

        articuloInsumoRepository.save(muzzarella);
        articuloInsumoRepository.save(cocaCola);
        articuloInsumoRepository.save(harina);
        articuloInsumoRepository.save(salsa);

        // Crear y asociar SucursalInsumo para Harina
        SucursalInsumo sucursalInsumoHarina = new SucursalInsumo();
        sucursalInsumoHarina.setStockActual(100.0);
        sucursalInsumoHarina.setStockMinimo(50.0);
        sucursalInsumoHarina.setStockMaximo(500.0);
        sucursalInsumoHarina.setArticuloInsumo(harina);
        sucursalInsumoHarina.setSucursal(sucursal);
        SucursalInsumoRepository.save(sucursalInsumoHarina);

        // Crear y asociar SucursalInsumo para Salsa
        SucursalInsumo sucursalInsumoSalsa = new SucursalInsumo();
        sucursalInsumoSalsa.setStockActual(150.0);
        sucursalInsumoSalsa.setStockMinimo(70.0);
        sucursalInsumoSalsa.setStockMaximo(400.0);
        sucursalInsumoSalsa.setArticuloInsumo(salsa);
        sucursalInsumoSalsa.setSucursal(sucursal);
        SucursalInsumoRepository.save(sucursalInsumoSalsa);

        // Crear y asociar SucursalInsumo para Muzzarella
        SucursalInsumo sucursalInsumoMuzzarella = new SucursalInsumo();
        sucursalInsumoMuzzarella.setStockActual(80.0);
        sucursalInsumoMuzzarella.setStockMinimo(20.0);
        sucursalInsumoMuzzarella.setStockMaximo(300.0);
        sucursalInsumoMuzzarella.setArticuloInsumo(muzzarella);
        sucursalInsumoMuzzarella.setSucursal(sucursal);
        SucursalInsumoRepository.save(sucursalInsumoMuzzarella);


        //Crear ArticuloManufacturado
        ArticuloManufacturado pizzaMuzarella = new ArticuloManufacturado();
        pizzaMuzarella.setDenominacion("Pizza Muzarella");
        pizzaMuzarella.setDescripcion("Pizza clásica con salsa y muzzarella");
        pizzaMuzarella.setUnidadMedida(unidad);
        pizzaMuzarella.setPrecioVenta(800.0);
        pizzaMuzarella.setTiempoEstimadoMinutos(20);
        pizzaMuzarella.setCategoria(subcategoriaPizza);
        articuloManufacturadoRepository.save(pizzaMuzarella);

        // Crear los detalles (receta) del artículo manufacturado
        ArticuloManufacturadoDetalle detalleSalsa = new ArticuloManufacturadoDetalle();
        detalleSalsa.setArticuloInsumo(salsa);
        detalleSalsa.setCantidad(200.0);
        detalleSalsa.setArticuloManufacturado(pizzaMuzarella);
        articuloManufacturadoDetalleRepository.save(detalleSalsa);

        ArticuloManufacturadoDetalle detalleMuzzarella = new ArticuloManufacturadoDetalle();
        detalleMuzzarella.setArticuloInsumo(muzzarella);
        detalleMuzzarella.setCantidad(300.0);
        detalleMuzzarella.setArticuloManufacturado(pizzaMuzarella);
        articuloManufacturadoDetalleRepository.save(detalleMuzzarella);

        // Asociar los detalles a la receta del artículo manufacturado
        pizzaMuzarella.getDetalles().add(detalleSalsa);
        pizzaMuzarella.getDetalles().add(detalleMuzzarella);

        articuloManufacturadoRepository.save(pizzaMuzarella);
        // Primera promoción: 1 Coca + 1 Muzzarella al 30% de descuento
        Promocion promoCocaMuzzarella = new Promocion();
        promoCocaMuzzarella.setDenominacion("1 Coca + 1 Muzzarella al 30% de descuento");
        promoCocaMuzzarella.setFechaDesde(LocalDate.now());
        promoCocaMuzzarella.setFechaHasta(LocalDate.now().plusMonths(1));
        promoCocaMuzzarella.setDescuento(0.30); // 30% de descuento
        promoCocaMuzzarella.setSucursal(sucursal);
        promocionRepository.save(promoCocaMuzzarella);

        // Detalles de la primera promoción
        PromocionDetalle detalleCoca = new PromocionDetalle();
        detalleCoca.setCantidad(1);
        detalleCoca.setPromocion(promoCocaMuzzarella);
        detalleCoca.setArticulo(cocaCola);
        promocionDetalleRepository.save(detalleCoca);

        PromocionDetalle detalleMuzzarellaPromocion = new PromocionDetalle();
        detalleMuzzarellaPromocion.setCantidad(1);
        detalleMuzzarellaPromocion.setPromocion(promoCocaMuzzarella);
        detalleMuzzarellaPromocion.setArticulo(muzzarella);
        promocionDetalleRepository.save(detalleMuzzarellaPromocion);

        // Segunda promoción: 2 Muzzarella al 50% de descuento
        Promocion promoDosMuzzarella = new Promocion();
        promoDosMuzzarella.setDenominacion("2 Muzzarella al 50% de descuento");
        promoDosMuzzarella.setFechaDesde(LocalDate.now());
        promoDosMuzzarella.setFechaHasta(LocalDate.now().plusMonths(1));
        promoDosMuzzarella.setDescuento(0.50);
        promoDosMuzzarella.setSucursal(sucursal);
        promocionRepository.save(promoDosMuzzarella);

        // Detalles de la segunda promoción
        PromocionDetalle detalleDosMuzzarella = new PromocionDetalle();
        detalleDosMuzzarella.setCantidad(2);
        detalleDosMuzzarella.setPromocion(promoDosMuzzarella);
        detalleDosMuzzarella.setArticulo(muzzarella);
        promocionDetalleRepository.save(detalleDosMuzzarella);

        // Crear el pedido de venta
        PedidoVenta pedidoVenta = new PedidoVenta();
        pedidoVenta.setFechaPedido(LocalDate.now());
        pedidoVenta.setHoraPedido(LocalTime.now());
        pedidoVenta.setEstado(Estado.PENDIENTE); // Estado inicial del pedido
        pedidoVenta.setTipoEnvio(TipoEnvio.TAKE_AWAY); // Ejemplo: Retiro en sucursal
        pedidoVenta.setFormaPago(FormaPago.EFECTIVO); // Ejemplo: Pago en efectivo
        pedidoVenta.setGastoEnvio(0.0); // Sin gasto de envío
        pedidoVenta.setDescuento(0.0); // Se calcularán descuentos sobre promociones
        pedidoVenta.setSucursal(sucursal);
        pedidoVenta.setCliente(cliente);

        // Crear detalle para la promoción 2 Muzzarella al 50%
        PedidoVentaDetalle detallePromo2Muzzarella = new PedidoVentaDetalle();
        detallePromo2Muzzarella.setCantidad(1); // Se usa 1 porque es una promoción (ya incluye las 2 muzzarellas)
        detallePromo2Muzzarella.setPromocion(promoDosMuzzarella);
        detallePromo2Muzzarella.setPedidoVenta(pedidoVenta);
        detallePromo2Muzzarella.setSubtotal((2*1200)/0.5); // Precio original (2*1200) menos el descuento (50%)

        // Crear detalle para la Coca-Cola
        PedidoVentaDetalle detalleCocaVenta = new PedidoVentaDetalle();
        detalleCocaVenta.setCantidad(1);
        detalleCocaVenta.setArticulo(cocaCola);
        detalleCocaVenta.setPedidoVenta(pedidoVenta);
        detalleCocaVenta.setSubtotal(cocaCola.getPrecioVenta() * detalleCocaVenta.getCantidad());

        // Asociar los detalles al pedido
        pedidoVenta.setPedidosVentaDetalle(Set.of(detallePromo2Muzzarella, detalleCocaVenta));

        // Calcular totales del pedido
        double totalVenta = detallePromo2Muzzarella.getSubtotal() + detalleCocaVenta.getSubtotal();
        pedidoVenta.setTotalVenta(totalVenta);
        pedidoVenta.setTotalCosto(totalVenta); //corregir

        // Guardar pedido y detalles
        pedidoVentaRepository.save(pedidoVenta);

        // Crear la factura basada en el pedido de venta
        Factura factura = new Factura();
        factura.setFechaFacturacion(LocalDate.now());
        factura.setNroComprobante(13546313); //ver metodo de generar comprobante
        factura.setFormaPago(pedidoVenta.getFormaPago());
        factura.setDescuento(pedidoVenta.getDescuento());
        factura.setGastoEnvio(pedidoVenta.getGastoEnvio());
        factura.setTotalVenta(pedidoVenta.getTotalVenta());
        factura.setPedidoVenta(pedidoVenta);
        factura.setCliente(pedidoVenta.getCliente());
        factura.setSucursal(pedidoVenta.getSucursal());

        // Crear detalles de la factura para cada detalle del pedido
        Set<FacturaDetalle> facturaDetalles = new HashSet<>();
        for (PedidoVentaDetalle detalle : pedidoVenta.getPedidosVentaDetalle()) {
            FacturaDetalle facturaDetalle = new FacturaDetalle();
            facturaDetalle.setFactura(factura);
            facturaDetalle.setArticulo(detalle.getArticulo());
            facturaDetalles.add(facturaDetalle);
        }
        factura.setFacturaDetalles(facturaDetalles);

        // Guardar la factura
        facturaRepository.save(factura);

        // Registrar datos de Mercado Pago para la factura
        DatoMercadoPago datoMercadoPago = new DatoMercadoPago();
        datoMercadoPago.setDate_created(LocalDate.now());
        datoMercadoPago.setDate_approved(LocalDate.now());
        datoMercadoPago.setDate_last_updated(LocalDate.now());
        datoMercadoPago.setPayment_type_id("credit_card");
        datoMercadoPago.setPayment_method_id("visa");
        datoMercadoPago.setStatus("approved");
        datoMercadoPago.setStatus_detail("Pago aprobado exitosamente mediante Mercado Pago.");
        datoMercadoPago.setFactura(factura);

        // Guardar datos en Mercado Pago
        datoMercadoPagoRepository.save(datoMercadoPago);

    }
}