/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.datos.singleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import modelo.usuarios.Cliente;
import modelo.usuarios.DatosPersonales;
import modelo.usuarios.Empleado;
import modelo.usuarios.Persona;
import modelo.usuarios.Usuario;
import modelo.varios.Direccion;
import modelo.varios.Empresa;
import modelo.varios.Telefono;

/**
 *
 * @author davidmendozaloor
 */
public class Conexion {
    private static Conexion conexion;
    private Connection connection;
    
    private Conexion(){
        
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    
    
    public static  Conexion getConexion() throws SQLException{
        if (conexion==null){
            conexion=new Conexion();
            conexion.setConnection(DriverManager.getConnection("jdbc:mysql://127.0.0.1:3308/my_home", "root", "password"));
            return conexion;
        }
        else{
            return conexion;
        }
    }
    
    public List<Usuario> getUsuarios() throws SQLException{
        
        LinkedList<Usuario> usuarios=new LinkedList();
        Statement stm=connection.createStatement();
        ResultSet rs=stm.executeQuery("Select * from user");
        while(rs.next()){
            DatosPersonales datos=new DatosPersonales(rs.getString("nombre"), rs.getString("apellido"), rs.getString("cedula"), rs.getString("estadoCivil"));
            ArrayList<Telefono> telefonos=new ArrayList<>();
            telefonos.add(new Telefono(rs.getString("celular"), "celular"));
            telefonos.add(new Telefono(rs.getString("telefono"), "telefono"));
            Direccion direccion=new Direccion(rs.getString("domicilio"));
            Persona persona=new Persona(datos, direccion, telefonos, rs.getString("email")) ;
            Usuario user=new Usuario(persona, rs.getString("usuario"), rs.getString("contrasena"));
            usuarios.add(user);
        }
        return usuarios;
    }
     public List<Cliente> getClientes() throws SQLException{
        
        LinkedList<Cliente> clientes=new LinkedList();
        Statement stm=connection.createStatement();
        ResultSet rs=stm.executeQuery("select * from cliente join user on cliente.user_id = user.id_user");
        while(rs.next()){
            DatosPersonales datos=new DatosPersonales(rs.getString("nombre"), rs.getString("apellido"), rs.getString("cedula"), rs.getString("estadoCivil"));
            ArrayList<Telefono> telefonos=new ArrayList<>();
            telefonos.add(new Telefono(rs.getString("celular"), "celular"));
            telefonos.add(new Telefono(rs.getString("telefono"), "telefono"));
            Direccion direccion=new Direccion(rs.getString("domicilio"));
            Empresa empresa=new Empresa(rs.getString("empresa"));
            Cliente cliente=new Cliente(datos, direccion, telefonos, rs.getString("email"),empresa,rs.getString("cargo"),rs.getInt("numeroHijos")) ;
            clientes.add(cliente);
        }
        return clientes;
    }
     
     public List<Persona> getEmpleados() throws SQLException{
        
        LinkedList<Persona> empleados=new LinkedList();
        Statement stm=connection.createStatement();
        ResultSet rs=stm.executeQuery("select * from empleado join user on empleado.user_id = user.id_user");
        while(rs.next()){
            DatosPersonales datos=new DatosPersonales(rs.getString("nombre"), rs.getString("apellido"), rs.getString("cedula"), rs.getString("estadoCivil"));
            ArrayList<Telefono> telefonos=new ArrayList<>();
            telefonos.add(new Telefono(rs.getString("celular"), "celular"));
            telefonos.add(new Telefono(rs.getString("telefono"), "telefono"));
            Direccion direccion=new Direccion(rs.getString("domicilio"));
            Empleado empleado=new Empleado(datos, direccion, telefonos, rs.getString("email")) ;
            empleados.add(empleado);
        }
        return empleados;
    } 
    
     public void insertEmpleadoyUsuario(Empleado empleado,String usuario, String contrasena,String quiosco) throws SQLException{
         Statement stm=connection.createStatement();
         String celular = null;
         String telefono=null;
         for(Telefono t:empleado.getTelefonos()){
             if(t.getTipo().equals("celular")) celular=t.getNumero();
             if(t.getTipo().equals("telefono")) telefono=t.getNumero();
         }
         stm.executeUpdate("Insert into user(nombre,apellido,celular,cedula,pasaporte,email,domicilio,telefono,estadoCivil,cargo,usuario,contrasena) values('"+empleado.getDatos().getNombres()+"','"+empleado.getDatos().getApellidos()+"','"+celular+"','"+empleado.getDatos().getIdentificacion()+"','"+empleado.getDatos().getIdentificacion()+"','"+empleado.getEmail()+"','"+empleado.getDomicilio().getDireccion()+"','"+telefono+"','"+empleado.getDatos().getEstadoCivil()+"','empleado','"+usuario+"','"+contrasena+"')");
     }
     
    
    
    
    public static void main(String[] args) throws SQLException {
        Conexion con=Conexion.getConexion();
        DatosPersonales datos=new DatosPersonales("David","Mendoza", "0925496234", "Casado");
        ArrayList<Telefono> telefonos=new ArrayList<>();
            telefonos.add(new Telefono("094834343", "celular"));
            telefonos.add(new Telefono("948343443", "telefono"));
            Direccion direccion=new Direccion("Punta Esmeralda");
            Empleado empleado=new Empleado(datos, direccion, telefonos, "drdav@gmail.com") ;
        con.insertEmpleadoyUsuario(empleado, "davidm", "hola", "1");
    }
}
