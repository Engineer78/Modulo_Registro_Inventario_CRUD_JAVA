-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS inventariocrudbd;
USE inventariocrudbd;

-- Tabla "categoria"
CREATE TABLE categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre_categoria VARCHAR(100) NOT NULL
);

-- Tabla "proveedor"
CREATE TABLE proveedor (
    id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
    nombre_proveedor VARCHAR(100) NOT NULL,
    nit_proveedor VARCHAR(50) NOT NULL,
    telefono_proveedor VARCHAR(20),
    direccion_proveedor VARCHAR(200)
);

-- Tabla "producto"
CREATE TABLE producto (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    codigo_producto VARCHAR(50) NOT NULL,
    nombre_producto VARCHAR(100) NOT NULL,
    cantidad_producto INT NOT NULL,
    valorUnitario_producto DECIMAL(20,2) NOT NULL,
    valorTotal_producto DECIMAL(20,2) NOT NULL,
    imagen LONGBLOB,
    categoria_id_categoria INT NOT NULL,
    FOREIGN KEY (categoria_id_categoria) REFERENCES categoria(id_categoria)
);

-- Tabla "producto_proveedor"
CREATE TABLE producto_proveedor (
    id_producto_proveedor INT AUTO_INCREMENT PRIMARY KEY,
    precio_compra DECIMAL(10,2) NOT NULL,
    producto_id_producto INT NOT NULL,
    proveedor_id_proveedor INT NOT NULL,
    FOREIGN KEY (producto_id_producto) REFERENCES producto(id_producto),
    FOREIGN KEY (proveedor_id_proveedor) REFERENCES proveedor(id_proveedor)
);

-- Insertar datos en "categoria"
INSERT INTO categoria (nombre_categoria) VALUES
('Herramientas'),
('Materiales de Construcción'),
('Tuberías y Conexiones'),
('Pinturas'),
('Electricidad');

-- Insertar datos en "proveedor"
INSERT INTO proveedor (nombre_proveedor, nit_proveedor, telefono_proveedor, direccion_proveedor) VALUES
('Proveedor ABC', '900123456', '3123456789', 'Calle 123 #45-67'),
('Proveedor XYZ', '900654321', '3019876543', 'Carrera 12 #34-56'),
('Proveedor Ferremax', '800456789', '3051122334', 'Av. Siempreviva #123'),
('Proveedor Sigma', '800987654', '3101231234', 'Calle Principal #1'),
('Proveedor Beta', '801234567', '3194567890', 'Zona Industrial #45');

-- Insertar datos en "producto"

