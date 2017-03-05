package zkAlfa;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.DataBinder;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;

import br.com.financas.impl.MovimentacaoDAOImpl;
import br.com.financas.interfaces.MovimentacaoInterface;
import br.com.financas.modelo.Conta;
import bsh.This;

public class CContaGerenciar extends GenericForwardComposer {

	private ArrayList<LinkedHashMap<String, Object>> listaconta = new ArrayList<LinkedHashMap<String, Object>>();

	private DataBinder binder;
	private Listbox lstbxconta;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		this.binder = new AnnotateDataBinder(comp);
		this.binder.loadAll();

	}

	public ArrayList<LinkedHashMap<String, Object>> getListaconta() {
		return listaconta;
	}

	public void setListaconta(
			ArrayList<LinkedHashMap<String, Object>> listaconta) {
		this.listaconta = listaconta;
	}

	public void onClick$btnAlterar() throws RemoteException {
		int index = this.lstbxconta.getSelectedIndex();

		if (index >= 0) {

			int conta_id = (int) listaconta.get(index).get("id");

			MovimentacaoInterface g = new MovimentacaoDAOImpl();

			Conta conta = g.obter_Conta(conta_id);

			if (conta != null) {
				Executions.getCurrent().getDesktop().getSession().setAttribute("CONTA", conta);
				
				Executions.getCurrent().sendRedirect("ContaManutencao.zul");
			}
		}
	}

	public void onClick$btnCriar() throws RemoteException {
		
		Conta conta = new Conta();
		Executions.getCurrent().getDesktop().getSession().setAttribute("CONTA", conta);
		
		Executions.getCurrent().sendRedirect("ContaManutencao.zul");
	}

	public void onClick$btnApagar() throws RemoteException {

		// Obter o index do objeto para apagar
		int index = this.lstbxconta.getSelectedIndex();

		int contaApagar = (int) listaconta.get(index).get("id");

		MovimentacaoInterface g = new MovimentacaoDAOImpl();

		Conta conta = g.obter_Conta(contaApagar);

		if (conta != null) {
			g.apagar_Conta(conta);
		}
		// Atualizar tela apos apagar
		this.onClick$btnLista();
	}

	public void onClick$btnLista() throws RemoteException {
		listaconta.clear();
		MovimentacaoInterface g = new MovimentacaoDAOImpl();

		for (Conta conta : g.lista_Conta()) {
			LinkedHashMap<String, Object> hash = new LinkedHashMap<String, Object>();
			hash.put("id", conta.getId());
			hash.put("banco", conta.getBanco());
			hash.put("agencia", conta.getAgencia());
			hash.put("numero", conta.getNumero());
			hash.put("titular", conta.getTitular());
			hash.put("endereco", conta.getEndereco());

			listaconta.add(hash);
		}

		this.binder.loadComponent(this.lstbxconta);
	}
}
