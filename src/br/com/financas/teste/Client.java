package br.com.financas.teste;

import java.math.BigDecimal;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.financas.interfaces.MovimentacaoInterface;
import br.com.financas.modelo.Conta;
import br.com.financas.modelo.Movimentacao;
import br.com.financas.modelo.TipoMovimentacao;

public class Client {
	public static void main(String[] args) {
		Conta conta = new Conta();
		conta.setAgencia("1");
		conta.setBanco("2");
		conta.setNumero("3");
		conta.setTitular("Marcelo Narciso");
		

		MovimentacaoInterface movInterface = null;
		Conta contaRetorno = null;

		try {
			Registry registry = LocateRegistry.getRegistry("localhost", 2001);
			movInterface = (MovimentacaoInterface) registry.lookup("MovimentacaoBancariaBD");
			
	        List<Conta> listacontas = movInterface.lista_Conta();
	        System.out.println(listacontas.toString());			

			contaRetorno = movInterface.gravar_Conta(conta);

			Movimentacao movimentacao = new Movimentacao();
			movimentacao.setConta(contaRetorno);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			movimentacao.setData(calendar);
			movimentacao.setDescricao("Tranferindo pagamento Eike Batista");
			TipoMovimentacao tipoMovimentacao = TipoMovimentacao.ENTRADA;
			movimentacao.setTipoMovimentacao(tipoMovimentacao);
			movimentacao.setValor(BigDecimal.TEN);

			movInterface.gravar_Movimentacao(movimentacao);
			
			System.out.println(String.valueOf(contaRetorno.getId()).concat(contaRetorno.getTitular()));
		} catch (Exception e) {
			System.out.println("Client exception: " + e.getMessage());
		}
	}
}
