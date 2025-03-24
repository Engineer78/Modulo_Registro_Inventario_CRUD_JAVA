# Hardware Store Inventory FFIG

## Descripción del Proyecto
Este proyecto corresponde al **Proyecto Formativo SENA: Hardware Store Inventory FFIG** y aborda el módulo de **Registro de Inventario**. Se enfoca en el desarrollo de un sistema CRUD en **Java** utilizando las arquitecturas **MVC (Model-View-Controller)** y **DTO (Data Transfer Object)**, conectado a una base de datos MySQL.

### Versión
**1.0**

### Centro de Formación
Centro de Comercio y Turismo Armenia, Regional Quindío.

### Tecnología
Programa de **Análisis y Desarrollo de Software (2024 - 2025)**, liderado por el SENA.

---

## Equipo de Desarrollo
- **Instructor:** Carlos Alberto Fuel Tucan.
- **Aprendices:**
  - David Ricardo Graffe Rodríguez, Ficha: 2879724.
  - Joaquín Humberto Jiménez Rosas, Ficha: 2879723.
  - Juan David Gallego López, Ficha: 2879723.

@Todos los derechos reservados 2024 - 2025.

---

## Funcionalidades del CRUD
El proyecto incluye las siguientes funcionalidades:
- **Crear Producto:** Permite registrar nuevos productos en el inventario, asignarles categorías, proveedores y una imagen.
- **Crear Proveedores y Categorías:** Gestiona el registro de proveedores y categorías para mantener la integridad del inventario.
- **Consultar Producto:** Permite buscar productos por proveedor, categoría, código o nombre. Si el producto tiene una imagen asociada, esta se visualiza en la interfaz.

La implementación utiliza arquitectura MVC y gestiona la comunicación con MySQL.

---

## Requisitos para la Ejecución
1. **Configuración de la Base de Datos:**
   - Ajusta el archivo `ConexionBD.java` (ubicado en el directorio `util`) para incluir tus credenciales de usuario y contraseña de MySQL.

2. **Script de Base de Datos:**
   - El repositorio incluye un archivo **SQL** con las estructuras de las tablas y datos de prueba necesarios. Ejecuta este script para configurar tu entorno.

3. **Carga de Imágenes:**
   - Las imágenes no se cargan automáticamente en los datos de ejemplo ya que no existe un módulo de actualización en esta versión. Sin embargo, al crear un nuevo registro mediante la funcionalidad de "Crear Producto", podrás asignar una imagen al producto directamente desde la aplicación.
