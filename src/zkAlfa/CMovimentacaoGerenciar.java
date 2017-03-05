package zkAlfa;

import java.rmi.RemoteException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.DataBinder;
import org.zkoss.zul.Listbox;

import br.com.financas.impl.MovimentacaoDAOImpl;
import br.com.financas.interfaces.MovimentacaoInterface;
import br.com.financas.modelo.Conta;
import br.com.financas.modelo.Movimentacao;

public class CMovimentacaoGerenciar extends GenericForwardComposer {

	private ArrayList<LinkedHashMap<String, Object>> listaMovimentacao = new ArrayList<LinkedHashMap<String, Object>>();

	private DataBinder binder;
	private Listbox lstbxmovimentacao;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		this.binder = new AnnotateDataBinder(comp);
		this.binder.loadAll();

	}

	public ArrayList<LinkedHashMap<String, Object>> getListaMovimentacao() {
		return listaMovimentacao;
	}

	public void setListaMovimentacao(
			ArrayList<LinkedHashMap<String, Object>> listaMovimentacao) {
		this.listaMovimentacao = listaMovimentacao;
	}

	public void onClick$btnAlterar() throws RemoteException {
		int index = this.lstbxmovimentacao.getSelectedIndex();

		if (index >= 0) {

			int conta_id = (int) listaMovimentacao.get(index).get("id");

			MovimentacaoInterface g = new MovimentacaoDAOImpl();

			Movimentacao movimentacao = g.obter_Movimentacao(conta_id);

			if (movimentacao != null) {
				Executions.getCurrent().getDesktop().getSession()
						.setAttribute("MOVIMENTACAO", movimentacao);

				Executions.getCurrent().sendRedirect(
						"MovimentacaoManutencao.zul");
			}
		}
	}

	public void onClick$btnCriar() throws RemoteException {
		Movimentacao movimentacao = new Movimentacao();
		Executions.getCurrent().getDesktop().getSession()
				.setAttribute("MOVIMENTACAO", movimentacao);

		Executions.getCurrent().sendRedirect("MovimentacaoManutencao.zul");
	}

	public void onClick$btnApagar() throws RemoteException {

		// Obter o index do objeto para apagar
		int index = this.lstbxmovimentacao.getSelectedIndex();

		if (index >= 0) {
			int idMovimentacao = (int) listaMovimentacao.get(index).get("id");

			MovimentacaoInterface g = new MovimentacaoDAOImpl();

			Movimentacao movimentacao = g.obter_Movimentacao(idMovimentacao);

			if (movimentacao != null) {
				g.apagar_Movimentacao(movimentacao);
			}
			// Atualizar tela apos apagar
			this.onClick$btnLista();
		}
	}

	public void onClick$btnLista() throws RemoteException {
		listaMovimentacao.clear();
		MovimentacaoInterface g = new MovimentacaoDAOImpl();

		for (Movimentacao movimentacao : g.lista_Movimentacao()) {
			LinkedHashMap<String, Object> hash = new LinkedHashMap<String, Object>();
			hash.put("id", movimentacao.getId());

			hash.put("conta", movimentacao.getConta().getId());
			hash.put("descricao", movimentacao.getDescricao());
			hash.put("tipoMovimentacao", movimentacao.getTipoMovimentacao());
			hash.put("valor", movimentacao.getValor());

			DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
			String sData = fmt.format(new Date(movimentacao.getData().getTimeInMillis()));

			hash.put("data", sData);

			listaMovimentacao.add(hash);
		}

		this.binder.loadComponent(this.lstbxmovimentacao);
	}
}
