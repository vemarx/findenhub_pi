# 🎉 FindenHub - Gerenciamento Inteligente de Eventos

**FindenHub** é um marketplace desenvolvido em Java com Spring Boot que conecta organizadores de eventos a fornecedores de serviços. Projeto acadêmico completo com autenticação, persistência em MongoDB e interface web moderna.

---

## 🚀 Funcionalidades Principais

### Para Organizadores de Eventos
- ✅ Cadastro e login seguro
- 🔍 Busca de serviços por categoria
- 📋 Criação e gerenciamento de eventos
- 💬 Contato direto com fornecedores
- ⭐ Sistema de avaliações

### Para Fornecedores de Serviços
- ✅ Cadastro como fornecedor
- 📝 CRUD completo de serviços
- 📊 Dashboard com estatísticas
- 🎯 Gerenciamento de perfil empresarial
- 💰 Controle de preços e disponibilidade

### Funcionalidades Gerais
- 🔐 Autenticação segura com Spring Security
- 🗄️ Persistência de dados em MongoDB
- 🎨 12 categorias de serviços para eventos
- 📱 Design responsivo com Bootstrap 5
- 🔄 Atualizações em tempo real
- 🛡️ Proteção CSRF

---

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
  - Spring Web
  - Spring Data MongoDB
  - Spring Security
  - Spring Validation
- **Lombok** (redução de código boilerplate)
- **Maven** (gerenciamento de dependências)

### Frontend
- **Thymeleaf** (template engine)
- **Bootstrap 5.3** (framework CSS)
- **Bootstrap Icons** (ícones)
- **CSS customizado** (animações e design moderno)

### Banco de Dados
- **MongoDB 7.0** (NoSQL, porta 27017)

---

## 📁 Estrutura do Projeto

```
findenhub/
├── src/
│   ├── main/
│   │   ├── java/com/findenhub/
│   │   │   ├── FindenHubApplication.java       # Classe principal
│   │   │   ├── model/                          # Entidades
│   │   │   │   ├── User.java
│   │   │   │   ├── Service.java
│   │   │   │   ├── Event.java
│   │   │   │   ├── Category.java
│   │   │   │   ├── Review.java
│   │   │   │   └── Message.java
│   │   │   ├── repository/                     # Repositórios MongoDB
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── ServiceRepository.java
│   │   │   │   ├── EventRepository.java
│   │   │   │   ├── CategoryRepository.java
│   │   │   │   ├── ReviewRepository.java
│   │   │   │   └── MessageRepository.java
│   │   │   ├── service/                        # Lógica de negócio
│   │   │   │   ├── UserService.java
│   │   │   │   ├── ServiceService.java
│   │   │   │   ├── EventService.java
│   │   │   │   └── CategoryService.java
│   │   │   ├── controller/                     # Controllers
│   │   │   │   ├── HomeController.java
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── DashboardController.java
│   │   │   │   ├── ProfileController.java
│   │   │   │   ├── ServiceController.java
│   │   │   │   └── EventController.java
│   │   │   ├── security/                       # Segurança
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── CustomUserDetailsService.java
│   │   │   │   └── SecurityUtils.java
│   │   │   ├── config/                         # Configurações
│   │   │   │   └── DataInitializer.java
│   │   │   └── dto/                            # Data Transfer Objects
│   │   │       ├── RegistrationDTO.java
│   │   │       ├── ServiceDTO.java
│   │   │       ├── EventDTO.java
│   │   │       ├── ReviewDTO.java
│   │   │       ├── MessageDTO.java
│   │   │       └── SearchFilterDTO.java
│   │   └── resources/
│   │       ├── templates/                      # Views Thymeleaf
│   │       │   ├── index.html
│   │       │   ├── login.html
│   │       │   ├── register.html
│   │       │   ├── organizer-dashboard.html
│   │       │   ├── supplier-dashboard.html
│   │       │   ├── profile.html
│   │       │   ├── service-form.html
│   │       │   ├── services-list.html
│   │       │   └── service-detail.html
│   │       └── application.properties          # Configurações
│   └── test/                                   # Testes (futuro)
├── pom.xml                                     # Dependências Maven
└── README.md                                   # Este arquivo
```

