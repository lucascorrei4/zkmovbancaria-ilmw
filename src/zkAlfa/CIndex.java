package zkAlfa;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.DataBinder;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;

import br.com.financas.impl.MovimentacaoDAOImpl;
import br.com.financas.interfaces.MovimentacaoInterface;
import br.com.financas.modelo.Conta;

public class CIndex extends GenericForwardComposer {
	private Integer valorSoma;
	private Integer valorOperandoMultiplicacao1;
	private Integer valorOperandoMultiplicacao2;
	private ArrayList<LinkedHashMap<String, Object>> historicoMultiplicacao = new ArrayList<LinkedHashMap<String, Object>>();

	private DataBinder binder;
	private Intbox intbxMultiplicacao;
	private Listbox lstbxHistorico;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		this.binder = new AnnotateDataBinder(comp);
		this.binder.loadAll();

	}

	public ArrayList<LinkedHashMap<String, Object>> getHistoricoMultiplicacao() {
		return historicoMultiplicacao;
	}

	public void setHistoricoMultiplicacao(ArrayList<LinkedHashMap<String, Object>> historicoMultiplicacao) {
		this.historicoMultiplicacao = historicoMultiplicacao;
	}

	public Integer getValorOperandoMultiplicacao1() {
		return valorOperandoMultiplicacao1;
	}

	public void setValorOperandoMultiplicacao1(Integer valorOperandoMultiplicacao1) {
		this.valorOperandoMultiplicacao1 = valorOperandoMultiplicacao1;
	}

	public Integer getValorOperandoMultiplicacao2() {
		return valorOperandoMultiplicacao2;
	}

	public void setValorOperandoMultiplicacao2(Integer valorOperandoMultiplicacao2) {
		this.valorOperandoMultiplicacao2 = valorOperandoMultiplicacao2;
	}

	public void onClick$btnMultiplicar() {
		LinkedHashMap<String, Object> hash = new LinkedHashMap<String,Object>();
		
		hash.put("op1", this.valorOperandoMultiplicacao1);
		hash.put("op2", this.valorOperandoMultiplicacao2);
		hash.put("result", this.valorOperandoMultiplicacao1*this.valorOperandoMultiplicacao2);

		this.historicoMultiplicacao.add(hash);
		this.binder.loadComponent(this.lstbxHistorico);
	}
	
	public void onClick$btnMovimentacao() throws RemoteException {
		Executions.getCurrent().sendRedirect("MovimentacaoGerenciar.zul");
		
	}		
	
	public void onClick$btnConta() throws RemoteException {
		Executions.getCurrent().sendRedirect("ContaGerenciar.zul");
		
	}	

	/*
	void submit() throws Exception {
		Customer cust = new Customer();
		cust.setName(customerName.getValue());
		java.util.Date utilDate = date.getValue();
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		cust.setDate(sqlDate);
		com.test.services.CustomerService custSvc = new com.test.services.CustomerService();
		custSvc.addCustomer(cust);
		Executions.getCurrent().sendRedirect("index.zul");
		addCustomerWin.detach();

	}	
*/
}
