package br.com.financas.teste;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.financas.impl.MovimentacaoDAOImpl;
import br.com.financas.interfaces.MovimentacaoInterface;
import br.com.financas.modelo.Conta;
import br.com.financas.modelo.Movimentacao;
import br.com.financas.modelo.TipoMovimentacao;
import br.com.financas.util.JPAUtil;

public class TesteJPA {
	public static void main(String[] args) throws RemoteException{
	
		
		EntityManager em = new JPAUtil().getEntityManager();
		
		MovimentacaoInterface g = new MovimentacaoDAOImpl();
		
		for (Conta conta : g.lista_Conta()){
				
			System.out.println(conta.getId());
			System.out.println(conta.getBanco());
			System.out.println(conta.getAgencia());
			System.out.println(conta.getNumero());
			System.out.println(conta.getTitular());
			System.out.println(conta.getEndereco());				
							
		}
		
		
//		List<Conta> resultado = new ArrayList<Conta>();
		
		//resultado = em.createQuery("select * from conta").getResultList();
		
//        System.out.println(resultado.toString());				
		
		
		Conta oConta = new Conta();
		
		
		Conta oConta2;
		oConta.setId(1234);
		oConta.setAgencia("1234");
		
		Movimentacao oMovimentacao = new Movimentacao();
		oMovimentacao.setData(Calendar.getInstance());
		oMovimentacao.setDescricao("pagamento bol");
		oMovimentacao.setTipoMovimentacao(TipoMovimentacao.SAIDA);
		oMovimentacao.setValor(new BigDecimal("110.20"));
		
		em.getTransaction().begin();
		oConta2 = em.merge(oConta);
		oMovimentacao.setConta(oConta2);
		
		em.persist(oMovimentacao);		
		em.getTransaction().commit();
		em.close();
	}

}
