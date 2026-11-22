# 🎪 Festival Raízes: Operação Check-in

Sistema de controle de ingressos de um festival, feito em **Java puro** (sem frameworks), que usa **duas árvores de busca ao mesmo tempo**, uma **ABB** (sem balanceamento) e uma **AVL** (balanceada), para comparar o desempenho das duas na mesma carga de dados.

Projeto acadêmico de Estrutura de Dados (Universidade Presbiteriana Mackenzie).

## O que o sistema faz

Cada ingresso tem **código, nome do titular, setor, dia** e a informação de já ter entrado ou não. Os ingressos são carregados de um arquivo de texto e guardados na ABB e na AVL em paralelo. Pelo menu de console:

| Opção | O que faz |
|---|---|
| **1) Carregar arquivo** | Lê um arquivo `codigo;nome;setor;dia`. Linhas com defeito são ignoradas, com aviso do motivo: linha vazia, número de campos diferente de 4, código não numérico ou código repetido. |
| **2) Check-in** | Busca o ingresso e responde **VÁLIDO**, **JÁ UTILIZADO** ou **INVÁLIDO**. Mostra quantas comparações a busca fez na AVL e na ABB. |
| **3) Venda na portaria** | Emite um novo ingresso com o próximo código (maior código + 1) e mostra as rotações que a AVL fez para se rebalancear. |
| **4) Cancelamento** | Remove um ingresso que ainda não foi usado, mostrando as rotações da AVL. Ingresso já utilizado não pode ser cancelado. |
| **5) Consulta por faixa** | Conta quantos ingressos existem entre dois códigos. |
| **6) Painel** | Total de ingressos, altura da ABB e da AVL, raiz da AVL com seu fator de balanceamento, contadores de rotações (RSE, RSD, RDE, RDD) e check-ins realizados e restantes. |
| **7) Sair** | Encerra o programa. |
| **8) Relatório de público** | Extensão: vendidos, já entraram e restantes por **setor** (Pista, Pista Premium, Arquibancada, Camarote) e por **dia** (Sexta, Sábado, Domingo). |

**Comparações** = quantidade de nós visitados na busca, contando o nó encontrado (ou todos até chegar em `null`, se o código não existir).

## Como rodar

Requisito: JDK instalado (`javac` e `java`).

Na pasta raiz do projeto:

```bash
javac -d out codigo/*.java
java -cp out Main
```

Na opção 1, informe o caminho do arquivo a partir da pasta onde você executou o comando, por exemplo:

```
Arquivo: entradas/ingressos_exemplo.txt
```

## Arquivos de teste

A pasta `entradas/` tem arquivos prontos, e `saidas_esperadas/` mostra o que o programa deve exibir com cada um. Mais detalhes no [`LEIAME.txt`](LEIAME.txt).

| Arquivo de entrada | O que testa |
|---|---|
| `ingressos_exemplo.txt` | 11 ingressos válidos e 5 linhas com defeito; uma sessão usando todas as opções do menu. |
| `rotacao_RSE.txt`, `rotacao_RSD.txt`, `rotacao_RDE.txt`, `rotacao_RDD.txt` | 3 ingressos que provocam exatamente uma rotação de cada tipo. |
| `remocao_cascata.txt` | Cancelamento que provoca duas rotações em cascata e troca a raiz. |
| `ingressos_aleatorio.txt` | 1000 ingressos fora de ordem: a ABB fica com altura 23 e a AVL com 12. |
| `so_erros.txt` | Nenhuma linha válida; as opções 2 a 6 devem recusar. |

Na raiz há ainda `ingressos.txt` (5000 ingressos) e `ingressos_teste.txt` (12 ingressos), usados para testes maiores.

## Estrutura

```
codigo/
├── Main.java       # menu e fluxo do programa
├── Ingresso.java   # dados do ingresso
├── Node.java       # nó das árvores (guarda um Ingresso, altura e ponteiros)
├── ABB.java        # árvore binária de busca, sem balanceamento
└── AVL.java        # árvore AVL, com as 4 rotações (RSE, RSD, RDE, RDD)
entradas/           # arquivos de ingressos para testar
saidas_esperadas/   # saída esperada de cada teste
```

## Conceitos aplicados

- Árvore binária de busca e árvore AVL (fator de balanceamento = altura da direita − altura da esquerda).
- Rotações simples e duplas, com contadores por tipo.
- Remoção com substituição pelo sucessor e rebalanceamento até a raiz.
- Cálculo de altura de forma **iterativa**, evitando estouro de pilha em árvores degeneradas (a ABB pode chegar a milhares de níveis).
- Comparação prática de custo de busca entre ABB e AVL.

## Autores

Pedro Henrique Silva Guimarães e Lucas Natal.
