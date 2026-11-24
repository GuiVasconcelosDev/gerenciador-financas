package br.com.guilhermedev.financas.principal;

import br.com.guilhermedev.financas.model.TipoTransacao;
import br.com.guilhermedev.financas.model.Transacao;
import br.com.guilhermedev.financas.service.GerenciadorArquivo;
import br.com.guilhermedev.financas.service.GerenciadorBanco;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        GerenciadorBanco gerenciador = new GerenciadorBanco();
        GerenciadorArquivo gerenciadorArquivo = new GerenciadorArquivo();
        gerenciador.inicializarBanco();

        Scanner leitura = new Scanner(System.in);
        int opcao = 0;

        String menu = """
                =========================================
                GERENCIADOR DE FINANÇAS PESSOAIS
                =========================================
                1) Adicionar Receita (Entrada)
                2) Adicionar Despesa (Saída)
                3) Listar Transações e Saldo Geral
                4) Resumo Estatístico Avançado (Streams)\s
                5) Exportar CSV
                6) Sair
                =========================================
               \s""";

        while (opcao != 6) {
            System.out.println(menu);
            System.out.print("Escolha uma opção: ");
            opcao = leitura.nextInt();
            leitura.nextLine();

            if (opcao == 1 || opcao == 2) {
                System.out.print("Digite a descrição da movimentação: ");
                String descricao = leitura.nextLine();

                System.out.print("Digite o valor (Ex: 150,50): ");
                double valor = leitura.nextDouble();

                TipoTransacao tipo = (opcao == 1) ? TipoTransacao.RECEITA : TipoTransacao.DESPESA;

                Transacao novaTransacao = new Transacao(LocalDate.now(), descricao, tipo, valor);

                gerenciador.salvarTransacao(novaTransacao);
            } else if (opcao == 3) {
                System.out.println("\n--- HISTÓRICO DE TRANSAÇÕES ---");
                List<Transacao> lista = gerenciador.listarTransacoes();

                if (lista.isEmpty()) {
                    System.out.println("Nenhuma transação registrada até o momento.");
                } else {
                    double saldoFinal = 0.0;

                    for (Transacao t : lista) {
                        String sinal = (t.tipo() == TipoTransacao.RECEITA) ? "[+]" : "[-]";
                        System.out.printf("%s %s - %s: R$ %.2f\n", t.data(), sinal, t.descricao(), t.valor());

                        if (t.tipo() == TipoTransacao.RECEITA) {
                            saldoFinal += t.valor();
                        } else {
                            saldoFinal -= t.valor();
                        }
                    }

                    System.out.println("--------------------------------------");
                    System.out.printf("SALDO ATUAL: R$ %.2f\n", saldoFinal);
                    if (saldoFinal < 0) {
                        System.out.println("Atenção: A sua conta está no vermelho!");
                    } else {
                        System.out.println("Parabéns! As suas finanças estão controladas.");
                    }
                    System.out.println("--------------------------------------\n");
                }
            } else if (opcao == 4) {
                System.out.println("\n--- ANALISE ESTATÍSTICA AVANÇADA (STREAM) ---"); // o uso de stream para a filtragem de contagem, muito bom pae!
                List<Transacao> lista = gerenciador.listarTransacoes();

                if (lista.isEmpty()) {
                    System.out.println("Nenhuma transação para analisar.");
                } else {
                    double totalReceitas = lista.stream()
                            .filter(t -> t.tipo() == TipoTransacao.RECEITA)
                            .mapToDouble(Transacao::valor)
                            .sum();
                    double totalDespesas = lista.stream()
                            .filter(t -> t.tipo() == TipoTransacao.DESPESA)
                            .mapToDouble(Transacao::valor)
                            .sum();

                    double  maiorDespesa = lista.stream()
                            .filter(t -> t.tipo() == TipoTransacao.DESPESA)
                            .mapToDouble(Transacao::valor)
                            .max()
                            .orElse(0.0);

                    System.out.printf("Total acumulado de Entradas : R$ %.2f\n", totalReceitas);
                    System.out.printf("Total acumulado de Saídas : R$ %.2f\n", totalDespesas);
                    System.out.printf("Maior despesa identificada : R$ %.2f\n", maiorDespesa);
                    System.out.println("----------------------------------------------------\n");
                }
            } else if (opcao == 5) {
                gerenciadorArquivo.exportarTransacoes(gerenciador.listarTransacoes());
            } else if (opcao == 6) {
                System.out.println("Sistema encerrado. Cuide bem do seu dinheiro!");
            } else {
                System.out.println("Opção inválida! Tente novamente.");
            }

        }

        leitura.close();
    }
}