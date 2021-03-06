package Model;

import com.sun.corba.se.impl.orbutil.ObjectWriter;
import controller.GerenciaMensagemServidor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {
	
    private String nome;
    private String login;
    private String senha;
    private String enderecoIP = "127.0.0.1";//MUDAR
    private String enderecoIPServidor = "10.4.6.39";
    private int status; // 0 :vendedor | 1 : comprador
    private String nroCartao;
    private String codigoSeguranca;
    private String mesVencimento;
    private String anoVencimento;	
    private Socket clienteSocket = null;
    private Socket clienteParaClienteSocket = null;
    private ServerSocket clienteServidorSocket = null;
    private ArrayList<Cliente> listaClientesLogados = null;
    
    
    /*telas clientSide*/
    //Login telaLogin = new Login();
             
    GerenciaMensagemServidor gerenciaMensagemServer = null;

    public Cliente(){
            this.listaClientesLogados = new ArrayList<Cliente>();
    }

	
    public void enviaMensagemCliente(String mensagem, int posicao, Socket destinatario){      
        try {
            PrintWriter out = new PrintWriter(destinatario.getOutputStream(), true);
            out.print("{" + mensagem + "," + posicao + "}");
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	
    public void estabelecerConexao(){
       
        try {
            this.clienteSocket  = new Socket(this.getEnderecoIPServidor(),8000);
            //telaLogin.setVisible(true);
            //telaLogin.entrarButton.addActionListener(this);
            
            gerenciaMensagemServer = new GerenciaMensagemServidor(this.clienteSocket.getInputStream(),this,clienteSocket);
                        
            new Thread(gerenciaMensagemServer).start();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }  
        
    }
   
   
     public Socket getClienteSocket() {
		return clienteSocket;
	}


	public void setClienteSocket(Socket clienteSocket) {
		this.clienteSocket = clienteSocket;
	}
   
	/*getters and setters*/
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getEnderecoIP() {
		return enderecoIP;
	}
	public void setEnderecoIP(String enderecoIP) {
		this.enderecoIP = enderecoIP;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getNroCartao() {
		return nroCartao;
	}
	public String getEnderecoIPServidor() {
		return enderecoIPServidor;
	}

	public void setEnderecoIPServidor(String enderecoIPServidor) {
		this.enderecoIPServidor = enderecoIPServidor;
	}

	public void setNroCartao(String nroCartao) {
		this.nroCartao = nroCartao;
	}
	public String getCodigoSeguranca() {
		return codigoSeguranca;
	}
	public void setCodigoSeguranca(String codigoSeguranca) {
		this.codigoSeguranca = codigoSeguranca;
	}
	public String getMesVencimento() {
		return mesVencimento;
	}
	public void setMesVencimento(String mesVencimento) {
		this.mesVencimento = mesVencimento;
	}
	public ArrayList<Cliente> getListaClientesLogados() {
		return listaClientesLogados;
	}

	public void setListaClientesLogados(ArrayList<Cliente> listaClientesLogados) {
		this.listaClientesLogados = listaClientesLogados;
	}

	public String getAnoVencimento() {
		return anoVencimento;
	}
	public void setAnoVencimento(String anoVencimento) {
		this.anoVencimento = anoVencimento;
	}
	public Socket getClienteParaClienteSocket() {
		return clienteParaClienteSocket;
	}

	public void setClienteParaClienteSocket(Socket clienteParaClienteSocket) {
		this.clienteParaClienteSocket = clienteParaClienteSocket;
	}

	public ServerSocket getClienteServidorSocket() {
		return clienteServidorSocket;
	}

	public void setClienteServidorSocket(ServerSocket clienteServidorSocket) {
		this.clienteServidorSocket = clienteServidorSocket;
	}       
}
