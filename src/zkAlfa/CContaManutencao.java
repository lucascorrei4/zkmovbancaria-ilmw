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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import br.com.financas.impl.MovimentacaoDAOImpl;
import br.com.financas.interfaces.MovimentacaoInterface;
import br.com.financas.modelo.Conta;
import bsh.This;

public class CContaManutencao extends GenericForwardComposer {

    private DataBinder binder;
    private Intbox intbxAgencia;
    private Intbox intbxBanco;
    private Textbox intbxEndereco;
    private Intbox intbxId;
    private Intbox intbxNumero;
    private Textbox intbxTitular;
    private Conta conta = new Conta();

    private Integer id;
    private String titular;
    private String numero;
    private String banco;
    private String agencia;
    private String endereco;

    public Conta getConta() {
	return conta;
    }

    public void setConta(Conta conta) {
	this.conta = conta;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getTitular() {
	return titular;
    }

    public void setTitular(String titular) {
	this.titular = titular;
    }

    public String getNumero() {
	return numero;
    }

    public void setNumero(String numero) {
	this.numero = numero;
    }

    public String getBanco() {
	return banco;
    }

    public void setBanco(String banco) {
	this.banco = banco;
    }

    public String getAgencia() {
	return agencia;
    }

    public void setAgencia(String agencia) {
	this.agencia = agencia;
    }

    public String getEndereco() {
	return endereco;
    }

    public void setEndereco(String endereco) {
	this.endereco = endereco;
    }

    public void doAfterCompose(Component comp) throws Exception {

	// Recupenrando Objeto da Sessao
	Session session = Sessions.getCurrent();
	conta = (Conta) session.getAttribute("CONTA");
	setAgencia(conta.getAgencia());
	setBanco(conta.getBanco());
	setEndereco(conta.getEndereco());
	setId(conta.getId());
	setNumero(conta.getNumero());
	setTitular(conta.getTitular());

	super.doAfterCompose(comp);
	this.binder = new AnnotateDataBinder(comp);
	this.binder.loadAll();

    }

    public void onClick$btnCancelar() throws RemoteException {
	System.out.println("Deve implementar o fechar tela!");
    }

    public void onClick$btnGravar() throws RemoteException {
	if (!validarCampos(intbxBanco.getText(), intbxEndereco.getText(),
		intbxNumero.getText(), intbxTitular.getText(),
		intbxAgencia.getText())) {

	    Messagebox.show("Favor, preencha todos os campos!");
	    return;
	}
	if (intbxId.getText() != "") {
	    conta.setId(Integer.parseInt(intbxId.getText()));
	}

	conta.setBanco(intbxBanco.getText());
	conta.setEndereco(intbxEndereco.getText());
	conta.setNumero(intbxNumero.getText());
	conta.setTitular(intbxTitular.getText());
	conta.setAgencia(intbxAgencia.getText());

	MovimentacaoInterface g = new MovimentacaoDAOImpl();
	g.gravar_Conta(conta);
	Executions.getCurrent().sendRedirect("ContaGerenciar.zul");
    }

    private boolean validarCampos(String text, String text2, String text3,
	    String text4, String text5) {
	if ((text == null || text == "") || (text2 == null || text2 == "")
		|| (text3 == null || text3 == "")
		|| (text4 == null || text4 == "")
		|| (text5 == null || text5 == "")) {
	    return false;
	}
	return true;
    }
}
