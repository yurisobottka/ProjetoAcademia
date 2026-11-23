package br.edu.fatec.academia.model;

import br.edu.fatec.academia.interfaces.Avaliavel;
import br.edu.fatec.academia.exception.MatriculaBloqueadaException;
import java.util.Objects;

public class AlunoAcademia implements Comparable<AlunoAcademia>, Avaliavel {
    private int matricula;
    private String nome;
    private double mensalidade;
    private double peso;   // em kg
    private double altura; // em metros
    private boolean ativo;
    private int frequencia; // contador de acessos

    public AlunoAcademia() {
        this.ativo = true;
        this.frequencia = 0;
    }

    public AlunoAcademia(int matricula, String nome, double mensalidade, double peso, double altura) {
        this();
        this.matricula = matricula;
        this.nome = nome;
        this.mensalidade = mensalidade;
        this.peso = peso;
        this.altura = altura;
    }
    public void registrarFrequencia() throws MatriculaBloqueadaException {
        if (!this.ativo) {
            throw new MatriculaBloqueadaException("Acesso negado: A matrícula de " + this.nome + " está suspensa!");
        }
        this.frequencia++;
    }

    public int getMatricula() { return matricula; }
    public void setMatricula(int matricula) { this.matricula = matricula; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public double getMensalidade() { return mensalidade; }
    public void setMensalidade(double mensalidade) { this.mensalidade = mensalidade; }
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
    public double getAltura() { return altura; }
    public void setAltura(double altura) { this.altura = altura; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public int getFrequencia() { return frequencia; }
    public void setFrequencia(int frequencia) { this.frequencia = frequencia; }

    @Override
    public double calcularIndiceMassaCorporal() {
        if (this.altura <= 0) {
            return 0;
        }
        return this.peso / (this.altura * this.altura);
    }
    @Override
    public String classificarIMC() {
        return Avaliavel.super.classificarIMC();
    }

    @Override
    public int compareTo(AlunoAcademia outro) {
        return Integer.compare(this.matricula, outro.matricula);
    }

}