---

## 🎯 Como Executar o Projeto

### Pré-requisitos

1. **Java 17 ou superior**
   ```bash
   java -version
   ```

2. **Maven**
   ```bash
   mvn -version
   ```

3. **MongoDB instalado e rodando**
   ```bash
   # Verificar se MongoDB está rodando
   mongosh
   ```

### Instalação

1. **Clone ou extraia o projeto**

2. **Configure o MongoDB**
   - Certifique-se de que o MongoDB está rodando na porta 27017
   - O banco de dados `findenhub` será criado automaticamente

3. **Compile o projeto**
   ```bash
   mvn clean install
   ```

4. **Execute a aplicação**
   ```bash
   mvn spring-boot:run
   ```

5. **Acesse no navegador**
   ```
   http://localhost:8081
   ```

---

## 📖 Como Usar

### 1. Cadastro de Usuário

1. Acesse `http://localhost:8081
2. Clique em **"Cadastrar Grátis"**
3. Escolha o tipo de conta:
   - **Organizador**: Para quem busca serviços
   - **Fornecedor**: Para quem oferece serviços
4. Preencha os dados e clique em **"Cadastrar"**

### 2. Login

1. Acesse `http://localhost:8081/login`
2. Entre com email e senha
3. Será redirecionado para o dashboard apropriado

### 3. Dashboard do Organizador

- Visualize todas as categorias de serviços
- Busque serviços disponíveis
- Crie e gerencie seus eventos
- Entre em contato com fornecedores

### 4. Dashboard do Fornecedor

- Cadastre seus serviços
- Edite informações de serviços existentes
- Visualize estatísticas (visualizações, contatos)
- Gerencie seu perfil empresarial

### 5. Gestão de Serviços (Fornecedores)

**Criar Serviço:**
1. No dashboard, clique em **"Cadastrar Novo Serviço"**
2. Preencha:
   - Título
   - Descrição (mínimo 20 caracteres)
   - Categoria
   - Preço
   - Localização (opcional)
   - Características (opcional)
3. Clique em **"Cadastrar Serviço"**

**Editar/Deletar:**
- No dashboard, use os botões de cada serviço

---

## 📊 Categorias Disponíveis

O sistema inclui 12 categorias pré-cadastradas:

1. 🍽️ **Buffet** - Serviços de alimentação e bebidas
2. 🎨 **Decoração** - Decoração e ambientação
3. 📸 **Fotografia** - Fotografia e filmagem profissional
4. 🎵 **Música** - Bandas, DJs e entretenimento
5. 💡 **Iluminação** - Iluminação e efeitos especiais
6. 🔊 **Som** - Equipamentos de som
7. 🏢 **Locação** - Aluguel de espaços
8. 🛡️ **Segurança** - Segurança e controle de acesso
9. ✉️ **Convites** - Design e impressão
10. 🎭 **Cerimonial** - Mestres de cerimônia
11. 🌸 **Flores** - Arranjos florais
12. 🚗 **Transporte** - Transporte de convidados

---

## 🔒 Segurança

- **Senhas**: Criptografadas com BCrypt
- **Autenticação**: Spring Security com sessões gerenciadas
- **CSRF**: Proteção habilitada em todos os formulários
- **Validação**: Backend e frontend
- **Autorização**: Rotas protegidas por perfil (Organizador/Fornecedor)

---

## 📝 Modelos de Dados

### User (Usuário)
```java
{
  "id": String,
  "name": String,
  "email": String (único),
  "password": String (criptografado),
  "userType": ORGANIZER | SUPPLIER,
  "phone": String,
  "description": String,
  "city": String,
  "state": String,
  "companyName": String (fornecedor),
  "rating": Double,
  "totalReviews": Integer
}
```

