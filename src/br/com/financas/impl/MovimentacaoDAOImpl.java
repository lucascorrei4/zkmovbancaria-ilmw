package br.com.financas.impl;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.financas.interfaces.MovimentacaoInterface;
import br.com.financas.modelo.Conta;
import br.com.financas.modelo.Movimentacao;
import br.com.financas.util.JPAUtil;

public class MovimentacaoDAOImpl extends UnicastRemoteObject implements
		MovimentacaoInterface, Serializable {

	private static final long serialVersionUID = 1L;

	public MovimentacaoDAOImpl() throws RemoteException {
		super();
	}

	public static void main(String args[]) {
		try {
			MovimentacaoDAOImpl obj = new MovimentacaoDAOImpl();
			Registry registry = LocateRegistry.createRegistry(2001);
			registry.rebind("MovimentacaoBancariaBD", obj);
			System.out.println("Servidor carregado no registry");
		} catch (Exception e) {
			System.out.println("OlaImpl erro: " + e.getMessage());
		}
	}

	@Override
	public Conta gravar_Conta(Conta conta) throws RemoteException {
		EntityManager em = new JPAUtil().getEntityManager();
		em.getTransaction().begin();
		Conta contaRetorno = em.merge(conta);
		em.getTransaction().commit();
		em.close();
		return contaRetorno;
	}

	public void apagar_Conta(Conta conta) throws RemoteException {
		EntityManager em = new JPAUtil().getEntityManager();

		Conta contaApagar = em.find(Conta.class, conta.getId());

		if (contaApagar != null) {
			em.getTransaction().begin();

			// Apagando movimentações para depois apagar conta
			for (Movimentacao movimentacao : lista_Movimentacao(contaApagar)) {
				apagar_Movimentacao(movimentacao);
			}

			em.remove(contaApagar);
			em.getTransaction().commit();
		}

		em.close();
	}

	@Override
	public void gravar_Movimentacao(Movimentacao movimentacao)
			throws RemoteException {
		EntityManager em = new JPAUtil().getEntityManager();
		em.getTransaction().begin();
		em.merge(movimentacao);
		em.getTransaction().commit();
		em.close();
	}

	@Override
	public Conta obter_Conta(Integer idConta) throws RemoteException {
		EntityManager em = new JPAUtil().getEntityManager();

		Conta results = em.find(Conta.class, idConta);

		em.close();

		return results;
	}

	public List<Conta> lista_Conta() throws RemoteException {

		EntityManager em = new JPAUtil().getEntityManager();

		TypedQuery<Conta> query = em.createQuery("select c from Conta c",
				Conta.class);
		List<Conta> results = query.getResultList();

		em.close();

		return results;
	}

	public List<Movimentacao> lista_Movimentacao() throws RemoteException {

		EntityManager em = new JPAUtil().getEntityManager();

		TypedQuery<Movimentacao> query = em.createQuery(
				"select m from Movimentacao m", Movimentacao.class);
		List<Movimentacao> results = query.getResultList();

		em.close();

		return results;
	}

	public List<Movimentacao> lista_Movimentacao(Conta conta)
			throws RemoteException {

		EntityManager em = new JPAUtil().getEntityManager();

		TypedQuery<Movimentacao> query = em.createQuery(
				"select m from Movimentacao m where conta = :conta",
				Movimentacao.class).setParameter("conta", conta);

		List<Movimentacao> results = query.getResultList();

		em.close();

		return results;
	}

	public Movimentacao obter_Movimentacao(Integer idMovimentacao)
			throws RemoteException {
		EntityManager em = new JPAUtil().getEntityManager();

		Movimentacao results = em.find(Movimentacao.class, idMovimentacao);

		em.close();

		return results;
	}

	public void apagar_Movimentacao(Movimentacao movimentacao)
			throws RemoteException {
		EntityManager em = new JPAUtil().getEntityManager();

		Movimentacao movimentacaoApagar = em.find(Movimentacao.class,
				movimentacao.getId());

		if (movimentacaoApagar != null) {
			em.getTransaction().begin();
			em.remove(movimentacaoApagar);
			em.getTransaction().commit();
		}
		em.close();

	}

	@Override
	public Conta obter_ContaNumero(String numero) throws RemoteException {

		EntityManager em = new JPAUtil().getEntityManager();

		TypedQuery<Conta> query = em.createQuery(
				"select m from Conta m where numero = :numero", Conta.class)
				.setParameter("numero", numero);

		Conta conta = query.getSingleResult();

		em.close();

		return conta;
	}

}
