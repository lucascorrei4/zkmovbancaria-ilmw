package zkAlfa;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.DataBinder;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Textbox;

import br.com.financas.impl.MovimentacaoDAOImpl;
import br.com.financas.interfaces.MovimentacaoInterface;
import br.com.financas.modelo.Conta;
import br.com.financas.modelo.Movimentacao;
import br.com.financas.modelo.TipoMovimentacao;

public class CMovimentacaoManutencao extends GenericForwardComposer {

    private DataBinder binder;

    private Intbox intbxId;
    private Selectbox selectbxConta;
    private Textbox textbxData;
    private Textbox textbxDescricao;
    private Selectbox textbxTipoMovimentacao;
    private Intbox intbxValor;

    private Movimentacao movimentacao = new Movimentacao();

    private Integer id;
    private BigDecimal valor;
    private TipoMovimentacao tipoMovimentacao;
    private String data;
    private String descricao;
    private String conta;

    @Wire
    private Combobox contaCombobox;

    @SuppressWarnings("rawtypes")
    private ListModelList typesModel = new ListModelList(
	    TipoMovimentacao.values());

    private ListModelList typesConta = null;

    public List<String> contas() throws RemoteException {
	List<String> contas = new ArrayList<String>();
	for (Conta conta : listaContas()) {
	    contas.add(conta.getNumero());
	}
	return contas;
    }

    public ListModelList getTypesConta() throws RemoteException {
	if (this.typesConta == null) {
	    this.typesConta = new ListModelList(contas());
	}
	return typesConta;
    }

    public void setTypesConta(ListModelList typesConta) {
	this.typesConta = typesConta;
    }

    public ListModelList getTypesModel() {
	return typesModel;
    }

    public void setTypesModel(ListModelList typesModel) {
	this.typesModel = typesModel;
    }

    private TipoMovimentacao[] tipoMovimentacaoTipos;

    public TipoMovimentacao[] getTipoMovimentacaoTipos() {
	setTipoMovimentacaoTipos(TipoMovimentacao.values());
	return tipoMovimentacaoTipos;
    }

    public void setTipoMovimentacaoTipos(
	    TipoMovimentacao[] tipoMovimentacaoTipos) {
	this.tipoMovimentacaoTipos = tipoMovimentacaoTipos;
    }

    public String getConta() {
	return conta;
    }

    public String getData() {
	return data;
    }

    public void setData(String data) {
	this.data = data;
    }

    public void setConta(String conta) {
	this.conta = conta;
    }

    public void doAfterCompose(Component comp) throws Exception {

	// Recupenrando Objeto da Sessao

	Session session = Sessions.getCurrent();
	movimentacao = (Movimentacao) session.getAttribute("MOVIMENTACAO");
	setId(movimentacao.getId());
	setValor(movimentacao.getValor());
	setTipoMovimentacao(TipoMovimentacao.ENTRADA);
	setDescricao(movimentacao.getDescricao());

	Calendar datamovimentacao = movimentacao.getData();

	Date dataLocal;

	if (datamovimentacao != null) {
	    dataLocal = new Date(movimentacao.getData().getTimeInMillis());

	} else {
	    Calendar calendar = Calendar.getInstance();

	    dataLocal = new Date(calendar.getTimeInMillis());
	}
	DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
	String sData = fmt.format(dataLocal);

	setData(sData);

	Conta conta = movimentacao.getConta();

	super.doAfterCompose(comp);
	this.binder = new AnnotateDataBinder(comp);
	this.binder.loadAll();

	if (conta != null) {
	    setConta(movimentacao.getConta().getNumero());
	}
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public BigDecimal getValor() {
	return valor;
    }

    public void setValor(BigDecimal valor) {
	this.valor = valor;
    }

    public TipoMovimentacao getTipoMovimentacao() {
	return tipoMovimentacao;
    }

    public void setTipoMovimentacao(TipoMovimentacao tipoMovimentacao) {
	this.tipoMovimentacao = tipoMovimentacao;
    }

    public String getDescricao() {
	return descricao;
    }

    public void setDescricao(String descricao) {
	this.descricao = descricao;
    }

    public void onClick$btnCancelar() throws RemoteException {
	System.out.println("Deve implementar o fechar tela!");
    }

    public Combobox getContaCombobox() {
	return contaCombobox;
    }

    public void setContaCombobox(Combobox contaCombobox) {
	this.contaCombobox = contaCombobox;
    }

    public List<Conta> listaContas() throws RemoteException {
	MovimentacaoInterface g = new MovimentacaoDAOImpl();

	return g.lista_Conta();

    }

    public void onClick$btnGravar() throws RemoteException {
	if (!validarCampos(textbxData.getText(), textbxDescricao.getText(),
		intbxValor.getText())) {

	    Messagebox.show("Favor, preencha todos os campos!");
	    return;
	}

	MovimentacaoInterface g = new MovimentacaoDAOImpl();

	if (intbxId.getText() != "") {
	    movimentacao.setId(Integer.parseInt(intbxId.getText()));
	}

	Conta conta = g.obter_ContaNumero(contaCombobox.getValue());
	movimentacao.setConta(conta);
	movimentacao.setData(Calendar.getInstance());
	movimentacao.setDescricao(textbxDescricao.getText());

	movimentacao.setTipoMovimentacao(TipoMovimentacao.ENTRADA);
	movimentacao.setValor(new BigDecimal(intbxValor.getText()));

	g.gravar_Movimentacao(movimentacao);
	Executions.getCurrent().sendRedirect("MovimentacaoGerenciar.zul");
	
    }

    private boolean validarCampos(String text, String text2, String text3) {
	if ((text == null || text == "") || (text2 == null || text2 == "")
		|| (text3 == null || text3 == "")) {
	    return false;
	}
	return true;
    }

}
