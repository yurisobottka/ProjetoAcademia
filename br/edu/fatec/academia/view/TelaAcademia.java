package br.edu.fatec.academia.view;

import br.edu.fatec.academia.exception.MatriculaBloqueadaException;
import br.edu.fatec.academia.interfaces.Avaliavel;
import br.edu.fatec.academia.model.AlunoAcademia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class TelaAcademia extends JFrame implements ActionListener {
    private final List<AlunoAcademia> listaAlunos = new ArrayList<>();
    private JTextField txtMatricula, txtNome, txtMensalidade, txtPeso, txtAltura;
    private JCheckBox chkAtivo;
    private JTextArea txtRelatorio;
    private JButton btnCadastrar, btnPesquisar, btnFrequencia, btnSuspender;
    private JButton btnListar, btnOrdenarMat, btnOrdenarVal, btnSair;

    public TelaAcademia() {
        super("Sistema de Gestão de Academia & Fichas de Treino FATEC");
        inicializarComponentes();
        configurarJanela();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        JPanel pnlForm = new JPanel(new GridLayout(6, 2, 8, 8));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Dados do Aluno"));

        pnlForm.add(new JLabel(" N° Matrícula:"));
        txtMatricula = new JTextField();
        pnlForm.add(txtMatricula);

        pnlForm.add(new JLabel(" Nome Completo:"));
        txtNome = new JTextField();
        pnlForm.add(txtNome);

        pnlForm.add(new JLabel(" Mensalidade (R$):"));
        txtMensalidade = new JTextField();
        pnlForm.add(txtMensalidade);

        pnlForm.add(new JLabel(" Peso (kg):"));
        txtPeso = new JTextField();
        pnlForm.add(txtPeso);

        pnlForm.add(new JLabel(" Altura (m):"));
        txtAltura = new JTextField();
        pnlForm.add(txtAltura);

        pnlForm.add(new JLabel(" Status da Matrícula:"));
        chkAtivo = new JCheckBox("Matrícula Ativa", true);
        pnlForm.add(chkAtivo);
        add(pnlForm, BorderLayout.NORTH);
        txtRelatorio = new JTextArea();
        txtRelatorio.setEditable(false);
        txtRelatorio.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(txtRelatorio);
        scroll.setBorder(BorderFactory.createTitledBorder("Relatório de Alunos e Fichas"));
        add(scroll, BorderLayout.CENTER);
        JPanel pnlBotoes = new JPanel(new GridLayout(2, 4, 6, 6));
        pnlBotoes.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));

        btnCadastrar = new JButton("Cadastrar");
        btnPesquisar = new JButton("Pesquisar");
        btnFrequencia = new JButton("Registrar Presença");
        btnSuspender = new JButton("Alternar Status");
        btnListar = new JButton("Listar Todos");
        btnOrdenarMat = new JButton("Ordenar (Matrícula)");
        btnOrdenarVal = new JButton("Ordenar (Mensalidade)");
        btnSair = new JButton("Sair");

        JButton[] botoes = {
                btnCadastrar, btnPesquisar, btnFrequencia, btnSuspender,
                btnListar, btnOrdenarMat, btnOrdenarVal, btnSair
        };

        for (JButton btn : botoes) {
            btn.addActionListener(this);
            pnlBotoes.add(btn);
        }

        add(pnlBotoes, BorderLayout.SOUTH);
    }

    private void configurarJanela() {
        setSize(780, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmarSaida();
            }
        });
    }

    private void confirmarSaida() {
        int opcao = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente encerrar o sistema? Os dados em memória serão perdidos.",
                "Confirmar Saída",
                JOptionPane.YES_NO_OPTION
        );
        if (opcao == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private AlunoAcademia buscarPorMatricula(int matricula) {
        for (AlunoAcademia a : listaAlunos) {
            if (a.getMatricula() == matricula) {
                return a;
            }
        }
        return null;
    }

    private void atualizarRelatorio(List<AlunoAcademia> lista) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-6s | %-20s | %-10s | %-6s | %-18s | %-5s | %-8s\n",
                "MAT.", "NOME", "MENSALID.", "IMC", "CLASSIFICAÇÃO", "FREQ.", "STATUS"));
        sb.append("-".repeat(88)).append("\n");

        for (AlunoAcademia a : lista) {
            sb.append(String.format("%-6d | %-20s | R$ %7.2f | %6.2f | %-18s | %-5d | %-8s\n",
                    a.getMatricula(),
                    a.getNome(),
                    a.getMensalidade(),
                    a.calcularIndiceMassaCorporal(),
                    a.classificarIMC(),
                    a.getFrequencia(),
                    a.isAtivo() ? "ATIVO" : "SUSPENSO"));
        }
        txtRelatorio.setText(sb.toString());
    }

    private void limparCampos() {
        txtMatricula.setText("");
        txtNome.setText("");
        txtMensalidade.setText("");
        txtPeso.setText("");
        txtAltura.setText("");
        chkAtivo.setSelected(true);
        txtMatricula.requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnCadastrar) {
            try {
                int mat = Integer.parseInt(txtMatricula.getText().trim());
                String nome = txtNome.getText().trim();
                double mensalidade = Double.parseDouble(txtMensalidade.getText().trim().replace(",", "."));
                double peso = Double.parseDouble(txtPeso.getText().trim().replace(",", "."));
                double altura = Double.parseDouble(txtAltura.getText().trim().replace(",", "."));

                if (nome.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Informe o nome do aluno.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!Avaliavel.validarDadosFisicos(peso, altura)) {
                    JOptionPane.showMessageDialog(this, "Peso (20-350kg) ou altura (0.5-2.5m) inválidos!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                AlunoAcademia novo = new AlunoAcademia(mat, nome, mensalidade, peso, altura);
                novo.setAtivo(chkAtivo.isSelected());

                if (listaAlunos.contains(novo)) {
                    JOptionPane.showMessageDialog(this, "Já existe um aluno com esta matrícula!", "Duplicidade", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                listaAlunos.add(novo);
                atualizarRelatorio(listaAlunos);
                limparCampos();
                JOptionPane.showMessageDialog(this, "Aluno cadastrado com sucesso!");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Preencha matrícula, mensalidade, peso e altura com valores numéricos válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            }

        } else if (src == btnPesquisar) {
            try {
                int mat = Integer.parseInt(txtMatricula.getText().trim());
                AlunoAcademia encontrado = buscarPorMatricula(mat);
                if (encontrado != null) {
                    txtNome.setText(encontrado.getNome());
                    txtMensalidade.setText(String.valueOf(encontrado.getMensalidade()));
                    txtPeso.setText(String.valueOf(encontrado.getPeso()));
                    txtAltura.setText(String.valueOf(encontrado.getAltura()));
                    chkAtivo.setSelected(encontrado.isAtivo());
                } else {
                    JOptionPane.showMessageDialog(this, "Aluno não encontrado.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Digite um número de matrícula válido para pesquisar.");
            }

        } else if (src == btnFrequencia) {
            try {
                int mat = Integer.parseInt(txtMatricula.getText().trim());
                AlunoAcademia aluno = buscarPorMatricula(mat);
                if (aluno == null) {
                    JOptionPane.showMessageDialog(this, "Aluno não encontrado.");
                    return;
                }
                aluno.registrarFrequencia();
                atualizarRelatorio(listaAlunos);
                JOptionPane.showMessageDialog(this, "Presença registada! Total de acessos: " + aluno.getFrequencia());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Informe a matrícula do aluno.");
            } catch (MatriculaBloqueadaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Acesso Bloqueado", JOptionPane.ERROR_MESSAGE);
            }

        } else if (src == btnSuspender) {
            try {
                int mat = Integer.parseInt(txtMatricula.getText().trim());
                AlunoAcademia aluno = buscarPorMatricula(mat);
                if (aluno != null) {
                    aluno.setAtivo(!aluno.isAtivo());
                    chkAtivo.setSelected(aluno.isAtivo());
                    atualizarRelatorio(listaAlunos);
                } else {
                    JOptionPane.showMessageDialog(this, "Aluno não encontrado.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Informe a matrícula do aluno.");
            }

        } else if (src == btnListar) {
            atualizarRelatorio(listaAlunos);

        } else if (src == btnOrdenarMat) {
            Collections.sort(listaAlunos);
            atualizarRelatorio(listaAlunos);

        } else if (src == btnOrdenarVal) {
            listaAlunos.sort(Comparator.comparingDouble(AlunoAcademia::getMensalidade));
            atualizarRelatorio(listaAlunos);

        } else if (src == btnSair) {
            confirmarSaida();
        }
    }
}
