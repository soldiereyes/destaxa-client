# DESTAXA-CLIENT

Descrição

O DESTAXA-CLIENT é uma aplicação cliente desenvolvida em Java Spring Boot que funciona como intermediário entre a
interface do usuário e o servidor backend. Ele processa requisições HTTP, constrói mensagens no formato ISO8583 e as
envia ao servidor, recebendo e interpretando as respostas.

Este projeto faz parte do sistema DESTAXA, implementado como um desafio técnico para simular o fluxo de autorização de
pagamentos.

# FUNCIONALIDADES

API REST:
Endpoint para envio de transações no formato JSON.
Conversão do JSON recebido para o formato ISO8583.
Comunicação com o servidor via sockets TCP.

Regras de Negócio:
Geração de mensagens ISO8583 com os campos necessários.
Interpretação de respostas do servidor, retornando um código de status para o cliente HTTP.

# TECNOLOGIAS UTILIZADAS

Java 17: Linguagem base do projeto.

Spring Boot 3: Framework para construção de APIs REST.

Maven: Gerenciador de dependências e build.

ISO8583: Protocolo financeiro para mensagens de transações.

# COMO CONFIGURAR E EXECUTAR

1. Clone o Repositório:
   https://github.com/soldiereyes/destaxa-client
2. Entre na pasta do projeto:
   cd destaxa-client
3. Configure a conexão com o Servidor:
   server.host=localhost
   server.port=5000
4. Gere o JAR executável: mvn clean package
5. Execute o Client:  java -jar target/client-0.0.1-SNAPSHOT.jar

# USO DA API

A aplicação expõe um endpoint REST para envio de transações.
Endpoint: /api/payment/authorize

    URL: http://localhost:8081/api/payment/authorize
    Método: POST
    Headers:
        Content-Type: application/json
    Body: JSON com os seguintes campos:
        card_number (string): Número do cartão.
        value (string): Valor da transação.

Exemplo de Requisição:

Exemplo de Resposta:

{

"Response Code": "000"

}

Códigos de Resposta:

        "000": Transação aprovada.
        "051": Transação rejeitada.

# TESTES DE INTEGRAÇÃO

Instruções para Testes no Insomnia

Abra o Insomnia e clique em New Request.

Configure os seguintes campos:
        Name: Nome da requisição (ex.: "Transação Aprovada").
        Method: POST.
        URL: http://localhost:8081/api/payment/authorize. 

Vá para a aba Body e selecione o tipo JSON.
    Insira um dos JSONs de exemplo fornecidos acima.
    Clique em Send para enviar a requisição e observe a resposta.

``` json 

1. Transação Aprovada (Response Code: 000):

{
"card_number": "4111111111111111",
"value": "500.00"
}

2. Transação Rejeitada (Response Code: 051):

{
    "card_number": "4111111111111111",
    "value": "-100.00"
}

3. Timeout Simulado (Nenhuma Resposta do Servidor):

{
    "card_number": "4111111111111111",
    "value": "1500.00"
}

4. Erros de Campos Ausentes ou Inválidos:

{
    "value": "500.00"
}

5. Campo value ausente:

{
    "card_number": "4111111111111111"
}

6. Ambos os campos ausentes:

{}

7. Valor não numérico:

{
    "card_number": "4111111111111111",
    "value": "invalid"
}

8. Número do cartão Inválido:

{
    "card_number": "1234abcd",
    "value": "500.00"
}



