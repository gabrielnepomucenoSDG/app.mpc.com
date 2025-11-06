# Estágio 1: Construção (Build)
# Usamos uma imagem Java completa para compilar o código
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Execução (Runtime)
# Usamos uma imagem minimalista (Alpine) que é mais leve e segura
# ATENÇÃO: Verifique se o OpenJDK 17 é a versão que você usa
FROM openjdk:17-jdk-alpine
WORKDIR /app

# Copia o JAR do estágio de build para o runtime
# SUBSTITUA 'seu-arquivo-executavel.jar' pelo nome REAL do arquivo JAR (veja seu pom.xml)
COPY --from=build /app/target/narraterpro.jar app.jar

# Define a porta que o Spring Boot vai escutar (usando a variável de ambiente PORT do Render)
ENV SERVER_PORT 8080
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]