### Service (Serviço)
```java
{
  "id": String,
  "title": String,
  "description": String,
  "price": Double,
  "categoryId": String,
  "supplierId": String,
  "location": String,
  "features": List<String>,
  "capacity": Integer,
  "duration": String,
  "available": Boolean,
  "views": Integer,
  "contacts": Integer
}
```

### Event (Evento)
```java
{
  "id": String,
  "title": String,
  "description": String,
  "organizerId": String,
  "eventDate": LocalDateTime,
  "venue": String,
  "city": String,
  "expectedGuests": Integer,
  "budget": Double,
  "eventType": WEDDING | BIRTHDAY | CORPORATE | etc,
  "status": PLANNING | CONFIRMED | IN_PROGRESS | COMPLETED | CANCELLED,
  "serviceIds": List<String>,
  "categoryNeeds": List<String>
}
```

---

## 🎨 Interface

- **Design Moderno**: Gradientes vibrantes e animações suaves
- **Responsivo**: Funciona em desktop, tablet e mobile
- **Intuitivo**: Navegação clara e ações óbvias
- **Acessível**: Formulários com labels e mensagens claras

---

## 🚀 Próximas Melhorias (Sugestões)

- [ ] Sistema completo de avaliações e comentários
- [ ] Mensagens diretas entre usuários
- [ ] Upload real de imagens
- [ ] Dashboard com gráficos e métricas
- [ ] Filtros avançados de busca
- [ ] API REST para mobile
- [ ] Sistema de favoritos
- [ ] Notificações por email
- [ ] Calendário de disponibilidade
- [ ] Agendamento de serviços
- [ ] Testes unitários e de integração
- [ ] Deploy em cloud (AWS, Heroku, etc)

---

## 🧪 Testando a Aplicação

### Criar Usuários de Teste

**Organizador:**
- Email: `organizador@teste.com`
- Senha: `123456`
- Tipo: Organizador

**Fornecedor:**
- Email: `fornecedor@teste.com`
- Senha: `123456`
- Tipo: Fornecedor

### Cenários de Teste

1. ✅ Cadastro de novo usuário
2. ✅ Login com credenciais válidas
3. ✅ Acesso aos dashboards específicos
4. ✅ Criação de serviço (fornecedor)
5. ✅ Busca de serviços (organizador)
6. ✅ Visualização de detalhes de serviço
7. ✅ Edição de perfil
8. ✅ Criação de evento (organizador)
9. ✅ Filtros de busca

---

## 🛠️ Troubleshooting

### Problema: MongoDB não conecta
```bash
# Solução: Verificar se MongoDB está rodando
sudo systemctl status mongod

# Iniciar MongoDB
sudo systemctl start mongod
```

### Problema: Porta 5000 já está em uso
```properties
# Solução: Alterar porta em application.properties
server.port=8080
```

### Problema: Erro ao compilar
```bash
# Solução: Limpar cache do Maven
mvn clean
rm -rf ~/.m2/repository
mvn install
```

---

## 📧 Configuração de Produção

### Deploy com MongoDB Atlas (Cloud)

1. Crie conta no [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
2. Obtenha a connection string
3. Altere `application.properties`:

```properties
spring.data.mongodb.uri=mongodb+srv://usuario:senha@cluster.mongodb.net/findenhub
```

### Build para Produção

```bash
mvn clean package -DskipTests
java -jar target/findenhub-1.0.0.jar
```

---

## 👥 Contribuindo

Este é um projeto acadêmico, mas sugestões são bem-vindas!

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/NovaFuncionalidade`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/NovaFuncionalidade`)
5. Abra um Pull Request

---

## 📄 Licença

Este projeto foi desenvolvido para fins acadêmicos.

---

## 🎓 Créditos

**Desenvolvido com:**
- ☕ Java & Spring Boot
- 🍃 MongoDB
- 🎨 Bootstrap 5
- 💙 Muito café e dedicação

---

## 📞 Suporte

Para dúvidas ou problemas:
1. Verifique a documentação acima
2. Confira os logs da aplicação
3. Revise as configurações do MongoDB

---

**FindenHub**  🎉