INSERT INTO producto (codigo_producto, nombre_producto, cantidad_producto, valorUnitario_producto, valorTotal_producto, imagen, categoria_id_categoria)
VALUES
('H001', 'Martillo', 50, 15000.00, 750000.00, NULL, 1),
('H002', 'Destornillador', 100, 8000.00, 800000.00, NULL, 1),
('H003', 'Alicate', 60, 12000.00, 720000.00, NULL, 1),
('H004', 'Llave Inglesa', 35, 18000.00, 630000.00, NULL, 1),
('H005', 'Taladro Eléctrico', 20, 350000.00, 7000000.00, NULL, 1),
('H006', 'Sierra Manual', 25, 24000.00, 600000.00, NULL, 1),
('MC001', 'Cemento', 100, 25000.00, 2500000.00, NULL, 2),
('MC002', 'Arena (bolsa)', 200, 20000.00, 4000000.00, NULL, 2),
('MC003', 'Grava (bolsa)', 150, 18000.00, 2700000.00, NULL, 2),
('MC004', 'Adhesivo de Contacto', 100, 12000.00, 1200000.00, NULL, 2),
('MC005', 'Teja Plástica', 75, 45000.00, 3375000.00, NULL, 2),
('MC006', 'Bloques de Concreto', 200, 3000.00, 600000.00, NULL, 2),
('TC001', 'Tubo PVC 1"', 300, 5000.00, 1500000.00, NULL, 3),
('TC002', 'Codo PVC 1"', 400, 2500.00, 1000000.00, NULL, 3),
('TC003', 'Adaptador PVC', 250, 7000.00, 1750000.00, NULL, 3),
('TC004', 'Tubo Cobre 1/2"', 80, 25000.00, 2000000.00, NULL, 3),
('TC005', 'Unión PVC', 120, 3000.00, 360000.00, NULL, 3),
('TC006', 'Tubo Galvanizado 1"', 50, 45000.00, 2250000.00, NULL, 3),
('P001', 'Pintura blanca galón', 50, 35000.00, 1750000.00, NULL, 4),
('P002', 'Pintura negra galón', 40, 35000.00, 1400000.00, NULL, 4),
('P003', 'Rodillo para pintar', 70, 12000.00, 840000.00, NULL, 4),
('P004', 'Pintura Azul galón', 30, 37000.00, 1110000.00, NULL, 4),
('P005', 'Pincel', 60, 7000.00, 420000.00, NULL, 4),
('P006', 'Barniz galón', 20, 50000.00, 1000000.00, NULL, 4),
('E001', 'Interruptor eléctrico', 120, 5000.00, 600000.00, NULL, 5),
('E002', 'Tomacorriente doble', 150, 8000.00, 1200000.00, NULL, 5),
('E003', 'Cable eléctrico 10m', 100, 25000.00, 2500000.00, NULL, 5),
('E004', 'Foco LED', 150, 20000.00, 3000000.00, NULL, 5),
('E005', 'Caja Eléctrica', 100, 15000.00, 1500000.00, NULL, 5),
('E006', 'Disyuntor', 50, 45000.00, 2250000.00, NULL, 5),
('H007', 'Nivel de Burbuja', 40, 25000.00, 1000000.00, NULL, 1),
('H008', 'Cincel', 80, 15000.00, 1200000.00, NULL, 1),
('H009', 'Llave de Tubo', 25, 30000.00, 750000.00, NULL, 1),
('H010', 'Lima Metálica', 50, 12000.00, 600000.00, NULL, 1),
('H011', 'Tenazas', 30, 18000.00, 540000.00, NULL, 1),
('MC007', 'Yeso en Bolsa', 100, 10000.00, 1000000.00, NULL, 2),
('MC008', 'Piedra Triturada', 150, 30000.00, 4500000.00, NULL, 2),
('MC009', 'Malla Electrosoldada', 60, 50000.00, 3000000.00, NULL, 2),
('MC010', 'Placa Antiderrapante', 30, 120000.00, 3600000.00, NULL, 2),
('TC007', 'Abrazadera 1"', 90, 2000.00, 180000.00, NULL, 3),
('TC008', 'Codo de Hierro', 70, 3500.00, 245000.00, NULL, 3),
('TC009', 'Válvula de Paso', 40, 7000.00, 280000.00, NULL, 3);

-- Insertar datos en "producto_proveedor"
INSERT INTO producto_proveedor (precio_compra, producto_id_producto, proveedor_id_proveedor)
VALUES
-- Relacionando productos con proveedor 1 (13 productos asignados)
(12000.00, 1, 1), 
(7000.00, 2, 1), 
(9000.00, 3, 1), 
(17000.00, 4, 1), 
(350000.00, 5, 1), 
(24000.00, 6, 1), 
(25000.00, 7, 1), 
(20000.00, 8, 1), 
(3000.00, 9, 1), 
(45000.00, 10, 1), 
(37000.00, 11, 1), 
(12000.00, 12, 1), 
(5000.00, 13, 1), 

-- Relacionando productos con proveedor 2 (8 productos asignados)
(25000.00, 14, 2), 
(15000.00, 15, 2), 
(30000.00, 16, 2), 
(450000.00, 17, 2), 
(12000.00, 18, 2), 
(3500.00, 19, 2), 
(7000.00, 20, 2), 
(2500.00, 21, 2), 

-- Relacionando productos con proveedor 3 (7 productos asignados)
(18000.00, 22, 3), 
(3000.00, 23, 3), 
(32000.00, 24, 3), 
(25000.00, 25, 3), 
(4500.00, 4, 3), 
(6000.00, 7, 3), 
(15000.00, 10, 3), 

-- Relacionando productos con proveedor 4 (8 productos asignados)
(17000.00, 11, 4), 
(7000.00, 12, 4), 
(25000.00, 13, 4), 
(22000.00, 15, 4), 
(37000.00, 16, 4), 
(30000.00, 19, 4), 
(5000.00, 20, 4), 
(45000.00, 25, 4), 

-- Relacionando productos con proveedor 5 (6 productos asignados)
(45000.00, 2, 5), 
(12000.00, 5, 5), 
(17000.00, 8, 5), 
(20000.00, 9, 5), 
(3500.00, 12, 5), 
(22000.00, 18, 5);