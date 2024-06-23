# FriendKeeper

Agenda de Contatos com Recursos Avançados

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)


## Grupo
- [Daniel Jordão Schutz](https://github.com/Daniel-Schutz)

- [Pedro Henrique Weber Carvalhaes](https://github.com/PedroWC)

## Papéis e Usuários:

1. Usuário Cadastrado:

   - Pode criar uma conta e fazer login no aplicativo.
   - Pode adicionar, editar, excluir e visualizar contatos.
   - Pode visualizar a localização dos contatos em um mapa integrado.

2. Visitante (Não Cadastrado):

   - Não pode acessar a funcionalidade do aplicativo.
   - Pode apenas visualizar a tela de login e a opção de cadastro.


## Requisitos Funcionais:

1. Tela de Login e Cadastro

   - O aplicativo deve possuir uma tela de login que permita que os usuários cadastrados acessem suas informações.
   - Deve haver uma opção de cadastro para que novos usuários possam criar uma conta.
   - O cadastro deve solicitar informações básicas do usuário, como nome, e-mail e senha.

2. Gerenciamento de Contatos

   - O usuário cadastrado pode adicionar novos contatos, informando nome, número de telefone, e-mail, endereço, data de nascimento e foto do contato.
   - O usuário pode editar as informações de um contato existente.
   - O usuário pode excluir um contato.
   - O usuário pode visualizar a lista de todos os seus contatos cadastrados.

3. Lembretes de Aniversários

   - O aplicativo deve exibir uma notificação com som no dispositivo do usuário quando for o aniversário de um de seus contatos.

4. Menu

   - O aplicativo deve ter um menu lateral com opções de alterar o idioma, alterar para modo noturno e gerenciar permissões.


5. Localização de Contatos

   - O aplicativo deve integrar com um serviço de mapeamento para exibir a localização dos contatos no mapa.
   - O usuário pode visualizar a localização de seus contatos no mapa.

7. Perfil do Usuário

- O aplicativo deve armazenar e exibir corretamente as informações de perfil do usuário.
- O aplicativo deve possuir uma funcionalidade para o usuário realizar logout.

## Requisitos Não Funcionais:

1. Segurança:

   - O aplicativo deve garantir a segurança dos dados dos usuários, utilizando criptografia e autenticação.
   - Deve ser implementado um sistema de permissões de acesso, impedindo que usuários não cadastrados acessem informações privadas.

2. Usabilidade:

   - A interface do aplicativo deve ser intuitiva, de fácil navegação e minimalista.

3. Desempenho:

   - O aplicativo deve ter um tempo de resposta rápido, especialmente no carregamento de listas de contatos e no acesso a informações.
   - O aplicativo deve ser otimizado para consumo eficiente de recursos do dispositivo, como bateria e memória.

## Testes de Caixa Preta:

1. Validação de Usuário Não Cadastrado:

    __Objetivo__: Verificar se um usuário não cadastrado não consegue acessar a funcionalidade do aplicativo.  
    __Entrada__: Tentativa de login com credenciais inválidas.  
    __Resultado esperado__: O aplicativo deve impedir o acesso do usuário não cadastrado e exibir uma mensagem de erro informando que o acesso é restrito a usuários cadastrados.  

2. Validação de Campos Obrigatórios:

    __Objetivo__: Verificar se o aplicativo valida a presença de campos obrigatórios.  
    __Entrada__: Tentativa de adicionar um novo contato com um campo obrigatório (como *número de telefone*) vazio.  
    __Resultado esperado__: O aplicativo deve exibir uma mensagem de erro informando que o campo obrigatório precisa ser preenchido.  

4. Validação de Entrada Inválida:

    __Objetivo__: Verificar se o aplicativo valida a entrada de dados em campos numéricos.  
    __Entrada__: Tentar inserir um texto em um campo numérico (como o campo de *número de telefone*).  
    __Resultado esperado__: O aplicativo deve impedir a entrada de texto em um campo numérico e exibir uma mensagem de erro informando que o formato da entrada é inválido.

## Entidades

1. User
   - id: INTEGER (PK, AutoIncrement)
   - name: TEXT
   - email: TEXT (Unique)
   - passwordHash: TEXT (Armazena o hash da senha concatenado com o salt)
   - profilePictureUri: TEXT

2. Contact
   - id: INTEGER (PK, AutoIncrement)
   - userId: INTEGER (FK, referencia User.id)
   - name: TEXT
   - phoneNumber: TEXT
   - email: TEXT
   - address: TEXT
   - birthdate: TEXT
   - photoUri: TEXT
   - location: TEXT

### Relacionamentos
    - User-Contact: Um usuário pode ter vários contatos.
