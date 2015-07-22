package controller;

import Model.Cliente;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Thais
 */
public class GerenciaMensagemServidor implements Runnable {

    private InputStream servidor = null;
    private Cliente cliente = null;
    private Socket clienteSocket = null;

    private String mesagemServidor = "";
    String mensagem = "";
    Thread telas;
    String itemsFormatados = "";
    String items = "";
    Float lanceMax = 0.0f;
    Float lanceMinimo = 0.0f;

    public GerenciaMensagemServidor(InputStream servidor,Cliente cliente,Socket clientSocket) {
            this.servidor = servidor;
            this.cliente = cliente;       
            this.clienteSocket = clientSocket;
    }

    public void run() {
        Scanner s = new Scanner(this.servidor);
        int idMensagem ;
     
        
        while (s.hasNext()) {

        }
    }
       
	/*getters and setters*/
	public InputStream getServidor() {
		return servidor;
	}

	public void setServidor(InputStream servidor) {
		this.servidor = servidor;
	}
        
	public String getMesagemServidor() {
		return mesagemServidor;
	}

	public void setMesagemServidor(String mesagemServidor) {
		this.mesagemServidor = mesagemServidor;
	}
        
        public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Socket getClienteSocket() {
		return clienteSocket;
	}

	public void setClienteSocket(Socket clienteSocket) {
		this.clienteSocket = clienteSocket;
	}

}
