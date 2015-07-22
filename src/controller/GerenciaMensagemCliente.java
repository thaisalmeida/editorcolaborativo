package controller;

import Model.Cliente;
import Model.Servidor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Thais
 */
public class GerenciaMensagemCliente implements Runnable {
 
   private Cliente cliente;
   private Cliente clienteVencedor;
   private Servidor servidor;
   private final int FAZER_LOGIN = 11;
   private final int SUBMETER_LOTE = 21;
   private final int ENVIAR_LANCE = 31;
   private final int ENVIAR_DADOS = 52;
   private final int SOLICITA_USUARIOS = 61;
   private final int ECHO = 71;

   private final int FINALIZA_CONEXAO = 14;

   String labelSubmissao = "";
   String itemsFormatados = "";
   String items = "";
   String mensagem = "";
   float lanceMax = 0.0f;
   float lanceMinimo = 0.0f;
   
   public GerenciaMensagemCliente(Servidor servidor,Cliente cliente) {
     this.cliente = cliente;
     this.servidor = servidor;
   }
 
   public String[] segmentaFAZER_LOGIN(String login, String senha){
       String palavrasSegmentadas[] = new String[2];
       palavrasSegmentadas[0] = login;
       palavrasSegmentadas[1] = senha;       
       return palavrasSegmentadas;
   }
   
