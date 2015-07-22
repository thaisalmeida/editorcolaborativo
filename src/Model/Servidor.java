package Model;

import controller.GerenciaMensagemCliente;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import views.ServidorView;

/**
 *
 * @author Thais
 */

public class Servidor {

    private String enderecoIP;
    private int nroPorta = 8000;
    private Socket clienteSocket = null;
   // private ArrayList<Cliente> listaClientes = null;
    private ServerSocket servidorSocket = null;
    public boolean leilaoIniciado = false;
    public ServidorView tela = new ServidorView();   
    
    public Servidor(String enderecoIP, int nroPorta){
        this.enderecoIP = enderecoIP;
        this.nroPorta = nroPorta;
        tela.setVisible(true);
    } 
    
    public void estabeleceConexao () throws IOException {
    this.servidorSocket = new ServerSocket(this.nroPorta);

    while (true) {
      // aceita um cliente
      clienteSocket = servidorSocket.accept();

      // adiciona cliente à lista
      Cliente cliente = new Cliente();
      cliente.setClienteSocket(clienteSocket);          

      // cria tratador de cliente numa nova thread
      GerenciaMensagemCliente gerenciaMensagemCliente = new GerenciaMensagemCliente(this,cliente);
      new Thread(gerenciaMensagemCliente).start();
    }
} 
    
    
    /*getters and setters*/
            
    public String getEnderecoIP() {
        return enderecoIP;
    }

    public void setEnderecoIP(String enderecoIP) {
        this.enderecoIP = enderecoIP;
    }

    public int getNroPorta() {
        return nroPorta;
    }

    public void setNroPorta(int nroPorta) {
        this.nroPorta = nroPorta;
    }

    public Socket getClienteSocket() {
        return clienteSocket;
    }

    public void setClienteSocket(Socket clienteSocket) {
        this.clienteSocket = clienteSocket;
    }

    public ServerSocket getServidorSocket() {
        return servidorSocket;
    }

    public void setServidorSocket(ServerSocket servidorSocket) {
        this.servidorSocket = servidorSocket;
    }

    public boolean isLeilaoIniciado() {
        return leilaoIniciado;
    }

    public void setLeilaoIniciado(boolean leilaoIniciado) {
        this.leilaoIniciado = leilaoIniciado;
    }

    public ServidorView getTela() {
        return tela;
    }

    public void setTela(ServidorView tela) {
        this.tela = tela;
    }

}
