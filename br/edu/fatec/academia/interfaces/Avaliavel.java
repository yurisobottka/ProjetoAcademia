package br.edu.fatec.academia.interfaces;

public interface Avaliavel {

    double calcularIndiceMassaCorporal();

    default String classificarIMC() {
        double imc = calcularIndiceMassaCorporal();
        if (imc < 18.5) return "Abaixo do peso";
        if (imc < 24.9) return "Peso normal";
        if (imc < 29.9) return "Sobrepeso";
        if (imc < 34.9) return "Obesidade Grau I";
        if (imc < 39.9) return "Obesidade Grau II";
        return "Obesidade Grau III (Mórbida)";
    }

    static boolean validarDadosFisicos(double peso, double altura) {
        return peso > 20.0 && peso < 350.0 && altura > 0.5 && altura < 2.5;
    }
}