   @Override
   public void run() {
	   
    Scanner s;
    try {
        s = new Scanner(this.cliente.getClienteSocket().getInputStream());
              
        while (s.hasNext()) {
             
            System.out.println(s.next());
       }
        
     } catch (IOException ex) {
        Logger.getLogger(GerenciaMensagemCliente.class.getName()).log(Level.SEVERE, null, ex);
     }  
   }
   public void enviaRespostaestouVivo(){
        try {
            PrintWriter out = new PrintWriter(this.cliente.getClienteSocket().getOutputStream(), true);           
            out.print("72\0");
            out.flush();

        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
   /*
    public void atualizaTela(int idMensagem){
        String atualizaCampoMsg = servidor.tela.txtMensagensRecebidas.getText()+"\n";
        
        switch(idMensagem){
            case FAZER_LOGIN:
                servidor.tela.txtMensagensRecebidas.setText("");
                servidor.tela.txtMensagensRecebidas.setText(atualizaCampoMsg+mensagem);
                break;
            case SUBMETER_LOTE:
                servidor.tela.txtMensagensRecebidas.setText(atualizaCampoMsg+mensagem);
                if(servidor.leilaoIniciado){
                    labelSubmissao =  servidor.tela.labelSubmetidoPor.getText();
                    servidor.tela.labelSubmetidoPor.setText(labelSubmissao+cliente.getClienteSocket().getInetAddress().getHostAddress());
                    servidor.tela.txtItens.setText(items);
                    servidor.tela.txtLanceMin.setText(String.valueOf(lanceMinimo));
                    servidor.tela.txtLanceMax.setText(String.valueOf(lanceMax));
                    servidor.tela.txtIncentivo.setEnabled(true);
                    servidor.tela.btnFinalizar.setEnabled(true);
                    servidor.tela.btnIncentivo.setEnabled(true);
                }
                break;
            case ENVIAR_LANCE:
                servidor.tela.txtMensagensRecebidas.setText(atualizaCampoMsg+mensagem);
                break;
            case ENVIAR_DADOS:
                enviaRespostaTransacaoConcluida();
                JOptionPane.showMessageDialog(null,"Transação concluída com sucesso!");
                break;    
            case FINALIZA_CONEXAO:
                String atualizaCampoUserLogados = servidor.tela.txtUsuariosLogados.getText()+"\n";
                servidor.tela.txtUsuariosLogados.setText(atualizaCampoUserLogados+"\n"+
                        cliente.getClienteSocket().getInetAddress().getHostAddress()+"(desconectado)");
                servidor.tela.txtMensagensRecebidas.setText(atualizaCampoMsg+mensagem);
                break;             
       case SOLICITA_USUARIOS:
                servidor.tela.txtMensagensRecebidas.setText(atualizaCampoMsg+mensagem);
                break;                
            default:
               break;    
        }

        
    }

    
    public void enviaRespostaSolicitacaoUsuarios(){
        //"62\n{quant}\n{nome}\t{IP}\0"
     try {
        String mensagem = "62";
        String quantidade = String.valueOf(this.servidor.getListaClientes().size());
        String nomeIP = "";
        for (Cliente obj : this.servidor.getListaClientes()) {
            nomeIP += obj.getLogin()+"\t"+obj.getClienteSocket().getInetAddress().getHostAddress()+"\n";
        }
        nomeIP = nomeIP.substring(0,nomeIP.length()-1);
        mensagem = mensagem +"\n" +quantidade+"\n"+nomeIP+"\0";
 
            PrintWriter out = new PrintWriter(this.cliente.getClienteSocket().getOutputStream(), true);
            out.print(mensagem);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void enviaRespostaLance(String lance,int mensagem){
        try {
            
            float valor = Float.parseFloat(lance);
            PrintWriter out = new PrintWriter(this.cliente.getClienteSocket().getOutputStream(), true);
            if(valor > lanceMinimo && valor > lanceMax){
                out.print("32\0");
                out.flush();
                enviaParaTodos("34\n"+valor+"\n"+cliente.getLogin()+"\0");
                lanceMax = valor;
                this.cliente.setLance(valor);
                servidor.tela.txtLanceMax.setText(String.valueOf(lanceMax));
            }else{
                out.println("33\0");
                out.flush();
            }
            atualizaTela(mensagem);

        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
    }
    
    public void validaLogin(String login,String senha){
        boolean existe = false;
        String atualiza = this.servidor.tela.txtUsuariosLogados.getText()+"\n";
        try {
            FileReader fileReader = new FileReader("src/arquivos/loginsenha.txt");
            BufferedReader buffer = new BufferedReader(fileReader);

            while(buffer.ready()){
                String linha = buffer.readLine();
                String linhaSemChaves = linha.substring(1, linha.length()-1);
                String linhaSegmentada[] = linhaSemChaves.split(":");
                if( (login.equals(linhaSegmentada[0])) && (senha.equals(linhaSegmentada[1])) && !servidor.leilaoIniciado){
                        existe = true;
                        this.cliente.setLogin(login);
                        this.cliente.setSenha(senha);
                        this.cliente.setStatus(Cliente.COMPRADOR);
                        this.servidor.getListaClientes().add(cliente);
                        this.servidor.tela.txtUsuariosLogados.setText(atualiza+this.cliente.getClienteSocket().getInetAddress().getHostAddress());
                        break;
                }
            }            
        } catch (IOException e) {
                System.out.println(e.getMessage());
                existe = false;
        }
        enviaRespostaLogin(existe);
    }    
    
    public void reiniciarTelaServidor(){
        servidor.tela.txtMensagensRecebidas.setText("Aguardando Mensagens");
        servidor.tela.txtItens.setText("Aguardando Submissão");
        servidor.tela.txtUsuariosLogados.setText("Aguardando conexões");
        servidor.tela.labelSubmetidoPor.setText("Leilão submetido por: ");
        servidor.tela.txtLanceMax.setText("");
        servidor.tela.txtLanceMin.setText("");
        servidor.tela.txtIncentivo.setEnabled(false);
        servidor.tela.btnFinalizar.setEnabled(false);    
        servidor.tela.btnIncentivo.setEnabled(false);    

        servidor.leilaoIniciado = false;
        items = "";
        itemsFormatados="";
        servidor.setListaClientes(new ArrayList<Cliente>());
    }
    
    
    public void enviaRespostaLogin(boolean valida){      
        try {
            PrintWriter out = new PrintWriter(this.cliente.getClienteSocket().getOutputStream(), true);           
            if(valida)
                out.print("12\0");
            else
                out.print("13\0");
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void enviaRespostaTransacaoConcluida(){
        try {
            PrintWriter out = new PrintWriter(clienteVencedor.getClienteSocket().getOutputStream(), true);
            out.print("53\0");
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void enviaRespostaSubmisaoLote(String lanceMin,String quantidade,String items,int mensagem){
        lanceMinimo = Float.parseFloat(lanceMin);
        lanceMax = lanceMinimo;
        String formatado = items.substring(0,items.length()-1) +"\0" ;//POSSIVEL ERRO \0
        try {
            PrintWriter out = new PrintWriter(this.cliente.getClienteSocket().getOutputStream(), true);
            if(!servidor.leilaoIniciado){
                servidor.leilaoIniciado = true;
                atualizaTela(mensagem);
                this.cliente.setStatus(Cliente.LEILOEIRO);
                out.print("22\0");
                out.flush();
                enviaParaTodos("24\n"+lanceMin+"\n"+quantidade+"\n"+formatado);            
                System.out.println("enviei");
            }else{
                out.print("23\0");
            }    
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    public void enviaParaTodos(String msg) {
        try {
            PrintStream saida;
            for (Cliente clienteAtual : this.servidor.getListaClientes()) {
                saida = new PrintStream(clienteAtual.getClienteSocket().getOutputStream());
                saida.print(msg);
                saida.flush();
           }
        } catch (IOException ex) {
           Logger.getLogger(GerenciaMensagemCliente.class.getName()).log(Level.SEVERE, null, ex);
       }
     }
    
    public Cliente identificaVencedor(){
         Cliente vencedor = null;
         if(servidor.getListaClientes()!= null && !servidor.getListaClientes().isEmpty()){
            vencedor = servidor.getListaClientes().get(0);
            for (Cliente obj : servidor.getListaClientes()) {
                if(obj.getLance()>vencedor.getLance()){
                    vencedor = obj;
                }
            }
         }
         return vencedor;
     }
    
    public void desconectaClientes(){
       try {
            if(servidor.getListaClientes()!= null){
               for (Cliente obj : servidor.getListaClientes()) {
                    obj.getClienteSocket().close();
               }
             }
            servidor.setListaClientes(null);
        } catch (IOException ex) {
            Logger.getLogger(GerenciaMensagemCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void enviaRespostaLeilaoFinalizado(){
        
        try {
            clienteVencedor = identificaVencedor();
            
            if(clienteVencedor!=null && clienteVencedor.getStatus()!= Cliente.LEILOEIRO){
                enviaParaTodos("41\n"+clienteVencedor.getLogin()+"\n"+clienteVencedor.getLance()+"\0");             
                PrintWriter out = new PrintWriter(clienteVencedor.getClienteSocket().getOutputStream(), true);
                out.print("51\0");
                out.flush();
            }else{
                enviaParaTodos("42\0");             
            }
            servidor.getListaClientes().remove(clienteVencedor);
            desconectaClientes();
            reiniciarTelaServidor();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   */
   //getters and setters
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }    
    
 }
