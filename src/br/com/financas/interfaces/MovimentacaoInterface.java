package br.com.financas.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import br.com.financas.modelo.Conta;
import br.com.financas.modelo.Movimentacao;

public interface MovimentacaoInterface extends Remote {
	Conta gravar_Conta(Conta conta) throws RemoteException;
	Conta obter_Conta(Integer idConta) throws RemoteException;
	Conta obter_ContaNumero(String numero) throws RemoteException;	
	List<Conta> lista_Conta() throws RemoteException;
	void apagar_Conta(Conta conta) throws RemoteException;	
	
	Movimentacao obter_Movimentacao(Integer idMovimentacao) throws RemoteException;	
	void gravar_Movimentacao(Movimentacao movimentacao) throws RemoteException;
	List<Movimentacao> lista_Movimentacao() throws RemoteException;
	void apagar_Movimentacao(Movimentacao movimentacao) throws RemoteException;	
	
}
