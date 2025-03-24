/*
 * Proyecto Formativo SENA: Hardware Store Inventory FFIG
 * Módulo: Registro de Inventario
 * Desarrollo de CRUD en JAVA usando MVC y DTO
 * Versión 1.0
 * Centro de Comercio y Turismo Armenía, Regional Quindío
 * Aprendices: 
 * David Ricargo Graffe Rodríguez, Ficha: 2879724
 * Joaquín Humberto Jiménez Rosas, Ficha: 2879723
 * Juan David Gallego López, Ficha: 2879723
 * @Todos los derechos reservados 2024 - 2025
 */

package controller;

import javax.swing.*;
import javax.swing.JToggleButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import view.ConsultarProductoView;
import view.CrearProductoView;

public class MenuBarController {

    private CrearProductoView crearProductoView;
    private ConsultarProductoView consultarProductoView;


    public MenuBarController(CrearProductoView crearProductoView, ConsultarProductoView consultarProductoView) {
        this.crearProductoView = crearProductoView;
        this.consultarProductoView = consultarProductoView;

        // Registrar eventos para las pestañas en CrearProductoView
        configurarEventos(crearProductoView.getjToggleButtonRegistrar(),
                crearProductoView.getjToggleButtonConsultar(),
                crearProductoView);

        // Registrar eventos para las pestañas en ConsultarProductoView
        configurarEventos(consultarProductoView.getjToggleButtonRegistrar(),
                consultarProductoView.getjToggleButtonConsultar(),
                consultarProductoView);
    }

    private void configurarEventos(JToggleButton jToggleButtonRegistrar, JToggleButton jToggleButtonConsultar,
                 JFrame currentFrame){
            
            // Evento para "Registrar Producto"
            jToggleButtonRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearProductoView.setVisible(true);
                currentFrame.dispose();
            }
        });

        // Evento para "Consultar Producto"
        jToggleButtonConsultar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarProductoView.setVisible(true);
                currentFrame.dispose();
            }
        });
    }

}
